package org.podcastpedia.batch.jobs.addpodcast.service;

import org.podcastpedia.batch.common.entities.Podcast;


/**
 * Social media services interaction 
 */
public interface SocialMediaService {
	
	public void postOnTwitterAboutNewPodcast(Podcast podcast, String urlOnPodcastpedia);
	
}
