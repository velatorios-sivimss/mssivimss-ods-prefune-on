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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.configuration.MyBatisConfig;
import com.imss.sivimss.ods.prefune.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.model.response.DetalleHistorialServicios;
import com.imss.sivimss.ods.prefune.on.model.response.DetalleResponse;
import com.imss.sivimss.ods.prefune.on.service.HistorialOrdenService;
import com.imss.sivimss.ods.prefune.on.service.beans.HistorialOrdenes;
import com.imss.sivimss.ods.prefune.on.utils.AppConstantes;
import com.imss.sivimss.ods.prefune.on.utils.LogUtil;
import com.imss.sivimss.ods.prefune.on.utils.PaginadoUtil;
import com.imss.sivimss.ods.prefune.on.utils.Response;

@Service
public class HistorialOrdenServiceImpl implements HistorialOrdenService {

	@Autowired
	private LogUtil logUtil;

	@Autowired
	private HistorialOrdenes historialOrdenes;

	@Autowired
	private MyBatisConfig myBatisConfig;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PaginadoUtil paginadoUtil;

	private static final Logger log = LoggerFactory.getLogger(HistorialOrdenServiceImpl.class);

	private static final String ERROR = "error: {}";

	@Override
	public Response<Object> consultarOrdenes(Paginado paginado, Integer idContratante, Authentication authentication)
			throws IOException {
		Page<Map<String, Object>> result = null;
		try {
			result = paginadoUtil.paginado(paginado.getPagina(), paginado.getTamanio(),
					historialOrdenes.consultarOrdenes(idContratante));
			if (result.isEmpty()) {
				return new Response<>(false, HttpStatus.OK.value(), AppConstantes.HISTORIAL_ORDENES_MENSAJE,
						Arrays.asList());
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

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, result);
	}

	@Override
	public Response<Object> consultarVerDetalleOrdenes(Integer idOrden, Authentication authentication)
			throws IOException {
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		List<Map<String, Object>> consultaVerDetalle = new ArrayList<>();
		List<Map<String, Object>> consultaHistorialDetalle = new ArrayList<>();
		List<DetalleResponse> detalleResponse = new ArrayList<>();
		List<DetalleHistorialServicios> detalleHistorialServicios = new ArrayList<>();
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			Consultas consultas = sqlSession.getMapper(Consultas.class);

			consultaVerDetalle = consultas.selectNativeQuery(historialOrdenes.consultarVerDetalle(idOrden));
			consultaHistorialDetalle = consultas
					.selectNativeQuery(historialOrdenes.consultarVerDetalleHistorialServicio(idOrden));

			detalleResponse = Arrays.asList(mapper.map(consultaVerDetalle, DetalleResponse[].class));
			detalleHistorialServicios = Arrays
					.asList(mapper.map(consultaHistorialDetalle, DetalleHistorialServicios[].class));

			if (!detalleResponse.isEmpty()) {
				detalleResponse.get(0).setHistorialDetalle(detalleHistorialServicios);
			}

			return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, detalleResponse);

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

}
