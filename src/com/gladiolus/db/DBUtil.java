package com.gladiolus.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtil {

	private static ComboPooledDataSource ds = null;
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	static {
		ds = new ComboPooledDataSource("mysqlConfig");
	}

	/**
	 * 从连接池中得到数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = threadLocal.get();
		if (conn == null) {
			conn = getDataSource().getConnection();
			threadLocal.set(conn);
		}
		return conn;
	}

	public static DataSource getDataSource() {
		return ds;
	}

	/**
	 * 开启事务
	 */
	public static void beginTransaction() {
		try {
			Connection conn = threadLocal.get();
			if (conn == null) {
				conn = getConnection();
				threadLocal.set(conn); // 把 conn绑定到当前线程上
			}
			conn.setAutoCommit(false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 事物回滚
	 */
	public static void rollback() {
		try {
			Connection conn = threadLocal.get();
			if (conn != null) {
				conn.rollback();
				conn.setAutoCommit(true);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 事物提交
	 */
	public static void commit() {
		try {
			Connection conn = threadLocal.get();
			if (conn != null) {
				conn.commit();
				conn.setAutoCommit(true);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 释放资源
	 */
	public static void releaseConnection() {
		try {
			Connection conn = threadLocal.get();
			if (conn != null) {
				conn.close();
				threadLocal.remove(); // 解除当前线程上绑定conn
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
