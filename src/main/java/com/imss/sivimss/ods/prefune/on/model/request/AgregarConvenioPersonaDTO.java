package com.imss.sivimss.ods.prefune.on.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgregarConvenioPersonaDTO {
    private Integer idConvenioPF;
    private String folio;
    private Integer idVelatorio;
    private Integer idTipoContratacion;
    private Integer idPromotor;
    private Integer idUsuario;
    private Integer idPaquete;
    private Integer idContratoConvenioPaquete;
    private Integer idContratante;
    private Integer idEnfermedad;
    private String otraEnfermedad;
    private Integer idPersona;
    private String cveMatricula;
    private Integer idDomicilio;
    private String calle;
    private String noExterior;
    private String noInterior;
    private String cp;
    private String colonia;
    private String municipio;
    private String estado;
    private Integer idValidaDocumento;
    private boolean validaIne;
    private String nombreIne;
    private boolean validaCurp;
    private String nombreCurp;
    private boolean validaRfc;
    private String nombreRfc;
    private String archivoIne;
    private String archivoCurp;
    private String archivoRfc;
    private Integer idPais;
    private String rfc;
    private String curp;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String fechaNacimiento;
    private String telefono;
    private String correo;

}
