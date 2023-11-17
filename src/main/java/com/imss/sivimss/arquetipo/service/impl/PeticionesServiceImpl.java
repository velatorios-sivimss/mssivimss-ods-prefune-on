package com.imss.sivimss.arquetipo.service.impl;

import java.io.IOException;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.arquetipo.model.request.Persona;
import com.imss.sivimss.arquetipo.model.request.UsuarioDto;
import com.imss.sivimss.arquetipo.service.PeticionesService;
import com.imss.sivimss.arquetipo.utils.DatosRequestUtil;
import com.imss.sivimss.arquetipo.utils.Response;


@Service
public class PeticionesServiceImpl implements PeticionesService {

	@Value("${endpoints.ms-principal-endpoint}")
	private String urlDominioPrincipal;

	@Autowired
	private DatosRequestUtil datosUtil;
	
	@Autowired
	private ConsultasPersonas consultasUtil;
	

	@Override
	public Response<Object>  consultarById(Integer id,Authentication authentication) throws IOException {
		return consultasUtil.consultarById(id);
	}
	
	@Override
	public Response<Object> consultar(Authentication authentication) throws IOException {
		return consultasUtil.consultar();
	}
	
	@Override
	public Response<Object> consultarPaginado(Map<String, Object> params, Authentication authentication) throws Throwable {
		Integer pagina =  Integer.parseInt( params.get("pagina").toString() );
		Integer tamanio =  Integer.parseInt( params.get("tamanio").toString() );
		return consultasUtil.consultaPaginado( pagina, tamanio);
		
	}

//	@Override
//	public Response<Object> guardarDatos(Persona request, Authentication authentication) throws Throwable {
//		UsuarioDto user = datosUtil.getUserData(authentication);
//		return consultasUtil.guardarDatos(request, user.getIdUsuario());
//		
//	}	

	@Override
	public Response<Object> actualizaDatos(Persona request, Authentication authentication) throws Throwable {
		UsuarioDto user = datosUtil.getUserData(authentication);
		return consultasUtil.actualizarDatos(request, user.getIdUsuario());
		
	}

	@Override
	public Response<Object> borrarDatos(Persona request, Authentication authentication) throws Throwable {
		UsuarioDto user = datosUtil.getUserData(authentication);

		return consultasUtil.borrarDatos(request.getIdPersona(), user.getIdUsuario());
		
	}
	
	@Override
	public Response<Object> consultaMyBatis() throws Throwable {
//		return consultasUtil.consultaMyBatis();
		return null;
		
	}

}
