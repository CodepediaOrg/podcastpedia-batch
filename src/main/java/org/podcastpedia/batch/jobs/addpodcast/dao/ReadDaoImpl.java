package org.podcastpedia.batch.jobs.addpodcast.dao;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.podcastpedia.batch.common.entities.Category;
import org.podcastpedia.batch.common.entities.Episode;
import org.podcastpedia.batch.common.entities.Podcast;
import org.podcastpedia.batch.common.entities.Tag;
import org.podcastpedia.batch.common.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

public class ReadDaoImpl implements ReadDao {
	
//	@PersistenceContext(unitName = "batchPersistenceUnit")
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<Category> findCategoriesByNames(String[] categoryNames) {
		
		String jpql = "SELECT c FROM Category c WHERE c.name IN :categoryNames";
		TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
		query.setParameter("categoryNames", Arrays.asList(categoryNames));
		
		return query.getResultList();
	}

	@Override
	public List<Tag> getTagsByNames(String[] tags) {
		
		String jpql = "SELECT t FROM Tag t WHERE t.name IN :tags";
		TypedQuery<Tag> query = entityManager.createQuery(jpql, Tag.class);
		query.setParameter("tags", Arrays.asList(tags));
		
		return query.getResultList();
	}

	@Override
	public Podcast getPodcastByFeedUrl(String feedUrl) {
		String jpql = "SELECT p FROM Podcast p WHERE p.url = :url";
		TypedQuery<Podcast> query = entityManager.createQuery(jpql, Podcast.class);
		query.setParameter("url", feedUrl);
		
		List<Podcast> resultList = query.getResultList();
		if(resultList !=null && resultList.size() > 0)
			return resultList.get(0);
		else
			return null; 
	}

	@Override
	public User getMeUser() {
		
		String sql = "SELECT u FROM User u WHERE u.email=?1 ";
		TypedQuery<User> query = entityManager.createQuery(sql, User.class);
		query.setParameter(1, "adrianmatei@gmail.com");
		
		User meTheUser = query.getSingleResult();
		List<Episode> episodes = meTheUser.getPodcasts().get(0).getEpisodes();
		
		System.out.println("Episodes size of the first subscribed pod " + episodes.size());
		
		return meTheUser;
	}

}
