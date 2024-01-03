package com.imss.sivimss.ods.prefune.on.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DetalleResponse {
	
	public String folio;
	
	public String estatus;
	
	public String nombreContratante;
	
	public String nombrefinado;
	
	public String fechaAlta;
	
	public List<DetalleHistorialServicios> historialDetalle= new ArrayList<>();
	
}
