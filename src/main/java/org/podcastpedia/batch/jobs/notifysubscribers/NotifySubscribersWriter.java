package org.podcastpedia.batch.jobs.notifysubscribers;

import java.util.List;

import javax.inject.Inject;

import org.podcastpedia.batch.common.entities.User;
import org.podcastpedia.batch.jobs.notifysubscribers.service.EmailNotificationService;
import org.springframework.batch.item.ItemWriter;

public class NotifySubscribersWriter implements ItemWriter<User> {

	@Inject
	private EmailNotificationService emailNotificationService;
	
	@Override
	public void write(List<? extends User> items) throws Exception {

		for(User item : items){			
			System.out.println("************* WRITER **************");
			emailNotificationService.sendNewEpisodesNotification(item);
		}

	}

}
