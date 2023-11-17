package com.imss.sivimss.arquetipo.model.request;

import javax.validation.constraints.NotBlank;


import lombok.Data;

@Data
public class RequestValidationForm {
	@NotBlank(message = "Nombre no puede ser vacío")
    private String nombre;
}
