package org.podcastpedia.batch.jobs.notifysubscribers;

import javax.sql.DataSource;

import org.podcastpedia.batch.common.configuration.InfrastructureConfiguration;
import org.podcastpedia.batch.common.entities.User;
import org.podcastpedia.batch.common.listeners.LogProcessListener;
import org.podcastpedia.batch.common.listeners.ProtocolListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Configuration
@EnableBatchProcessing
@Import({InfrastructureConfiguration.class, NotifySubscribersServicesConfiguration.class})
public class NotifySubscribersJobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilders;
 
	@Autowired
	private StepBuilderFactory stepBuilders;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public Job newEpisodesNotificationJob(){
		return jobBuilders.get("newEpisodesNotificationJob")
				.listener(protocolListener())
				.start(notifySubscribersStep())
				.build();
	}	
	
	@Bean
	public Step notifySubscribersStep(){
		return stepBuilders.get("notifySubscribersStep")
				.<User,User>chunk(1) //important to be one in this case to commit after every line read
				.reader(notifySubscribersReader())
				.processor(notifySubscribersProcessor())
				.writer(notifySubscribersWriter())
				.listener(logProcessListener())
				.faultTolerant()
				.skipLimit(10) //default is set to 0
				.skip(MySQLIntegrityConstraintViolationException.class)
				.build();
	}	
	
	@Bean
	public ItemReader<User> notifySubscribersReader(){
		
		JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<User>();
		String sql = "select * from users where is_email_subscriber is not null";
		
		reader.setSql(sql);
		reader.setDataSource(dataSource);
		reader.setRowMapper(rowMapper());		
	
		return reader;
	}

	@Bean
	public UserRowMapper rowMapper(){
		return new UserRowMapper();
	}

	/** configure the processor related stuff */
    @Bean
    @StepScope
    public ItemProcessor<User, User> notifySubscribersProcessor() {
        return new NotifySubscribersItemProcessor();
    }
    
    @Bean
    public ItemWriter<User> notifySubscribersWriter() {
    	return new NotifySubscribersWriter();
    }
    
	@Bean
	public ProtocolListener protocolListener(){
		return new ProtocolListener();
	}
 
	@Bean
	public LogProcessListener logProcessListener(){
		return new LogProcessListener();
	}    
}
