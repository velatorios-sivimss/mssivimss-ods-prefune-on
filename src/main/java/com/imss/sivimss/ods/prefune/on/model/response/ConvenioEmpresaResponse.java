package com.imss.sivimss.ods.prefune.on.model.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConvenioEmpresaResponse {

	private DatosEmpresaResponse datosEmpresaResponse;
	private List<Map<String, Object>> personasEmpresa = new ArrayList<>();
}
