package org.podcastpedia.batch;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.podcastpedia.batch.common.listeners.LogProcessListener;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class ApplicationWithJobLauncher {

    public static void main(String[] args) throws BeansException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, InterruptedException {
    	
    	Log log = LogFactory.getLog(ApplicationWithJobLauncher.class);
    	    	
//        ApplicationContext ctx = SpringApplication.run(ApplicationSimpleRun.class, args);
        SpringApplication app = new SpringApplication(ApplicationWithJobLauncher.class);
        app.setWebEnvironment(false);
        ConfigurableApplicationContext ctx= app.run(args);
        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        if("1".equals(args[0])){
        	//addNewPodcastJob
        	JobExecution jobExecution = jobLauncher.run(ctx.getBean("addNewPodcastJob", Job.class), new JobParameters());
        	
        	BatchStatus batchStatus = jobExecution.getStatus();
        	while(batchStatus.isRunning()){
        		log.info("*********** Still running.... **************");
        		Thread.sleep(1000);
        	}
        	log.info(String.format("*********** Exit status: %s", jobExecution.getExitStatus().getExitCode()));
        	JobInstance jobInstance = jobExecution.getJobInstance();
        	
        	log.info(String.format("*********** job instance Id: %d", jobInstance.getId()));
        	
        	System.exit(0);
        	
        } else {
        	jobLauncher.run(ctx.getBean("newEpisodesNotificationJob",  Job.class), new JobParameters());   
        }   
        
//        System.out.println("************************************************************************************");        
//        
//        System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//        String[] beanNames = ctx.getBeanDefinitionNames();
//        Arrays.sort(beanNames);
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
//        
//        System.out.println("************************************************************************************");        
    
    }

}