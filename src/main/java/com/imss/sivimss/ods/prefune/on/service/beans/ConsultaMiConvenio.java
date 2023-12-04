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
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("SCP.ID_CONVENIO_PF AS idConvenio", "SCP.DES_FOLIO AS folioConvenio",
				"SP.CVE_CURP AS curp, CONCAT(SP.NOM_PERSONA,' ',SP.NOM_PRIMER_APELLIDO,' ',SP.NOM_SEGUNDO_APELLIDO) AS nombreAfiliado",
				"SCP.ID_ESTATUS_CONVENIO AS idEstatus", "SECP.DES_ESTATUS AS estatus",
				"DATE_FORMAT(SCP.FEC_ALTA,'%d/%m/%Y') AS fechaExpedicion",
				"CASE WHEN SCP.IND_TIPO_CONTRATACION = 0" +
						" then SV.DES_VELATORIO " +
						" else  '' " +
						" end AS ciudadExpedicion",
				"CASE when SCP.IND_TIPO_CONTRATACION = 0" +
						" then 0 else 1 end " + " AS tipoContrato",
				"SCP.ID_TIPO_PREVISION AS tipoPrevision",
				"IF(RENO.ID_ESTATUS=2, TRUE, FALSE) AS banderaDocumentos")
				.from("SVT_CONVENIO_PF SCP ")
				.innerJoin("SVC_VELATORIO SV ", "SCP.ID_VELATORIO=SV.ID_VELATORIO ")
				.innerJoin("SVC_ESTATUS_CONVENIO_PF SECP", "SCP.ID_ESTATUS_CONVENIO = SECP.ID_ESTATUS_CONVENIO_PF")
				.innerJoin("SVT_CONTRA_PAQ_CONVENIO_PF SCPA", "SCP.ID_CONVENIO_PF = SCPA.ID_CONVENIO_PF")
				.innerJoin("SVC_CONTRATANTE SC", "SCPA.ID_CONTRATANTE = SC.ID_CONTRATANTE")
				.innerJoin("SVC_PERSONA SP", "SC.ID_PERSONA =SP.ID_PERSONA")
				.leftJoin("SVT_RENOVACION_CONVENIO_PF RENO",
						"SCP.ID_CONVENIO_PF = RENO.ID_CONVENIO_PF AND RENO.ID_ESTATUS = 2 ")
				.where("SCP.IND_TIPO_CONTRATACION =1")
				.and("SC.ID_CONTRATANTE = " + idContratante)
				.orderBy("SECP.DES_ESTATUS DESC");
		query = selectQueryUtil.build();
		log.info("consultaMiConvenio: {}", query);

		return query;
	}

	public String consultarDatosGeneales(Integer idConvenio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilBeneficiarios = new SelectQueryUtil();

		selectQueryUtilBeneficiarios.select("COUNT(SCBE.ID_PERSONA) ")
				.from("SVT_CONTRA_PAQ_CONVENIO_PF SCPAC ")
				.innerJoin("SVT_CONTRATANTE_BENEFICIARIOS SCBE",
						"SCPAC.ID_CONTRA_PAQ_CONVENIO_PF = SCBE.ID_CONTRA_PAQ_CONVENIO_PF ")
				.where("SCPAC.ID_CONVENIO_PF = ".concat(idConvenio.toString()).concat(" AND SCBE.IND_ACTIVO =1 "));

		selectQueryUtil.select("SCP.ID_CONVENIO_PF AS idConvenio", "SP.CVE_CURP AS curp",
				"SCP.ID_VELATORIO AS idVelatorio",
				"V.DES_VELATORIO AS velatorio",
				"SECP.DES_ESTATUS AS estatus",
				"SCP.DES_FOLIO AS folioConvenio",
				"SP.NOM_PERSONA AS nombreAfiliado",
				"SP.NOM_PRIMER_APELLIDO AS primerApellido",
				"SP.NOM_SEGUNDO_APELLIDO AS segundoApellido",
				" CASE SP.NUM_SEXO " +
						" WHEN 1 THEN 'FEMENINO' " +
						" WHEN 2 THEN 'MASCULINO' " +
						" ELSE IFNULL(SP.REF_OTRO_SEXO,'')" +
						" END " +
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
				.innerJoin("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
				.innerJoin("SVC_VELATORIO V", "V.ID_VELATORIO = SCP.ID_VELATORIO")
				.where("SCP.ID_CONVENIO_PF = " + idConvenio);

		query = selectQueryUtil.build();
		log.info("consultarDatosGeneales: {}", query);
		return query;
	}

	public String consultarBeneficiariosConvenio(Integer idConvenio) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
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
				"IFNULL(SCB.REF_UBICACION_ACTA_NACIMIENTO,'') AS actaNacimiento",
				"IFNULL(SCB.REF_UBICACION_INE_BENEFICIARIO,'') AS ine")
				.from("SVT_CONVENIO_PF SCP ")
				.innerJoin("SVC_VELATORIO SV", "SCP.ID_VELATORIO = SV.ID_VELATORIO ")
				.innerJoin("SVC_ESTATUS_CONVENIO_PF SECP", "SCP.ID_ESTATUS_CONVENIO = SECP.ID_ESTATUS_CONVENIO_PF ")
				.innerJoin("SVT_CONTRA_PAQ_CONVENIO_PF SCPA ", "SCP.ID_CONVENIO_PF = SCPA.ID_CONVENIO_PF")
				.innerJoin("SVT_CONTRATANTE_BENEFICIARIOS SCB",
						"SCPA.ID_CONTRA_PAQ_CONVENIO_PF = SCB.ID_CONTRA_PAQ_CONVENIO_PF")
				.innerJoin("SVC_PARENTESCO SPAC", "SCB.ID_PARENTESCO = SPAC.ID_PARENTESCO ")
				.innerJoin("SVC_PERSONA SP", "SCB.ID_PERSONA = SP.ID_PERSONA ")
				.where("SCP.ID_CONVENIO_PF =".concat(idConvenio.toString()).concat(" AND SCB.IND_ACTIVO =1 "));
		query = selectQueryUtil.build();
		log.info("consultarBeneficiariosConvenio: {}", query);
		return query;
	}

	public String consultarRenovacion(Integer idConvenio) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		SelectQueryUtil subQueryUtil = new SelectQueryUtil();
		subQueryUtil.select("TRUE")
				.from("SVC_FINADO SF")
				.join("SVT_NOTA_REMISION nota",
						"SF.ID_ORDEN_SERVICIO = nota.ID_ORDEN_SERVICIO AND nota.IND_ESTATUS = 2")
				.join("SVC_PERSONA SP", "SF.ID_PERSONA = SP.ID_PERSONA")
				.join("SVT_CONVENIO_PF PF", "SF.ID_CONTRATO_PREVISION = PF.ID_CONVENIO_PF")
				.join("SVT_CONTRA_PAQ_CONVENIO_PF spaq", "PF.ID_CONVENIO_PF = spaq.ID_CONVENIO_PF")
				.join("SVC_CONTRATANTE SC",
						"SCPC.ID_CONTRATANTE = SC.ID_CONTRATANTE  AND SF.ID_PERSONA = SC.ID_PERSONA")
				.where("SF.ID_TIPO_ORDEN = 2").and("PF.ID_CONVENIO_PF = SCP.ID_CONVENIO_PF");
		String subQuery = subQueryUtil.build();
		queryUtil.select("SCP.ID_ESTATUS_CONVENIO AS idEstatusConvenio",
				//"RPF.ID_ESTATUS AS estatusRenovacion",
				"IF(SCP.ID_TIPO_PREVISION=1, 'Plan Nuevo', 'Plan Anterior') AS previsionFuneraria",
				"DATE_FORMAT(SCP.FEC_ALTA , '%d-%m-%Y') AS fecContratacion",
				"IF(SCP.IND_RENOVACION=false, (DATE_FORMAT(SCP.FEC_VIGENCIA, '%d-%m-%Y')), DATE_FORMAT(RPF.FEC_VIGENCIA, '%d-%m-%Y')) AS fecVigencia",
				"PAQ.MON_PRECIO AS cuotaRecuperacion",
				"PAQ.REF_PAQUETE_NOMBRE AS tipoPaquete",
				//"IF(SCP.IND_RENOVACION=false, ' ', DATE_FORMAT(RPF.FEC_ALTA, '%d-%m-%Y')) AS fecRenovacion",
				"IFNULL((".concat(subQuery) + "), FALSE) AS titularFallecido",
				"DATE_FORMAT(CURDATE(), '%d-%m-%Y') AS fecActual",
				"TIMESTAMPDIFF(DAY,IF(SCP.IND_RENOVACION=false, DATE_FORMAT(SCP.FEC_VIGENCIA, '%Y-%m-%01'), DATE_FORMAT(RPF.FEC_VIGENCIA, '%Y-%m-%01')), CURDATE()) AS diferenciaDias")
				.from("SVT_CONVENIO_PF SCP ")
				.leftJoin("SVT_RENOVACION_CONVENIO_PF RPF",
						"SCP.ID_CONVENIO_PF=RPF.ID_CONVENIO_PF AND RPF.ID_ESTATUS IN (1,2)  ")
				.join("SVT_CONTRA_PAQ_CONVENIO_PF SCPC", "SCP.ID_CONVENIO_PF = SCPC.ID_CONVENIO_PF")
				.join("SVT_PAQUETE PAQ", "SCPC.ID_PAQUETE = PAQ.ID_PAQUETE")
				.where("SCP.ID_CONVENIO_PF =" + idConvenio);
		query = queryUtil.build();
		log.info("consultaRenovacionConvenio: {}", query);
		return query;
	}

	public String busquedaFolioParaReporte(String idConvenio) {
		SelectQueryUtil querySelect = new SelectQueryUtil();
		querySelect
				.select("SP.CVE_RFC AS rfc", "SP.CVE_CURP AS curp", "SP.CVE_NSS AS nss",
						"SP.NOM_PERSONA AS nombrePersona",
						"SP.NOM_PRIMER_APELLIDO AS primerApellido",
						"SP.NOM_SEGUNDO_APELLIDO AS segundoApellido",
						"SP.NUM_INE AS numIne", "SCP.DES_FOLIO AS folio",
						"CPF.ID_PAQUETE AS idPaquete",
						"PAQ.REF_PAQUETE_NOMBRE AS nombrePaquete",
						"PAQ.REF_PAQUETE_DESCRIPCION AS desPaquete",
						"PAQ.MON_PRECIO AS monPrecio")
				.from("SVT_CONVENIO_PF SCP")
				.leftJoin("SVT_CONTRA_PAQ_CONVENIO_PF CPF", "SCP.ID_CONVENIO_PF = CPF.ID_CONVENIO_PF")
				.leftJoin("SVT_PAQUETE PAQ", "CPF.ID_PAQUETE = PAQ.ID_PAQUETE")
				.leftJoin("SVC_CONTRATANTE SC", "CPF.ID_CONTRATANTE = SC.ID_CONTRATANTE")
				.leftJoin("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
				.where("SCP.ID_CONVENIO_PF = '" + idConvenio + "'")
				.groupBy("SCP.DES_FOLIO");
		query = querySelect.build();
		log.info("consulta busquedaFolioParaReporte convenio nuevo pf: {} ", query);
		return query;
	}

	public String consultarDatosConvenio(String idConvenio) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("SCP.DES_FOLIO AS folio",
				"SCP.IND_RENOVACION AS indRenovacion",
				"IF(SCP.IND_RENOVACION=false, SCP.FEC_VIGENCIA, RPF.FEC_VIGENCIA) AS fecVigencia",
				"SCP.ID_VELATORIO AS idVelatorio",
				"CONCAT(SP.NOM_PERSONA, ' ', SP.NOM_PRIMER_APELLIDO, ' ', SP.NOM_SEGUNDO_APELLIDO) as nomContratante",
				"PAQ.MON_PRECIO AS cuotaRecuperacion",
				"SC.ID_CONTRATANTE AS idContratante")
				.from("SVT_CONVENIO_PF SCP ")
				.leftJoin("SVT_RENOVACION_CONVENIO_PF RPF",
						"SCP.ID_CONVENIO_PF=RPF.ID_CONVENIO_PF AND RPF.ID_ESTATUS=2  ")
				.join("SVT_CONTRA_PAQ_CONVENIO_PF SCPC", "SCP.ID_CONVENIO_PF = SCPC.ID_CONVENIO_PF")
				.join("SVC_CONTRATANTE SC", "SCPC.ID_CONTRATANTE = SC.ID_CONTRATANTE")
				.join("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
				.join("SVT_PAQUETE PAQ", "SCPC.ID_PAQUETE = PAQ.ID_PAQUETE")
				.where("SCP.ID_CONVENIO_PF =" + idConvenio)
				.limit(1);
		query = queryUtil.build();
		log.info("renovacion: {}", query);
		return query;
	}

}
