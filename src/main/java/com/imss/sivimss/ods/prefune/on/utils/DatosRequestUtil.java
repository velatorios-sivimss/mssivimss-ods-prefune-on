package com.imss.sivimss.ods.prefune.on.utils;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.imss.sivimss.ods.prefune.on.model.request.UsuarioDto;

@Component
public class DatosRequestUtil {

	
	public String getDatosJson (DatosRequest datosRequest) {
		return datosRequest.getDatos().get(AppConstantes.DATOS).toString();
	}
	
	public UsuarioDto getUserData(Authentication authentication) {
		Gson gson = new Gson();
		return gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
	}
}
