package org.podcastpedia.batch.jobs.addpodcast.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class EmailNotificationServiceImpl1 implements EmailNotificationService1 {
	
	@Inject
    private JavaMailSender mailSender;
	
	@Inject
    private VelocityEngine velocityEngine;

	public void sendPodcastAdditionConfirmation(final String name, final String email, final String podcastUrl) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {


			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
			     MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			     message.setTo("adrianmatei@gmail.com");
	             message.setBcc("adrian.matei@yahoo.com");			     
			     message.setFrom("contact@podcastpedia.org");
			     message.setSubject("Your podcast has been added to Podcastpedia.org");
			     message.setSentDate(new Date());
			     
			     Map model = new HashMap();	             
			     model.put("name", name);
			     model.put("podcastUrl", podcastUrl);
			     
			     String text = VelocityEngineUtils.mergeTemplateIntoString(
			        velocityEngine, "templates/podcast_addition_notification.vm", "UTF-8", model);
			     
			     message.setText(text, true);
			     //IMPORTANT - see documentation for setText
//			     message.addInline("fb_logo", new ClassPathResource("img/fb_51.png"));
//			     message.addInline("twitter_logo", new ClassPathResource("img/twitter_51.png"));
//			     message.addInline("gplus_logo", new ClassPathResource("img/gplus_51.png"));	
				
			}
		};
		       this.mailSender.send(preparator);			
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
}

