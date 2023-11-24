package com.imss.sivimss.ods.prefune.on.service;

import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.utils.Response;

public interface ConvenioPfService {

	public Response<Object>consultaMiConvenio(Paginado paginado,Integer idContratante);
	
	public Response<Object>consultaDetalleConvenio(Integer idConvenio);
	
}
