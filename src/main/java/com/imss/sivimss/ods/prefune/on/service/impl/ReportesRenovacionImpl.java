package com.imss.sivimss.ods.prefune.on.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.configuration.MyBatisConfig;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.ConvenioMapper;
import com.imss.sivimss.ods.prefune.on.model.request.PdfDto;
import com.imss.sivimss.ods.prefune.on.model.response.BusquedaInformacionReporteResponse;
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
	
	@Override
	public Response<Object> generarDoc(PdfDto pdfDto, Authentication authentication) throws Throwable {
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory(); 
		Map<String, Object> datosPdf = new HashMap<>();
		try (SqlSession sqlSession=sqlSessionFactory.openSession()){
		List<Map<String, Object>> resultDatosReporte = new ArrayList<>();
		//	List<BusquedaInformacionReporteResponse>infoReporte= new ArrayList<>();
			ConvenioMapper consultas= sqlSession.getMapper(ConvenioMapper.class);
		  String query = renConvenio.obtenerFolio(pdfDto.getIdConvenio()); 
		  resultDatosReporte=consultas.selectNativeQuery(query);
		
            datosPdf.put("rutaNombreReporte", renovacionPlanNuevo);
            datosPdf.put("tipoReporte", "pdf");
         datosPdf.put("folio", resultDatosReporte.get(0).get("DES_FOLIO").toString());
            datosPdf.put("planPF", "Prevision Funeraria Plan Nuevo");
            datosPdf.put("directoraFideicomiso", "Dra. Cristinne Leo Martel");
			
			return providerRestTemplate.consumirServicioReportes(datosPdf, urlReportes,
	                authentication);
		} catch (Exception e) {
			log.info("ERROR {}",e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
		
	}

}
