package com.proasecal.licman.vo;


import java.sql.Timestamp;

public interface AuditColumns {
	public void setFecha_actualizacion(Timestamp fechaActualizacion);
	public void setFecha_creacion(Timestamp fechaCreacion);
}
