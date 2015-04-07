package com.novadart.novabill.service;

import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Commodity;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

@Service
public class LuceneIndexerService {
	
	@Value("${hibernate.search.default.indexBase}")
	private String indexBase;
	
	@Value("${lucene.reindex}")
	private Boolean reindex;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@PostConstruct
	public void init() throws InterruptedException{
		File baseDir = new File(indexBase);
		if(!baseDir.exists())
			baseDir.mkdir();
		if(reindex)
			reindex();
	}
	
	public void reindex() throws InterruptedException{
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		fullTextEntityManager.createIndexer(Client.class).startAndWait();
		fullTextEntityManager.createIndexer(Commodity.class).startAndWait();
	}

}
