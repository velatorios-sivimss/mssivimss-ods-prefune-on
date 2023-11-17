package com.imss.sivimss.ods.prefun.on.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VelatorioDTO {
	private Integer idVelatorio;
	private String desVelatorio; 
	private Integer idUsuarioAdmin; 
	private String nomRepSanitario;
	private String cveAsignacion; 
	private Integer idDomicilio; 
	private String numTel; 
	private Integer idUsuarioAlta; 
	private Integer idDelegacion;
	private Integer idUsuarioModifica; 
	private Integer idUsuarioBaja; 

}
