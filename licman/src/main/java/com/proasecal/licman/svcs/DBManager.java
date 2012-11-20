package com.proasecal.licman.svcs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {
	private static final String PERSISTENCE_UNIT_NAME = "licmanPU";
	private static EntityManagerFactory emf;
	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(DBManager.class);

	public static void init() {
		LOGGER.debug("Configurando PesistenceUnit");
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		LOGGER.debug(emf.toString());
	}

	public static EntityManager getEntityManager() throws Exception {
		EntityManager em = emf.createEntityManager();
		LOGGER.debug("EntityManager requested: " + em);
		return em;
	}

	public static void close() {
		LOGGER.debug("EntityManagerFactory closed");
		emf.close();
	}
}
