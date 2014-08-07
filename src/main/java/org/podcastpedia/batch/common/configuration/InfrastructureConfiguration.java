package org.podcastpedia.batch.common.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public interface InfrastructureConfiguration {
	
	@Bean
	public abstract DataSource dataSource();

	@Bean
    public abstract LocalContainerEntityManagerFactoryBean entityManagerFactory();
 
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf);
   
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation();
   
}
