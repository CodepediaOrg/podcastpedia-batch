package org.podcastpedia.batch.jobs.addpodcast;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.podcastpedia.batch.common.entities.Podcast;
import org.podcastpedia.batch.jobs.addpodcast.model.SuggestedPodcast;
import org.podcastpedia.batch.jobs.addpodcast.service.EmailNotificationService;
import org.podcastpedia.batch.jobs.addpodcast.service.SocialMediaService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class Writer implements ItemWriter<SuggestedPodcast>{

	@Autowired
	private EntityManager entityManager;
	
	@Inject
	private EmailNotificationService emailNotificationService;
	
	@Inject
	private SocialMediaService socialMediaService;
	
	@Override
	public void write(List<? extends SuggestedPodcast> items) throws Exception {

		if(items.get(0) != null){
			SuggestedPodcast suggestedPodcast = items.get(0);
			
			//first insert the data in the database 
			Podcast podcast = suggestedPodcast.getPodcast();
			
			podcast.setInsertionDate(new Date());
			entityManager.persist(podcast);
			entityManager.flush();
			
			//notify submitter about the insertion and post a twitt about it 
			String url = buildUrlOnPodcastpedia(podcast);
			
			emailNotificationService.sendPodcastAdditionConfirmation(
					suggestedPodcast.getName(), suggestedPodcast.getEmail(),
					url);
			if(podcast.getTwitterPage() != null){
				socialMediaService.postOnTwitterAboutNewPodcast(podcast,
				url);				
			}					
		}

	}

	private String buildUrlOnPodcastpedia(Podcast podcast) {
		StringBuffer urlOnPodcastpedia = new StringBuffer(
				"http://www.podcastpedia.org");
		if (podcast.getIdentifier() != null) {
			urlOnPodcastpedia.append("/" + podcast.getIdentifier());
		} else {
			urlOnPodcastpedia.append("/podcasts/");
			urlOnPodcastpedia.append(String.valueOf(podcast.getPodcastId()));
			urlOnPodcastpedia.append("/" + podcast.getTitleInUrl());
		}		
		String url = urlOnPodcastpedia.toString();
		return url;
	}

}
