package org.podcastpedia.batch.jobs.addpodcast.service;



/**
 * Email notification service for user interaction with podcastpedia.org
 * 
 * @author adrian
 *
 */
public interface EmailNotificationService1 {

	/**
	 * Sends back a confirmation of podcast addition to the podcast submitter
	 *  
	 * @param name
	 * @param email
	 * @param podcastUrl
	 */
	public void sendPodcastAdditionConfirmation(String name, String email, String podcastUrl); 
}
