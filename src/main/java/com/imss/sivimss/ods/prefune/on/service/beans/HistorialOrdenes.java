package com.imss.sivimss.ods.prefune.on.service.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.utils.SelectQueryUtil;

@Service
public class HistorialOrdenes {
	
	
	private static final Logger log = LoggerFactory.getLogger(HistorialOrdenes.class);

	private String query;
	
	public String consultarOrdenes(Integer idContratante) {
	
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		
		selectQueryUtil.select(
				 "SO.ID_ORDEN_SERVICIO as idOrden",
					"IFNULL(SO.CVE_FOLIO,'') as folio",
					"CONCAT(SP.NOM_PERSONA, ' ', SP.NOM_PRIMER_APELLIDO, ' ', SP.NOM_SEGUNDO_APELLIDO) AS nombreContratante")
		.from("SVC_ORDEN_SERVICIO SO")
		.innerJoin("SVC_CONTRATANTE SC", "SC.ID_CONTRATANTE = SO.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
		.innerJoin("SVC_ESTATUS_ORDEN_SERVICIO E ", "SO.ID_ESTATUS_ORDEN_SERVICIO = E.ID_ESTATUS_ORDEN_SERVICIO")
		.where("SO.ID_ESTATUS_ORDEN_SERVICIO IN (1, 2, 0, 4, 3, 6) ")
		.and("SC.ID_CONTRATANTE="+idContratante)
		.orderBy("SO.ID_ORDEN_SERVICIO DESC");
		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	public String consultarVerDetalle(Integer idOrden) {
		
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		
		selectQueryUtil.select(
				"SO.CVE_FOLIO AS folio",
				"E.DES_ESTATUS AS estatus",
				"CONCAT(SP.NOM_PERSONA, ' ', SP.NOM_PRIMER_APELLIDO, ' ', SP.NOM_SEGUNDO_APELLIDO) AS nombreContratante", 
			    "IFNULL(CONCAT(SP2.NOM_PERSONA, ' ', SP2.NOM_PRIMER_APELLIDO, ' ', SP2.NOM_SEGUNDO_APELLIDO), '') AS nombrefinado",
			    "DATE_FORMAT(SO.FEC_ALTA,'%d-%m-%Y') AS fechaAlta"
				)
		.from("SVC_ORDEN_SERVICIO SO")
		.innerJoin("SVC_CONTRATANTE SC", "SC.ID_CONTRATANTE = SO.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
		.innerJoin("SVC_ESTATUS_ORDEN_SERVICIO E ", "SO.ID_ESTATUS_ORDEN_SERVICIO = E.ID_ESTATUS_ORDEN_SERVICIO")
		.innerJoin("SVC_FINADO SFA", "SFA.ID_ORDEN_SERVICIO = SO.ID_ORDEN_SERVICIO")
		.leftJoin("SVC_PERSONA SP2", "SFA.ID_PERSONA = SP2.ID_PERSONA")
		.where("SO.ID_ESTATUS_ORDEN_SERVICIO IN (1, 2, 0, 4, 3, 6) ")
		.and("SO.ID_ORDEN_SERVICIO="+idOrden);
		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	
	public String consultarVerDetalleHistorialServicio(Integer idOrden) {
		
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilCapilla= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilEstatus= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilFecha= new SelectQueryUtil();
		
		
		selectQueryUtilCapilla.select("SC.DES_CAPILLA ")
		.from("SVT_DISPONIBILIDAD_CAPILLAS SDC")
		.innerJoin("SVC_CAPILLA SC", "SDC.ID_CAPILLA = SC.ID_CAPILLA ")
		.where("SDC.ID_ORDEN_SERVICIO = ST.ID_ORDEN_SERVICIO");
		
		String whereConsulta = " AND SSO.ID_ORDEN_SERVICIO = ST.ID_ORDEN_SERVICIO AND SSO.IND_ACTIVO=1 ";
		selectQueryUtilEstatus.select("COUNT(SSO.IND_ACTIVO) ")
		.from("SVC_ORDENES_HIST_SERVICIOS SSO")
		.where("SSO.ID_TIPO_SERVICIO = STS.ID_TIPO_SERVICIO "
				+ whereConsulta);
		
		selectQueryUtilFecha.select("SSO.TIM_SERVICIO ")
		.from("SVC_ORDENES_HIST_SERVICIOS SSO")
		.where("SSO.ID_TIPO_SERVICIO =STS.ID_TIPO_SERVICIO "
				+ whereConsulta);
		
		
		
		
		selectQueryUtil.select(
				"STS.ID_TIPO_SERVICIO AS idTipoServicio" ,
				"STS.DES_TIPO_SERVICIO AS nombreServicio" ,
				"CASE WHEN STS.ID_TIPO_SERVICIO = 1 "
				+ " THEN ("+selectQueryUtilCapilla.build()+")"
				+"ELSE '' END AS capilla",
				"CASE WHEN ("+selectQueryUtilEstatus.build()  +") >= 1 "
				+"THEN 'CONCLUIDO' ELSE 'PENDIENTE' END AS estatus",
				"CASE WHEN ("+selectQueryUtilFecha.build() +")  IS NOT NULL  "
				+" THEN SOHS.TIM_SERVICIO ELSE '' END AS fecha",
				"CASE WHEN ("+selectQueryUtilFecha.build() +")  IS NOT NULL  "
				+" THEN SOHS.REF_NOTAS ELSE '' END AS notas"
				)
		.from("SVC_ORDEN_SERVICIO ST")
		.innerJoin("SVC_CARAC_PRESUPUESTO SCP", "SCP.ID_ORDEN_SERVICIO = ST.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_DETALLE_CARAC_PRESUP SDCP", "SDCP.ID_CARAC_PRESUPUESTO = SCP.ID_CARAC_PRESUPUESTO")
		.innerJoin("SVT_SERVICIO SS ", "SS.ID_SERVICIO = SDCP.ID_SERVICIO")
		.innerJoin("SVC_TIPO_SERVICIO STS ", "STS.ID_TIPO_SERVICIO = SS.ID_TIPO_SERVICIO")
		.leftJoin("SVC_ORDENES_HIST_SERVICIOS SOHS", "SOHS.ID_ORDEN_SERVICIO = ST.ID_ORDEN_SERVICIO ")
		.where("ST.ID_ORDEN_SERVICIO = "+idOrden);
		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}

}
