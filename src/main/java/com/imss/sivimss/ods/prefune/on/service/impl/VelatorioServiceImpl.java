package com.imss.sivimss.ods.prefune.on.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.configuration.MyBatisConfig;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefune.on.model.response.MapaVelatoriosResponse;
import com.imss.sivimss.ods.prefune.on.service.CatalogosService;
import com.imss.sivimss.ods.prefune.on.service.VelatorioService;
import com.imss.sivimss.ods.prefune.on.service.beans.Catalogos;
import com.imss.sivimss.ods.prefune.on.service.beans.ConsultaMiConvenio;
import com.imss.sivimss.ods.prefune.on.service.beans.MapaVelatorio;
import com.imss.sivimss.ods.prefune.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.Response;

@Service
public class VelatorioServiceImpl implements VelatorioService, CatalogosService{

	@Autowired
	private MapaVelatorio mapaVelatorio;
	
	@Autowired
	private Catalogos catalogos;
	
	@Autowired
	private LogUtil logUtil;
	
	@Autowired
	private MyBatisConfig myBatisConfig;
		
	private static final Logger log = LoggerFactory.getLogger(VelatorioServiceImpl.class);
	
	@Autowired
	private ModelMapper mapper;
	
	private static final String ERROR = "error: {}";
	
	private List<Map<String, Object>> resultServiciosVelatorios = new ArrayList<>();
	
	private int i;
	
	@Override
	public Response<Object> consultarServiciosVelatorios(Authentication authentication) throws IOException {
		List<MapaVelatoriosResponse>velatoriosResponses= new ArrayList<>();	
		List<Map<String, Object>> resultVelatorios = new ArrayList<>();
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		
		try(SqlSession sqlSession= sqlSessionFactory.openSession()) {
			Consultas consultas= sqlSession.getMapper(Consultas.class);
			resultVelatorios=consultas.selectNativeQuery(mapaVelatorio.consultarVelatorios());
			velatoriosResponses=Arrays.asList(mapper.map(resultVelatorios, MapaVelatoriosResponse[].class));
			velatoriosResponses.stream().forEach(registro->{
				resultServiciosVelatorios=consultas.selectNativeQuery(mapaVelatorio.consultarServiciosVelatorios(registro.getIdVelatorio()));
				
				Object [] arreglo= new Object[resultServiciosVelatorios.size()];
				i=0;
				resultServiciosVelatorios.forEach(r->{
					arreglo[i]= r.get("servicio");
					i++;
					registro.setServicios(arreglo);
				});
				
				
			});
			return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, velatoriosResponses);
		} catch (Exception e) {
			log.info(ERROR,e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
	}

	/**
	 * consulta catalogo de parentesco
	 */
	@Override
	public Response<Object> consultarCatalogo(Authentication authentication) throws IOException {
		List<Map<String, Object>> resultParentesco = new ArrayList<>();
		SqlSessionFactory sqlSessionFactory= myBatisConfig.buildqlSessionFactory();
		try(SqlSession sqlSession= sqlSessionFactory.openSession()) {
			Consultas consultas= sqlSession.getMapper(Consultas.class);
			resultServiciosVelatorios=consultas.selectNativeQuery(catalogos.consultarParentesco());
			
			return new Response<>(true, HttpStatus.OK.value(), AppConstantes.EXITO, resultServiciosVelatorios);
		} catch (Exception e) {
			log.info(ERROR,e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
	}

}
