package com.imss.sivimss.ods.prefune.on.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MapaVelatoriosResponse {

	private Integer idVelatorio;
	private String nombreVelatorio;
	private String direccion;
	private String telefono;
	private String longitud;
	private String latitud;
	
	private Object [] servicios; 
}
