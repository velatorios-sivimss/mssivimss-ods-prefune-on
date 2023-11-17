package com.imss.sivimss.arquetipo.model.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestEjemplo {

	@NotBlank(message = "Nombre no puede ser vacío")
    private String nombre;
	
}
