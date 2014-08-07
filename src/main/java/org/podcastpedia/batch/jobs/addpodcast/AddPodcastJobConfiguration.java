package org.podcastpedia.batch.jobs.addpodcast;

import org.podcastpedia.batch.common.configuration.StandaloneInfrastructureConfiguration;
import org.podcastpedia.batch.common.listeners.LogProcessListener;
import org.podcastpedia.batch.common.listeners.ProtocolListener;
import org.podcastpedia.batch.jobs.addpodcast.model.SuggestedPodcast;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Configuration
@EnableBatchProcessing
@Profile({"dev_work"})
@Import({StandaloneInfrastructureConfiguration.class, ServicesConfiguration.class})
public class AddPodcastJobConfiguration {

	@Autowired
	private JobBuilderFactory jobs;
 
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job addNewPodcastJob(){
		return jobs.get("addNewPodcastJob")
				.incrementer(new RunIdIncrementer())
				.listener(protocolListener())
				.start(step())
				.build();
	}	
	
	@Bean
	public Step step(){
		return stepBuilderFactory.get("step")
				.<SuggestedPodcast,SuggestedPodcast>chunk(1) //important to be one in this case to commit after every line read
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.listener(logProcessListener())
				.faultTolerant()
				.skipLimit(10) //default is set to 0
				.skip(MySQLIntegrityConstraintViolationException.class)
				.build();
	}	
	
	@Bean
	public ItemReader<SuggestedPodcast> reader(){
		FlatFileItemReader<SuggestedPodcast> reader = new FlatFileItemReader<SuggestedPodcast>();
		reader.setLinesToSkip(1);//first line is title definition 
		reader.setResource(new ClassPathResource("suggested-podcasts.txt"));
		reader.setLineMapper(lineMapper());
		return reader; 
	}

	@Bean
	public LineMapper<SuggestedPodcast> lineMapper() {
		DefaultLineMapper<SuggestedPodcast> lineMapper = new DefaultLineMapper<SuggestedPodcast>();
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(";");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[]{"FEED_URL", "IDENTIFIER_ON_PODCASTPEDIA", "CATEGORIES", "LANGUAGE", "MEDIA_TYPE", "UPDATE_FREQUENCY", "KEYWORDS", "FB_PAGE", "TWITTER_PAGE", "GPLUS_PAGE", "NAME_SUBMITTER", "EMAIL_SUBMITTER"});
		
		BeanWrapperFieldSetMapper<SuggestedPodcast> fieldSetMapper = new BeanWrapperFieldSetMapper<SuggestedPodcast>();
		fieldSetMapper.setTargetType(SuggestedPodcast.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(suggestedPodcastFieldSetMapper());
		
		return lineMapper;
	}

	@Bean
	public SuggestedPodcastFieldSetMapper suggestedPodcastFieldSetMapper() {
		return new SuggestedPodcastFieldSetMapper();
	}

	/** configure the processor related stuff */
    @Bean
    public ItemProcessor<SuggestedPodcast, SuggestedPodcast> processor() {
        return new SuggestedPodcastItemProcessor();
    }
    
    @Bean
    public ItemWriter<SuggestedPodcast> writer() {
    	return new Writer();
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
