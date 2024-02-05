package com.imss.sivimss.ods.prefune.on.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.ods.prefune.on.service.CatalogosService;
import com.imss.sivimss.ods.prefune.on.service.VelatorioService;
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
@RequestMapping("/")
public class VelatoriosController {

	private final VelatorioService velatorioService;
	
	private final CatalogosService catalogosService;
	
	private final ProviderServiceRestTemplate providerRestTemplate;
	
	private final LogUtil logUtil;
	
	private static final String CONSULTA = "consulta";
	
	
	@GetMapping("mapa/velatorios")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarVelatorios(Authentication authentication) throws IOException{
		Response<Object>response=velatorioService.consultarServiciosVelatorios(authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	
	@GetMapping("catalogo/parentesco")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarCatalogoParentesco(Authentication authentication) throws IOException{
		Response<Object>response=catalogosService.consultarCatalogoParentesco(authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	
	@GetMapping("catalogo/promotores")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarCatalogoPromotores(Authentication authentication) throws IOException{
		Response<Object>response=catalogosService.consultarCatalogoPromotores(authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	
	@GetMapping("catalogo/paquetes/{idVelatorio}")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarCatalogoPaquetes(@PathVariable(required = true) Integer idVelatorio, Authentication authentication) throws IOException{
		Response<Object>response=catalogosService.consultarCatalogoPaquete(idVelatorio, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	
	@GetMapping("/consultar/codigo-postal/{dato}")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsultaPostal")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsultaPostal")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> obtenerCodigoPostal(@PathVariable(required = true) String dato, Authentication authentication)
			throws IOException {
		Response<Object> response = catalogosService.consultarCodigoPostal(dato, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo().intValue())));
	}
	
	@GetMapping("consultar/paises")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarPaises(Authentication authentication) throws IOException{
		Response<Object>response=catalogosService.consultarPais(authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	
	@GetMapping("consultar/estados")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarEstado(Authentication authentication) throws IOException{
		Response<Object>response=catalogosService.consultarEstado(authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}
	
	@GetMapping("/consultar/matricula/{dato}")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsultaNss")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsultaNss")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> obtenerNss(@PathVariable(required = true) String dato, Authentication authentication)
			throws IOException {
		Response<Object> response = catalogosService.consultarNss(dato, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo().intValue())));
	}
	
	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsulta(Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsulta(@PathVariable(required = true) Integer idVelatorio, Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsultaPostal(@PathVariable(required = true) String dato, Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsultaNss(@PathVariable(required = true) String dato, Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
}
