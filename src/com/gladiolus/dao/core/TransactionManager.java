package com.gladiolus.dao.core;

import java.sql.Connection;
import java.sql.SQLException;

import com.gladiolus.db.DBUtil;

/**
 * 事务管理
 * 
 * @author xieguoping
 * 
 */
public class TransactionManager {

	private Connection conn;

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	protected TransactionManager(Connection conn) {
		this.conn = conn;
	}

	protected TransactionManager(){
		
	}
	/**
	 * 开启事务
	 */

	public void startTransaction() {
		DBUtil.beginTransaction();
	}

	/**
	 * 设置事务隔离级别(1到5)
	 */

	public void setTransactionLevel(int Level) {
		try {
			conn.setTransactionIsolation(Level);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否开启了事务 true表明外部已经开启事务，false表示没有开启事务
	 * 
	 * @return
	 */

	public boolean isStartTransacion() {
		try {
			return !conn.getAutoCommit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 事务回滚
	 */

	public void rollback() {
		DBUtil.rollback();
	}

	/**
	 * 事务提交
	 */

	public void commit() {
		DBUtil.commit();
	}

}
