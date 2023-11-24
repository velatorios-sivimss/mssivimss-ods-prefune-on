package com.imss.sivimss.ods.prefune.on.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.model.request.PersonaNombres;
import com.imss.sivimss.ods.prefune.on.service.ConvenioPfService;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.ProviderServiceRestTemplate;
import com.imss.sivimss.ods.prefune.on.utils.Response;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/convenio-pf")
public class ConvenioPfController {

	private final ConvenioPfService convenioPfService;
	
	private final LogUtil logUtil;
	
	private final ProviderServiceRestTemplate providerRestTemplate;
	
	private static final String CONSULTA = "consulta";
	private static final String INSERT = "insert";
	private static final String UPDATE = "update";
	
	@GetMapping("/mis-convenios/{idContratante}")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultaMiConvenio(@PathVariable(required = true) Integer idContratante,Authentication authentication){
		Response<Object>response=convenioPfService.consultaMiConvenio(idContratante);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	
	/*
	 * 
	 * FallBack
	 * 
	 */
	
	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsultaPaginada(@RequestBody Paginado paginado,Authentication authentication,
			CallNotPermittedException e) throws Throwable {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		 logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),e.getMessage(),CONSULTA,authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsulta(Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		 logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),e.getMessage(),CONSULTA,authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsulta(@PathVariable Integer idContratante,Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		 logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),e.getMessage(),CONSULTA,authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackInsert(@RequestBody PersonaNombres persona, Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		 logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),e.getMessage(),INSERT,authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackUpdate(@RequestBody PersonaNombres persona,
			@PathVariable int id, Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		 logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),e.getMessage(),UPDATE,authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
}
