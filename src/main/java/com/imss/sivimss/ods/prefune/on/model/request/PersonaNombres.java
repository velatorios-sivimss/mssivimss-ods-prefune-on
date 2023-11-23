package com.imss.sivimss.ods.prefune.on.model.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PersonaNombres {
	
	@NotBlank(message = "Nombre no puede ser vacío")
    private String nomPersona;
	
	@NotBlank(message = "Primer Apellido no puede ser vacío")
	private String primerApellido;
	
	@NotBlank(message = "Segundo Apellido no puede ser vacío")
	private String segundoApellido;
	
}
