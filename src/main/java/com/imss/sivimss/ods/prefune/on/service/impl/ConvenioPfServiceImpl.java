package com.imss.sivimss.ods.prefune.on.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imss.sivimss.ods.prefune.on.configuration.MyBatisConfig;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.BeneficiariosMapper;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.ConvenioMapper;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.ConvenioPFMapper;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.ConvenioPFMapperEmpresa;
import com.imss.sivimss.ods.prefune.on.model.entity.ConvenioEntityMyBatis;
import com.imss.sivimss.ods.prefune.on.model.request.ActualizarBeneficiarioDTO;
import com.imss.sivimss.ods.prefune.on.model.request.AgregarBeneficiarioDTO;
import com.imss.sivimss.ods.prefune.on.model.request.AgregarConvenioEmpresaDTO;
import com.imss.sivimss.ods.prefune.on.model.request.AgregarConvenioPersonaDTO;
import com.imss.sivimss.ods.prefune.on.model.request.ConvenioRequest;
import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.model.request.PdfDto;
import com.imss.sivimss.ods.prefune.on.model.response.BusquedaInformacionReporteResponse;
import com.imss.sivimss.ods.prefune.on.model.response.ConvenioEmpresaResponse;
import com.imss.sivimss.ods.prefune.on.model.response.DatosEmpresaResponse;
import com.imss.sivimss.ods.prefune.on.model.response.MiConvenioResponse;
import com.imss.sivimss.ods.prefune.on.model.response.RenapoResponse;
import com.imss.sivimss.ods.prefune.on.service.ConvenioPfService;
import com.imss.sivimss.ods.prefune.on.service.beans.ConsultaMiConvenio;
import com.imss.sivimss.ods.prefune.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.PaginadoUtil;
import com.imss.sivimss.ods.prefune.on.utils.ProviderServiceRestTemplate;
import com.imss.sivimss.ods.prefune.on.utils.Response;

@Service
public class ConvenioPfServiceImpl implements ConvenioPfService {

	@Autowired
	private LogUtil logUtil;

	@Autowired
	private MyBatisConfig myBatisConfig;

	@Autowired
	private ConsultaMiConvenio miConvenio;

	@Autowired
	private PaginadoUtil paginadoUtil;

	private static final Logger log = LoggerFactory.getLogger(ConvenioPfServiceImpl.class);

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Value("${reporte.convenio-nuevo-pf}")
	private String convenioNuevoPlan;

	@Value("${endpoints.renapo}")
	private String urlRenapo;

	private final String ERROR = "error: {}";

	private List<Map<String, Object>> resultServiciosCatalogo = new ArrayList<>();
	
	private DatosEmpresaResponse empresaResponse;

	private static final String PERIODO_RENOVACION = "periodoRenovacion";
	private static final String PATTERN = "dd-MM-yyyy";

	@Override
	public Response<Object> consultaMiConvenio(Paginado paginado, Integer idContratante, Authentication authentication)
			throws IOException {
		Page<Map<String, Object>> result = null;
		try {
			result = paginadoUtil.paginado(paginado.getPagina(), paginado.getTamanio(),
					miConvenio.consultaMiConvenio(idContratante));
		} catch (Exception e) {

			log.info(ERROR, e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
					authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO,
					Arrays.asList());
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, result);
	}

