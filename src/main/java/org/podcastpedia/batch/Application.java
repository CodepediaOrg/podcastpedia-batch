package org.podcastpedia.batch;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
    	    	
//        ApplicationContext ctx = SpringApplication.run(ApplicationSimpleRun.class, args);
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebEnvironment(false);
        ApplicationContext ctx= app.run(args);
        
        System.out.println("************************************************************************************");        
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        
        System.out.println("************************************************************************************");        
    
    }

}