package com.gladiolus.dao.help;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.gladiolus.dao.annotatioin.AnnotationUtil;
import com.gladiolus.dao.annotatioin.ForeignColumn;
import com.gladiolus.dao.annotatioin.InderectColumn;
import com.gladiolus.util.ClassUtil;

@SuppressWarnings({ "unused", "rawtypes" })
public class BuildSql {

	// 构建sql中需要连接的所有的表
	private Set<String> tables = new HashSet<String>();
	// 表与表间的连接条件
	private Set<String> connectives = new HashSet<String>();
	// 实体注解工具类
	private AnnotationUtil annotationUtil = new AnnotationUtil();

	/**
	 * 构建插入的Sql语句
	 * 
	 * @param obj
	 * @return
	 */

	public BuildResult buildSaveSql(Object obj) {
		// sql语句构建结果返回类
		BuildResult constructrResult = new BuildResult();
		// 构建完成的sql语句
		StringBuilder sql = new StringBuilder();
		// 构建完成的sql语句对应的参数
		List<Object> params = new ArrayList<Object>();

		sql.append("insert into ");
		Class clazz = obj.getClass();
		if (!annotationUtil.isHasTableAnnotation(clazz)) {
			throw new RuntimeException("构建插入SQL语句失败! 原因：该类不存在@Table注解!");
		}
		sql.append(annotationUtil.getClassTable(clazz) + "(");
		Field[] fields = clazz.getDeclaredFields();
		try {
			for (Field field : fields) {
				Object value = BeanUtils.getProperty(obj, field.getName());
				if (value != null) {
					sql.append(field.getName() + ",");
					params.add(value);
				}
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			sql.append(")").append("values(");
			for (Object temp : params) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			sql.append(")");
		} catch (Exception e) {
			e.printStackTrace();
		}
		constructrResult.setSql(sql.toString());
		constructrResult.setParams(params.toArray());
		return constructrResult;
	}

	/**
	 * 构建更新的SQL语句
	 * 
	 * @param obj
	 * @return
	 */
	public BuildResult buildUpdateSQL(Object obj) {
		// sql语句构建结果返回类
		BuildResult constructrResult = new BuildResult();
		// 构建完成的sql语句
		StringBuilder sql = new StringBuilder();
		// 构建完成的sql语句对应的参数
		List<Object> params = new ArrayList<Object>();

		sql.append("update ");
		Class clazz = obj.getClass();
		if (!annotationUtil.isHasTableAnnotation(clazz)) {// 判断是否存在Table注解
			throw new RuntimeException("构建更新SQL语句失败! 原因：该类不存在@Table注解!");
		}
		sql.append(annotationUtil.getClassTable(clazz) + " set ");
		try {
			if (BeanUtils.getProperty(obj, annotationUtil
					.getPrimaryField(clazz).getName()) == null) {
				throw new RuntimeException("构建更新SQL语句失败! 原因：主键未设值!");
			}
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				Object value = BeanUtils.getProperty(obj, field.getName());
				if (value != null) {
					sql.append(field.getName() + "=?,");
					params.add(value);
				}
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			Object value = BeanUtils.getProperty(obj, annotationUtil
					.getPrimaryField(clazz).getName());
			if (value != null) {
				sql.append(" where "
						+ annotationUtil.getPrimaryField(clazz).getName()
						+ "=?");
				params.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		constructrResult.setSql(sql.toString());
		constructrResult.setParams(params.toArray());
		return constructrResult;
	}

	/**
	 * 构建按id删除的SQL语句
	 * 
	 * @param clazz
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unused")
	public BuildResult buildDeleteSql(Class clazz, Integer[] ids) {
		// sql语句构建结果返回类
		BuildResult constructrResult = new BuildResult();
		// 构建完成的sql语句
		StringBuilder sql = new StringBuilder();
		// 构建完成的sql语句对应的参数
		List<Object> params = new ArrayList<Object>();

		sql.append("delete from ");
		if (!annotationUtil.isHasTableAnnotation(clazz)) {// 判断是否存在Table注解
			throw new RuntimeException("构建删除SQL语句失败! 原因：该类不存在@Table注解!");
		}
		sql.append(annotationUtil.getClassTable(clazz) + " where ");
		Field field = annotationUtil.getPrimaryField(clazz);
		sql.append(" " + field.getName() + " in(");
		for (Integer id : ids) {
			sql.append("?,");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		constructrResult.setSql(sql.toString());
		constructrResult.setParams(params.toArray());
		return constructrResult;
	}

	/**
	 * 构建删除的SQL语句
	 * 
	 * @param obj
	 * @return
	 */
	public BuildResult buildDeleteSql(Object obj) {
		// sql语句构建结果返回类
		BuildResult constructrResult = new BuildResult();
		// 构建完成的sql语句
		StringBuilder sql = new StringBuilder();
		// 构建完成的sql语句对应的参数
		List<Object> params = new ArrayList<Object>();

		sql.append("delete from ");
		Class clazz = obj.getClass();
		if (!annotationUtil.isHasTableAnnotation(clazz)) {// 判断是否存在TableAnnotation注解
			return null;
		}
		sql.append(annotationUtil.getClassTable(clazz) + " where 1=1");

		try {
			if (BeanUtils.getProperty(obj, annotationUtil
					.getPrimaryField(clazz).getName()) == null) {
				throw new RuntimeException("构建删除SQL语句失败! 原因：主键未设值!");
			}
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				Object value = BeanUtils.getProperty(obj, field.getName());
				if (value != null) {
					sql.append(" and " + field.getName() + "=?");
					params.add(value);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		constructrResult.setSql(sql.toString());
		constructrResult.setParams(params.toArray());
		return constructrResult;
	}

	public BuildResult buildQuery(Object... entitys) {

		this.getAllTables(entitys);

		this.buildForeignConnective(ClassUtil.getClass(entitys));
		this.buildInderectConnetionPart(ClassUtil.getClass(entitys));

		StringBuilder sql = this.buildSelectPart(tables);

		sql.append(" where 1=1");

		for (String con : connectives) {
			sql.append(" and ").append(con);
		}
		BuildResult buildResult = this.buildWhereConnective(entitys);
		sql.append(buildResult.getSql());

		buildResult.setSql(sql.toString());

		return buildResult;

	}

	/**
	 * 获得类级别的所有表名
	 * 
	 * @param objects
	 */
	private void getAllTables(Object[] objects) {
		for (Object obj : objects) {
			tables.add(annotationUtil.getClassTable(obj.getClass()));
		}
	}

	/**
	 * 构建select部分语句
	 * 
	 * @param tables
	 * @return
	 */
	private StringBuilder buildSelectPart(Set<String> tables) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ");
		for (String table : tables) {
			sql.append(table).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		return sql;
	}

	/**
	 * 
	 * 获得两个类之间外键的连接条件
	 * 
	 * @param frist
	 *            第一个类
	 * @param second
	 *            第二个类
	 */
	private void getReferenceConnective(Class frist, Class second) {
		// 第一个类中注解为外键的属性
		List<Field> fristFields = annotationUtil.getForeignColumns(frist);

		String fristTable = annotationUtil.getClassTable(frist);

		String secondTable = annotationUtil.getClassTable(second);

		for (Field fristField : fristFields) {

			ForeignColumn fristForeignColumn = fristField
					.getAnnotation(ForeignColumn.class);

			if (secondTable.equalsIgnoreCase(fristForeignColumn.Table())) {
				// 添加frist和second实体间外键的连接条件
				connectives.add(fristForeignColumn.Table() + "."
						+ fristForeignColumn.ColumnName() + "=" + fristTable
						+ "." + fristField.getName());
			}
		}

	}

	/**
	 * 获得所有类间的外键连接条件
	 * 
	 * @param calzzs
	 */
	private void buildForeignConnective(Class[] calzzs) {
		for (int i = 0; i < calzzs.length; i++) {
			Class outTemp = calzzs[i];
			for (int j = i + 1; j < calzzs.length; j++) {
				Class inTemp = calzzs[j];
				this.getReferenceConnective(outTemp, inTemp);
				this.getReferenceConnective(inTemp, outTemp);
			}
		}

	}

	/**
	 * 获得所有类中的中间表连接条件
	 * 
	 * @param calzzs
	 */
	private void buildInderectConnetionPart(Class[] calzzs) {
		for (int i = 0; i < calzzs.length; i++) {
			Class outTemp = calzzs[i];
			for (int j = i + 1; j < calzzs.length; j++) {
				Class inTemp = calzzs[j];
				this.getInderectConnective(outTemp, inTemp);
			}
		}
	}

	/**
	 * 获得获得两个类的中间表连接条件
	 * 
	 * @param frist
	 * @param second
	 */
	private void getInderectConnective(Class frist, Class second) {

		List<Field> fristFields = annotationUtil.getInderectColumns(frist);

		List<Field> secondFields = annotationUtil.getInderectColumns(second);

		for (Field fristField : fristFields) {
			InderectColumn fristInderectColumn = fristField
					.getAnnotation(InderectColumn.class);

			for (Field secondField : secondFields) {
				InderectColumn secondInderectColumn = secondField
						.getAnnotation(InderectColumn.class);

				if (fristInderectColumn.Table().equalsIgnoreCase(
						secondInderectColumn.Table())) {
					// 添加连接条件
					connectives.add(fristInderectColumn.Table() + "."
							+ fristInderectColumn.ColumnName() + "="
							+ annotationUtil.getClassTable(frist) + "."
							+ fristField.getName());

					connectives.add(fristInderectColumn.Table() + "."
							+ secondInderectColumn.ColumnName() + "="
							+ annotationUtil.getClassTable(second) + "."
							+ secondField.getName());
					// 添加表名
					tables.add(fristInderectColumn.Table());
				}

			}
		}
	}

	private BuildResult buildWhereConnective(Object[] objs) {
		BuildResult buildResult = new BuildResult();
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		for (Object obj : objs) {

			Field[] fields = obj.getClass().getDeclaredFields();
			try {

				for (Field field : fields) {
					Object value = BeanUtils.getProperty(obj, field.getName());
					if (value != null) {
						sql.append(" and " + field.getName() + "=?");
						params.add(value);
					}
				}

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		buildResult.setSql(sql.toString());
		buildResult.setParams(params.toArray());
		return buildResult;

	}

}
