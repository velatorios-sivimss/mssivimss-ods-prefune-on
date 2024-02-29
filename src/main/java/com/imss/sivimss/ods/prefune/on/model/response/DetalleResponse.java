package com.imss.sivimss.ods.prefune.on.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DetalleResponse {
	
	private String folio;
	
	private String estatus;
	
	private String nombreContratante;
	
	private String nombrefinado;
	
	private String fechaAlta;
	
	private List<DetalleHistorialServicios> historialDetalle= new ArrayList<>();
	
}
