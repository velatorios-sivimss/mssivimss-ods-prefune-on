package com.imss.sivimss.ods.prefune.on.service.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.utils.SelectQueryUtil;

@Service
public class ConsultaMiConvenio {
	
	private String query="";
	
	private static final Logger log = LoggerFactory.getLogger(ConsultaMiConvenio.class);

	public String consultaMiConvenio(Integer idContratante) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select( "SCP.ID_CONVENIO_PF AS idConvenio", "SCP.DES_FOLIO AS folioConvenio", 
				"SP.CVE_CURP AS curp, CONCAT(SP.NOM_PERSONA,' ',SP.NOM_PRIMER_APELLIDO,' ',SP.NOM_SEGUNDO_APELLIDO) AS nombreAfiliado","SCP.ID_ESTATUS_CONVENIO AS idEstatus", "SECP.DES_ESTATUS AS estatus",
				"SCP.ID_TIPO_PREVISION AS tipoPrevision")
		.from("SVT_CONVENIO_PF SCP ")
		.innerJoin("SVC_ESTATUS_CONVENIO_PF SECP", "SCP.ID_ESTATUS_CONVENIO = SECP.ID_ESTATUS_CONVENIO_PF")
		.innerJoin("SVT_CONTRA_PAQ_CONVENIO_PF SCPA", "SCP.ID_CONVENIO_PF = SCPA.ID_CONVENIO_PF")
		.innerJoin("SVC_CONTRATANTE SC", "SCPA.ID_CONTRATANTE = SC.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SC.ID_PERSONA =SP.ID_PERSONA")
		.where("SCP.IND_TIPO_CONTRATACION =1")
		.and("SC.ID_CONTRATANTE = "+idContratante)
		.orderBy("SECP.DES_ESTATUS DESC");
		query=selectQueryUtil.build();
		log.info("consultaMiConvenio: ".concat(query));
		return query;
	}

}
