package org.podcastpedia.batch.common.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="categories")
public class Category implements Serializable{

	private static final long serialVersionUID = 219264453988823416L;
	
	@Id
	@Column(name="category_id")
	protected int categoryId;
	
	@Column(name="name")
	protected String name;
	
	@Column(name="description")
	protected String description;
	
	@ManyToMany(mappedBy="categories")
	private List<Podcast> podcasts;
		
	public List<Podcast> getPodcasts() {
		return podcasts;
	}
	public void setPodcasts(List<Podcast> podcasts) {
		this.podcasts = podcasts;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
}
