package com.imss.sivimss.ods.prefune.on.service;

import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.model.request.PersonaNombres;
import com.imss.sivimss.ods.prefune.on.utils.Response;


public interface PeticionesArquetipo {
	
	public Response<Object> consultaUsandoQuerysNativas() ;
	public Response<Object> nuevoRegistroUsandoMappersObj( PersonaNombres persona);
	public Response<Object> actualizarRegistroUsandoMappersObj(PersonaNombres persona, int id );
	public Response<Object> paginadoGenerico(Paginado paginado );

}
