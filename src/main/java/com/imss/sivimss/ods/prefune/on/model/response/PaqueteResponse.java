package com.imss.sivimss.ods.prefune.on.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaqueteResponse {

	private Integer idPaquete;
	private String nomPaquete;
	private String descPaquete;
	private Object [] serviciosPaquetes;
}
