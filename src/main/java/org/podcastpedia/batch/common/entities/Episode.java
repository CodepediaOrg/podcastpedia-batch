package org.podcastpedia.batch.common.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="episodes")
public class Episode implements Serializable{

	/**
	 * automatic generated serialVersionUID 
	 */
	private static final long serialVersionUID = -1957667986801174870L;
	
	@EmbeddedId
	private EpisodeId id;
	
	/** description of the episode */
	@Column(name="description")
	private String description;
	
	/** title of the episode */ 
	@Column(name="title")
	private String title;

	/** link of the episode - this should be the link to the episode on the provider's website
	 * Some providers set this as the url to the media file */
	@Column(name="link")
	private String link;
	
	/** this is the url to the media (audio or video) of this episode */
	@Column(name="media_url")
	private String mediaUrl; 
				
	/** publication date of the episode */
	@Column(name="publication_date")
	private Date publicationDate; 
	
	/** media type (either audio or video) */
	@Column(name="media_type")
	@Convert(converter=MediaTypeConverter.class)	
	private MediaType mediaType; 
	
	/** length of the episode */
	@Column(name="length")
	private Long length;
	
	/** episode's transformed title with hyphens to be added in the URL for SEO optimization */
	@Column(name="title_in_url")
	private String titleInUrl;
		
	/** 
	 * holds the the httpStatus for the episode's url  - see org.apache.http.HttpStatus for the codes semnification  
	 * or custom exception code */
	@Column(name="availability")
	private Integer availability;
	
	/** any value not null or zero means the episode is new */
	@Column(name="is_new")
	private Integer isNew;
	
	/** type of the enclosure */
	@Column(name="enclosure_type")
	private String enclosureType;
	
	/** author of the episode */
	@Column(name="author")	
	private String author; 	
	
	/** podcast the episode belongs to */
	@MapsId("podcastId")
	@ManyToOne //a podcast has to have at least one episode to be considered valid for Podcastpedia...
	@JoinColumn(name="podcast_id")	
	private Podcast podcast;
		
	public Episode(){}
	
	public String getDescription() {
		return description;
	}

	public EpisodeId getId() {
		return id;
	}

	public void setId(EpisodeId id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public Podcast getPodcast() {
		return podcast;
	}

	public void setPodcast(Podcast podcast) {
		this.podcast = podcast;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public String getTitleInUrl() {
		return titleInUrl;
	}

	public void setTitleInUrl(String titleInUrl) {
		this.titleInUrl = titleInUrl;
	}

	public Integer getAvailability() {
		return availability;
	}

	public void setAvailability(Integer availability) {
		this.availability = availability;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public String getEnclosureType() {
		return enclosureType;
	}

	public void setEnclosureType(String enclosureType) {
		this.enclosureType = enclosureType;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
