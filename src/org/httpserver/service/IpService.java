package org.httpserver.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.httpserver.logic.Ip;

/**
 * @author Kseniia
 *
 */
public class IpService {

	EntityManagerFactory emf;
	EntityManager em;
	
	public IpService(){
		emf=Persistence.createEntityManagerFactory("HttpServer");
		em=emf.createEntityManager();
	}	
	
	public void addIP(Ip i) {
		em.getTransaction().begin();
		em.merge(i);
		em.getTransaction().commit();
		emf.close();
	}
}
