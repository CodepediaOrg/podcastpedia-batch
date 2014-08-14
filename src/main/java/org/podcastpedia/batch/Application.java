package org.podcastpedia.batch;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class Application {

    private static final String NEW_EPISODES_NOTIFICATION_JOB = "newEpisodesNotificationJob";
	private static final String ADD_NEW_PODCAST_JOB = "addNewPodcastJob";

	public static void main(String[] args) throws BeansException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, InterruptedException {
    	
    	Log log = LogFactory.getLog(Application.class);
    	    	
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebEnvironment(false);
        ConfigurableApplicationContext ctx= app.run(args);
        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
    	        		
        if(ADD_NEW_PODCAST_JOB.equals(args[0])){
        	//addNewPodcastJob
        	Job addNewPodcastJob = ctx.getBean(ADD_NEW_PODCAST_JOB, Job.class);
        	JobParameters jobParameters = new JobParametersBuilder()
    		.addDate("date", new Date())
    		.toJobParameters();  
        	
        	JobExecution jobExecution = jobLauncher.run(addNewPodcastJob, jobParameters);
        	
        	BatchStatus batchStatus = jobExecution.getStatus();
        	while(batchStatus.isRunning()){
        		log.info("*********** Still running.... **************");
        		Thread.sleep(1000);
        	}
        	log.info(String.format("*********** Exit status: %s", jobExecution.getExitStatus().getExitCode()));
        	JobInstance jobInstance = jobExecution.getJobInstance();
        	log.info(String.format("********* Name of the job %s", jobInstance.getJobName()));
        	
        	log.info(String.format("*********** job instance Id: %d", jobInstance.getId()));
        	
        	System.exit(0);
        	
        } else if(NEW_EPISODES_NOTIFICATION_JOB.equals(args[0])){
        	JobParameters jobParameters = new JobParametersBuilder()
    		.addDate("date", new Date())
    		.addString("updateFrequency", args[1])
    		.toJobParameters();  
        	
        	jobLauncher.run(ctx.getBean(NEW_EPISODES_NOTIFICATION_JOB,  Job.class), jobParameters);   
        } else {
        	throw new IllegalArgumentException("Please provide a valid Job name as first application parameter");
        }
     
        System.exit(0);
    }
    
}