	@Override
	public Response<Object> consultaDetalleConvenio(Integer idConvenio, Authentication authentication)
			throws IOException {
		List<Map<String, Object>> resultDatosGenerales = new ArrayList<>();
		List<Map<String, Object>> resultDatosBeneficios = new ArrayList<>();
		List<Map<String, Object>> resultDatosRenovacion = new ArrayList<>();
		MiConvenioResponse convenioResponse = new MiConvenioResponse();
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();

		try (SqlSession session = sqlSessionFactory.openSession()) {
			Consultas consultas = session.getMapper(Consultas.class);
			resultDatosGenerales = consultas.selectNativeQuery(miConvenio.consultarDatosGeneales(idConvenio));

			resultDatosBeneficios = consultas.selectNativeQuery(miConvenio.consultarBeneficiariosConvenio(idConvenio));
			resultDatosRenovacion = consultas.selectNativeQuery(miConvenio.consultarRenovacion(idConvenio));
			String vigenciaFin = resultDatosRenovacion.get(0).get("fecVigencia").toString();
			SimpleDateFormat formatter = new SimpleDateFormat(PATTERN);
			Date vigencia = formatter.parse(vigenciaFin);
			String fechaHoy = resultDatosRenovacion.get(0).get("fecActual").toString();
			Date fecActual = formatter.parse(fechaHoy);
			Integer difDias = Integer.parseInt(resultDatosRenovacion.get(0).get("diferenciaDias").toString());
			String periodo = formatearFecha(vigenciaFin);
			Integer dia = obtenerDia(vigenciaFin);
			resultDatosRenovacion.get(0).put("fecRenovacion", "Del 01-" + periodo + " Al 20-" + periodo);
			if (dia > 20) {
				resultDatosRenovacion.get(0).put("fecRenovacion", "Del 01-" + periodo + " Al " + vigenciaFin);
			}
			if (difDias < 0) {
				log.info("NO ESTA EN TEMPORADA DE RENOVACION");
				resultDatosRenovacion.get(0).put(PERIODO_RENOVACION, 0);
			} else if (difDias >= 0 && difDias <= 19) {
				resultDatosRenovacion.get(0).put(PERIODO_RENOVACION, 1);
			} else if (difDias > 19 && (vigencia.after(fecActual) || vigencia.equals(fecActual))) {
				resultDatosRenovacion.get(0).put(PERIODO_RENOVACION, 1);
			} else if (fecActual.after(vigencia)) {
				log.info("contrato cerrado {}", idConvenio);
				consultas.actualizarConvenio(idConvenio);
				session.commit();
				resultDatosRenovacion.get(0).put("idEstatus", 4);
				resultDatosRenovacion.get(0).put(PERIODO_RENOVACION, 0);
			}
		} catch (Exception e) {
			log.info(ERROR, e.getCause().getMessage());

			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
					authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO,
					Arrays.asList());
		}
		convenioResponse.setDatosGenerales(resultDatosGenerales);
		convenioResponse.setBeneficiarios(resultDatosBeneficios);
		convenioResponse.setDatosRenovacion(resultDatosRenovacion);
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, convenioResponse);
	}

	private Integer obtenerDia(String vigenciaFin) throws ParseException {
		Date dia = new SimpleDateFormat(PATTERN).parse(vigenciaFin);
		DateFormat format = new SimpleDateFormat("dd");
		return Integer.parseInt(format.format(dia));
	}

	private String formatearFecha(String vigenciaFin) throws ParseException {
		Date mesAnio = new SimpleDateFormat(PATTERN).parse(vigenciaFin);
		DateFormat format = new SimpleDateFormat("MM-yyyy");
		return format.format(mesAnio);
	}

	@Override
	public Response<Object> generarPDF(PdfDto pdfDto, Authentication authentication) throws IOException {
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		Map<String, Object> datosPdf = new HashMap<>();
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			List<Map<String, Object>> resultDatosReporte = new ArrayList<>();
			List<BusquedaInformacionReporteResponse> infoReporte = new ArrayList<>();
			Consultas consultas = sqlSession.getMapper(Consultas.class);
			resultDatosReporte = consultas
					.selectNativeQuery(miConvenio.busquedaFolioParaReporte(pdfDto.getIdConvenio()));
			infoReporte = Arrays.asList(mapper.map(resultDatosReporte, BusquedaInformacionReporteResponse[].class));

			datosPdf.put("rutaNombreReporte", convenioNuevoPlan);
			datosPdf.put("tipoReporte", "pdf");
			datosPdf.put("nombreAfiliado",
					infoReporte.get(0).getNombrePersona() + " " + infoReporte.get(0).getPrimerApellido() + " "
							+ infoReporte.get(0).getSegundoApellido());
			datosPdf.put("numeroINE", infoReporte.get(0).getNumIne());
			datosPdf.put("paqueteContratado", infoReporte.get(0).getNombrePaquete());
			datosPdf.put("serviciosIncluidos", infoReporte.get(0).getDesPaquete());
			datosPdf.put("costoPaquete", infoReporte.get(0).getMonPrecio());
			datosPdf.put("nombreTitular",
					infoReporte.get(0).getNombrePersona() + " " + infoReporte.get(0).getPrimerApellido() + " "
							+ infoReporte.get(0).getSegundoApellido());
			datosPdf.put("rfc", infoReporte.get(0).getRfc());
			datosPdf.put("idConvenio", pdfDto.getIdConvenio());
			datosPdf.put("ciudadExpedicion", pdfDto.getCiudadExpedicion());
			datosPdf.put("fechaExpedicion", pdfDto.getFechaExpedicion());
			datosPdf.put("folioConvenio", infoReporte.get(0).getFolio());

			return providerRestTemplate.consumirServicioReportes(datosPdf, urlReportes,
					authentication);
		} catch (Exception e) {
			log.info(ERROR, e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
					authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO,
					Arrays.asList());
		}

	}

	@Override
	public Response<Object> renovarConvenio(String idConvenio, Authentication authentication) {
		List<Map<String, Object>> detalleConvenio = new ArrayList<>();

		ConvenioEntityMyBatis convenioEntity = new ConvenioEntityMyBatis();
		ConvenioRequest convenio;
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Consultas consultas = session.getMapper(Consultas.class);
			detalleConvenio = consultas.selectNativeQuery(miConvenio.consultarDatosConvenio(idConvenio));
			convenio = new ConvenioRequest(detalleConvenio.get(0));
			convenioEntity.setIdConvenio(Integer.parseInt(idConvenio));
			convenioEntity.setFolio(convenio.getFolio());
			convenioEntity.setCuotaRecuperacion(convenio.getCuotaRecuperacion());
			convenioEntity.setFecVigencia(convenio.getFechaVigencia());
			convenioEntity.setIdVelatorio(convenio.getIdVelatorio());
			convenioEntity.setIdContratante(convenio.getIdContratante());
			convenioEntity.setDatosBancarios(convenio.getDatosBancarios());
			ConvenioMapper convenioMapper = session.getMapper(ConvenioMapper.class);
			try {
				convenioMapper.nuevoRegistroObj(convenioEntity);
				convenio.setIdRegistro(convenioEntity.getIdRegistro());
				if (Boolean.FALSE.equals(convenio.getIndRenovacion())) {
					convenioMapper.actualizarBanderaConvenio(idConvenio);
				} else {
					convenioMapper.actualizarEstatusRenovacion(idConvenio);
				}
				// logUtil.crearArchivoLog(Level.INFO.toString(),
				// this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"NO
				// HAY INFORMACION RELACIONADA A TU BUSQUEDA", CONSULTA);

			} catch (Exception e) {
				session.rollback();

				log.info("error: {}", e.getCause().getMessage());
				return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(),
						AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
			}
			session.commit();

			return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, convenio.getIdRegistro());
		}

	}

	public Response<Object> actualizarBeneficiario(ActualizarBeneficiarioDTO datos, Authentication authentication)
			throws IOException {
		Integer idUsuario = 1;
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			BeneficiariosMapper mapperQuery = session.getMapper(BeneficiariosMapper.class);
			try {
				datos.setIdUsuario(idUsuario);
				if (datos.isActualizaArchivo())
					mapperQuery.actualizarContratanteDocumento(datos);
				mapperQuery.actualizarPersona(datos);

			} catch (Exception e) {
				session.rollback();
				log.info("{}", e.getMessage());
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(),
						AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
						authentication);
				return new Response<>(true, 200, AppConstantes.OCURRIO_ERROR_GENERICO, e.getMessage());
			}

			session.commit();
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, null);

	}

	@Override
	public Response<Object> consultarCurpRfc(@RequestBody JsonNode curpRfc, Authentication authentication)
			throws IOException {
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			String curp = curpRfc.get("curp").asText();

			Consultas consultas = sqlSession.getMapper(Consultas.class);
			resultServiciosCatalogo = consultas.selectNativeQuery(miConvenio.consultarCurpRfc(curp));
			RenapoResponse rp = null;

			if (resultServiciosCatalogo.isEmpty()) {
				if (curp.isEmpty()) {
					return new Response<>(true, 200, "33");

				}
				return consultarCurp(curp);
			}
			return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, resultServiciosCatalogo);
		} catch (Exception e) {
			log.info(ERROR, e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
					authentication);

			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO,
					Arrays.asList());
		}
	}

	public Response<Object> altaBeneficiario(AgregarBeneficiarioDTO datos, Authentication authentication)
			throws IOException {

		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		Integer idUsuario = 1;
		datos.setIdUsuario(idUsuario);
		Boolean validaBeneficiarioAsociado = true;
		ActualizarBeneficiarioDTO actualizarBeneficiarioDTO = new ActualizarBeneficiarioDTO();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			BeneficiariosMapper mapperQuery = session.getMapper(BeneficiariosMapper.class);

			try {

				ObjectMapper objMapper = new ObjectMapper();
				Object personaAsociada;
				String json;
				JsonNode datosJson;

				if ((datos.getIdPersona() == null ? 0 : datos.getIdPersona()) > 0) {
					personaAsociada = mapperQuery.beneficiarioAsociado(datos);
					json = new ObjectMapper().writeValueAsString(personaAsociada);
					datosJson = objMapper.readTree(json);
					Integer validaExistencia = datosJson.get("noPersona").asInt();
					Integer estatusBeneficiario = datosJson.get("estatus").asInt();

					actualizarBeneficiarioDTO.setCorreo(datos.getCorreo());
					actualizarBeneficiarioDTO.setTelefono(datos.getTelefono());
					actualizarBeneficiarioDTO.setIdPersona(datos.getIdPersona());
					if (validaExistencia > 0 && estatusBeneficiario == 1)
						return new Response<>(false, HttpStatus.OK.value(), AppConstantes.BENEFICIARIO_REGISTRADO,
								null);
					else if (validaExistencia == 0 && estatusBeneficiario > 0) {

						validaBeneficiarioAsociado = true;
					} else if (validaExistencia == 0 && estatusBeneficiario == -1) {
						validaBeneficiarioAsociado = true;
					} else {
						validaBeneficiarioAsociado = false;
					}

				}

				if ((datos.getIdPersona() == null ? 0 : datos.getIdPersona()) > 0) {
					// se hace una actualizacion decorreo y telefono de la persona
					log.info("actualizando persona");
					mapperQuery.actualizarPersona(actualizarBeneficiarioDTO);
					log.info("correo y telefono de persona actualizado");

				} else {
					// se inserta el registro
					log.info("insertando persona");
					mapperQuery.insertaPersona(datos);
					log.info("persona agregada");

				}

				if (Boolean.TRUE.equals(validaBeneficiarioAsociado)) {
					// se agregar nuevo registro en contratante beneficiario
					log.info("insertando beneficiario");
					mapperQuery.insertaBeneficiarioContratante(datos);
					log.info("beneficiario agregado");

				} else {

					// se actualzia el registro si estaba inactivo
					log.info("actualizando beneficiario");
					mapperQuery.actualizarContratanteDocumento2(datos);
					log.info("finalizando actualizacion beneficiario");
				}

			} catch (Exception e) {
				session.rollback();
				log.info("{}", e.getMessage());
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(),
						AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
						authentication);
				return new Response<>(true, 200, AppConstantes.OCURRIO_ERROR_GENERICO, e.getMessage());
			}
			session.commit();
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,
				null);

	}

	private Response<Object> consultarCurp(String curp) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			Response<Object> respuestaRenapo = providerRestTemplate
					.consumirServicioExternoGet(urlRenapo + "/" + curp.replace("\"", ""));
			JsonNode jsonRespuesta = objectMapper.readTree(respuestaRenapo.getDatos().toString());
			RenapoResponse rp = null;
			rp = RenapoResponse.builder().curp(jsonRespuesta.get("curp").asText()).rfc("").nss("").idPais("")
					.idEstado("").desEstado(jsonRespuesta.get("desEntidadNac").asText())
					.fechaNacimiento(jsonRespuesta.get("fechNac").asText())
					.sexo(tipoSexo(jsonRespuesta.get("sexo").asText())).nomPersona(jsonRespuesta.get("nombre").asText())
					.primerApellido(jsonRespuesta.get("apellido1").asText())
					.segundoApellido(jsonRespuesta.get("apellido2").asText()).correo(jsonRespuesta.get("").asText())
					.build();
			return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, Arrays.asList(rp));
		} catch (Exception e) {
			return new Response<>(true, 200, AppConstantes.ERROR_CONSULTAR_RENAPO, e.getMessage());

		}
	}

	private String tipoSexo(String tipo) {
		String sexo = "";
		if (Objects.nonNull(tipo)) {
			sexo = String.valueOf(sexo.equals("HOMBRE") ? '2' : '1');
		} else {
			sexo = tipo;
		}
		return sexo;
	}

	public Response<Object> desactivarBeneficiario(ActualizarBeneficiarioDTO datos, Authentication authentication)
			throws IOException {
		Integer idUsuario = 1;
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			BeneficiariosMapper mapperQuery = session.getMapper(BeneficiariosMapper.class);
			try {

				datos.setIdUsuario(idUsuario);
				log.info("desactivando beneficiario");
				mapperQuery.desactivarBeneficiario(datos);
				log.info("beneficiario desactivado");
			} catch (Exception e) {
				session.rollback();
				log.info("{}", e.getMessage());
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(),
						AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
						authentication);
				return new Response<>(true, 200, AppConstantes.OCURRIO_ERROR_GENERICO, e.getMessage());
			}

			session.commit();
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, null);

	}

	public Response<Object> consultaGeneralConvenio(Integer idVelatorio, Authentication authentication)
			throws IOException {
		Integer idContratante = 111;
		Map<String, Object> datosGenerales = new HashMap<>();
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			BeneficiariosMapper mapperQuery = session.getMapper(BeneficiariosMapper.class);
			try {

				AgregarBeneficiarioDTO contratante = new AgregarBeneficiarioDTO();
				contratante.setIdContratante(idContratante);
				contratante.setIdVelatorio(idVelatorio);
				log.info("buscando datos personales contratante");
				datosGenerales = mapperQuery.datosPersonalesContratante(contratante);
				log.info("finalizando busqueda de datos personales contratante");

			} catch (Exception e) {
				session.rollback();
				log.info("{}", e.getMessage());
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(),
						AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
						authentication);
				return new Response<>(true, 200, AppConstantes.OCURRIO_ERROR_GENERICO, e.getMessage());
			}

		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, datosGenerales);

	}

	public Response<Object> altaPlanPFPersona(AgregarConvenioPersonaDTO datos, Authentication authentication)
			throws IOException {

		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		Integer idUsuario = 1;
		datos.setIdUsuario(idUsuario);
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ConvenioPFMapper convenio = session.getMapper(ConvenioPFMapper.class);

			try {
				log.info("agregando convenio por persona");
				convenio.agregarConvenioPF(datos);
				log.info("finalizando convenio por persona");
				log.info("agregando domicolio por persona");
				convenio.agregarDomicilio(datos);
				log.info("finalizando domiclio por persona");
				log.info("agregando contratante por persona");
				convenio.agregarContratante(datos);
				log.info("finalizando contratante por persona");
				log.info("agregando convenio paquete  por persona");
				convenio.agregarContratoConvenioPaquete(datos);
				log.info("finalizando convenio paquete por persona");
				log.info("agregando documentacion convenio por persona");
				convenio.agregaDocumentacion(datos);
				log.info("finalizando documentacion convenio por persona");
				ObjectMapper objMapper = new ObjectMapper();
				Object datosConsulta;
				String json;
				JsonNode datosJson;

				datosConsulta = convenio.folioConvenio(datos);
				json = new ObjectMapper().writeValueAsString(datosConsulta);
				datosJson = objMapper.readTree(json);
				String folio = datosJson.get("folio").asText();
				datos.setFolio(folio);

			} catch (Exception e) {
				session.rollback();
				log.info("{}", e.getMessage());
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(),
						AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
						authentication);
				return new Response<>(true, 200, AppConstantes.OCURRIO_ERROR_GENERICO, e.getMessage());
			}
			session.commit();
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,
				datos);

	}

	public Response<Object> altaPlanPFEmpresa(AgregarConvenioEmpresaDTO datos, Authentication authentication)
			throws IOException {
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		Integer idUsuario = 1;
		datos.setIdUsuario(idUsuario);
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ConvenioPFMapperEmpresa convenio = session.getMapper(ConvenioPFMapperEmpresa.class);

			try {
				log.info("agregando convenio por empresa");
				convenio.agregarConvenioPFEmpresa(datos);
				log.info("finaliza insercion por  empresa");
				log.info("agregando domicilio por empresa");
				convenio.agregarDomicilio(datos);
				log.info("finaliza domicilio por empresa");
				log.info("agregando datos por empresa");
				convenio.agregarEmpresaConvenioPF(datos);
				log.info("fianlizando datos por empresa");

				ObjectMapper objMapper = new ObjectMapper();
				Object datosConsulta;
				String json;
				JsonNode datosJson;

				datosConsulta = convenio.folioConvenio(datos);
				json = new ObjectMapper().writeValueAsString(datosConsulta);
				datosJson = objMapper.readTree(json);
				String folio = datosJson.get("folio").asText();
				datos.setFolio(folio);

			} catch (Exception e) {
				session.rollback();
				log.info("{}", e.getMessage());
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(),
						AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
						authentication);
				return new Response<>(true, 200, AppConstantes.OCURRIO_ERROR_GENERICO, e.getMessage());
			}
			// session.commit();
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,
				datos);
	}

	@Override
	public Response<Object> consultaPlanPFEmpresa(Integer idConvenio, Authentication authentication)
			throws IOException {
		List<Map<String, Object>> resultDatosEmpresa = new ArrayList<>();
		List<Map<String, Object>> resultDatosPersonaEmpresa = new ArrayList<>();
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		ConvenioEmpresaResponse convenioEmpresaResponse= new ConvenioEmpresaResponse();
		List<DatosEmpresaResponse> datosEmpresaResponse= new ArrayList<>();
		try (SqlSession session = sqlSessionFactory.openSession()) {
		
			try {

				Consultas consultas = session.getMapper(Consultas.class);
				resultDatosEmpresa = consultas.selectNativeQuery(miConvenio.consultarDatosConvenioEmpresa(idConvenio));
				resultDatosPersonaEmpresa = consultas.selectNativeQuery(miConvenio.consultarDatosConvenioEmpresaPersona(idConvenio));
				datosEmpresaResponse=Arrays.asList(mapper.map(resultDatosEmpresa, DatosEmpresaResponse[].class));
				datosEmpresaResponse.stream().forEach(r->{
					 empresaResponse= DatosEmpresaResponse.builder()
								.idConvenio(r.getIdConvenio())
								.idEmpresa(r.getIdEmpresa())
								.nombre(r.getNombre())
								.razonSocial(r.getRazonSocial())
								.rfc(r.getRfc())
								.idPais(r.getIdPais())
								.cp(r.getCp())
								.calle(r.getCalle())
								.colonia(r.getColonia())
								.municipio(r.getMunicipio())
								.estado(r.getEstado())
								.numInterior(r.getNumInterior())
								.numExterior(r.getNumExterior())
								.telefono(r.getTelefono())
								.correo(r.getCorreo())
								.build();
				});
				convenioEmpresaResponse.setDatosEmpresaResponse(empresaResponse);
				convenioEmpresaResponse.setPersonasEmpresa(resultDatosPersonaEmpresa);

				return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, convenioEmpresaResponse);
				
			} catch (Exception e) {
				session.rollback();
				log.info("{}", e.getMessage());
				logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(),
						AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
						authentication);
				return new Response<>(true, 200, AppConstantes.OCURRIO_ERROR_GENERICO, e.getMessage());
			}

		}
	
	}

}
