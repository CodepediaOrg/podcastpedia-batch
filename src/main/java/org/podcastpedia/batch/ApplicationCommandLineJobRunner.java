package org.podcastpedia.batch;

import java.util.Arrays;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class ApplicationCommandLineJobRunner {

    public static void main(String[] args) throws Exception {
    	
    	CommandLineJobRunner command = new CommandLineJobRunner();
    	
    	CommandLineJobRunner.main(args);
    	    	
        ApplicationContext ctx = SpringApplication.run(ApplicationCommandLineJobRunner.class, args);
        
        System.out.println("******************************************");        
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        
        System.out.println("******************************************");        
    
    }

}