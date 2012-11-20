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
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

import com.proasecal.licman.svcs.AuditListener;

/**
 * 
 * @author acunac2
 */
@Entity
@EntityListeners({ AuditListener.class })
@Cacheable(false) 
@Table(name = "Producto", uniqueConstraints=@UniqueConstraint(columnNames={"nombre","version"}))
//@SequenceGenerator(name = "SEQPROD", initialValue = 1, sequenceName = "SEQ_PRODUCTO", allocationSize = 2)
@NamedQueries({
		@NamedQuery(name = "ProductoVO.findByNameVersion", query = "SELECT p FROM ProductoVO p WHERE UPPER(p.nombre) LIKE :nombre and UPPER(p.version) LIKE :version"),
		@NamedQuery(name = "ProductoVO.findActivosByName", query = "SELECT p FROM ProductoVO p WHERE p.activo IS TRUE AND UPPER(p.nombre) LIKE CONCAT('%',:nombre, '%')")
})
public class ProductoVO extends ValueObjectImpl implements Serializable,
		AuditColumns {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4402146949740815991L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SEQPROD")
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "nombre", nullable = false)
	private String nombre;
	@Column(name = "version", nullable = false)
	private String version;
	@Column(name = "descripcion", nullable = false)
	private String descripcion;
	@Column(name = "activo", nullable = false)
	private Boolean activo;
	@Column(name = "fecha_creacion", insertable = true, updatable = false, nullable = false)
	private Timestamp fecha_creacion;
	@Column(name = "fecha_actualizacion", nullable = true)
	private Timestamp fecha_actualizacion;
	@Transient
	private String nombreVersion;
	
	public ProductoVO() {
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Timestamp getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Timestamp fechaCreacion) {
		this.fecha_creacion = fechaCreacion;
	}

	public Timestamp getFecha_actualizacion() {
		return fecha_actualizacion;
	}

	public void setFecha_actualizacion(Timestamp fechaActualizacion) {
		this.fecha_actualizacion = fechaActualizacion;
	}

	public void setNombreVersion(String nombreVersion) {
		this.nombreVersion = nombreVersion;
	}

	public String getNombreVersion() {
		return nombreVersion;
	}

	@SuppressWarnings("unused")
	@PostLoad
	private void updateNombreVersion() {
		nombreVersion = nombre + " V" + version;
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
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ProductoVO))
			return false;
		ProductoVO other = (ProductoVO) obj;
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
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Producto[id=" + id + " | name=" + nombre + " | version="
				+ version + "]";
	}
}