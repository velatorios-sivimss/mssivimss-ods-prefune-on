package com.imss.sivimss.ods.prefune.on.model.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MiConvenioResponse {

	List<Map<String, Object>> datosGenerales = new ArrayList<>();
	List<Map<String, Object>> beneficiarios = new ArrayList<>();
	List<Map<String, Object>> datosRenovacion = new ArrayList<>();
}
