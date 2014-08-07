package org.podcastpedia.batch.jobs.notifysubscribers.model;

import java.util.Date;

public class EmailNotificationPlaceholder {

	/** name of the email receiver */
	String name;
	
	String email;
	
	Integer podcastId;
	
	String podcastTitle;
	
	String podcastTitleInUrl;
	
	Integer episodeId;
	
	String episodeTitle;
	
	String episodeTitleInUrl;
	
	Date episodePublicationDate;

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

	public Integer getPodcastId() {
		return podcastId;
	}

	public void setPodcastId(Integer podcastId) {
		this.podcastId = podcastId;
	}

	public String getPodcastTitle() {
		return podcastTitle;
	}

	public void setPodcastTitle(String podcastTitle) {
		this.podcastTitle = podcastTitle;
	}

	public String getPodcastTitleInUrl() {
		return podcastTitleInUrl;
	}

	public void setPodcastTitleInUrl(String podcastTitleInUrl) {
		this.podcastTitleInUrl = podcastTitleInUrl;
	}

	public Integer getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(Integer episodeId) {
		this.episodeId = episodeId;
	}

	public String getEpisodeTitle() {
		return episodeTitle;
	}

	public void setEpisodeTitle(String episodeTitle) {
		this.episodeTitle = episodeTitle;
	}

	public String getEpisodeTitleInUrl() {
		return episodeTitleInUrl;
	}

	public void setEpisodeTitleInUrl(String episodeTitleInUrl) {
		this.episodeTitleInUrl = episodeTitleInUrl;
	}

	public Date getEpisodePublicationDate() {
		return episodePublicationDate;
	}

	public void setEpisodePublicationDate(Date episodePublicationDate) {
		this.episodePublicationDate = episodePublicationDate;
	}
	
}
