package org.podcastpedia.batch.jobs.notifysubscribers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.podcastpedia.batch.common.entities.User;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setEmail(rs.getString("email"));
		
		return user;
	}

}
