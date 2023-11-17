package com.imss.sivimss.ods.prefun.on.service;

import com.imss.sivimss.ods.prefun.on.model.request.PersonaNombres;
import com.imss.sivimss.ods.prefun.on.utils.Response;


public interface PeticionesArquetipo {
	public Response<Object> consultaUsandoMappers() ;
	public Response<Object>  consultaUsandoQuerysNativas() ;
	public Response<Object> nuevoRegistroUsandoMappersParam( PersonaNombres persona) ;
	public Response<Object>  nuevoRegistroUsandoMappersObj( PersonaNombres persona);
	public Response<Object> actualizarRegistroUsandoMappersObj(PersonaNombres persona, int id );
	public Response<Object> nuevoRegistroUsandoQuerysNativas(PersonaNombres persona);
}
