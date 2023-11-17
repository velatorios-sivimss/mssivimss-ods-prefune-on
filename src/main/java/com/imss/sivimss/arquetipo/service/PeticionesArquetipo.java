package com.imss.sivimss.arquetipo.service;

import com.imss.sivimss.arquetipo.model.request.PersonaNombres;
import com.imss.sivimss.arquetipo.utils.Response;


public interface PeticionesArquetipo {
	public Response<Object> consultaUsandoMappers() ;
	public Response<Object>  consultaUsandoQuerysNativas() ;
	public Response<Object> nuevoRegistroUsandoMappersParam( PersonaNombres persona) ;
	public Response<Object>  nuevoRegistroUsandoMappersObj( PersonaNombres persona);
	public Response<Object> actualizarRegistroUsandoMappersObj(PersonaNombres persona, int id );
	public Response<Object> nuevoRegistroUsandoQuerysNativas(PersonaNombres persona);
}
