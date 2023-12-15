package com.imss.sivimss.ods.prefune.on.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgregarBeneficiarioDTO {
    private Integer idPersona;
    private Integer idConvenio;
    private String curp;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String fechaNacimiento;
    private Integer edad;
    private Integer idParentesco;
    private String rfc;
    private String telefono;
    private String correo;
    private String documento;
    private String nombreIne;
    private String nombreActa;
    private Integer validaIne;
    private Integer validaActa;
    private Integer idContratante;
    private boolean actualizaArchivo;
    private Integer idUsuario;
    private Integer idVelatorio;
    private Integer idContratanteBeneficiarios;

}
