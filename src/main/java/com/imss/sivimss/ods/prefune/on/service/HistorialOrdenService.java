package com.imss.sivimss.ods.prefune.on.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.utils.Response;

public interface HistorialOrdenService {
	
	public Response<Object>consultarOrdenes(Paginado paginado, Integer idContratante, Authentication authentication) throws IOException;
	public Response<Object>consultarVerDetalleOrdenes(Integer idOrden, Authentication authentication) throws IOException;

}
