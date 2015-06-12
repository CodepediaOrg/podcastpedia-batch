package org.podcastpedia.batch.jobs.addpodcast;

import org.podcastpedia.batch.common.configuration.InfrastructureConfiguration;
import org.podcastpedia.batch.common.listeners.LogProcessListener;
import org.podcastpedia.batch.common.listeners.ProtocolListener;
import org.podcastpedia.batch.jobs.addpodcast.model.SuggestedPodcast;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileFilter;

@Configuration
@EnableBatchProcessing
@Import({InfrastructureConfiguration.class, ServicesConfiguration.class})
public class AddPodcastJobConfiguration {

	public static final String OVERRIDEN_BY_EXPRESSION_VALUE = "overriden by expression value";
	@Autowired
	private JobBuilderFactory jobs;
 
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job addNewPodcastJob(){
		return jobs.get("addNewPodcastJob")
				.listener(protocolListener())
				.start(step())
				.build();
	}	
	
	@Bean
	public Step step(){
		return stepBuilderFactory.get("step")
				.<SuggestedPodcast,SuggestedPodcast>chunk(1) //important to be one in this case to commit after every line read
				.reader(reader(OVERRIDEN_BY_EXPRESSION_VALUE))
				.processor(processor())
				.writer(writer())
				.listener(logProcessListener())
				.faultTolerant()
				.skipLimit(10) //default is set to 0
				.skip(Exception.class)
				.build();
	}	
	
	@Bean
	@StepScope
	public FlatFileItemReader<SuggestedPodcast> reader(@Value("#{jobParameters[directoryPath]}") String directoryPath){
		FlatFileItemReader<SuggestedPodcast> reader = new FlatFileItemReader<SuggestedPodcast>();
		reader.setLinesToSkip(1);//first line is title definition 
		reader.setResource(getFileFromDirectory(directoryPath));
		reader.setLineMapper(lineMapper());

		return reader; 
	}

	private Resource getFileFromDirectory(String directoryPath) {

		File fl = new File(directoryPath);

		File[] files = fl.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});

		if(files.length != 1) throw new RuntimeException("There must be only one file present in the folder to be processed");

		return new FileSystemResource(files[0]);
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
