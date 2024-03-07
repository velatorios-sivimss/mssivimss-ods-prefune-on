package com.imss.sivimss.ods.prefune.on.configuration.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.imss.sivimss.ods.prefune.on.configuration.mapper.Consultas.PureSqlProvider;
import com.imss.sivimss.ods.prefune.on.model.entity.ConvenioEntityMyBatis;

public interface ConvenioMapper {
	
	@SelectProvider(type = PureSqlProvider.class, method = "sql")
	public List<Map<String, Object>> selectNativeQuery(String sql);
	
	@Insert(value = "INSERT INTO SVT_RENOVACION_CONVENIO_PF(ID_CONVENIO_PF, REF_FOLIO_ADENDA, FEC_INICIO, FEC_VIGENCIA, REF_DATOS_BANCARIOS, ID_ESTATUS, FEC_ALTA, ID_USUARIO_ALTA) "
			+ "VALUES ( #{out.idConvenio}, CONCAT(#{out.folio}, '-',(SELECT LPAD(COUNT(*)+1,2,'0') FROM SVT_RENOVACION_CONVENIO_PF REN WHERE REN.ID_CONVENIO_PF = #{out.idConvenio})),#{out.fecVigencia}, DATE_ADD(#{out.fecVigencia}, INTERVAL 365 DAY), #{out.datosBancarios}, 1, CURRENT_TIMESTAMP(), 1 )")
	@Options(useGeneratedKeys = true,keyProperty = "out.idRegistro", keyColumn="id")
	public int nuevoRegistroObj(@Param("out")ConvenioEntityMyBatis convenio);
	
	@Update(value = ""
			+ "UPDATE SVT_CONVENIO_PF  "
			+ "SET  "
			+ "	IND_RENOVACION= 1,"
			+ " FEC_ACTUALIZACION = CURRENT_TIMESTAMP() "
			+ "WHERE ID_CONVENIO_PF=#{in}")
	public int actualizarBanderaConvenio(@Param("in")String idConvenio);
	
	@Update(value = ""
			+ "UPDATE SVT_RENOVACION_CONVENIO_PF  "
			+ "SET  "
			+ "	ID_ESTATUS = 3,"
			+ " FEC_ACTUALIZACION = CURRENT_TIMESTAMP() "
			+ "WHERE ID_CONVENIO_PF=#{in} AND ID_ESTATUS=2")
	public int actualizarEstatusRenovacion(@Param("in")String idConvenio);

}
