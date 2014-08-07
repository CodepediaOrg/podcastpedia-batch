package org.podcastpedia.batch.common.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfiguration {

	@Autowired
	private Environment environment;

	/**
	 * The Java Mail sender.
	 * It's not generally expected for mail sending to work in embedded mode.
	 * Since this mail sender is always invoked asynchronously, this won't cause problems for the developer.
	 */
	@Bean
	public JavaMailSender mailSender() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setHost(environment.getProperty("mail.smtp.host"));
		mailSender.setPort(Integer.valueOf(environment.getProperty("mail.port")));
		mailSender.setProtocol(environment.getProperty("mail.protocol"));
		mailSender.setUsername(environment.getProperty("mail.username"));
		mailSender.setPassword(environment.getProperty("mail.password"));
		
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth", Boolean.class, true));		
		mailSender.setJavaMailProperties(properties);
		
		return mailSender;
	}	
}
