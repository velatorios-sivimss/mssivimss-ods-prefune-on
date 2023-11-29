package com.imss.sivimss.ods.prefune.on.service.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.utils.SelectQueryUtil;

@Service
public class MapaVelatorio {

	private String query;
	
	
	private static final Logger log = LoggerFactory.getLogger(MapaVelatorio.class);
	
	public String consultarVelatorios() {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select(
				"SE.ID_VELATORIO AS idVelatorio",
				"SE.DES_VELATORIO AS nombreVelatorio",
				"SD.REF_CALLE AS direccion",
				"SE.NUM_TELEFONO AS telefono",
				"SE.NUM_LATITUD AS latitud",
				"SE.NUM_LONGITUD AS longitud")
		.from("SVC_VELATORIO SE")
		.innerJoin("SVT_DOMICILIO SD ", "SE.ID_DOMICILIO = SD.ID_DOMICILIO")
		.where("IND_ACTIVO = 1");
		
		query=selectQueryUtil.build();
		log.info("consultarVelatorios: {}",query);
		
		return query;
	}
	
	public String consultarServiciosVelatorios(Integer idVelatorio) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select(
				"SO.REF_SERVICIO_ONLINE  AS servicio")
		.from("SVC_VELATORIO SE")
		.innerJoin("SVC_VELATORIO_SERVICIO_ONLINE SSO ", "SE.ID_VELATORIO = SSO.ID_VELATORIO ")
		.innerJoin("SVC_SERVICIO_ONLINE SO ", "SSO.ID_SERVICIO_ONLINE = SO.ID_SERVICIO_ONLINE ")
		.where("SE.ID_VELATORIO = ".concat(idVelatorio.toString()))
		.and("SSO.IND_ACTIVO = 1");
		
		query=selectQueryUtil.build();
		log.info("consultarServiciosVelatorios: {}",query);
		
		return query;
	}

}
