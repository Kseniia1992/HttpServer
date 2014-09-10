package org.httpserver.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.httpserver.logic.Url;

/**
 * 
 * @author Kseniia
 *
 */
public class UrlService {
	EntityManagerFactory emf;
	EntityManager em;
	
	
	public UrlService(){
		emf=Persistence.createEntityManagerFactory("HttpServer");
		em=emf.createEntityManager();
	}
	
	public void addURL(Url u) {
		em.getTransaction().begin();
		em.merge(u);
		em.getTransaction().commit();
		emf.close();
	}
	
	/**
	 * 
	 * @return result total count of requests
	 */
	public int getAllUrlCount(){
		int result = ((Number)em.createNamedQuery("Url.getUrlCount").getSingleResult()).intValue();
		return result;
	}
	
	/**
	 * 
	 * @return result number of unique requests
	 */
	public int getUniqueUrl(){
		Query query = em.createNativeQuery("SELECT COUNT(*) FROM (SELECT DISTINCT url From url) AS unique_tb");
		int result = ((Number)query.getSingleResult()).intValue();
		return result;		
	}
	
	/**
	 * 
	 * @return result counter of requests for each IP 
	 */
	@SuppressWarnings("rawtypes")
	public List requestCounter(){
		Query query = em.createNativeQuery("SELECT DISTINCT ip, url, Max(data) From ip, url Group by ip.ip, url");
		List result = query.getResultList();
		return result;	
	}
	
	/**
	 * 
	 * @return result number of redirections for URLs
	 */
	@SuppressWarnings("rawtypes")
	public List urlCounter(){
		Query query = em.createNativeQuery("SELECT url, COUNT(url) AS count FROM url GROUP BY url");
		List result = query.getResultList();
		return result;
	}

}
