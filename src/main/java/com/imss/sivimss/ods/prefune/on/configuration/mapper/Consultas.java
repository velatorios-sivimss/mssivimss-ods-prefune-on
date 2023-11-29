package com.imss.sivimss.ods.prefune.on.configuration.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


@Repository
public interface Consultas {
	static class PureSqlProvider{
        public String sql(String sql) {
            return sql;
        }
 
        public String count(String from) {
            return "SELECT count(*) FROM " + from;
        }
    }
	@SelectProvider(type = PureSqlProvider.class, method = "sql")
	public List<Map<String, Object>> selectNativeQuery(String sql);
	
	@Update(value = ""
			+ "UPDATE SVT_CONVENIO_PF  "
			+ "SET  "
			+ "	ID_ESTATUS_CONVENIO= 4 "
			+ "WHERE ID_CONVENIO_PF=#{id}")
	public void actualizarConvenio(@Param("id")Integer id);
		
}
