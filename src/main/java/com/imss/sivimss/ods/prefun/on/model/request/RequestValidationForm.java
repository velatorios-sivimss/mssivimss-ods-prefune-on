package com.imss.sivimss.ods.prefun.on.model.request;

import javax.validation.constraints.NotBlank;


import lombok.Data;

@Data
public class RequestValidationForm {
	@NotBlank(message = "Nombre no puede ser vacío")
    private String nombre;
}
