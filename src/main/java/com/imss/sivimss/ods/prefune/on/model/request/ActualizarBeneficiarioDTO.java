package com.imss.sivimss.ods.prefune.on.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class ActualizarBeneficiarioDTO {
    private Integer idPersona;
    private String telefono;
    private String correo;
}
