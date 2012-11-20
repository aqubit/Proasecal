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
@Table(name = "Cliente")
@Cacheable(false) 
@EntityListeners({ AuditListener.class })
//@SequenceGenerator(name = "SEQCLIENTE", initialValue = 1, sequenceName = "SEQ_CLIENTE", allocationSize = 2)
@NamedQueries(@NamedQuery(name = "ClienteVO.findActivoByCodeLab", query = "SELECT c FROM ClienteVO c WHERE c.activo IS TRUE AND UPPER(c.codigo_laboratorio) LIKE CONCAT('%',:codigo, '%')"))
public class ClienteVO extends ValueObjectImpl implements Serializable,
		AuditColumns {
	/**
	 * 
	 */
	private static final long serialVersionUID = 586910634453698403L;
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SEQCLIENTE")
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "codigo_laboratorio", nullable = false, unique = true)
	private String codigo_laboratorio;
	@Column(name = "nombre_laboratorio", nullable = false)
	private String nombre_laboratorio;
	@Column(name = "nit", nullable = false)
	private String nit;
	@Column(name = "tipo_persona")
	private String tipo_persona;
	@Column(name = "nombre_contacto")
	private String nombre_contacto;
	@Column(name = "telefono_contacto")
	private String telefono_contacto;
	@Column(name = "activo", nullable = false)
	private Boolean activo;
	@Column(name = "fecha_creacion", insertable = true, updatable = false, nullable = false)
	private Timestamp fecha_creacion;
	@Column(name = "fecha_actualizacion", nullable = true)
	private Timestamp fecha_actualizacion;

	public ClienteVO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo_laboratorio() {
		return codigo_laboratorio;
	}

	public void setCodigo_laboratorio(String codigo_laboratorio) {
		this.codigo_laboratorio = codigo_laboratorio;
	}

	public String getNombre_laboratorio() {
		return nombre_laboratorio;
	}

	public void setNombre_laboratorio(String nombre_laboratorio) {
		this.nombre_laboratorio = nombre_laboratorio;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getNombre_contacto() {
		return nombre_contacto;
	}

	public void setNombre_contacto(String nombre_contacto) {
		this.nombre_contacto = nombre_contacto;
	}

	public String getTelefono_contacto() {
		return telefono_contacto;
	}

	public void setTelefono_contacto(String telefono_contacto) {
		this.telefono_contacto = telefono_contacto;
	}

	public String getTipo_persona() {
		return tipo_persona;
	}

	public void setTipo_persona(String tipo_persona) {
		this.tipo_persona = tipo_persona;
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

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activo == null) ? 0 : activo.hashCode());
		result = prime
				* result
				+ ((codigo_laboratorio == null) ? 0 : codigo_laboratorio
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result
				+ ((nombre_contacto == null) ? 0 : nombre_contacto.hashCode());
		result = prime
				* result
				+ ((nombre_laboratorio == null) ? 0 : nombre_laboratorio
						.hashCode());
		result = prime
				* result
				+ ((telefono_contacto == null) ? 0 : telefono_contacto
						.hashCode());
		result = prime * result
				+ ((tipo_persona == null) ? 0 : tipo_persona.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ClienteVO))
			return false;
		ClienteVO other = (ClienteVO) obj;
		if (activo == null) {
			if (other.activo != null)
				return false;
		} else if (!activo.equals(other.activo))
			return false;
		if (codigo_laboratorio == null) {
			if (other.codigo_laboratorio != null)
				return false;
		} else if (!codigo_laboratorio.equals(other.codigo_laboratorio))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		if (nombre_contacto == null) {
			if (other.nombre_contacto != null)
				return false;
		} else if (!nombre_contacto.equals(other.nombre_contacto))
			return false;
		if (nombre_laboratorio == null) {
			if (other.nombre_laboratorio != null)
				return false;
		} else if (!nombre_laboratorio.equals(other.nombre_laboratorio))
			return false;
		if (telefono_contacto == null) {
			if (other.telefono_contacto != null)
				return false;
		} else if (!telefono_contacto.equals(other.telefono_contacto))
			return false;
		if (tipo_persona == null) {
			if (other.tipo_persona != null)
				return false;
		} else if (!tipo_persona.equals(other.tipo_persona))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cliente[id=" + id + " | codlab=" + codigo_laboratorio
				+ " | lab=" + nombre_laboratorio + "]";
	}
}