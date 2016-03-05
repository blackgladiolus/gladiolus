package com.gladiolus.dao.jdbcTemplate.callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface ResultSetCallback {
	List doInResultSet(ResultSet rs) throws SQLException;
}