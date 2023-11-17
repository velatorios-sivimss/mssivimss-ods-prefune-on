package com.imss.sivimss.ods.prefun.on.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonaEntityMyBatis2 {

	private Integer ID_PERSONA;

	private String CVE_RFC;
	private String cveCURP;
	
	@Column(name = "CVE_NSS")
	private String cveNSS;
	@Column(name = "NOM_PERSONA")
	private String nomPersona;
	@Column(name = "NOM_PRIMER_APELLIDO")
	private String primerApellido;
	@Column(name = "NOM_SEGUNDO_APELLIDO")
	private String segundoApellido;
	@Column(name = "NUM_SEXO")
	private Integer numSexo;
	@Column(name = "REF_OTRO_SEXO")
	private String otroSexo;
	@Column(name = "FEC_NAC")
	private String fecNac;
	@Column(name = "ID_PAIS")
	private Integer idPais;
	@Column(name = "ID_ESTADO")
	private Integer idEstado;
	@Column(name = "REF_TELEFONO")
	private String telefono;
	@Column(name = "REF_TELEFONO_FIJO")
	private String telefonoFijo;
	@Column(name = "REF_CORREO")
	private String correo;
	@Column(name = "TIP_PERSONA")
	private String tipoPersona;
	@Column(name = "ID_USUARIO_ALTA")
	private Integer idUsuarioAlta;
	@Column(name = "FEC_ALTA")
	private String fecAlta;
	@Column(name = "FEC_ACTUALIZACION")
	private String fecActualizacion;
	@Column(name = "FEC_BAJA")
	private String fecBaja;
	@Column(name = "ID_USUARIO_MODIFICA")
	private Integer idUsuarioModifica;
	@Column(name = "ID_USUARIO_BAJA")
	private Integer idUsuarioBaja;
	@Column(name = "NUM_INE")
	private String numINE;
	
}
