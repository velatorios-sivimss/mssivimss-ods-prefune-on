package com.imss.sivimss.arquetipo.service;

import com.imss.sivimss.arquetipo.model.request.Paginado;
import com.imss.sivimss.arquetipo.model.request.PersonaNombres;
import com.imss.sivimss.arquetipo.utils.Response;


public interface PeticionesArquetipo {
	
	public Response<Object> consultaUsandoQuerysNativas() ;
	public Response<Object> nuevoRegistroUsandoMappersObj( PersonaNombres persona);
	public Response<Object> actualizarRegistroUsandoMappersObj(PersonaNombres persona, int id );
	public Response<Object> paginadoGenerico(Paginado paginado );

}
