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
			+ "ID_USUARIO_MODIFICA = #{in.idUsuario} ,"
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
			+ "ID_USUARIO_MODIFICA = #{in.idUsuario} ,"
			+ "REF_UBICACION_ACTA_NACIMIENTO=#{in.nombreActa} "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante} "
			+ "AND ID_PERSONA = #{in.idPersona} ")
	public int actualizarContratante(@Param("in") ActualizarBeneficiarioDTO persona);

	@Select(value = "SELECT COUNT(b.ID_CONTRATANTE_BENEFICIARIOS) AS existe " +
			" FROM SVT_CONTRATANTE_BENEFICIARIOS b " +
			" JOIN SVC_PERSONA sp ON sp.ID_PERSONA= b.ID_PERSONA" +
			" JOIN SVT_CONTRA_PAQ_CONVENIO_PF p " +
			"ON p.ID_CONTRA_PAQ_CONVENIO_PF= b.ID_CONTRA_PAQ_CONVENIO_PF " +
			"WHERE b.IND_ACTIVO = 1 " +
			" AND p.ID_CONVENIO_PF = #{in.idConvenio}" +
			" AND sp.CVE_CURP = #{in.curp} ")
	public Map<String, Object> personaExiste(@Param("in") AgregarBeneficiarioDTO persona);

	@Select(value = "SELECT  COUNT(*) AS  noPersona ,ifnull(IND_ACTIVO,-1) AS estatus " +
			" FROM SVT_CONTRATANTE_BENEFICIARIOS " +
			" WHERE ID_PERSONA= #{in.idPersona} " +
			" AND ID_CONTRA_PAQ_CONVENIO_PF in " +
			"(SELECT ID_CONTRA_PAQ_CONVENIO_PF " +
			"FROM  SVT_CONTRA_PAQ_CONVENIO_PF  where ID_CONVENIO_PF = #{in.idConvenio}   )")
	public Map<String, Object> beneficiarioAsociado(@Param("in") AgregarBeneficiarioDTO persona);

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
			"ID_USUARIO_ALTA," +
			"FEC_ALTA) " +
			" VALUES " +
			"( " +
			"#{out.rfc}," +
			"#{out.curp}, " +
			"#{out.nombre}, " +
			"#{out.primerApellido}, " +
			"#{out.segundoApellido}, " +
			"#{out.fechaNacimiento}, " +
			"119, " +
			"#{out.telefono}, " +
			"#{out.correo}, " +
			"#{out.idUsuario}, " +
			" CURRENT_DATE())")
	@Options(useGeneratedKeys = true, keyProperty = "out.idPersona", keyColumn = "ID_PERSONA")
	public int insertaPersona(@Param("out") AgregarBeneficiarioDTO persona);

	@Update(value = ""
			+ "UPDATE SVT_CONTRATANTE_BENEFICIARIOS  "
			+ "SET  "
			+ "FEC_ACTUALIZACION = CURRENT_DATE(), "
			+ "IND_ACTIVO = 1, "
			+ "ID_USUARIO_MODIFICA = #{in.idUsuario} ,"
			+ "IND_INE_BENEFICIARIO = #{in.validaIne} ,"
			+ "REF_UBICACION_INE_BENEFICIARIO = #{in.nombreIne}, "
			+ "IND_ACTA_NACIMIENTO  = #{in.validaActa},"
			+ "REF_UBICACION_ACTA_NACIMIENTO=#{in.nombreActa},"
			+ "REF_DOCUMENTO_BENEFICIARIO=#{in.documento} "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante} "
			+ " AND ID_PERSONA = #{in.idPersona}")
	public int actualizarContratanteDocumento2(@Param("in") AgregarBeneficiarioDTO persona);

	@Insert(value = "INSERT INTO SVT_CONTRATANTE_BENEFICIARIOS " +
			"(ID_CONTRA_PAQ_CONVENIO_PF," +
			" ID_PARENTESCO," +
			" ID_PERSONA," +
			" ID_USUARIO_ALTA," +
			" FEC_ALTA, " +
			" IND_ACTIVO, " +
			" IND_INE_BENEFICIARIO, " +
			" REF_UBICACION_INE_BENEFICIARIO," +
			" IND_ACTA_NACIMIENTO," +
			" REF_UBICACION_ACTA_NACIMIENTO," +
			" REF_DOCUMENTO_BENEFICIARIO) " +
			" VALUES " +
			"( (SELECT DISTINCT ID_CONTRA_PAQ_CONVENIO_PF" +
			" FROM  SVT_CONTRA_PAQ_CONVENIO_PF  where ID_CONVENIO_PF = #{out.idConvenio}  limit 1 )," +
			"#{out.idParentesco}, " +
			"#{out.idPersona}, " +
			"#{out.idUsuario}, " +
			"CURRENT_DATE(), " +
			"1, " +
			"#{out.validaIne}, " +
			"#{out.nombreIne}, " +
			"#{out.validaActa}, " +
			"#{out.nombreActa}, " +
			"#{out.documento} " +
			")")
	@Options(useGeneratedKeys = true, keyProperty = "out.idContratante", keyColumn = "ID_CONTRATANTE_BENEFICIARIOS")
	public int insertaBeneficiarioContratante(@Param("out") AgregarBeneficiarioDTO persona);

	@Update(value = ""
			+ "UPDATE SVT_CONTRATANTE_BENEFICIARIOS  "
			+ "SET  "
			+ "FEC_ACTUALIZACION = CURRENT_DATE(), "
			+ "ID_USUARIO_MODIFICA = #{in.idUsuario} ,"
			+ "IND_ACTIVO = 0 "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante} "
			+ " AND ID_PERSONA = #{in.idPersona}")
	public int desactivarBeneficiario(@Param("in") ActualizarBeneficiarioDTO persona);

}
