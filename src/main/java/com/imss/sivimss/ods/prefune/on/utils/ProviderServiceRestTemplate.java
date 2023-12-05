package com.imss.sivimss.ods.prefune.on.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.imss.sivimss.ods.prefune.on.security.jwt.JwtTokenProvider;


@Service
public class ProviderServiceRestTemplate {

	@Autowired
	private RestTemplateUtil restTemplateUtil;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private static final Logger log = LoggerFactory.getLogger(ProviderServiceRestTemplate.class);
	
	private static final String ERROR_RECUPERAR_INFORMACION = "Ha ocurrido un error al recuperar la informacion";


	@SuppressWarnings("unchecked")
	public Response<Object> consumirServicio(Map<String, Object> dato, String url, Authentication authentication)
			throws IOException {
		try {
			return (Response<Object>) restTemplateUtil.sendPostRequestByteArrayToken(url,
					new EnviarDatosRequest(dato), jwtTokenProvider.createToken((String) authentication.getPrincipal()),
					Response.class);
		} catch (IOException exception) {
			log.error("Ha ocurrido un error al recuperar la informacion");
			throw exception;
		}
	}

	@SuppressWarnings("unchecked")
	public Response<Object> consumirServicioArchivo(String datos, MultipartFile[] files, String url,
			Authentication authentication) throws IOException {
		try {
			return (Response<Object>) restTemplateUtil.sendPostRequestByteArrayArchviosToken(url,
					new EnviarDatosArchivosRequest(datos, files),
					jwtTokenProvider.createToken((String) authentication.getPrincipal()), Response.class);
		} catch (IOException exception) {
			log.error("Ha ocurrido un error al recuperar la informacion");
			throw exception;
		}
	}
	
	public Response<Object> consumirServicioExternoGet(String url)
			throws IOException {
		return restTemplateUtil.sendGetRequest(url);
	}

	public Response<Object> consumirServicioReportes(Map<String, Object> dato, String url,
			Authentication authentication) throws IOException {
		try {
			// esto es hasta que se tenga la parte de token
			return (Response<Object>) restTemplateUtil.sendPostRequestByteArrayReportesToken(url,
					//new DatosReporteDTO(dato), jwtTokenProvider.createToken((String) authentication.getPrincipal()),
					new DatosReporteDTO(dato), jwtTokenProvider.createToken(""),
					Response.class);
		} catch (IOException exception) {
			log.error(ERROR_RECUPERAR_INFORMACION);
			throw exception;
		}
	}
	

	@SuppressWarnings("unchecked")
	public Response<Object> respuestaProvider(String e) {
		StringTokenizer exeception = new StringTokenizer(e, ":");
		Gson gson = new Gson();
		int i = 0;
		int totalToken = exeception.countTokens();
		StringBuilder mensaje = new StringBuilder("");
		int codigoError = HttpStatus.INTERNAL_SERVER_ERROR.value();
		int isExceptionResponseMs = 0;
		while (exeception.hasMoreTokens()) {
			String str = exeception.nextToken();
			i++;
			if (i == 2) {
				String[] palabras = str.split("\\.");
				for (String palabra : palabras) {
					if ("BadRequestException".contains(palabra)) {
						codigoError = HttpStatus.BAD_REQUEST.value();
					} else if ("ResourceAccessException".contains(palabra)) {
						codigoError = HttpStatus.INTERNAL_SERVER_ERROR.value();

					}
				}
			} else if (i == 3) {

				if (str.trim().chars().allMatch( Character::isDigit )) {
					isExceptionResponseMs = 1;
				}

				mensaje.append(codigoError == HttpStatus.INTERNAL_SERVER_ERROR.value()
						?AppConstantes.CIRCUITBREAKER
						: str);

			} else if (i >= 4 && isExceptionResponseMs == 1) {
				if (i == 4) {
					mensaje.delete(0, mensaje.length());
				}
				mensaje.append(str).append(i != totalToken ? ":" : "");

			}
		}
		
		Response<Object> response;
		try {
			response = isExceptionResponseMs == 1 && !mensaje.toString().trim().equals("")? gson.fromJson(mensaje.substring(2, mensaje.length() - 1), Response.class)
				: new Response<>(true, codigoError,mensajeRespuesta(mensaje.toString().trim()) , Collections.emptyList());
			
			log.info("respuestaProvider error: {}",e);
		} catch (Exception e2) {
			log.info("respuestaProvider error: {}",e2.getMessage());
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Collections.emptyList());
		}
		
		return response;
	}
	
	private String mensajeRespuesta(String e) {
		return e.trim().equals("")?AppConstantes.CIRCUITBREAKER:e.trim();
	}
}
