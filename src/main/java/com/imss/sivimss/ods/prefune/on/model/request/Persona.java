package com.imss.sivimss.ods.prefune.on.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Persona {
	
	@NotBlank(message = "Nombre no puede ser vacío")
	@Size(min=1, max=45)
    private String nomPersona;
	
	@NotBlank(message = "Primer Apellido no puede ser vacío")
	@Size(min=1, max=45)
	private String primerApellido;
	
	@NotBlank(message = "Segundo Apellido no puede ser vacío")
	@Size(min=1, max=45)
	private String segundoApellido;
	
	@Min(value=18,message = "Edad Minima 18")
	private int edad;
	
	@Min(value=1,message = "Los velatorios deben estar dentro del rango")
	@Max(value=18,message = "Los velatorios deben estar dentro del rango")
	private int idVelatorio;
	
	@NotBlank
	@Pattern(regexp = "^([A-ZÑ\\x26]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))((-)?([A-Z\\d]{3}))?$",
			message = "Debe ser un RFC válido")
	private String cveRFC;
	
	@Email(message = "Debe ser un correo válido")
	@Size(min=1, max=45)
	private String correo;

	private int idPersona;
	@Pattern(regexp = "^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0\\d|1[0-2])(?:[0-2]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$"
			, message = "Debe ser CURP Valido")
	private String cveCURP;
	
	@Pattern(regexp = "/^(\\d{2})(\\d{2})(\\d{2})\\d{5}$/", message = "Debe ser CURP Valido")
	private String cveNSS;
	
	@Min(1)
	@Max(3)
	private Integer numSexo;
	
	@Size(min=0, max=45)
	private String otroSexo;
	
	@NotBlank(message = "Fecha de nacimiento no puede ser vacío")
	@Past(message = "La fecha debe ser anterior a la actual")
	private String fecNac;
	
	@Min(value=1,message = "Se debe seleccionar un país")
	private Integer idPais;
	
	@Min(value=1,message = "Se debe seleccionar un estado")
	private Integer idEstado;
	
	@NotBlank(message = "Telefono no puede ser vacío")
	@Size(min=1, max=45)
	private String telefono;
	
	@NotBlank(message = "Telefono FIjo no puede ser vacío")
	@Size(min=1, max=45)
	private String telefonoFijo;
	
	@NotBlank(message = "Tipo de persona no puede ser vacío")
	private String tipoPersona;
	
	@NotBlank(message = "INE no puede ser vacío")
	@Size(min=1, max=50)
	private String numINE;
	
}
