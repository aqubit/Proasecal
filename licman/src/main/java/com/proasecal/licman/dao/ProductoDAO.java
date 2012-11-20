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
import com.proasecal.licman.vo.ProductoVO;

public class ProductoDAO {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(ProductoDAO.class);

	public List<ProductoVO> findProductos(int startIndex, int blockSize,
			List<String> currentSortedColumns,
			List<String> currentSortedVersusColumns,
			LinkedHashMap<String, Object> filter) throws DatabaseException {
		List<ProductoVO> result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Class<ProductoVO> cls = ProductoVO.class;
			Metamodel model = em.getMetamodel();
			EntityType<ProductoVO> entity = model.entity(cls);
			CriteriaQuery<ProductoVO> cq = cb.createQuery(cls);
			Root<ProductoVO> pathProducto = cq.from(entity);
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
							cb.upper(pathProducto.<String> get(columnName)),
							literal));
				} else if (value instanceof Date) {
					Date fecha1 = (Date) value;
					entry = entries.next();
					Date fecha2 = (Date) entry.getValue();
					whereClauses
							.add(cb.between(
									pathProducto.<Date> get(columnName),
									fecha1, fecha2));
				} else if (value instanceof Integer) {
					Integer iValue = (Integer) value;
					whereClauses.add(cb.equal(
							pathProducto.<Integer> get(columnName), iValue));
				}
			}
			cq.where(whereClauses.toArray(new Predicate[0]));
			// order by clause
			Iterator<String> it = currentSortedVersusColumns.iterator();
			List<Order> lstOrder = new ArrayList<Order>();
			for (String sortColumn : currentSortedColumns) {
				String order = it.next();
				if (order.equals("ASC")) {
					lstOrder.add(cb.asc(pathProducto.get(sortColumn)));
				} else {
					lstOrder.add(cb.desc(pathProducto.get(sortColumn)));
				}
			}
			cq.orderBy(lstOrder);
			TypedQuery<ProductoVO> query = em.createQuery(cq);
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

	public int findProductosCount(LinkedHashMap<String, Object> filter)
			throws DatabaseException {
		int result = 0;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			Class<ProductoVO> cls = ProductoVO.class;
			Metamodel model = em.getMetamodel();
			EntityType<ProductoVO> entity = model.entity(cls);
			CriteriaQuery<Long> cQuery = criteriaBuilder
					.createQuery(Long.class);
			Root<ProductoVO> fromLicencia = cQuery.from(entity);// cls
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

	public void delete(ProductoVO p) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			ProductoVO pAttached = em.find(ProductoVO.class, p.getId());
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
							.getString("error.delete.foreign.constraint.producto"),
					e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public void save(ProductoVO p) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			TypedQuery<ProductoVO> query = em.createNamedQuery(
					"ProductoVO.findByNameVersion", ProductoVO.class);
			query.setParameter("nombre", p.getNombre().toUpperCase());
			query.setParameter("version", p.getVersion().toUpperCase());
			if (query.getResultList().size() > 0) {
				throw new DatabaseException(
						new Throwable(
								ConfigManager
										.getString("error.creation.foreign.constraint.producto")));
			}
			em.persist(p);
			em.getTransaction().commit();
			LOGGER.debug("Exit");
		} catch (Exception e) {
			p.setId(null);
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
							.getString("error.creation.foreign.constraint.producto"),
					e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public void update(ProductoVO p) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			em.merge(p);
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
							.getString("error.creation.foreign.constraint.producto"),
					e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public ProductoVO findProducto(Integer id) throws DatabaseException {
		ProductoVO result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			result = em.find(ProductoVO.class, id);
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

	public List<ProductoVO> findProductosActivos(String name)
			throws DatabaseException {
		List<ProductoVO> result = null;
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			TypedQuery<ProductoVO> query = em.createNamedQuery(
					"ProductoVO.findActivosByName", ProductoVO.class);
			query.setParameter("nombre", name.toUpperCase());
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