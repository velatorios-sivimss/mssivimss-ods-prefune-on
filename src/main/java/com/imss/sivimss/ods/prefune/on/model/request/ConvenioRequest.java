package com.imss.sivimss.ods.prefune.on.model.request;

import java.util.Map;

import lombok.Data;

@Data
public class ConvenioRequest {
	
	private Integer idRegistro;
	private Integer idConvenio;
	private String folio;
	private String fechaVigencia;
	private Boolean indRenovacion;
	private String idVelatorio;
	private String nomContratante;
	private String cuotaRecuperacion;
	private String idContratante;
	private String datosBancarios;
	
	public ConvenioRequest(Map<String, Object> map) {
	 this.folio = map.get("folio").toString();
	 this.fechaVigencia = map.get("fecVigencia").toString();
	 this.indRenovacion = (Boolean) map.get("indRenovacion");
	 this.idVelatorio = map.get("idVelatorio").toString();
	 this.nomContratante = map.get("nomContratante").toString(); 
	 this.cuotaRecuperacion = map.get("cuotaRecuperacion").toString();
	 this.idContratante = map.get("idContratante").toString();
	if(map.get("idContratante")!=null) {
		this.idContratante = map.get("idContratante").toString();	
	}
	 
	}

}
