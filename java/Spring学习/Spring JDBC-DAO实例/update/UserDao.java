package cn.edu.jxau.lang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	private JdbcTemplate jdbcTemplate;

	private static final String SQL_ADD_USER = "INSERT INTO t_user VALUES(NULL,?,?)";

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addUser0(User user) {
		jdbcTemplate.update(SQL_ADD_USER, user.getUsername(), user.getPassword());
	}

	public void addUser1(User user) {

		Object[] params = new Object[] { user.getUsername(), user.getPassword() };
		jdbcTemplate.update(SQL_ADD_USER, params);

	}

	public void addUser2(User user) {

		Object[] params = new Object[] { user.getUsername(), user.getPassword() };
		jdbcTemplate.update(SQL_ADD_USER, params, new int[] { Types.VARCHAR, Types.VARCHAR });
	}

	public void addUser3(User user) {

		jdbcTemplate.update(SQL_ADD_USER, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
			}
		});
	}

	public void addUser4(User user) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SQL_ADD_USER, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
				return ps;
			}
		}, keyHolder);
		System.out.println("add a user:" + keyHolder.getKey().intValue());
	}

	public void addUser5(User user) {

		String[] sql = new String[10];
		Arrays.fill(sql, SQL_ADD_USER);
		int[] arr = jdbcTemplate.batchUpdate(SQL_ADD_USER,new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1,user.getUsername());
				ps.setString(2,user.getPassword()+"-"+i);
			}
			
			@Override
			public int getBatchSize() {
				return 10;
			}
			
		});
		System.out.println(Arrays.toString(arr));
	}
}
