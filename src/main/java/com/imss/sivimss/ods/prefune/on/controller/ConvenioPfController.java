package com.imss.sivimss.ods.prefune.on.controller;

import java.io.IOException;
import java.util.Map;

import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.springframework.http.HttpHeaders;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.imss.sivimss.ods.prefune.on.model.request.ActualizarBeneficiarioDTO;
import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.model.request.PdfDto;
import com.imss.sivimss.ods.prefune.on.model.request.PersonaNombres;
import com.imss.sivimss.ods.prefune.on.service.ConvenioPfService;
import com.imss.sivimss.ods.prefune.on.utils.DatosRequest;
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

	@PostMapping("/mis-convenios")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsultaPaginada")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsultaPaginada")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> consultaMiConvenio(@Validated @RequestBody Paginado paginado,
			Authentication authentication) throws IOException {
		Response<Object> response = convenioPfService.consultaMiConvenio(paginado, 121, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}

	@GetMapping("/detalle-convenio/{idConvenio}")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsulta")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> consultaDetalleConvenio(@PathVariable(required = true) Integer idConvenio,
			Authentication authentication) throws IOException {
		Response<Object> response = convenioPfService.consultaDetalleConvenio(idConvenio, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/renovar-convenio")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackInsert")
	@Retry(name = "msflujo", fallbackMethod = "fallbackInsert")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> renovarConvenioPF(@RequestBody Map<String, Object> datos,
			Authentication authentication) throws Throwable {
		String idConvenio = datos.get("idConvenio").toString();
		Response<Object> response = convenioPfService.renovarConvenio(idConvenio, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}

	@PostMapping("/generarDocumentoNuevoPlan")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackDescargarArchivos")
	@Retry(name = "msflujo", fallbackMethod = "fallbackDescargarArchivos")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> generarDocumentosNuevoPlan(@RequestBody PdfDto pdfDto,
			Authentication authentication)
			throws IOException {
		String tipoReporte;
		if (Objects.nonNull(pdfDto.getTipoReporte()) && pdfDto.getTipoReporte().equalsIgnoreCase("xls")) {
			tipoReporte = "xlsx";
		} else {
			tipoReporte = "pdf";
		}
		Response<?> response = convenioPfService.generarPDF(pdfDto, authentication);

		return CompletableFuture.supplyAsync(() -> response.getCodigo() == HttpStatus.OK.value()
				? ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/" + tipoReporte)
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=formato-prueba." + tipoReporte)
						.body(Base64.getDecoder().decode(response.getDatos().toString()))
				: new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/actualizar-beneficiario")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackActualizarBeneficiario")
	@Retry(name = "msflujo", fallbackMethod = "fallbackActualizarBeneficiario")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> actualizarBeneficiario(@RequestBody ActualizarBeneficiarioDTO request,
			Authentication authentication) {

		Response<Object> response = convenioPfService.actualizarBeneficiario(request, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response,
						HttpStatus.valueOf(response.getCodigo())));
	}
	
	
	@PostMapping("/validar-curp-rfc")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackConsultaCurpRfc")
	@Retry(name = "msflujo", fallbackMethod = "fallbackConsultaCurpRfc")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>consultarCatalogoEmpresa(@RequestBody JsonNode curpRfc, Authentication authentication) throws IOException{
		
		Response<Object>response=convenioPfService.consultarCurpRfc(curpRfc, authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));

	}

	/*
	 * 
	 * FallBack
	 * 
	 */

	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsultaPaginada(@RequestBody Paginado paginado,
			Authentication authentication,
			CallNotPermittedException e) throws Throwable {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
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
	private CompletableFuture<Object> fallbackConsulta(@PathVariable Integer idContratante,
			Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackInsert(@RequestBody Map<String, Object> datos,
			Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), INSERT, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackUpdate(@RequestBody PersonaNombres persona,
			@PathVariable int id, Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), UPDATE, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<Object> fallbackDescargarArchivos(@RequestBody PdfDto datos,
			Authentication authentication, CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), CONSULTA + " generar reporte", authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackActualizarBeneficiario(@RequestBody ActualizarBeneficiarioDTO request,
			Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<Object> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), UPDATE, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@SuppressWarnings("unused")
	private CompletableFuture<Object> fallbackConsultaCurpRfc(@RequestBody JsonNode curpRfc,
			Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);

		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
}
