package com.imss.sivimss.ods.prefune.on.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ods.prefune.on.utils.Response;

public interface CatalogosService {

	Response<Object>consultarCatalogoParentesco(Authentication authentication) throws IOException;
	
	Response<Object>consultarCatalogoPromotores(Authentication authentication) throws IOException;

	Response<Object>consultarCatalogoPaquete(Integer idVelatorio,Authentication authentication) throws IOException;

	Response<Object>consultarCodigoPostal(String codigoPostal,Authentication authentication) throws IOException;
	
	Response<Object>consultarPais(Authentication authentication) throws IOException;
	
}
