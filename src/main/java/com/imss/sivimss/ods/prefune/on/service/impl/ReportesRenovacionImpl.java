package com.imss.sivimss.ods.prefune.on.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.imss.sivimss.ods.prefune.on.configuration.MyBatisConfig;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.ConvenioMapper;
import com.imss.sivimss.ods.prefune.on.model.request.PdfDto;
import com.imss.sivimss.ods.prefune.on.service.ReportesRenovacionService;
import com.imss.sivimss.ods.prefune.on.service.beans.ConsultaRenovacionConvenio;
import com.imss.sivimss.ods.prefune.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.ProviderServiceRestTemplate;
import com.imss.sivimss.ods.prefune.on.utils.Response;

@Service
public class ReportesRenovacionImpl implements ReportesRenovacionService {

	
	@Autowired
	private LogUtil logUtil;
	
	@Autowired
	private MyBatisConfig myBatisConfig;
	
	@Autowired
	private ConsultaRenovacionConvenio renConvenio;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Value("${reporte.ren-convenio-pf-nuevo}")
	private String renovacionPlanNuevo;
	
	@Value("${reporte.ren-convenio-pf-anterior}")
	private String renovacionPlanAnterior;
	
	@Value("${reporte.hoja-afiliacion}")
	private String hojaAfiliacion;
	
	private static final Logger log = LoggerFactory.getLogger(ConvenioPfServiceImpl.class);
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
    private ProviderServiceRestTemplate providerRestTemplate;
	
	private static final String CONSULTA = "consulta";
	private static final String INSERT = "insert";
	private static final String UPDATE = "update";
	private static final String FIRMA_FIDEICOMISO = "firmaFideicomiso";
	private static final String RUTA = "rutaNombreReporte";
	private static final String TIPO_REPORTE = "rutaNombreReporte";
	private static final String NOMBRE_FIBESO = "pdf";
	private static final String IMG_FIRMA = "imgFirmaDigital";
	private static final String ERROR = "Error, {}";
	
	@Override
	public Response<Object> generarDoc(PdfDto pdfDto, Authentication authentication) throws Throwable {
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory(); 
		Map<String, Object> datosPdf = new HashMap<>();
		try (SqlSession sqlSession=sqlSessionFactory.openSession()){
		List<Map<String, Object>> resultFolio = new ArrayList<>();
		//	List<BusquedaInformacionReporteResponse>infoReporte= new ArrayList<>();
			ConvenioMapper consultas= sqlSession.getMapper(ConvenioMapper.class);
		  String query = renConvenio.obtenerFolio(pdfDto.getIdConvenio()); 
		  resultFolio=consultas.selectNativeQuery(query);
		
            datosPdf.put(RUTA, renovacionPlanNuevo);
            datosPdf.put(TIPO_REPORTE, "pdf");
            datosPdf.put("folio", resultFolio.get(0).get("DES_FOLIO").toString());
            datosPdf.put("planPF", "Prevision Funeraria Plan Nuevo");
            datosPdf.put("directoraFideicomiso", resultFolio.get(0).get(NOMBRE_FIBESO).toString());
            datosPdf.put(IMG_FIRMA, resultFolio.get(0).get(FIRMA_FIDEICOMISO).toString());
			
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

	@Override
	public Response<?> generarHojaAfilicion(PdfDto pdfDto, Authentication authentication) throws Throwable {
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory(); 
		Map<String, Object> datosPdf = new HashMap<>();
		try (SqlSession sqlSession=sqlSessionFactory.openSession()) {
			List<Map<String, Object>> resultFolio = new ArrayList<>();
			//	List<BusquedaInformacionReporteResponse>infoReporte= new ArrayList<>();
				ConvenioMapper consultas= sqlSession.getMapper(ConvenioMapper.class);
			  String query = renConvenio.obtenerFolio(pdfDto.getIdConvenio()); 
			  resultFolio=consultas.selectNativeQuery(query);
	            datosPdf.put(RUTA, hojaAfiliacion);
	            datosPdf.put(TIPO_REPORTE, "pdf");
	            datosPdf.put("idConvenio", Integer.parseInt(pdfDto.getIdConvenio()));
	            datosPdf.put("tipoConvenio", "Previsión Funeraria Plan Anterior");
	            datosPdf.put(NOMBRE_FIBESO, resultFolio.get(0).get(NOMBRE_FIBESO).toString());
	            datosPdf.put(IMG_FIRMA, resultFolio.get(0).get(FIRMA_FIDEICOMISO).toString());
				
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

	@SuppressWarnings("unchecked")
	@Override
	public Response<?> generarDocPlanAnterior(PdfDto pdfDto, Authentication authentication) throws IOException {
		Double costo;
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory(); 
		Map<String, Object> datosPdf = new HashMap<>();
		List<Map<String, Object>> mapping;
		try (SqlSession sqlSession=sqlSessionFactory.openSession()){
		List<Map<String, Object>> resultCuota = new ArrayList<>();
			ConvenioMapper consultas= sqlSession.getMapper(ConvenioMapper.class);
		  String query = renConvenio.obtenerCostoRecuperacion(pdfDto.getIdConvenio()); 
		  resultCuota=consultas.selectNativeQuery(query);
			mapping = Arrays.asList(mapper.map(resultCuota, Map[].class));
		  costo = Double.parseDouble(mapping.get(0).get("costoRecuperacion").toString());
		                                       
		  RuleBasedNumberFormat rule = new RuleBasedNumberFormat(new Locale("es-ES"), RuleBasedNumberFormat.SPELLOUT);
			String costoLetra = rule.format(costo);
            datosPdf.put(RUTA, renovacionPlanAnterior);
            datosPdf.put(TIPO_REPORTE, "pdf");
            datosPdf.put("idConvenio", Integer.parseInt(pdfDto.getIdConvenio()));
            datosPdf.put("costoConvenio", costo);
            datosPdf.put("version", "1.0.0");
            datosPdf.put("letraCosto", costoLetra.toUpperCase() +" PESOS 00/100 M/N");
            datosPdf.put("nomFibeso", resultCuota.get(0).get(NOMBRE_FIBESO).toString());
            datosPdf.put(IMG_FIRMA, resultCuota.get(0).get(FIRMA_FIDEICOMISO).toString());
            datosPdf.put("selloRenovacion", resultCuota.get(0).get("selloRenovacion").toString());
			
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
