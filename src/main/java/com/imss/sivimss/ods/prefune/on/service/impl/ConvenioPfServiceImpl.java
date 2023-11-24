package com.imss.sivimss.ods.prefune.on.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.configuration.MyBatisConfig;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefune.on.service.ConvenioPfService;
import com.imss.sivimss.ods.prefune.on.service.beans.ConsultaMiConvenio;
import com.imss.sivimss.ods.prefune.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.Response;

@Service
public class ConvenioPfServiceImpl implements ConvenioPfService{

	@Autowired
	private LogUtil logUtil;
	
	@Autowired
	private MyBatisConfig myBatisConfig;
	
	@Autowired
	private ConsultaMiConvenio miConvenio;
	
	private static final Logger log = LoggerFactory.getLogger(ConvenioPfServiceImpl.class);

	@Override
	public Response<Object> consultaMiConvenio(Integer idContratante) {
		List<Map<String, Object>> result = new ArrayList<>();
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		
		try(SqlSession session = sqlSessionFactory.openSession()) {
			Consultas consultas = session.getMapper(Consultas.class);
			result = consultas.selectNativeQuery(miConvenio.consultaMiConvenio(idContratante));
		}
		
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, result);
	}

}
