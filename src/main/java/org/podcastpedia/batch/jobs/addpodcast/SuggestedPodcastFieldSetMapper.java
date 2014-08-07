package org.podcastpedia.batch.jobs.addpodcast;

import org.podcastpedia.batch.common.entities.LanguageCode;
import org.podcastpedia.batch.common.entities.MediaType;
import org.podcastpedia.batch.common.entities.Podcast;
import org.podcastpedia.batch.common.entities.UpdateFrequency;
import org.podcastpedia.batch.jobs.addpodcast.model.SuggestedPodcast;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class SuggestedPodcastFieldSetMapper implements FieldSetMapper<SuggestedPodcast> {

	@Override
	public SuggestedPodcast mapFieldSet(FieldSet fieldSet) throws BindException {
		
		SuggestedPodcast suggestedPodcast = new SuggestedPodcast();
		
		suggestedPodcast.setCategories(fieldSet.readString("CATEGORIES"));
		suggestedPodcast.setEmail(fieldSet.readString("EMAIL_SUBMITTER"));
		suggestedPodcast.setName(fieldSet.readString("NAME_SUBMITTER"));
		suggestedPodcast.setTags(fieldSet.readString("KEYWORDS"));
		
		//some of the attributes we can map directly into the Podcast entity that we'll insert later into the database
		Podcast podcast = new Podcast();
		podcast.setUrl(fieldSet.readString("FEED_URL"));
		podcast.setIdentifier(fieldSet.readString("IDENTIFIER_ON_PODCASTPEDIA"));
		podcast.setLanguageCode(LanguageCode.valueOf(fieldSet.readString("LANGUAGE")));
		podcast.setMediaType(MediaType.valueOf(fieldSet.readString("MEDIA_TYPE")));
		podcast.setUpdateFrequency(UpdateFrequency.valueOf(fieldSet.readString("UPDATE_FREQUENCY")));
		podcast.setFbPage(fieldSet.readString("FB_PAGE"));
		podcast.setTwitterPage(fieldSet.readString("TWITTER_PAGE"));
		podcast.setGplusPage(fieldSet.readString("GPLUS_PAGE"));
		
		suggestedPodcast.setPodcast(podcast);

		return suggestedPodcast;
	}
	

}
