package org.podcastpedia.batch.common.configuration;

import org.podcastpedia.batch.jobs.addpodcast.AddPodcastJobConfiguration;
import org.podcastpedia.batch.jobs.notifysubscribers.NotifySubscribersJobConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing(modular=true)
public class AppConfig {
    
    @Bean
    public ApplicationContextFactory addNewPodcastJobs(){
    	return new GenericApplicationContextFactory(AddPodcastJobConfiguration.class);
    }
    
    @Bean
    public ApplicationContextFactory newEpisodesNotificationJobs(){
    	return new GenericApplicationContextFactory(NotifySubscribersJobConfiguration.class);
    }    
    
}
