package com.imss.sivimss.ods.prefune.on.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.imss.sivimss.ods.prefune.on.configuration.MyBatisConfig;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.model.request.PdfDto;
import com.imss.sivimss.ods.prefune.on.model.response.BusquedaInformacionReporteResponse;
import com.imss.sivimss.ods.prefune.on.model.response.MiConvenioResponse;
import com.imss.sivimss.ods.prefune.on.service.ConvenioPfService;
import com.imss.sivimss.ods.prefune.on.service.beans.ConsultaMiConvenio;
import com.imss.sivimss.ods.prefune.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.PaginadoUtil;
import com.imss.sivimss.ods.prefune.on.utils.ProviderServiceRestTemplate;
import com.imss.sivimss.ods.prefune.on.utils.Response;


@Service
public class ConvenioPfServiceImpl implements ConvenioPfService{

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
	
	private final String ERROR = "error: {}";

	@Override
	public Response<Object> consultaMiConvenio(Paginado paginado,Integer idContratante, Authentication authentication) throws IOException {
		Page<Map<String, Object>> result=null; 
		try {
			result = paginadoUtil.paginado(paginado.getPagina(), paginado.getTamanio(),miConvenio.consultaMiConvenio(idContratante));
		} catch (Exception e) {
			
			log.info(ERROR,e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
		
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, result);
	}

	@Override
	public Response<Object> consultaDetalleConvenio(Integer idConvenio, Authentication authentication) throws IOException {
		List<Map<String, Object>> resultDatosGenerales = new ArrayList<>();
		List<Map<String, Object>> resultDatosBeneficios = new ArrayList<>();
		List<Map<String, Object>> resultDatosRenovacion = new ArrayList<>();
		MiConvenioResponse convenioResponse= new MiConvenioResponse();
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		
		try(SqlSession session = sqlSessionFactory.openSession()) {
			Consultas consultas = session.getMapper(Consultas.class);
			resultDatosGenerales = consultas.selectNativeQuery(miConvenio.consultarDatosGeneales(idConvenio));
			resultDatosBeneficios=consultas.selectNativeQuery(miConvenio.consultarBeneficiariosConvenio(idConvenio));
		    resultDatosRenovacion = consultas.selectNativeQuery(miConvenio.consultarRenovacion(idConvenio));
		   String vigenciaFin = resultDatosRenovacion.get(0).get("fecVigencia").toString();
		   SimpleDateFormat formatter =  new SimpleDateFormat("dd-MM-yyyy");
		    Date vigencia = formatter.parse(vigenciaFin);
		    String fechaHoy = resultDatosRenovacion.get(0).get("fecActual").toString();
		    Date fecActual = formatter.parse(fechaHoy);
		    Integer difDias = Integer.parseInt(resultDatosRenovacion.get(0).get("diferenciaDias").toString());
		    String periodoRenovacion = "periodoRenovacion";
			if(difDias<0) {
		    	 log.info("NO ESTA EN TEMPORADA DE RENOVACION");
				   resultDatosRenovacion.get(0).put(periodoRenovacion, 0);
		    }else if(difDias>=0 && difDias<=19) {
			   resultDatosRenovacion.get(0).put(periodoRenovacion, 1);
		   }else if(difDias>19 && (vigencia.after(fecActual) || vigencia.equals(fecActual))) {
			   resultDatosRenovacion.get(0).put(periodoRenovacion, 1);
		   }else if(fecActual.after(vigencia)) {
			   log.info("contrato cerrado {}",idConvenio);
			   consultas.actualizarConvenio(idConvenio);
			   session.commit();
			   resultDatosRenovacion.get(0).put("idEstatus", 4);
			   resultDatosRenovacion.get(0).put(periodoRenovacion, 0);
		   }
		}catch (Exception e) {
			log.info(ERROR,e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
		convenioResponse.setDatosGenerales(resultDatosGenerales);
        convenioResponse.setBeneficiarios(resultDatosBeneficios);
        convenioResponse.setDatosRenovacion(resultDatosRenovacion);
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, convenioResponse);
	}

	@Override
	public Response<Object> generarPDF(PdfDto pdfDto, Authentication authentication) throws IOException {
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory(); 
		Map<String, Object> datosPdf = new HashMap<>();
		try (SqlSession sqlSession=sqlSessionFactory.openSession()){
			List<Map<String, Object>> resultDatosReporte = new ArrayList<>();
			List<BusquedaInformacionReporteResponse>infoReporte= new ArrayList<>();
			Consultas consultas= sqlSession.getMapper(Consultas.class);
			resultDatosReporte=consultas.selectNativeQuery(miConvenio.busquedaFolioParaReporte(pdfDto.getIdConvenio()));
			infoReporte=Arrays.asList(mapper.map(resultDatosReporte, BusquedaInformacionReporteResponse[].class));
			
			
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
			log.info(ERROR,e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
		
		
	}

}
