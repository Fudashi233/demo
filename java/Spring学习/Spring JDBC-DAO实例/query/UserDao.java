package cn.edu.jxau.lang;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	private JdbcTemplate jdbcTemplate;

	private static final String SQL_QUERY_USER_BY_ID = " SELECT * FROM t_user WHERE id=? ";

	private static final String SQL_QUERY_ALL_USER = " SELECT * FROM t_user ";
	
	private static final String SQL_QUERY_COUNT = " SELECT COUNT(*) FROM t_user ";
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public User query0(int id) {

		User user = new User();
		jdbcTemplate.query(SQL_QUERY_USER_BY_ID, new Object[] { id }, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
			}
		});
		return user;
	}

	public List<User> query1() {

		List<User> userList = new ArrayList<>();
		jdbcTemplate.query(SQL_QUERY_ALL_USER, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				User user = new User();
				user.setPassword(rs.getString("password"));
				user.setUsername(rs.getString("username"));
				userList.add(user);
			}
		});
		return userList;
	}

	public List<User> query2() {

		return jdbcTemplate.query(SQL_QUERY_ALL_USER, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setPassword(rs.getString("password"));
				user.setUsername(rs.getString("username"));
				return user;
			}
		});
	}

	public int query3() {

		RowCountCallbackHandler rowCountCallbackHandler = new RowCountCallbackHandler();
		jdbcTemplate.query(SQL_QUERY_ALL_USER, rowCountCallbackHandler);
		return rowCountCallbackHandler.getRowCount();
	}

	public int query4() {

		return jdbcTemplate.queryForObject(SQL_QUERY_COUNT,Integer.class);
	}
}
