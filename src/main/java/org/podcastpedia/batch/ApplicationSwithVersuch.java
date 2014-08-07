package org.podcastpedia.batch;

import java.util.Date;

import org.podcastpedia.batch.jobs.addpodcast.AddPodcastJobConfiguration;
import org.podcastpedia.batch.jobs.notifysubscribers.NotifySubscribersJobConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class ApplicationSwithVersuch {

    @SuppressWarnings("resource")
	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
    	    	                      
    	JobLauncher jobLauncher;
        switch (args[0]){
        case "addNewPodcastJob":
	        AnnotationConfigApplicationContext addPodcastJobAppContext = new AnnotationConfigApplicationContext(AddPodcastJobConfiguration.class);
	        jobLauncher = addPodcastJobAppContext.getBean(JobLauncher.class);
	        Job addNewPodcastJob = addPodcastJobAppContext.getBean("addNewPodcastJob", Job.class );                  
	        JobParameters addNewPodcastJobParams = new JobParametersBuilder()
	                       .addDate("date", new Date())
	                       .toJobParameters();
	        JobExecution addNewPodcastJobExecution = jobLauncher.run(addNewPodcastJob, addNewPodcastJobParams);                     
	        break;
        case "emailNotificationAboutNewEpisodesJob":
        	AnnotationConfigApplicationContext notifySubscribersJobAppContext = new AnnotationConfigApplicationContext(NotifySubscribersJobConfiguration.class);
	        Job notifySubscribersJob = notifySubscribersJobAppContext.getBean("emailNotificationAboutNewEpisodesJob", Job.class );     
	        jobLauncher = notifySubscribersJobAppContext.getBean(JobLauncher.class);
	        JobParameters notifySubscribersJobParams = new JobParametersBuilder()
	                       .addDate("date", new Date())
	                       .toJobParameters();
	        JobExecution notifySubscribersJobExecution = jobLauncher.run(notifySubscribersJob, notifySubscribersJobParams);
	        break;
        default:
        	throw new IllegalArgumentException("please provide a valid job name as first app parameter to execute " + args[0]);       
        }                
    
    }

}