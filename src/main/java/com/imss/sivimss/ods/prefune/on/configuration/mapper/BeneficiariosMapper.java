package com.imss.sivimss.ods.prefune.on.configuration.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.imss.sivimss.ods.prefune.on.model.request.ActualizarBeneficiarioDTO;

public interface BeneficiariosMapper {

	@Update(value = ""
			+ "UPDATE svc_persona  "
			+ "SET  "
			+ "	REF_TELEFONO = #{in.telefono}, "
			+ "	REF_CORREO = #{in.correo} "
			+ "WHERE ID_PERSONA = #{in.idPersona}")
	public int actualizarPersona(@Param("in") ActualizarBeneficiarioDTO persona);

	@Update(value = ""
			+ "UPDATE SVT_CONTRATANTE_BENEFICIARIOS  "
			+ "SET  "
			+ "	FEC_ACTUALIZACION = CURRENT_DATE(), "
			+ "	IND_INE_BENEFICIARIO = #{in.validaIne} ,"
			+ "	REF_UBICACION_INE_BENEFICIARIO = #{in.nombreIne}, "
			+ "IND_ACTA_NACIMIENTO  = #{in.validaActa},"
			+ "REF_UBICACION_ACTA_NACIMIENTO=#{in.nombreActa},"
			+ "REF_DOCUMENTO_BENEFICIARIO=#{in.documento} "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante}"
			+ " AND ID_PERSONA = #{in.idPersona}")
	public int actualizarContratanteDocumento(@Param("in") ActualizarBeneficiarioDTO persona);

	@Update(value = ""
			+ "UPDATE SVT_CONTRATANTE_BENEFICIARIOS  "
			+ "SET  "
			+ "	FEC_ACTUALIZACION = CURRENT_DATE(), "
			+ "	IND_INE_BENEFICIARIO = #{in.validaIne} ,"
			+ "	REF_UBICACION_INE_BENEFICIARIO = #{in.nombreIne}, "
			+ " IND_ACTA_NACIMIENTO  = #{in.validaActa},"
			+ " REF_UBICACION_ACTA_NACIMIENTO=#{in.nombreActa}"
			+ " WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante}"
			+ " AND ID_PERSONA = #{in.idPersona}")
	public int actualizarContratante(@Param("in") ActualizarBeneficiarioDTO persona);

}
