package org.podcastpedia.batch.common.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;



/**
 * Simple JavaBean domain object representing a podcast. 
 * 
 * @author amasia
 *
 */
@Entity
@Table(name="podcasts")
public class Podcast implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** id of the podcast - primary key in db */
	@Id
	@Column(name="podcast_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer podcastId;	
	
	/** e.g. http://www.podcastpedia.org/<strong>quarks</strong> */
	@Column(name="identifier")
	protected String identifier;

	/** stores the date when new episodes were added to the podcast  */
	@Column(name="last_update")
	protected Date lastUpdate;
	
	/** feed url of the podcast - based on this with rome will get further details */
	@Column(name="url")
	protected String url;
	
	/** date when the podcast is inserted in the database */
	@Column(name="insertion_date")
	protected Date insertionDate;
		
	/** media type of the podcast (either audio, video or videoHD) */
	@Column(name="media_type")
	@Convert(converter=MediaTypeConverter.class)
	protected MediaType mediaType; 
	
	/** description of the podcast */
	@Column(name="description")
	protected String description;
		
	/** title of the podcast */
	@Column(name="title")
	protected String title;
	
	/** link of the image that represents the podcast 
	 * - all these three last fields are for performance purposes 
	 */
	@Column(name="podcast_image_url")
	protected String urlOfImageToDisplay;
		
	/** copyright of the podcast */
	@Column(name="copyright")
	protected String copyright; 
		
	/** podcast publication date -  contains the date of the last published episode */
	@Column(name="publication_date")
	protected Date publicationDate;
	
	/** link of the podcast */
	@Column(name="podcast_link")
	protected String link; 
	
	/** short description - it is basically the first 320 of the description if it is longer than that */
	@Column(name="short_description")
	protected String shortDescription; 
	
	/** stores the "etag" value in the HTTP header for the podcast feed */
	@Column(name="etag_header_field")
	protected String etagHeaderField;
	
	/** stores the "last modified" in the HTTP header for the podcast feed */
	@Column(name="last_modified_header_field")
	protected Date lastModifiedHeaderField;
	
	/** same as above just this time is stored as string TODO - in the end one of these has to disappear*/
	@Column(name="last_modified_header_field_str")
	protected String lastModifiedHeaderFieldStr;
	
	/** holds the title to be displayed in the url "quarks & co" becomes "quarks-co"*/
	@Column(name="title_in_url")
	protected String titleInUrl;
	
	/** holds the the httpStatus for the podcasts's url  - see org.apache.http.HttpStatus for the codes semnification  and extra exception 
	 * codes and modification */
	@Column(name="availability")
	private Integer availability;
	
	/** stores in the database the language code - for example Romanian will be stored as "ro" */
	@Column(name="language_code")
	@Enumerated(EnumType.STRING)
	private LanguageCode languageCode;
	
	/** author of the podcast */
	@Column(name="author")
	private String author; 
	
	/** Facebook fan page */
	@Column(name="social_fb_page")
	private String fbPage;
	
	/** twitter fan page */
	@Column(name="social_twitter_page")
	private String twitterPage;
	
	/** Gplus fan page */
	@Column(name="social_gplus_page")
	private String gplusPage;

	@Column(name="update_frequency")
	@Convert(converter=UpdateFrequencyConverter.class)
	private UpdateFrequency updateFrequency;
		
	/** media url of the last episode */
	@Column(name="last_episode_url")
	protected String lastEpisodeMediaUrl;	
	
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "podcast")
	private List<Episode> episodes;

	public void addEpisode(Episode episode){
		if(episode != null) {
			if(episodes == null) {
				episodes = new ArrayList<Episode>();
			}
			episodes.add(episode);
			episode.setPodcast(this);
		}
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name="podcasts_tags",
			joinColumns={@JoinColumn(name="podcast_id")},
			inverseJoinColumns={@JoinColumn(name="tag_id")}
	)		
    private List<Tag> tags;
    
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name="podcasts_categories",
			joinColumns={@JoinColumn(name="podcast_id")},
			inverseJoinColumns={@JoinColumn(name="category_id")}
	)		
    private List<Category> categories;
		
	
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Episode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Integer getPodcastId() {
		return podcastId;
	}

	public void setPodcastId(Integer podcastId) {
		this.podcastId = podcastId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(Date insertionDate) {
		this.insertionDate = insertionDate;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getDescription() {
		return description;
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

	public String getUrlOfImageToDisplay() {
		return urlOfImageToDisplay;
	}

	public void setUrlOfImageToDisplay(String urlOfImageToDisplay) {
		this.urlOfImageToDisplay = urlOfImageToDisplay;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getEtagHeaderField() {
		return etagHeaderField;
	}

	public void setEtagHeaderField(String etagHeaderField) {
		this.etagHeaderField = etagHeaderField;
	}

	public Date getLastModifiedHeaderField() {
		return lastModifiedHeaderField;
	}

	public void setLastModifiedHeaderField(Date lastModifiedHeaderField) {
		this.lastModifiedHeaderField = lastModifiedHeaderField;
	}

	public String getLastModifiedHeaderFieldStr() {
		return lastModifiedHeaderFieldStr;
	}

	public void setLastModifiedHeaderFieldStr(String lastModifiedHeaderFieldStr) {
		this.lastModifiedHeaderFieldStr = lastModifiedHeaderFieldStr;
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

	public LanguageCode getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(LanguageCode languageCode) {
		this.languageCode = languageCode;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFbPage() {
		return fbPage;
	}

	public void setFbPage(String fbPage) {
		this.fbPage = fbPage;
	}

	public String getTwitterPage() {
		return twitterPage;
	}

	public void setTwitterPage(String twitterPage) {
		this.twitterPage = twitterPage;
	}

	public String getGplusPage() {
		return gplusPage;
	}

	public void setGplusPage(String gplusPage) {
		this.gplusPage = gplusPage;
	}

	public UpdateFrequency getUpdateFrequency() {
		return updateFrequency;
	}

	public void setUpdateFrequency(UpdateFrequency updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

	public String getLastEpisodeMediaUrl() {
		return lastEpisodeMediaUrl;
	}

	public void setLastEpisodeMediaUrl(String lastEpisodeMediaUrl) {
		this.lastEpisodeMediaUrl = lastEpisodeMediaUrl;
	}
			
}
