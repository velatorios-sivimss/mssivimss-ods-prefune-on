package com.imss.sivimss.arquetipo.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.imss.sivimss.arquetipo.model.request.PersonaNombres;
import com.imss.sivimss.arquetipo.service.PeticionesArquetipo;
import com.imss.sivimss.arquetipo.utils.LogUtil;
import com.imss.sivimss.arquetipo.utils.ProviderServiceRestTemplate;
import com.imss.sivimss.arquetipo.utils.Response;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/arquetipo")
public class ArqController {
	@Autowired
	private PeticionesArquetipo arq;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	@Autowired
	private LogUtil logUtil;

	private static final String CONSULTA = "consulta";
	private static final String INSERT = "insert";
	private static final String UPDATE = "update";
	
	@GetMapping("/consulta/mappers")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> consultaUsandoMappers(Authentication authentication)	throws IOException {
		Response<Object> response = arq.consultaUsandoMappers();
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@GetMapping("/consulta/querynativa")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> consultaUsandoQuerysNativas(Authentication authentication)	throws Throwable {
		Response<Object> response = arq.consultaUsandoQuerysNativas();
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/insert/mappers")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackInsert")
	@Retry(name = "msflujo", fallbackMethod = "fallbackInsert")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> nuevoRegistroUsandoMappersParam(@Validated @RequestBody PersonaNombres persona, Authentication authentication)	throws Throwable {
		Response<Object> response = arq.nuevoRegistroUsandoMappersParam(persona);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/insert/querynativa")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackInsert")
	@Retry(name = "msflujo", fallbackMethod = "fallbackInsert")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> nuevoRegistroUsandoQueryNativa(@RequestBody PersonaNombres persona, Authentication authentication)	throws Throwable {
		Response<Object> response = arq.nuevoRegistroUsandoQuerysNativas(persona);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/insert/mappers/obj")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackInsert")
	@Retry(name = "msflujo", fallbackMethod = "fallbackInsert")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> nuevoRegistroUsandoMappersObj(@RequestBody PersonaNombres persona, Authentication authentication)	throws Throwable {
		Response<Object> response = arq.nuevoRegistroUsandoMappersObj(persona);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/update/mappers/obj/{id}")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackUpdate")
	@Retry(name = "msflujo", fallbackMethod = "fallbackUpdate")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> actualizarRegistroUsandoMappersObj(@Validated @RequestBody PersonaNombres persona,
			@PathVariable("id") @Min(1) int id, Authentication authentication)	throws Throwable {
		Response<Object> response = arq.actualizarRegistroUsandoMappersObj(persona,id);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	
	/*
	 * 
	 * FallBack
	 * 
	 */
	

	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsulta(Authentication authentication,
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
