package com.imss.sivimss.arquetipo.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"idVelatorio", "nomVelatorio"})
public interface VelatorioResponse {

	Integer getIdVelatorio();
	String getNomVelatorio();

}
