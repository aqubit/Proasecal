package com.proasecal.licman.vo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "Licencia")
@Cacheable(false) 
@EntityListeners({ AuditListener.class })
//@SequenceGenerator(name = "SEQLICENCIA", initialValue = 1, sequenceName = "SEQ_LICENCIA", allocationSize = 2)
@NamedQueries({
		@NamedQuery(name = "LicenciaVO.findNroLicenciasXRangoVencimiento", query = "SELECT count(x) FROM LicenciaVO x where x.fecha_vencimiento BETWEEN :today AND :end"),
		@NamedQuery(name = "LicenciaVO.findNroLicenciasXRangoCreacion", query = "SELECT count(x) FROM LicenciaVO x where x.fecha_creacion BETWEEN :start AND :today"),
		@NamedQuery(name = "LicenciaVO.findNroLicenciasVencidas", query = "SELECT count(x) FROM LicenciaVO x where x.fecha_vencimiento < :today"), })
public class LicenciaVO extends ValueObjectImpl implements Serializable,
		AuditColumns {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4502885879516614020L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SEQLICENCIA")
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "fecha_vencimiento", nullable = false)
	private Date fecha_vencimiento;
	@Column(name = "numero_orden", nullable = false)
	private String numero_orden;
	@Column(name = "numero_licencia", nullable = false)
	private String numero_licencia;
	@Column(name = "clave_activacion", nullable = false)
	private String clave_activacion;
	@Column(name = "depto_activacion", nullable = false)
	private String depto_activacion;
	@Column(name = "nombre_persona", nullable = false)
	private String nombre_persona;
	@Column(name = "telefono_persona", nullable = false)
	private String telefono_persona;
	@Column(name = "fecha_creacion", insertable = true, updatable = false, nullable = false)
	private Timestamp fecha_creacion;
	@Column(name = "fecha_actualizacion", nullable = true)
	private Timestamp fecha_actualizacion;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUCTO_ID")
	private ProductoVO productoVO;
	@ManyToOne(optional = false)
	@JoinColumn(name = "CLIENTE_ID")
	private ClienteVO clienteVO;
	@ManyToOne(optional = true)
	@JoinColumn(name = "PATROCINADOR_ID")
	private PatrocinadorVO patrocinadorVO;
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	@JoinColumn(name = "HIJO_ID")
	private LicenciaVO hijoVO;
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	@JoinColumn(name = "PADRE_ID")
	private LicenciaVO padreVO;
	@Column(name = "fue_renovada")
	private Boolean fue_renovada = false;

	public LicenciaVO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha_vencimiento() {
		return fecha_vencimiento;
	}

	public void setFecha_vencimiento(Date fecha_vencimiento) {
		this.fecha_vencimiento = fecha_vencimiento;
	}

	public String getNumero_licencia() {
		return numero_licencia;
	}

	public void setNumero_licencia(String numero_licencia) {
		this.numero_licencia = numero_licencia;
	}

	public String getClave_activacion() {
		return clave_activacion;
	}

	public void setClave_activacion(String clave_activacion) {
		this.clave_activacion = clave_activacion;
	}

	public String getDepto_activacion() {
		return depto_activacion;
	}

	public void setDepto_activacion(String depto_activacion) {
		this.depto_activacion = depto_activacion;
	}

	public String getNombre_persona() {
		return nombre_persona;
	}

	public void setNombre_persona(String nombre_persona) {
		this.nombre_persona = nombre_persona;
	}

	public String getTelefono_persona() {
		return telefono_persona;
	}

	public void setTelefono_persona(String telefono_persona) {
		this.telefono_persona = telefono_persona;
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

	public ProductoVO getProductoVO() {
		return productoVO;
	}

	public void setProductoVO(ProductoVO param) {
		this.productoVO = param;
	}

	public ClienteVO getClienteVO() {
		return clienteVO;
	}

	public void setClienteVO(ClienteVO param) {
		this.clienteVO = param;
	}

	public PatrocinadorVO getPatrocinadorVO() {
		return patrocinadorVO;
	}

	public void setPatrocinadorVO(PatrocinadorVO patrocinadorVO) {
		this.patrocinadorVO = patrocinadorVO;
	}

	public String getNumero_orden() {
		return numero_orden;
	}

	public void setNumero_orden(String numero_orden) {
		this.numero_orden = numero_orden;
	}

	public LicenciaVO getPadreVO() {
		return padreVO;
	}

	public void setPadreVO(LicenciaVO padreVO) {
		this.padreVO = padreVO;
	}

	public LicenciaVO getHijoVO() {
		return hijoVO;
	}

	public void setHijoVO(LicenciaVO hijoVO) {
		this.hijoVO = hijoVO;
	}

	public Boolean getFue_renovada() {
		return fue_renovada;
	}

	public void setFue_renovada(Boolean fue_renovada) {
		this.fue_renovada = fue_renovada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((clave_activacion == null) ? 0 : clave_activacion.hashCode());
		result = prime
				* result
				+ ((depto_activacion == null) ? 0 : depto_activacion.hashCode());
		result = prime * result
				+ ((numero_licencia == null) ? 0 : numero_licencia.hashCode());
		result = prime * result
				+ ((numero_orden == null) ? 0 : numero_orden.hashCode());
		result = prime
				* result
				+ ((fecha_vencimiento == null) ? 0 : fecha_vencimiento
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nombre_persona == null) ? 0 : nombre_persona.hashCode());
		result = prime
				* result
				+ ((telefono_persona == null) ? 0 : telefono_persona.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LicenciaVO))
			return false;
		LicenciaVO other = (LicenciaVO) obj;
		if (clave_activacion == null) {
			if (other.clave_activacion != null)
				return false;
		} else if (!clave_activacion.equals(other.clave_activacion))
			return false;
		if (depto_activacion == null) {
			if (other.depto_activacion != null)
				return false;
		} else if (!depto_activacion.equals(other.depto_activacion))
			return false;
		if (numero_orden == null) {
			if (other.numero_orden != null)
				return false;
		} else if (!numero_orden.equals(other.numero_orden))
			return false;
		if (numero_licencia == null) {
			if (other.numero_licencia != null)
				return false;
		} else if (!numero_licencia.equals(other.numero_licencia))
			return false;
		if (fecha_vencimiento == null) {
			if (other.fecha_vencimiento != null)
				return false;
		} else if (!fecha_vencimiento.equals(other.fecha_vencimiento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre_persona == null) {
			if (other.nombre_persona != null)
				return false;
		} else if (!nombre_persona.equals(other.nombre_persona))
			return false;
		if (telefono_persona == null) {
			if (other.telefono_persona != null)
				return false;
		} else if (!telefono_persona.equals(other.telefono_persona))
			return false;
		return true;
	}

	public LicenciaVO clonar() {
		LicenciaVO nuevaLicencia = new LicenciaVO();
		nuevaLicencia.setClienteVO(this.getClienteVO());
		nuevaLicencia.setProductoVO(this.getProductoVO());
		nuevaLicencia.setPatrocinadorVO(this.getPatrocinadorVO());
		nuevaLicencia.setClave_activacion(this.getClave_activacion());
		nuevaLicencia.setDepto_activacion(this.getDepto_activacion());
		nuevaLicencia.setNumero_licencia(this.getNumero_licencia());
		nuevaLicencia.setFecha_vencimiento(this.getFecha_vencimiento());
		nuevaLicencia.setNombre_persona(this.getNombre_persona());
		nuevaLicencia.setTelefono_persona(this.getTelefono_persona());
		nuevaLicencia.setNumero_orden(this.getNumero_orden());
		return nuevaLicencia;
	}

	@Override
	public String toString() {
		return "Licencia[id=" + id + " | vencimiento=" + fecha_vencimiento
				+ " | clave=" + clave_activacion + "]";
	}
}