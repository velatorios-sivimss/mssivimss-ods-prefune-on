package com.imss.sivimss.ods.prefune.on.service.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.utils.SelectQueryUtil;

@Service
public class ConsultaMiConvenio {
	
	private String query;
	
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
		log.info("consultaMiConvenio: {}",query);
		return query;
	}
	
	public String consultarDatosGeneales(Integer idConvenio) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilBeneficiarios= new SelectQueryUtil();
		
		
		selectQueryUtilBeneficiarios.select("COUNT(SCBE.ID_PERSONA) ")
		.from("SVT_CONTRA_PAQ_CONVENIO_PF SCPAC ")
		.innerJoin("SVT_CONTRATANTE_BENEFICIARIOS SCBE", "SCPAC.ID_CONTRA_PAQ_CONVENIO_PF = SCBE.ID_CONTRA_PAQ_CONVENIO_PF ")
		.where("SCPAC.ID_CONVENIO_PF = ".concat(idConvenio.toString()).concat(" AND SCBE.IND_ACTIVO =1 "));
		
		selectQueryUtil.select("SCP.ID_CONVENIO_PF AS idConvenio", "SP.CVE_CURP AS curp", 
				"SP.NOM_PERSONA AS nombreAfiliado",
				"SP.NOM_PRIMER_APELLIDO AS primerApellido",
				"SP.NOM_SEGUNDO_APELLIDO AS segundoApellido", 
				" CASE SP.NUM_SEXO "+ 
				" WHEN 1 THEN 'FEMENINO' "+
				" WHEN 2 THEN 'MASCULINO' "+
				" ELSE IFNULL(SP.REF_OTRO_SEXO,'')"+
				" END "+
				" AS sexo", 
				"SP.FEC_NAC AS fechaNacimiento",
				"SCP.ID_TIPO_PREVISION AS tipoPrevision",
				"SD.REF_ESTADO AS entidadFederativa",
				"(".concat(selectQueryUtilBeneficiarios.build()).concat(") AS totalBeneficiarios"))
		.from("SVT_CONVENIO_PF SCP")
		.innerJoin("SVC_ESTATUS_CONVENIO_PF SECP", "SCP.ID_ESTATUS_CONVENIO = SECP.ID_ESTATUS_CONVENIO_PF ")
		.innerJoin("SVT_CONTRA_PAQ_CONVENIO_PF SCPA", "SCP.ID_CONVENIO_PF = SCPA.ID_CONVENIO_PF ")
		.innerJoin("SVC_CONTRATANTE SC ", "SCPA.ID_CONTRATANTE = SC.ID_CONTRATANTE ")
		.innerJoin("SVT_DOMICILIO SD", "SC.ID_DOMICILIO = SD.ID_DOMICILIO ")
		.innerJoin("SVC_PERSONA SP", "SC.ID_PERSONA =SP.ID_PERSONA")
		.where("SCP.ID_CONVENIO_PF = "+idConvenio);
		
		
		query=selectQueryUtil.build();
		log.info("consulta: {}",query);
		return query;
	}
	
	public String consultarBeneficiariosConvenio(Integer idConvenio) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select(
				"SCB.ID_CONTRATANTE_BENEFICIARIOS AS idContratanteBeneficiarios", 
				"SCB.ID_PERSONA AS idPersona",
				"SP.NOM_PERSONA AS nombreAfiliado",
				"SP.NOM_PRIMER_APELLIDO AS primerApellido",
				"SP.NOM_SEGUNDO_APELLIDO AS segundoApellido",
				"SP.CVE_RFC AS rfc",
				"SP.CVE_CURP AS curp",
				"SCB.ID_PARENTESCO AS idParentesco",
				"SPAC.DES_PARENTESCO AS parentesco",
				"SV.DES_VELATORIO AS velatorio",
				"DATE_FORMAT(SP.FEC_NAC,'%d-%m-%Y') AS fechaNacimiento",
				"TIMESTAMPDIFF(YEAR, SP.FEC_NAC, CURDATE())  AS edad",
				"SP.REF_CORREO AS correo",
				"SP.REF_TELEFONO AS telefono",
				"SCB.REF_UBICACION_INE_BENEFICIARIO AS actaNacimiento",
				"SCB.REF_UBICACION_INE_BENEFICIARIO AS ine")
			.from("SVT_CONVENIO_PF SCP ")
			.innerJoin("SVC_VELATORIO SV", "SCP.ID_VELATORIO = SV.ID_VELATORIO ")
			.innerJoin("SVC_ESTATUS_CONVENIO_PF SECP", "SCP.ID_ESTATUS_CONVENIO = SECP.ID_ESTATUS_CONVENIO_PF ")
			.innerJoin("SVT_CONTRA_PAQ_CONVENIO_PF SCPA ", "SCP.ID_CONVENIO_PF = SCPA.ID_CONVENIO_PF")
			.innerJoin("SVT_CONTRATANTE_BENEFICIARIOS SCB", "SCPA.ID_CONTRA_PAQ_CONVENIO_PF = SCB.ID_CONTRA_PAQ_CONVENIO_PF")
			.innerJoin("SVC_PARENTESCO SPAC", "SCB.ID_PARENTESCO = SPAC.ID_PARENTESCO ")
			.innerJoin("SVC_PERSONA SP", "SCB.ID_PERSONA = SP.ID_PERSONA ")
			.where("SCP.ID_CONVENIO_PF =".concat(idConvenio.toString()).concat(" AND SCB.IND_ACTIVO =1 "));
		query=selectQueryUtil.build();
		log.info("consulta: {}",query);
		return query;
	}

}
