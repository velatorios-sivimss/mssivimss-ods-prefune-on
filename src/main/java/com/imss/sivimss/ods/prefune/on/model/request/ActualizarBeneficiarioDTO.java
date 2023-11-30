package com.imss.sivimss.ods.prefune.on.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActualizarBeneficiarioDTO {
    private Integer idPersona;
    private String telefono;
    private String correo;
    private String documento;
    private String nombreIne;
    private String nombreActa;
    private Integer validaIne;
    private Integer validaActa;
    private Integer idContratante;
    private boolean actualizaArchivo;

}
