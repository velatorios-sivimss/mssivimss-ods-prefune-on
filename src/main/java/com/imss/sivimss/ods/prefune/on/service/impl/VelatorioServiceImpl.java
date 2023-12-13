package com.imss.sivimss.ods.prefune.on.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.imss.sivimss.ods.prefune.on.model.response.MapaVelatoriosResponse;
import com.imss.sivimss.ods.prefune.on.model.response.PaqueteResponse;
import com.imss.sivimss.ods.prefune.on.service.CatalogosService;
import com.imss.sivimss.ods.prefune.on.service.VelatorioService;
import com.imss.sivimss.ods.prefune.on.service.beans.Catalogos;
import com.imss.sivimss.ods.prefune.on.service.beans.MapaVelatorio;
import com.imss.sivimss.ods.prefune.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.MensajeResponseUtil;
import com.imss.sivimss.ods.prefune.on.utils.ProviderServiceRestTemplate;
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
	
	private List<Map<String, Object>> resultServiciosCatalogo = new ArrayList<>();
	
	private int i;
	
	private static final String CODIGO_POSTAL_NO_EXISTE = "185"; // El codigo postal no existe.
	
	private static final String SERVICIO_SEPOMEX_NO_DISPONIBLE = "181"; // El servicio de SEPOMEX no esta disponible.
	
	@Value("${endpoints.sepomex}")
	private String urlSepomex;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
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
	public Response<Object> consultarCatalogoParentesco(Authentication authentication) throws IOException {
		
		SqlSessionFactory sqlSessionFactory= myBatisConfig.buildqlSessionFactory();
		try(SqlSession sqlSession= sqlSessionFactory.openSession()) {
			Consultas consultas= sqlSession.getMapper(Consultas.class);
			resultServiciosCatalogo=consultas.selectNativeQuery(catalogos.consultarParentesco());
			
			return new Response<>(true, HttpStatus.OK.value(), AppConstantes.EXITO, resultServiciosCatalogo);
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
	public Response<Object> consultarCatalogoPaquete(Integer idVelatorio,Authentication authentication) throws IOException {
		
		List<PaqueteResponse>paqueteResponses= new ArrayList<>();	
		List<Map<String, Object>> resultPaquetes = new ArrayList<>();
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		
		try(SqlSession sqlSession= sqlSessionFactory.openSession()) {
			Consultas consultas= sqlSession.getMapper(Consultas.class);
			resultPaquetes=consultas.selectNativeQuery(catalogos.obtenerPaquetes(idVelatorio));
			paqueteResponses=Arrays.asList(mapper.map(resultPaquetes, PaqueteResponse[].class));
			paqueteResponses.stream().forEach(registro->{
				resultServiciosVelatorios=consultas.selectNativeQuery(catalogos.obtenerCaracteristicasPaquete(registro.getIdPaquete()));
				
				Object [] arreglo= new Object[resultServiciosVelatorios.size()];
				i=0;
				resultServiciosVelatorios.forEach(r->{
					arreglo[i]= r.get("concepto");
					i++;
					registro.setServiciosPaquetes(arreglo);
				});
				
				
			});
			return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, paqueteResponses);
		} catch (Exception e) {
			log.info(ERROR,e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
	}

	@Override
	public Response<Object> consultarCatalogoPromotores(Authentication authentication) throws IOException {
		SqlSessionFactory sessionFactory=myBatisConfig.buildqlSessionFactory();
		try(SqlSession session=sessionFactory.openSession()) {
			Consultas consultas= session.getMapper(Consultas.class);
			resultServiciosCatalogo=consultas.selectNativeQuery(catalogos.consultarPromotores());
			return new Response<>(false,HttpStatus.OK.value(),AppConstantes.EXITO,resultServiciosCatalogo);
		} catch (Exception e) {
			log.info(ERROR,e.getCause().getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
	}

	@Override
	public Response<Object> consultarCodigoPostal(String codigoPostal, Authentication authentication)
			throws IOException {
		try {
			Response<Object>response=providerRestTemplate.consumirServicioExternoGet(urlSepomex+"/"+codigoPostal);
		    return MensajeResponseUtil.mensajeResponseExterno(response, CODIGO_POSTAL_NO_EXISTE, SERVICIO_SEPOMEX_NO_DISPONIBLE	);
		} catch (Exception e) {
			return new Response<>(true, 200,CODIGO_POSTAL_NO_EXISTE);
		}
		
	}


}
