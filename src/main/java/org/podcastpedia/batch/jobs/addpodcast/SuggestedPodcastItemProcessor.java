package org.podcastpedia.batch.jobs.addpodcast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.podcastpedia.batch.common.entities.Category;
import org.podcastpedia.batch.common.entities.Episode;
import org.podcastpedia.batch.common.entities.EpisodeId;
import org.podcastpedia.batch.common.entities.Podcast;
import org.podcastpedia.batch.common.entities.Tag;
import org.podcastpedia.batch.jobs.addpodcast.dao.ReadDao;
import org.podcastpedia.batch.jobs.addpodcast.model.SuggestedPodcast;
import org.podcastpedia.batch.jobs.addpodcast.service.PodcastAndEpisodeAttributesService;
import org.podcastpedia.batch.jobs.addpodcast.service.SyndFeedService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

public class SuggestedPodcastItemProcessor implements ItemProcessor<SuggestedPodcast, SuggestedPodcast> {

	private static final int TIMEOUT = 10;

	@Autowired
	ReadDao readDao;
	
	@Autowired
	PodcastAndEpisodeAttributesService podcastAndEpisodeAttributesService;
	
	@Autowired
	private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;  
	
	@Autowired
	private SyndFeedService syndFeedService;

	/**
	 * Method used to build the categories, tags and episodes of the podcast
	 */
	@Override
	public SuggestedPodcast process(SuggestedPodcast item) throws Exception {
		
		if(isPodcastAlreadyInTheDirectory(item.getPodcast().getUrl())) {
			return null;
		}
		
		String[] categories = item.getCategories().trim().split("\\s*,\\s*");		

		item.getPodcast().setAvailability(org.apache.http.HttpStatus.SC_OK);
		
		//set etag and last modified attributes for the podcast
		setHeaderFieldAttributes(item.getPodcast());
		
		//set the other attributes of the podcast from the feed 
		podcastAndEpisodeAttributesService.setPodcastFeedAttributes(item.getPodcast());
				
		//set the categories
		List<Category> categoriesByNames = readDao.findCategoriesByNames(categories);
		item.getPodcast().setCategories(categoriesByNames);
		
		//set the tags
		setTagsForPodcast(item);
		
		//build the episodes 
		setEpisodesForPodcast(item.getPodcast());
		
		return item;
	}

	private void setTagsForPodcast(SuggestedPodcast item) {
		String[] tags = item.getTags().trim().split("\\s*,\\s*");
		List<Tag> tagsByName = readDao.getTagsByNames(tags);		
		
		List<Tag> podcastTags = new ArrayList<Tag>();
		for(int i=0; i<tags.length; i++){
			boolean existingKeyword = false;
			for(Tag tag: tagsByName){
				if(tag.getName().equalsIgnoreCase(tags[i])){
					podcastTags.add(tag);
					existingKeyword = true;
					break;
				}
			}
			
			if(!existingKeyword){
				 podcastTags.add(new Tag(tags[i]));
			}		 
		}
		item.getPodcast().setTags(podcastTags);
	}
	
	private boolean isPodcastAlreadyInTheDirectory(String url) {
		Podcast response = readDao.getPodcastByFeedUrl(url);
		// TODO Auto-generated method stub
		return response!=null; 
	}

	/**
	 * Sets the header fields attributes "etag" and "last-modified" (if necessary) for a given podcast 
	 *    
	 * @param podcast
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws DateParseException 
	 * @throws Exception 
	 */
	private void setHeaderFieldAttributes(Podcast podcast) throws ClientProtocolException, IOException, DateParseException{
   	    
		HttpHead headMethod = null;
					
		headMethod = new HttpHead(podcast.getUrl());
		
				
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(TIMEOUT * 1000)
				.setConnectTimeout(TIMEOUT * 1000)
				.build();
		
		HttpClient httpClient = HttpClientBuilder
									.create()
									.setDefaultRequestConfig(requestConfig)
									.setConnectionManager(poolingHttpClientConnectionManager)
									.build();

		HttpResponse httpResponse = httpClient.execute(headMethod);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
      
		if (statusCode != HttpStatus.SC_OK) {
//			LOG.error("The introduced URL is not valid " + podcast.getUrl()  + " : " + statusCode);
		}
      
		//set the new etag if existent
		Header eTagHeader = httpResponse.getLastHeader("etag");
		if(eTagHeader != null){
			podcast.setEtagHeaderField(eTagHeader.getValue());
		}
      
		//set the new "last modified" header field if existent 
		Header lastModifiedHeader= httpResponse.getLastHeader("last-modified");
		if(lastModifiedHeader != null) {
			podcast.setLastModifiedHeaderField(DateUtil.parseDate(lastModifiedHeader.getValue()));
			podcast.setLastModifiedHeaderFieldStr(lastModifiedHeader.getValue());
		}	   	      	   	      	   	        	         	      

   	    // Release the connection.
    	headMethod.releaseConnection();	   	       	  		
	}
	

	@SuppressWarnings("unchecked")
	private void setEpisodesForPodcast(Podcast podcast) throws MalformedURLException, IllegalArgumentException, IOException, FeedException{
		
		int i=0; 
		List<Episode> episodes = new ArrayList<Episode>();
		
		SyndFeed syndFeedForUrl = syndFeedService.getSyndFeedForUrl(podcast.getUrl());
		if(null != syndFeedForUrl){
			for(SyndEntryImpl entry: (List<SyndEntryImpl>)syndFeedForUrl.getEntries()){
				
				Episode episode = new Episode();
//				episode.setPodcastId(podcast.getPodcastId());
				
				//because there is an insertion operation we set them in order they come from the entry list
				EpisodeId episodeId = new EpisodeId();
				episodeId.setEpisodeId(i);
				episode.setId(episodeId);
				
				//set attribues of the episode 
				podcastAndEpisodeAttributesService.setEpisodeAttributes(episode, podcast, entry);															
				i++;
				
				//at the beginning all the episodes are marked as available (200)
				episode.setAvailability(org.apache.http.HttpStatus.SC_OK);
				
				//insert the episode in the database
				try {
					//TODO when I move to InnoDB for table and lucene search, rollback the transaction with log message when updating the podcast
					//we add only the episodes that have a media file attached to them 
					boolean episodeMediaCouldBeSet = !episode.getMediaUrl().equals("noMediaUrl");
					if(episodeMediaCouldBeSet){
//						LOG.info("PodId[" + podcast.getPodcastId().toString() + "] - " + "INSERT EPISODE epId[" + episode.getEpisodeId()
//								+ "] - epURL " + episode.getMediaUrl());
//						episodes.add(episode);
						podcast.addEpisode(episode);
					}
					
				} catch (Exception e) {
//					LOG.error("ERROR inserting episode " + episode.getMediaUrl() + " for podcastId[" + episode.getPodcastId() + "]" + e.getMessage());
					continue; //do not mark it as new episode 
				}				
			
			}				
		}		
		
//		podcast.setEpisodes(episodes);
	}
	
}
