package org.podcastpedia.batch.common.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="tags")
public class Tag implements Serializable{
	
	private static final long serialVersionUID = -2370292880165225805L;

	/** id of the tag - BIGINT in MySQL DB */
	@Id
	@Column(name="tag_id")
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private long tagId;
	
	/** name of the tag */
	@Column(name="name")
	private String name;
	
	@ManyToMany(mappedBy="tags")
	private List<Podcast> podcasts;
	
	public Tag(){}
	
	public Tag(String name){
		this.name = name; 
	}
	
	public List<Podcast> getPodcasts() {
		return podcasts;
	}

	public void setPodcasts(List<Podcast> podcasts) {
		this.podcasts = podcasts;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

}
