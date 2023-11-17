package com.imss.sivimss.ods.prefun.on.model.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermisosUsuarioDto {

	private Integer idFuncionalidad;
	private List<PermisosDto>permisos;
}
