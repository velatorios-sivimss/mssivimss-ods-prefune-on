package com.imss.sivimss.arquetipo.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final HttpStatus codigo;

	private final String mensaje;

	private final String datos;

	private boolean error;

	public BadRequestException(HttpStatus codigo, String mensaje) {
		super(mensaje);
		this.codigo = codigo;
		this.datos = "";
		this.mensaje = mensaje;
		this.error = true;
	}
}