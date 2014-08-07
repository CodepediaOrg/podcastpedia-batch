package org.podcastpedia.batch.jobs.addpodcast.dao;

import org.podcastpedia.batch.common.entities.Episode;
import org.podcastpedia.batch.common.entities.Podcast;


public interface InsertDao {
	
	/**
	 * Inserts episode in the database 
	 * @param episode
	 */
	public void insertEpisode(Episode episode);
	
	/**
	 * Adds a new podcast to the database 
	 * 
	 * @param podcast
	 */
	public void addPodcast(Podcast podcast);		


}
