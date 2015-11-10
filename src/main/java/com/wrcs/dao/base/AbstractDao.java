package com.wrcs.dao.base;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class AbstractDao<T> {
	@PersistenceContext  
	private EntityManager em;
	
	public List<T> findById(String id) {
		Query query = em.createQuery("");
		
		
		return null;
	}
}
