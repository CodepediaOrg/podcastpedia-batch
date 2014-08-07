package org.podcastpedia.batch.jobs.addpodcast.dao;

import java.util.List;

import org.podcastpedia.batch.common.entities.Category;
import org.podcastpedia.batch.common.entities.Podcast;
import org.podcastpedia.batch.common.entities.Tag;
import org.podcastpedia.batch.common.entities.User;

public interface ReadDao {

	public List<Category> findCategoriesByNames(String[] categoryNames);

	public List<Tag> getTagsByNames(String[] tags);

	public Podcast getPodcastByFeedUrl(String url);		
	
	public User getMeUser();

}
