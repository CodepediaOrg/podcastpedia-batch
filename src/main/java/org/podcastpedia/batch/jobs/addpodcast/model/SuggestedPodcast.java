package org.podcastpedia.batch.jobs.addpodcast.model;

import org.podcastpedia.batch.common.entities.Podcast;


public class SuggestedPodcast {

	private Podcast podcast; 
		
	/** contains the categories submitted, comma separated as string */
	protected String categories;
		
	/** contains the tags submitted, comma separated as string */
	protected String tags;	
	
	/** Name of the person suggesting the podcast */
	protected String name;
	
	/** Email of the person suggesting the podcast */
	protected String email;
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Podcast getPodcast() {
		return podcast;
	}

	public void setPodcast(Podcast podcast) {
		this.podcast = podcast;
	}
	
	
}
