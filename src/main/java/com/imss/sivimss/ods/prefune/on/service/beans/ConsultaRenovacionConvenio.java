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
		queryUtil.select("DES_FOLIO",
				"(SELECT TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA WHERE DES_PARAMETRO= 'FIRMA_DIRECTORA') AS firmaFideicomiso")
		.from("SVT_CONVENIO_PF")
		.where("ID_CONVENIO_PF ="+idConvenio);
		query = queryUtil.build();
		log.info(query);
		return query;
	}

	public String obtenerCostoRecuperacion(String idConvenio) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("PAQ.MON_PRECIO AS costoRecuperacion",
				"(SELECT TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA WHERE DES_PARAMETRO= 'FIRMA_DIRECTORA') AS firmaFideicomiso",
				"(SELECT TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA WHERE DES_PARAMETRO= 'SELLO_RENOVACION') AS selloRenovacion")
		.from("SVT_CONVENIO_PF SCP")
		.join("SVT_RENOVACION_CONVENIO_PF RPF", "SCP.ID_CONVENIO_PF=RPF.ID_CONVENIO_PF")
		.join("SVT_CONTRA_PAQ_CONVENIO_PF SCPC", "SCP.ID_CONVENIO_PF = SCPC.ID_CONVENIO_PF")
		.join("SVT_PAQUETE PAQ", "SCPC.ID_PAQUETE = PAQ.ID_PAQUETE");
		queryUtil.where("RPF.ID_ESTATUS=2");
		queryUtil.where("SCP.ID_TIPO_PREVISION = 2");
			queryUtil.where("SCP.ID_CONVENIO_PF = " +idConvenio);
			query = queryUtil.build();
			log.info(query);
			return query;
	}
	
	

}
