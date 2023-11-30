package com.imss.sivimss.ods.prefune.on.service.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.utils.SelectQueryUtil;

@Service
public class Catalogos {
	
	private String query;
	
	private static final Logger log = LoggerFactory.getLogger(Catalogos.class);

	public String consultarParentesco() {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SO.ID_PARENTESCO AS idParentesco, SO.DES_PARENTESCO AS nombreParentesco")
		.from("SVC_PARENTESCO SO ")
		.orderBy("SO.DES_PARENTESCO ASC ");
		
		query=selectQueryUtil.build();
		log.info("consultarParentesco: {}",query);
		
		return query;
	}

}
