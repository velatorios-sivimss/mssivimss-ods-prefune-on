package com.imss.sivimss.ods.prefun.on.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.imss.sivimss.ods.prefun.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefun.on.model.entity.PersonaEntity;
import com.imss.sivimss.ods.prefun.on.model.request.Persona;
import com.imss.sivimss.ods.prefun.on.repository.PersonaRepository;
import com.imss.sivimss.ods.prefun.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefun.on.utils.DatosRequestUtil;
import com.imss.sivimss.ods.prefun.on.utils.Response;


@Component
public class ConsultasPersonas {

	@Autowired
	private PersonaRepository personaRepository;
	
	@Autowired
	private ServiciosArquetipo consultasMyBatis;
	

	public Response<Object> consultar() {
		List<Map<String, Object>> velatorios = personaRepository.buscarPersonas();
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,velatorios);
	}
	
	public Response<Object>  consultaPaginado( Integer pagina, Integer tamanio) {

		Pageable paginado = PageRequest.of(pagina, tamanio);
		List<Map<String, Object>> resp = personaRepository.buscarVelatorioPaginado(paginado);
		List<Map<String, Object>> respTotal = personaRepository.buscarVelatorioPaginadoTotal();
		Page<Map<String, Object>> objetoMapeado;
		Integer conteo =  Integer.parseInt( respTotal.get(0).get("conteo").toString() );
		objetoMapeado = new PageImpl<>(resp, paginado, conteo);


		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, objetoMapeado);
	}
	public Response<Object> consultarById (Integer id) {
		List<Map<String, Object>> velatorios = personaRepository.buscarPersonaById(id);
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,velatorios);
	}

	public Response<Object> consultarByIdPaginado (Integer id, Integer pagina, Integer tamanio) {
		Pageable paginado = PageRequest.of(pagina, tamanio);
		List<Map<String, Object>> resp = personaRepository.buscarVelatorioFiltroPag(paginado, id);
		List<Map<String, Object>> respTotal = personaRepository.buscarVelatorioFiltroPagTotal(id);
		Page<Map<String, Object>> objetoMapeado;
		Integer conteo =  Integer.parseInt( respTotal.get(0).get("conteo").toString() );
		objetoMapeado = new PageImpl<>(resp, paginado, conteo);
		
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,objetoMapeado);
	}

//	public Response<Object> guardarDatos (Persona persona, Integer idUsuarioAlta){
//	//	personaRepository.guardarPersona(persona, idUsuarioAlta);
//		consultasMyBatis.insertaPersona(persona, idUsuarioAlta);
//		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,null);
//	}

	public Response<Object> actualizarDatos (Persona persona, Integer idUsuarioModifica){
		personaRepository.actualizarPersona(persona, idUsuarioModifica);	
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,null);
	}

	public Response<Object> borrarDatos (int idPersona, Integer idUsuarioBaja){
		
		personaRepository.borrarPersona(idPersona,idUsuarioBaja);
		
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,null);
	}

//	public Response<Object> consultaMyBatis (){
//		
//		//consultasMyBatis.consultaPaginado(0,10);
//		
//		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,consultasMyBatis.consultaPaginado(0,10));
//	}
	
}
