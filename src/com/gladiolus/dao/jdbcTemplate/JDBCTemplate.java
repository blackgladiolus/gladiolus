package com.gladiolus.dao.jdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.gladiolus.dao.jdbcTemplate.callback.ResultSetCallback;
import com.gladiolus.db.DBUtil;

@SuppressWarnings("rawtypes")
public class JDBCTemplate {

	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public JDBCTemplate() {
		try {
			con = DBUtil.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		return con;
	}

	/**
	 * 查询方法模板
	 * 
	 * @param sql
	 * @param params
	 * @param callback
	 * @return
	 * @throws SQLException
	 */

	public List executeQuery(String sql, Object[] params,
			ResultSetCallback callback)  {
		try {
			ps = con.prepareStatement(sql);
			// 设置参数条件
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			rs = ps.executeQuery();
			return callback.doInResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (!con.isClosed())
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}