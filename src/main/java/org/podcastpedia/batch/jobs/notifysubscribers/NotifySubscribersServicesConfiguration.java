package org.podcastpedia.batch.jobs.notifysubscribers;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.podcastpedia.batch.common.configuration.MailConfiguration;
import org.podcastpedia.batch.jobs.notifysubscribers.service.EmailNotificationService;
import org.podcastpedia.batch.jobs.notifysubscribers.service.EmailNotificationServiceImpl;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
@Import(MailConfiguration.class)
@EnableBatchProcessing(modular=true)
public class NotifySubscribersServicesConfiguration {

	@Bean
	public EmailNotificationService emailNotificationService(){
		return new EmailNotificationServiceImpl();
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
