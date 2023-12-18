package com.imss.sivimss.ods.prefune.on.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgregarConvenioEmpresaDTO {
    private Integer idConvenioPF;
    private String folio;
    private Integer idPaquete;
    private Integer idVelatorio;
    private Integer idPromotor;
    private Integer idUsuario;
    private Integer idDomicilio;
    private String calle;
    private String noExterior;
    private String noInterior;
    private String cp;
    private String colonia;
    private String municipio;
    private String estado;
    private Integer idEmpresaConvenioPF;
    private String nombre;
    private String razonSocial;
    private String rfcEmpresa;
    private Integer idPais;
    private String telefono;
    private String correo;
}
