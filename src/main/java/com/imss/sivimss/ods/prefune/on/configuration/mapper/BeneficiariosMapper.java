package com.imss.sivimss.ods.prefune.on.configuration.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.imss.sivimss.ods.prefune.on.model.request.ActualizarBeneficiarioDTO;

public interface BeneficiariosMapper {

	@Update(value = ""
			+ "UPDATE svc_persona  "
			+ "SET  "
			+ "	REF_TELEFONO = #{out.correo}, "
			+ "	REF_CORREO = #{out.telefono} "
			+ "WHERE ID_PERSONA = #{out.idPersona}")
	public int actualizarPersona(@Param("out") ActualizarBeneficiarioDTO persona);

}
