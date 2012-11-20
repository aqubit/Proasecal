package com.proasecal.licman.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.DBManager;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.ClienteVO;
import com.proasecal.licman.vo.LicenciaVO;
import com.proasecal.licman.vo.PatrocinadorVO;
import com.proasecal.licman.vo.ProductoVO;

public class LicenciaDAO {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(LicenciaDAO.class);

	public List<LicenciaVO> findLicencias(int startIndex, int blockSize,
			List<String> currentSortedColumns,
			List<String> currentSortedVersusColumns,
			LinkedHashMap<String, Object> filter) throws DatabaseException {
		List<LicenciaVO> result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			Class<LicenciaVO> cls = LicenciaVO.class;
			Metamodel model = em.getMetamodel();
			EntityType<LicenciaVO> entity = model.entity(cls);
			CriteriaQuery<LicenciaVO> criteriaQueryLicencias = criteriaBuilder
					.createQuery(cls);
			Root<LicenciaVO> fromLicencia = criteriaQueryLicencias.from(entity);
			Path<ClienteVO> fromCliente = fromLicencia
					.<ClienteVO> get("clienteVO");
			Path<ProductoVO> fromProducto = fromLicencia
					.<ProductoVO> get("productoVO");
			Path<PatrocinadorVO> fromPatrocinador = fromLicencia
					.<PatrocinadorVO> get("patrocinadorVO");
			List<Predicate> whereClauses = new ArrayList<Predicate>();
			// where clause
			Iterator<Entry<String, Object>> entries = filter.entrySet()
					.iterator();
			while (entries.hasNext()) {
				Map.Entry<String, Object> entry = entries.next();
				String columnName = entry.getKey();
				Object value = entry.getValue();
				Path<?> fromGeneric = fromLicencia;
				String newColumn = columnName;
				if (columnName.contains("clienteVO")) {
					fromGeneric = fromCliente;
					newColumn = columnName.replaceFirst("clienteVO.", "");
				} else if (columnName.contains("productoVO")) {
					fromGeneric = fromProducto;
					newColumn = columnName.replaceFirst("productoVO.", "");
				} else if (columnName.contains("patrocinadorVO")) {
					fromGeneric = fromPatrocinador;
					newColumn = columnName.replaceFirst("patrocinadorVO.", "");
				}
				if (value instanceof String) {
					String param = "%" + value + "%";
					Expression<String> literal = criteriaBuilder
							.upper(criteriaBuilder.literal(param));
					whereClauses.add(criteriaBuilder.like(criteriaBuilder
							.upper(fromGeneric.<String> get(newColumn)),
							literal));
				} else if (value instanceof Date) {
					Date fecha1 = (Date) value;
					entry = entries.next();
					Date fecha2 = (Date) entry.getValue();
					whereClauses.add(criteriaBuilder.between(
							fromGeneric.<Date> get(newColumn), fecha1, fecha2));
				} else if (value instanceof Integer) {
					Integer iValue = (Integer) value;
					whereClauses.add(criteriaBuilder.equal(
							fromGeneric.<Integer> get(newColumn), iValue));
				} else if (value instanceof Boolean) {
					Boolean bValue = (Boolean) value;
					whereClauses.add(criteriaBuilder.equal(
							fromGeneric.<Boolean> get(newColumn), bValue));
				}
			}
			criteriaQueryLicencias
					.where(whereClauses.toArray(new Predicate[0]));
			// order by clause
			Iterator<String> it = currentSortedVersusColumns.iterator();
			List<Order> lstOrder = new ArrayList<Order>();
			for (String sortColumn : currentSortedColumns) {
				String order = it.next();
				Path<?> pathGeneric = fromLicencia;
				String newSortColumn = sortColumn;
				if (sortColumn.contains("clienteVO")) {
					pathGeneric = fromCliente;
					newSortColumn = sortColumn.replaceFirst("clienteVO.", "");
				} else if (sortColumn.contains("productoVO")) {
					pathGeneric = fromProducto;
					newSortColumn = sortColumn.replaceFirst("productoVO.", "");
				} else if (sortColumn.contains("patrocinadorVO")) {
					pathGeneric = fromPatrocinador;
					newSortColumn = sortColumn.replaceFirst("patrocinadorVO.",
							"");
				}
				if (order.equals("ASC")) {
					lstOrder.add(criteriaBuilder.asc(pathGeneric
							.get(newSortColumn)));
				} else {
					lstOrder.add(criteriaBuilder.desc(pathGeneric
							.get(newSortColumn)));
				}
			}
			criteriaQueryLicencias.orderBy(lstOrder);
			// consulta licencias
			TypedQuery<LicenciaVO> typedQueryLicencias = em
					.createQuery(criteriaQueryLicencias);
			typedQueryLicencias.setMaxResults(blockSize);
			typedQueryLicencias.setFirstResult(startIndex);
			result = typedQueryLicencias.getResultList();	
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

	public int findLicenciasCount(LinkedHashMap<String, Object> filter)
			throws DatabaseException {
		int result = 0;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			Class<LicenciaVO> cls = LicenciaVO.class;
			Metamodel model = em.getMetamodel();
			EntityType<LicenciaVO> entity = model.entity(cls);
			CriteriaQuery<Long> cQuery = criteriaBuilder
					.createQuery(Long.class);
			Root<LicenciaVO> fromLicencia = cQuery.from(entity);// cls
			CriteriaQuery<Long> selectCount = cQuery.select(criteriaBuilder
					.count(fromLicencia));
			Path<ClienteVO> fromCliente = fromLicencia
					.<ClienteVO> get("clienteVO");
			Path<ProductoVO> fromProducto = fromLicencia
					.<ProductoVO> get("productoVO");
			Path<PatrocinadorVO> fromPatrocinador = fromLicencia
					.<PatrocinadorVO> get("patrocinadorVO");
			List<Predicate> whereClauses = new ArrayList<Predicate>();
			// where clause
			Iterator<Entry<String, Object>> entries = filter.entrySet()
					.iterator();
			while (entries.hasNext()) {
				Map.Entry<String, Object> entry = entries.next();
				String columnName = entry.getKey();
				Object value = entry.getValue();
				Path<?> pathGeneric = fromLicencia;
				String newColumn = columnName;
				if (columnName.contains("clienteVO")) {
					pathGeneric = fromCliente;
					newColumn = columnName.replaceFirst("clienteVO.", "");
				} else if (columnName.contains("productoVO")) {
					pathGeneric = fromProducto;
					newColumn = columnName.replaceFirst("productoVO.", "");
				} else if (columnName.contains("patrocinadorVO")) {
					pathGeneric = fromPatrocinador;
					newColumn = columnName.replaceFirst("patrocinadorVO.", "");
				}
				if (value instanceof String) {
					String param = "%" + value + "%";
					Expression<String> literal = criteriaBuilder
							.upper(criteriaBuilder.literal(param));
					whereClauses.add(criteriaBuilder.like(criteriaBuilder
							.upper(pathGeneric.<String> get(newColumn)),
							literal));
				} else if (value instanceof Date) {
					Date fecha1 = (Date) value;
					entry = entries.next();
					Date fecha2 = (Date) entry.getValue();
					whereClauses.add(criteriaBuilder.between(
							pathGeneric.<Date> get(newColumn), fecha1, fecha2));
				} else if (value instanceof Integer) {
					Integer iValue = (Integer) value;
					whereClauses.add(criteriaBuilder.equal(
							pathGeneric.<Integer> get(newColumn), iValue));
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

	public void delete(List<LicenciaVO> lstLicencias) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			for (LicenciaVO lic : lstLicencias) {
				LicenciaVO licAttached = em.find(LicenciaVO.class, lic.getId());
				if (licAttached.getPadreVO() != null) {
					LicenciaVO licPadreAttached = em.find(LicenciaVO.class,
							licAttached.getPadreVO().getId());
					// el padre se pudo haber borrado en una iteración anterior
					if (licPadreAttached != null) {
						licPadreAttached.setHijoVO(null);
						licPadreAttached.setFue_renovada(false);
					}
				}
				em.remove(licAttached);
			}
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
							.getString("error.delete.foreign.constraint.licencia"),
					e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public void save(LicenciaVO p) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
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
			throw new DatabaseException(e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public void refreshLicencias(Set<LicenciaVO> setLicenciasSeleccionadas)
			throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			Set<LicenciaVO> setRefreshedLicencias = new LinkedHashSet<LicenciaVO>();
			em = DBManager.getEntityManager();
			for (LicenciaVO licAnterior : setLicenciasSeleccionadas) {
				LicenciaVO lic = em.find(LicenciaVO.class, licAnterior.getId());
				setRefreshedLicencias.add(lic);
			}
			setLicenciasSeleccionadas.clear();
			setLicenciasSeleccionadas.addAll(setRefreshedLicencias);
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
			throw new DatabaseException(e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public void renovarLicencias(Set<LicenciaVO> setLicenciasSeleccionadas,
			List<LicenciaVO> lstNuevasLicencias) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			Iterator<LicenciaVO> it = lstNuevasLicencias.iterator();
			for (LicenciaVO licAnterior : setLicenciasSeleccionadas) {
				LicenciaVO licNueva = it.next();
				LicenciaVO licAnteriorAttached = em.find(LicenciaVO.class,
						licAnterior.getId());
				licAnteriorAttached.setHijoVO(licNueva);
				licAnteriorAttached.setFue_renovada(true);
				licNueva.setPadreVO(licAnteriorAttached);				
				em.persist(licNueva);
			}
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
			throw new DatabaseException(e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public void update(LicenciaVO lic) throws DatabaseException {
		LOGGER.debug("Start");
		EntityManager em = null;
		try {
			em = DBManager.getEntityManager();
			em.getTransaction().begin();
			LicenciaVO nuevaLicencia = em.find(LicenciaVO.class, lic.getId());
			nuevaLicencia.setClienteVO(lic.getClienteVO());
			nuevaLicencia.setProductoVO(lic.getProductoVO());
			nuevaLicencia.setClave_activacion(lic.getClave_activacion());
			nuevaLicencia.setDepto_activacion(lic.getDepto_activacion());
			nuevaLicencia.setNumero_licencia(lic.getNumero_licencia());
			nuevaLicencia.setFecha_vencimiento(lic.getFecha_vencimiento());
			nuevaLicencia.setNombre_persona(lic.getNombre_persona());
			nuevaLicencia.setTelefono_persona(lic.getTelefono_persona());
			nuevaLicencia.setNumero_orden(lic.getNumero_orden());
			nuevaLicencia.setPatrocinadorVO(lic.getPatrocinadorVO());
			em.getTransaction().commit();
			LOGGER.debug("Exit");
		} catch (Exception e) {
			try {
				if (em != null && em.getTransaction().isActive()) {
					LOGGER.error("Transaction rollback");
					em.getTransaction().rollback();
				}
			} catch (Exception e2) {
				LOGGER.error(e2, e2);
			}
			throw new DatabaseException(e);
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public LicenciaVO findLicencia(Integer id) throws DatabaseException {
		LicenciaVO result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			LOGGER.debug("Cargando licencia id: " + id);
			result = em.find(LicenciaVO.class, id);
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

	public Long findNroLicXRangoVencimiento(int iNroDias)
			throws DatabaseException {
		Long result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			Date today = LicmanUtils.fechaHoy();
			Date theDate = LicmanUtils.diffHoy(iNroDias);
			Query q = em
					.createNamedQuery("LicenciaVO.findNroLicenciasXRangoVencimiento");
			q.setParameter("today", today);
			q.setParameter("end", theDate);
			result = (Long) q.getSingleResult();
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

	public Long findNroLicXRangoCreacion(int iNroDias) throws DatabaseException {
		Long result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			Date theDate = LicmanUtils.diffHoy(iNroDias);
			Query q = em
					.createNamedQuery("LicenciaVO.findNroLicenciasXRangoCreacion");
			q.setParameter("start", theDate);
			q.setParameter("today", new Date());
			result = (Long) q.getSingleResult();
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

	public Long findNroLicenciasVencidas() throws DatabaseException {
		Long result = null;
		EntityManager em = null;
		LOGGER.debug("Start");
		try {
			em = DBManager.getEntityManager();
			Date today = LicmanUtils.fechaHoy();
			Query q = em
					.createNamedQuery("LicenciaVO.findNroLicenciasVencidas");
			q.setParameter("today", today);
			result = (Long) q.getSingleResult();
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
}