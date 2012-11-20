package com.proasecal.licman.vo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

import com.proasecal.licman.svcs.AuditListener;

/**
 * 
 * @author acunac2
 */
@Entity
@Table(name = "Patrocinador")
@Cacheable(false) 
@EntityListeners({ AuditListener.class })
//@SequenceGenerator(name = "SEQPATROCINADOR", initialValue = 1, sequenceName = "SEQ_PATROCINADOR", allocationSize = 2)
@NamedQueries(@NamedQuery(name = "PatrocinadorVO.findActivosByName", query = "SELECT p FROM PatrocinadorVO p WHERE p.activo IS TRUE AND UPPER(p.nombre) LIKE CONCAT('%',:nombre, '%')"))
// p.activo IS TRUE AND
public class PatrocinadorVO extends ValueObjectImpl implements Serializable,
		AuditColumns {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4402146949740815991L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SEQPATROCINADOR")
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "nombre", nullable = false, unique = true)
	private String nombre;
	@Column(name = "activo", nullable = false)
	private Boolean activo;
	@Column(name = "descripcion", nullable = false)
	private String descripcion;
	@Column(name = "fecha_creacion", insertable = true, updatable = false, nullable = false)
	private Timestamp fecha_creacion;
	@Column(name = "fecha_actualizacion", nullable = true)
	private Timestamp fecha_actualizacion;

	public PatrocinadorVO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Timestamp getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Timestamp fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public Timestamp getFecha_actualizacion() {
		return fecha_actualizacion;
	}

	public void setFecha_actualizacion(Timestamp fecha_actualizacion) {
		this.fecha_actualizacion = fecha_actualizacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activo == null) ? 0 : activo.hashCode());
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PatrocinadorVO))
			return false;
		PatrocinadorVO other = (PatrocinadorVO) obj;
		if (activo == null) {
			if (other.activo != null)
				return false;
		} else if (!activo.equals(other.activo))
			return false;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Patrocinador[id=" + id + " | name=" + nombre;
	}
}