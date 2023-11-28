package com.imss.sivimss.ods.prefune.on.service.beans;

import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.utils.SelectQueryUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsultaRenovacionConvenio {

	private String query;

	public String obtenerFolio(String idConvenio) {
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("DES_FOLIO")
		.from("SVT_CONVENIO_PF")
		.where("ID_CONVENIO_PF ="+idConvenio);
		query = queryUtil.build();
		log.info(query);
		return query;
	}
	
	

}
