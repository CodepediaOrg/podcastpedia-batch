package org.podcastpedia.batch.jobs.addpodcast.service;

import java.io.IOException;
import java.net.MalformedURLException;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

public interface SyndFeedService {

	/**
	 * Given the url it returns the SyndFeed built with rome api
	 * 
	 * @param url
	 * @return
	 */
	public SyndFeed getSyndFeedForUrl(String url) throws MalformedURLException,
			IOException, IllegalArgumentException, FeedException;
}
