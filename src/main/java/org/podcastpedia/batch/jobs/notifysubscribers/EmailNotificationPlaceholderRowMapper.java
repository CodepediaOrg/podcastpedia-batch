package org.podcastpedia.batch.jobs.notifysubscribers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.podcastpedia.batch.jobs.notifysubscribers.model.EmailNotificationPlaceholder;
import org.springframework.jdbc.core.RowMapper;

public class EmailNotificationPlaceholderRowMapper implements RowMapper<EmailNotificationPlaceholder> {

	@Override
	public EmailNotificationPlaceholder mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		EmailNotificationPlaceholder emailNotificationPlaceholder = new EmailNotificationPlaceholder();
		
		emailNotificationPlaceholder.setEmail(rs.getString("email"));
		emailNotificationPlaceholder.setPodcastId(rs.getInt("podcast_id"));
		emailNotificationPlaceholder.setPodcastTitle(rs.getString("title"));
		emailNotificationPlaceholder.setPodcastTitleInUrl(rs.getString("title_in_url"));
		emailNotificationPlaceholder.setEpisodeId(rs.getInt("episode_id"));
		emailNotificationPlaceholder.setEpisodeTitle(rs.getString("episode_title"));
		emailNotificationPlaceholder.setEpisodeTitleInUrl(rs.getString("episode_title_in_url"));
		emailNotificationPlaceholder.setEpisodePublicationDate(rs.getDate("publication_date"));
		
		return emailNotificationPlaceholder;
	}

}
