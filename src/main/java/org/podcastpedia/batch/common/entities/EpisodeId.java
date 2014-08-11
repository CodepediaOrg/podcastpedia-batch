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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((episodeId == null) ? 0 : episodeId.hashCode());
		result = prime * result
				+ ((podcastId == null) ? 0 : podcastId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EpisodeId other = (EpisodeId) obj;
		if (episodeId == null) {
			if (other.episodeId != null)
				return false;
		} else if (!episodeId.equals(other.episodeId))
			return false;
		if (podcastId == null) {
			if (other.podcastId != null)
				return false;
		} else if (!podcastId.equals(other.podcastId))
			return false;
		return true;
	}
				
}
