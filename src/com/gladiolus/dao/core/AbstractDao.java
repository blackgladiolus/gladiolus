package com.gladiolus.dao.core;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gladiolus.dao.GenericDao;
import com.gladiolus.dao.help.BuildResult;
import com.gladiolus.dao.help.BuildSql;
import com.gladiolus.dao.help.ResultSetToBeans;
import com.gladiolus.db.DBUtil;

@SuppressWarnings("rawtypes")
public abstract class AbstractDao<T> implements GenericDao<T> {

	protected Connection conn;
	protected PreparedStatement ps = null;
	protected ResultSet rs = null;

	private BuildSql sqlUtil = new BuildSql();

	private Class clazz = null;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		// 使用反射技术得到T的真实类型
		ParameterizedType pt = (ParameterizedType) this.getClass()
				.getGenericSuperclass(); // 获取当前new的对象的 泛型的父类 类型
		this.clazz = (Class<T>) pt.getActualTypeArguments()[0]; // 获取第一个类型参数的真实类型
	}

	@Override
	public void openConnection() {
		try {
			this.conn = DBUtil.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public TransactionManager getTransactionManager() {
		if (conn == null) {
			return null;
		}
		return new TransactionManager(conn);
	}

	@Override
	public void closeConnection() {
		DBUtil.releaseConnection();
	}

	@Override
	public Connection getConnection() {
		return conn;
	}

	@Override
	public int update(Object entity) {
		BuildResult SqlResult = sqlUtil.buildUpdateSQL(entity);
		List<Object[]> paramslist = new ArrayList<Object[]>();
		paramslist.add(SqlResult.getParams());
		return updateBatch(SqlResult.getSql(), paramslist, 1);
	}

	@Override
	public boolean delete(Integer id) {
		Integer[] ids = { id };
		return this.delete(ids);
	}

	@Override
	public boolean delete(Integer[] ids) {
		BuildResult SqlResult = sqlUtil.buildDeleteSql(clazz, ids);
		List<Object[]> idlist = new ArrayList<Object[]>();
		idlist.add(ids);
		return updateBatch(SqlResult.getSql(), idlist, 1) > 0 ? true : false;
	}

	@Override
	public int save(String sql, Object[] params) {
		List<Object[]> paramslist = new ArrayList<Object[]>();
		paramslist.add(params);
		return updateBatch(sql, paramslist, 1);
	}

	@Override
	public int save(Object entity) {
		BuildResult SqlResult = sqlUtil.buildSaveSql(entity);
		return save(SqlResult.getSql(), SqlResult.getParams());
	}

	@Override
	public List queryForList(Object entity, Integer start, Integer end) {
		BuildResult result = sqlUtil.buildQuery(entity);
		return queryForList(result.getSql(), result.getParams(), start, end);
	}

	@Override
	public Object queryForObject(Object entity) {
		BuildResult result = sqlUtil.buildQuery(entity);
		return queryForObject(result.getSql(), result.getParams());
	}

	@Override
	public Object queryForObject(String sql, Object[] params) {
		List result = queryForList(sql, params, null, null);
		if (result != null) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public List queryForList(String sql, Integer start, Integer end) {
		return queryForList(sql, null, start, end);
	}

	@Override
	public boolean containsEntity(Object entity) {
		List result = queryForList(entity, null, null);
		if (result.isEmpty() || result.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object getById(Integer id) {
		Integer[] Ids = { id };
		List result = getByIds(Ids);
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public List getByIds(Integer[] ids) {
		BuildResult result = null;
		try {
			result = sqlUtil.buildQuery(clazz, ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.queryForList(result.getSql(), result.getParams(), null,
				null);
	}

	/**
	 * 释放ResultSet和PreparedStatement
	 * 
	 */
	private void release() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int updateBatch(String sql, List<Object[]> paramList, int batchCount) {
		int count = 0;// 统计累计插入记录数量
		boolean autocommit = true;
		int[] updateCount = null;
		try {
			autocommit = conn.getAutoCommit();
			ps = conn.prepareStatement(sql);
			if (autocommit)
				conn.setAutoCommit(false);
			for (int i = 0; i < paramList.size(); i++) {
				Object[] params = paramList.get(i);
				if (params != null) {
					for (int j = 1; j < params.length + 1; j++) {
						ps.setObject(j, params[j - 1]);
					}
				}
				ps.addBatch();
				count++;
				if (count % batchCount == 0)
					updateCount = ps.executeBatch();
			}
			ps.executeBatch();
			if (autocommit) {
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release();
		}
		return count == 1 ? updateCount[0] : count;
	}

	@Override
	public Integer persist_return_generated_key(String sql, Object[] params) {
		Integer generatedKeys = null;
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (params != null) {
				for (int i = 1; i < params.length + 1; i++) {
					try {
						ps.setObject(i, params[i - 1]);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			while (rs.next()) {
				generatedKeys = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release();
		}
		return generatedKeys;
	}

	@Override
	public List queryForList(String sql, Object[] params, Integer start,
			Integer end) {
		List resultList = null;
		try {
			if (start != null && end != null) {
				sql = sql + " limit " + start + "," + end;
			}
			System.out.println("limit sql--->" + sql);
			ps = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 1; i < params.length + 1; i++) {
					ps.setObject(i, params[i - 1]);
				}
			}
			rs = ps.executeQuery();
			resultList = ResultSetToBeans.bindDataToBeans(rs, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release();
		}
		return resultList;
	}
}
