package com.imss.sivimss.ods.prefune.on.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.configuration.MyBatisConfig;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.service.ConvenioPfService;
import com.imss.sivimss.ods.prefune.on.service.beans.ConsultaMiConvenio;
import com.imss.sivimss.ods.prefune.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.PaginadoUtil;
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

	@Override
	public Response<Object> consultaMiConvenio(Paginado paginado,Integer idContratante) {
		Page<Map<String, Object>> result = paginadoUtil.paginado(paginado.getPagina(), paginado.getTamanio(),miConvenio.consultaMiConvenio(idContratante));
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, result);
	}

	@Override
	public Response<Object> consultaDetalleConvenio(Integer idConvenio) {
		List<Object>detalleConvenio= new ArrayList<>();
		List<Map<String, Object>> resultDatosGenerales = new ArrayList<>();
		List<Map<String, Object>> resultDatosBeneficios = new ArrayList<>();
		
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		
		try(SqlSession session = sqlSessionFactory.openSession()) {
			Consultas consultas = session.getMapper(Consultas.class);
			resultDatosGenerales = consultas.selectNativeQuery(miConvenio.consultarDatosGeneales(idConvenio));
			resultDatosBeneficios=consultas.selectNativeQuery(miConvenio.consultarBeneficiariosConvenio(idConvenio));
		
		}catch (Exception e) {
			log.info("error: {}",e.getCause().getMessage());
			return new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.OCURRIO_ERROR_GENERICO, Arrays.asList());
		}
		detalleConvenio.add(resultDatosGenerales);
		detalleConvenio.add(resultDatosBeneficios);
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, detalleConvenio);
	}

}
