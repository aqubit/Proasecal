package com.proasecal.licman.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.DBManager;
import com.proasecal.licman.vo.ClienteVO;

public class ClienteDAO {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(ClienteDAO.class);

	public List<ClienteVO> findClientes(int startIndex, int blockSize,
			List<String> currentSortedColumns,
			List<String> currentSortedVersusColumns,
			LinkedHashMap<String, Object> filter) throws DatabaseException {
		List<ClienteVO> result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Class<ClienteVO> cls = ClienteVO.class;
			Metamodel model = em.getMetamodel();
			EntityType<ClienteVO> entity = model.entity(cls);
			CriteriaQuery<ClienteVO> cq = cb.createQuery(cls);
			Root<ClienteVO> pathCliente = cq.from(entity);
			List<Predicate> whereClauses = new ArrayList<Predicate>();
			// where clause
			Iterator<Entry<String, Object>> entries = filter.entrySet()
					.iterator();
			while (entries.hasNext()) {
				Map.Entry<String, Object> entry = entries.next();
				String columnName = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String) {
					String param = "%" + value + "%";
					Expression<String> literal = cb.upper(cb.literal(param));
					whereClauses.add(cb.like(
							cb.upper(pathCliente.<String> get(columnName)),
							literal));
				} else if (value instanceof Date) {
					Date fecha1 = (Date) value;
					entry = entries.next();
					Date fecha2 = (Date) entry.getValue();
					whereClauses
							.add(cb.between(pathCliente.<Date> get(columnName),
									fecha1, fecha2));
				} else if (value instanceof Integer) {
					Integer iValue = (Integer) value;
					whereClauses.add(cb.equal(
							pathCliente.<Integer> get(columnName), iValue));
				}
			}
			cq.where(whereClauses.toArray(new Predicate[0]));
			// order by clause
			Iterator<String> it = currentSortedVersusColumns.iterator();
			List<Order> lstOrder = new ArrayList<Order>();
			for (String sortColumn : currentSortedColumns) {
				String order = it.next();
				if (order.equals("ASC")) {
					lstOrder.add(cb.asc(pathCliente.get(sortColumn)));
				} else {
					lstOrder.add(cb.desc(pathCliente.get(sortColumn)));
				}
			}
			cq.orderBy(lstOrder);
			TypedQuery<ClienteVO> query = em.createQuery(cq);
			query.setMaxResults(blockSize);
			query.setFirstResult(startIndex);
			result = query.getResultList();
			LOGGER.debug("Exit");
		} catch (Exception e) {
			throw new DatabaseException(e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
		return result;
	}

	public int findClientesCount(LinkedHashMap<String, Object> filter)
			throws DatabaseException {
		int result = 0;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			Class<ClienteVO> cls = ClienteVO.class;
			Metamodel model = em.getMetamodel();
			EntityType<ClienteVO> entity = model.entity(cls);
			CriteriaQuery<Long> cQuery = criteriaBuilder
					.createQuery(Long.class);
			Root<ClienteVO> fromLicencia = cQuery.from(entity);// cls
			CriteriaQuery<Long> selectCount = cQuery.select(criteriaBuilder
					.count(fromLicencia));
			List<Predicate> whereClauses = new ArrayList<Predicate>();
			// where clause
			Iterator<Entry<String, Object>> entries = filter.entrySet()
					.iterator();
			while (entries.hasNext()) {
				Map.Entry<String, Object> entry = entries.next();
				String columnName = entry.getKey();
				Object value = entry.getValue();
				String newColumn = columnName;
				if (value instanceof String) {
					String param = "%" + value + "%";
					Expression<String> literal = criteriaBuilder
							.upper(criteriaBuilder.literal(param));
					whereClauses.add(criteriaBuilder.like(criteriaBuilder
							.upper(fromLicencia.<String> get(newColumn)),
							literal));
				} else if (value instanceof Date) {
					Date fecha1 = (Date) value;
					entry = entries.next();
					Date fecha2 = (Date) entry.getValue();
					whereClauses
							.add(criteriaBuilder.between(
									fromLicencia.<Date> get(newColumn), fecha1,
									fecha2));
				} else if (value instanceof Integer) {
					Integer iValue = (Integer) value;
					whereClauses.add(criteriaBuilder.equal(
							fromLicencia.<Integer> get(newColumn), iValue));
				}
			}
			// total filas
			selectCount.where(whereClauses.toArray(new Predicate[0]));
			TypedQuery<Long> typedQueryCount = em.createQuery(selectCount);
			Long iNumFilas = typedQueryCount.getSingleResult();
			result = iNumFilas.intValue();
		} catch (Exception e) {
			throw new DatabaseException(e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
		return result;
	}

	public void delete(ClienteVO c) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			ClienteVO pAttached = em.find(ClienteVO.class, c.getId());
			em.remove(pAttached);
			em.getTransaction().commit();
			LOGGER.debug("Exit");
		} catch (Exception e) {
			try {
				if (em != null && em.getTransaction().isActive()) {
					LOGGER.error("Transaction rollback");
					em.getTransaction().rollback();
				}
			} catch (Exception e2) {
				LOGGER.error("No se pudo hacer rollback", e2);
			}
			throw new DatabaseException(
					ConfigManager
							.getString("error.delete.foreign.constraint.cliente"),
					e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public void save(ClienteVO c) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			em.persist(c);
			em.getTransaction().commit();
			LOGGER.debug("Exit");
		} catch (Exception e) {
			c.setId(null);
			try {
				if (em != null && em.getTransaction().isActive()) {
					LOGGER.error("Transaction rollback");
					em.getTransaction().rollback();
				}
			} catch (Exception e2) {
				LOGGER.error("No se pudo hacer rollback", e2);
			}
			throw new DatabaseException(
					ConfigManager
							.getString("error.creation.foreign.constraint.cliente"),
					e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public void update(ClienteVO c) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			em.merge(c);
			em.getTransaction().commit();
			LOGGER.debug("Exit");
		} catch (Exception e) {
			try {
				if (em != null && em.getTransaction().isActive()) {
					LOGGER.error("Transaction rollback");
					em.getTransaction().rollback();
				}
			} catch (Exception e2) {
				LOGGER.error("No se pudo hacer rollback", e2);
			}
			throw new DatabaseException(
					ConfigManager
							.getString("error.creation.foreign.constraint.cliente"),
					e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public ClienteVO findCliente(Integer id) throws DatabaseException {
		ClienteVO result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			result = em.find(ClienteVO.class, id);
			LOGGER.debug("Exit");
		} catch (Exception e) {
			throw new DatabaseException(e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
		return result;
	}

	public List<ClienteVO> findClientesActivosByCode(String codigo)
			throws DatabaseException {
		List<ClienteVO> result = null;
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			TypedQuery<ClienteVO> query = em.createNamedQuery(
					"ClienteVO.findActivoByCodeLab", ClienteVO.class);
			query.setParameter("codigo", codigo.toUpperCase());
			result = query.getResultList();
		} catch (Exception e) {
			throw new DatabaseException(e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
		return result;
	}
}