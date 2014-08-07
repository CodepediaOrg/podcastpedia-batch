package org.podcastpedia.batch.jobs.addpodcast;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.podcastpedia.batch.common.configuration.MailConfiguration;
import org.podcastpedia.batch.jobs.addpodcast.dao.ReadDao;
import org.podcastpedia.batch.jobs.addpodcast.dao.ReadDaoImpl;
import org.podcastpedia.batch.jobs.addpodcast.service.EmailNotificationService1;
import org.podcastpedia.batch.jobs.addpodcast.service.EmailNotificationServiceImpl1;
import org.podcastpedia.batch.jobs.addpodcast.service.PodcastAndEpisodeAttributesService;
import org.podcastpedia.batch.jobs.addpodcast.service.PodcastAndEpisodeAttributesServiceImpl;
import org.podcastpedia.batch.jobs.addpodcast.service.SocialMediaService;
import org.podcastpedia.batch.jobs.addpodcast.service.SocialMediaServiceImpl;
import org.podcastpedia.batch.jobs.addpodcast.service.SyndFeedService;
import org.podcastpedia.batch.jobs.addpodcast.service.SyndFeedServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
@Import(MailConfiguration.class)
public class ServicesConfiguration {

	@Bean
	public ReadDao readDao(){
		return new ReadDaoImpl();
	}
	
	@Bean
	public SyndFeedService syndFeedService(){
		return new SyndFeedServiceImpl();
	}
	
	@Bean
	public PodcastAndEpisodeAttributesService podcastAndEpisodeAttributesService(){
		return new PodcastAndEpisodeAttributesServiceImpl();
	}
	
	@Bean
	public EmailNotificationService1 emailNotificationService1(){
		return new EmailNotificationServiceImpl1();
	}
	
	@Bean
	public SocialMediaService socialMediaService(){
		return new SocialMediaServiceImpl();
	}
	
	@Bean
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(){
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		poolingHttpClientConnectionManager.setMaxTotal(100);
		
		return poolingHttpClientConnectionManager;
	}
	
	@Bean
	public VelocityEngine velocityEngine() throws VelocityException, IOException{
	    VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
	    Properties props = new Properties();
	    props.put("resource.loader", "class");
	    props.put("class.resource.loader.class", 
	              "org.apache.velocity.runtime.resource.loader." + 
	              "ClasspathResourceLoader");
	    factory.setVelocityProperties(props);
	    
	    return factory.createVelocityEngine();
	}
}
