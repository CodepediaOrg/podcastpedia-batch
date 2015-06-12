package org.podcastpedia.batch;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
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

	public static void main(String[] args) throws BeansException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, InterruptedException, IOException {
    	
    	Log log = LogFactory.getLog(Application.class);
    	    	
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebEnvironment(false);
        ConfigurableApplicationContext ctx = app.run(args);
        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
    	        		
        if(ADD_NEW_PODCAST_JOB.equals(args[0])){
        	//addNewPodcastJob
        	Job addNewPodcastJob = ctx.getBean(ADD_NEW_PODCAST_JOB, Job.class);
        	JobParameters jobParameters = new JobParametersBuilder()
    		.addDate("date", new Date())
			.addString("directoryPath", args[1])
    		.toJobParameters();  
        	
        	JobExecution jobExecution = jobLauncher.run(addNewPodcastJob, jobParameters);
        	
        	BatchStatus batchStatus = jobExecution.getStatus();
        	while(batchStatus.isRunning()){
        		log.info("*********** Still running.... **************");
        		Thread.sleep(1000);
        	}
			ExitStatus exitStatus = jobExecution.getExitStatus();
			String exitCode = exitStatus.getExitCode();
			log.info(String.format("*********** Exit status: %s", exitCode));

			if(exitStatus.equals(ExitStatus.COMPLETED)){
				renameFile(args[1]);
			}

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

	private static void renameFile(String directoryPath) throws IOException {
		File fl = new File(directoryPath);

		File[] files = fl.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});

		if(files.length != 1) throw new RuntimeException("There must be only one file present in the folder to be processed");
		File file = files[0];
		String fileName = file.getName();
		String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
		String fileNameExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		String newFileName = fileNameWithoutExtension + "-completed" + fileNameExtension;

        Path source = file.toPath();
        Files.move(source, source.resolveSibling(newFileName));
    }

}