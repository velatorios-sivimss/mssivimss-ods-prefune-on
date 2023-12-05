package com.imss.sivimss.ods.prefune.on.configuration.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.imss.sivimss.ods.prefune.on.model.request.ActualizarBeneficiarioDTO;
import com.imss.sivimss.ods.prefune.on.model.request.AgregarBeneficiarioDTO;

public interface BeneficiariosMapper {

	@Update(value = ""
			+ "UPDATE SVC_PERSONA  "
			+ "SET  "
			+ "REF_TELEFONO = #{in.telefono}, "
			+ "REF_CORREO = #{in.correo} "
			+ "WHERE ID_PERSONA = #{in.idPersona}")
	public int actualizarPersona(@Param("in") ActualizarBeneficiarioDTO persona);

	@Update(value = ""
			+ "UPDATE SVT_CONTRATANTE_BENEFICIARIOS  "
			+ "SET  "
			+ "FEC_ACTUALIZACION = CURRENT_DATE(), "
			+ "IND_INE_BENEFICIARIO = #{in.validaIne} ,"
			+ "REF_UBICACION_INE_BENEFICIARIO = #{in.nombreIne}, "
			+ "IND_ACTA_NACIMIENTO  = #{in.validaActa},"
			+ "REF_UBICACION_ACTA_NACIMIENTO=#{in.nombreActa},"
			+ "REF_DOCUMENTO_BENEFICIARIO=#{in.documento} "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante} "
			+ " AND ID_PERSONA = #{in.idPersona}")
	public int actualizarContratanteDocumento(@Param("in") ActualizarBeneficiarioDTO persona);

	@Update(value = ""
			+ "UPDATE SVT_CONTRATANTE_BENEFICIARIOS  "
			+ "SET  "
			+ "FEC_ACTUALIZACION = CURRENT_DATE(), "
			+ "IND_INE_BENEFICIARIO = #{in.validaIne} ,"
			+ "REF_UBICACION_INE_BENEFICIARIO = #{in.nombreIne}, "
			+ "IND_ACTA_NACIMIENTO  = #{in.validaActa}, "
			+ "REF_UBICACION_ACTA_NACIMIENTO=#{in.nombreActa} "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante} "
			+ "AND ID_PERSONA = #{in.idPersona} ")
	public int actualizarContratante(@Param("in") ActualizarBeneficiarioDTO persona);

	@Select(value = "SELECT COUNT(b.ID_CONTRATANTE_BENEFICIARIOS) AS existe " +
			" FROM svt_contratante_beneficiarios b " +
			" JOIN svc_persona sp ON sp.ID_PERSONA= b.ID_PERSONA" +
			" JOIN svt_contra_paq_convenio_pf p " +
			"ON p.ID_CONTRA_PAQ_CONVENIO_PF= b.ID_CONTRA_PAQ_CONVENIO_PF " +
			"WHERE b.IND_ACTIVO = 1 " +
			" AND p.ID_CONVENIO_PF = #{in.idConvenio}" +
			" AND sp.CVE_CURP = #{in.curp} ")
	public Map<String, Object> beneficiarioExiste(@Param("in") AgregarBeneficiarioDTO persona);

	@Insert(value = "INSERT INTO SVC_PERSONA  " +
			"( " +
			"CVE_RFC," +
			"CVE_CURP, " +
			"NOM_PERSONA," +
			"NOM_PRIMER_APELLIDO," +
			"NOM_SEGUNDO_APELLIDO," +
			"FEC_NAC, " +
			"ID_PAIS, " +
			"REF_TELEFONO, " +
			"REF_CORREO, " +
			"ID_USUARIO_ALTA" +
			"FEC_ALTA) " +
			" VALUES " +
			"( " +
			"CVE_RFC," +
			"#{out.nomPersona}, " +
			"#{out.nomPersona}, " +
			"#{out.nomPersona}, " +
			"#{out.nomPersona}, " +
			"#{out.nomPersona}, " +
			"#{out.nomPersona}, " +
			"#{out.nomPersona}, " +
			"#{out.nomPersona}, " +
			"#{out.nomPersona}, " +
			" CURRENT_DATE())")
	@Options(useGeneratedKeys = true, keyProperty = "out.idPersona", keyColumn = "id")
	public int insertaPersona(@Param("out") AgregarBeneficiarioDTO persona);

}
