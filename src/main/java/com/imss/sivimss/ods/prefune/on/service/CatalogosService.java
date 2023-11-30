package com.imss.sivimss.ods.prefune.on.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ods.prefune.on.utils.Response;

public interface CatalogosService {

	public Response<Object>consultarCatalogo(Authentication authentication) throws IOException;
}
