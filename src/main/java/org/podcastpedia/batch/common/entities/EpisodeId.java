package org.podcastpedia.batch.common.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EpisodeId implements Serializable{

	private static final long serialVersionUID = 1201784239459713261L;
	
	@Column(name="podcast_id")
	Integer podcastId;
	
	@Column(name="episode_id")
	Integer episodeId;
	
	public Integer getPodcastId() {
		return podcastId;
	}
	public void setPodcastId(Integer podcastId) {
		this.podcastId = podcastId;
	}
	public Integer getEpisodeId() {
		return episodeId;
	}
	public void setEpisodeId(Integer episodeId) {
		this.episodeId = episodeId;
	}
	
	
		
}
