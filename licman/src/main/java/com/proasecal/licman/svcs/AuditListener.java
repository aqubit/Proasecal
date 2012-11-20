package com.proasecal.licman.svcs;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.proasecal.licman.vo.AuditColumns;

public class AuditListener {
	@PrePersist
	void onPrePersist(Object o) {
		AuditColumns target = (AuditColumns) o;
		target.setFecha_creacion(new Timestamp((new Date()).getTime()));
	}

	@PreUpdate
	void onPreUpdate(Object o) {
		AuditColumns target = (AuditColumns) o;
		target.setFecha_actualizacion(new Timestamp((new Date()).getTime()));
	}
}
