package org.podcastpedia.batch.jobs.notifysubscribers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.podcastpedia.batch.common.entities.Episode;
import org.podcastpedia.batch.common.entities.Podcast;
import org.podcastpedia.batch.common.entities.UpdateFrequency;
import org.podcastpedia.batch.common.entities.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@Scope("step")
public class NotifySubscribersItemProcessor implements ItemProcessor<User, User> {

	@Autowired
	EntityManager em;
	
	@Override
	public User process(User item) throws Exception {
		
		String sql = "SELECT u FROM User u WHERE u.email=?1 ";       
		TypedQuery<User> query = em.createQuery(sql, User.class);       
		query.setParameter(1, item.getEmail());
		
		User meTheUser = query.getSingleResult();
		List<Episode> episodes = meTheUser.getPodcasts().get(0).getEpisodes();

		System.out.println("Episodes size of the first subscribed pod " + episodes.size());
		
		String sqlInnerJoin = "select p from User u JOIN u.podcasts p WHERE p.updateFrequency=?1";
		TypedQuery<Podcast> queryInnerJoin = em.createQuery(sqlInnerJoin, Podcast.class);       
		queryInnerJoin.setParameter(1, UpdateFrequency.WEEKLY);
		
		List<Podcast> filteredPodcasts = queryInnerJoin.getResultList();
		
		String sqlInnerJoinEpisodes = "select e from User u JOIN u.podcasts p JOIN p.episodes e WHERE p.updateFrequency=?1 AND"
				+ " e.isNew IS NOT NULL order by e.podcast.podcastId ASC, e.publicationDate ASC";
		TypedQuery<Episode> queryInnerJoinepisodes = em.createQuery(sqlInnerJoinEpisodes, Episode.class);       
		queryInnerJoinepisodes.setParameter(1, UpdateFrequency.WEEKLY);		
				
		List<Episode> newEpisodes = queryInnerJoinepisodes.getResultList();
		
		return regroupPodcastsWithEpisodes(item, newEpisodes);
				
	}

	private User regroupPodcastsWithEpisodes(User item,
			List<Episode> newEpisodes) {
		if(newEpisodes.isEmpty()){
			return null; //there are no new episodes for this user, wo we go to the next
		} else {
			List<Podcast> podcastsToNotify = new ArrayList<Podcast>();
			
			Iterator<Episode> epIterator = newEpisodes.iterator();
			Episode firstEpisode = epIterator.next();
			Podcast podcast = firstEpisode.getPodcast();
			List<Episode> episodesForPodcast = new ArrayList<Episode>();
			episodesForPodcast.add(firstEpisode);
			Episode nextEpisode = null;
			
			while(epIterator.hasNext()){
				nextEpisode = epIterator.next();
				if(nextEpisode.getPodcast().getPodcastId() == podcast.getPodcastId()){
					episodesForPodcast.add(nextEpisode);
				} else {
					podcast.setEpisodes(episodesForPodcast);
					podcastsToNotify.add(podcast);
					
					podcast = nextEpisode.getPodcast();
					episodesForPodcast =  new ArrayList<>();
					episodesForPodcast.add(nextEpisode);					
				}								
			}
			
			podcast.setEpisodes(episodesForPodcast);
			podcastsToNotify.add(podcast);
			
			item.setPodcasts(podcastsToNotify);
			
		}
		
		return item;
	}


}
