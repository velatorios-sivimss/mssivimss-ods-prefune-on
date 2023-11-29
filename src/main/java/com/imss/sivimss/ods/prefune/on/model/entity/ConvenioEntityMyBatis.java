package com.imss.sivimss.ods.prefune.on.model.entity;

import lombok.Data;

@Data
public class ConvenioEntityMyBatis {
   
	private Integer idConvenio;
	private Integer idRegistro;
	private String folio;
	private String fecVigencia;
	private Boolean indRenovacion;
	private String idVelatorio;
	private String nomContratante;
	private String cuotaRecuperacion;
	private String idContratante;
	private String datosBancarios;
   
}
