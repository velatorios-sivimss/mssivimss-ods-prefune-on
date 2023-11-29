package com.imss.sivimss.ods.prefune.on.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ods.prefune.on.utils.Response;

public interface VelatorioService {

	public Response<Object>consultarServiciosVelatorios(Authentication authentication)throws IOException;
}
