package com.imss.sivimss.ods.prefune.on.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatosEmpresaResponse {

	private Integer idConvenio;
	private Integer idEmpresa;
	private String nombre;
	private String razonSocial;
	private String rfc;
	private Integer idPais;
	private String cp;
	private String colonia;
	private String municipio;
	private String estado;
	private String calle;
	private String numInterior;
	private String numExterior;
	private String telefono;
	private String correo;
	
}
