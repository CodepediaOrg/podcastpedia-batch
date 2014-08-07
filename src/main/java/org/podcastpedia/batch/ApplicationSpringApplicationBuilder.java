package org.podcastpedia.batch;

import java.util.Arrays;

import org.podcastpedia.batch.common.configuration.AppConfig;
import org.podcastpedia.batch.jobs.addpodcast.AddPodcastJobConfiguration;
import org.podcastpedia.batch.jobs.notifysubscribers.NotifySubscribersJobConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class ApplicationSpringApplicationBuilder {

    public static void main(String[] args) {
    	    	
        ConfigurableApplicationContext configurableApplicationContext = new SpringApplicationBuilder()
        .showBanner(false)
        .sources(AppConfig.class)
        .child(AddPodcastJobConfiguration.class)
        .child(NotifySubscribersJobConfiguration.class)
        .run(args);
        
            
        System.out.println("************************************************************************************");        
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = configurableApplicationContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        
        System.out.println("************************************************************************************");        
    
    }

}