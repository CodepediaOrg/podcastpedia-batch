package org.podcastpedia.batch.jobs.notifysubscribers.service;

import org.podcastpedia.batch.common.entities.User;



/**
 * Email notification service for new episodes on the subscribed podcasts
 * 
 *
 */
public interface EmailNotificationService {

	public void sendNewEpisodesNotification(User emailSubscriber); 
}
