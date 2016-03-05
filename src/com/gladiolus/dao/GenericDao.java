package com.gladiolus.dao;

import java.sql.Connection;
import java.util.List;

import com.gladiolus.dao.core.TransactionManager;

@SuppressWarnings("rawtypes")
public interface GenericDao<T> {

	/**
	 * 开启connection
	 */
	public void openConnection();

	/**
	 * 获取事务管理器
	 * 
	 * @return
	 */
	public TransactionManager getTransactionManager();

	/**
	 * 释放connection
	 */
	public void closeConnection();

	/**
	 * 获得connection
	 * 
	 * @return
	 */
	public Connection getConnection();

	/**
	 * 数据更新方法
	 * 
	 * @param entity
	 *            要更新的对象，对象中主键的属性值不能为空，其他属性为空表示忽略（不更新）
	 * @return
	 */
	public int update(Object entity);

	/**
	 * 删除方法，根据数据的id
	 * 
	 * @param clazz
	 *            要删除对象的字节码
	 * @param id
	 *            数据行的id
	 * @return
	 */
	public boolean delete(Integer id);

	/**
	 * 删除方法，根据数据的id
	 * 
	 * @param clazz
	 *            要删除对象的字节码
	 * @param ids
	 *            数据行的id
	 * @return
	 */

	public boolean delete(Integer[] ids);

	/**
	 * 数据持久化方法
	 * 
	 * @param sql
	 *            持久化对应的SQL语句
	 * @param params
	 *            SQL语句中占位符对应的参数
	 * @return
	 */
	public int save(String sql, Object[] params);

	/**
	 * 数据持久化方法
	 * 
	 * @param entity
	 *            要保存的对象
	 * @return
	 */
	public int save(Object entity);

	/**
	 * 查询方法，根据对象中属性的值查询数据库中对应的记录
	 * 
	 * @param entity
	 *            要查询的对象，当属性值全为空时查询所有记录
	 * @param start
	 *            开始记录位置,null表示不分页
	 * @param count
	 *            共要查询几条记录,null表示不分页
	 * @return
	 */
	public List queryForList(Object entity, Integer start, Integer count);

	/**
	 * 查询方法
	 * 
	 * @param clazz
	 *            要获取的对象的字节码
	 * @param sql
	 *            查询操作对应的SQL语句
	 * @param start
	 *            开始记录位置,null表示不分页
	 * @param count
	 *            共要查询几条记录,null表示不分页
	 * @return
	 */
	public List queryForList(String sql, Integer start, Integer count);

	/**
	 * 查询数据库中是否存在对象对应的数据项
	 * 
	 * @param entity
	 * @return
	 */
	public boolean containsEntity(Object entity);

	/**
	 * 根据id获取对应的数据
	 * 
	 * @param clazz
	 *            要获取的对象的字节码
	 * @param id
	 *            对应的id
	 * @return
	 */
	public Object getById(Integer id);

	/**
	 * 根据id获取对应的数据
	 * 
	 * @param clazz
	 *            要获取的对象的字节码
	 * @param ids
	 *            对应的id
	 * @return
	 */
	public List getByIds(Integer[] ids);

	/**
	 * 批量更新数据
	 * 
	 * @param sql
	 *            更新操作对应的SQL语句
	 * @param paramList
	 *            SQL中占位符对应的参数
	 * @param batchCount
	 *            每批处理数量，即多少条数据提交一次
	 * @return
	 */
	public int updateBatch(String sql, List<Object[]> paramList, int batchCount);

	/**
	 * 数据持久化（保存）并返回自动生成的主键id
	 * 
	 * @param sql
	 *            持久化对应的SQL语句
	 * @param params
	 *            SQL中占位符对应的参数，可以为空
	 * @return
	 */
	public Integer persist_return_generated_key(String sql, Object[] params);

	/**
	 * 查询方法
	 * 
	 * @param clazz
	 *            要查询的结果对象
	 * @param sql
	 *            对应的SQL语句
	 * @param params
	 *            SQL中占位符对应的参数，可以为空
	 * @param start
	 *            开始记录位置,null表示不分页
	 * @param count
	 *            共要查询几条记录,null表示不分页
	 * @return
	 */
	public List queryForList(String sql, Object[] params, Integer start,
			Integer count);

	/**
	 * 查询方法，根据对象中属性的值查询数据库中对应的记录
	 * 
	 * @param entity
	 * @return
	 */
	public Object queryForObject(Object entity);

	/**
	 * 查询方法
	 * 
	 * @param clazz
	 *            要查询的结果对象
	 * @param sql
	 *            对应的SQL语句
	 * @param params
	 *            SQL中占位符对应的参数，可以为空
	 * @return
	 */
	public Object queryForObject(String sql, Object[] params);
}