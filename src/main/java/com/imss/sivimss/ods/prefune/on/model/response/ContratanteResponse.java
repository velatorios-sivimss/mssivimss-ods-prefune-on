package com.imss.sivimss.ods.prefune.on.model.response;

import java.util.HashMap;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContratanteResponse {

	Map<String, Object> datosGenerales = new HashMap<>();
	Map<String, Object> domicilio = new HashMap<>();

}
