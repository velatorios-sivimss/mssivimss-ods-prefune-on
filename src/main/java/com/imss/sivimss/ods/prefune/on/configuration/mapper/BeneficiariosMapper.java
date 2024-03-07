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
			+ "FEC_ACTUALIZACION = CURRENT_TIMESTAMP(), "
			+ "IND_INE_BENEFICIARIO = #{in.validaIne} ,"
			+ "REF_UBICACION_INE_BENEFICIARIO = #{in.nombreIne}, "
			+ "IND_ACTA_NACIMIENTO  = #{in.validaActa},"
			+ "REF_UBICACION_ACTA_NACIMIENTO=#{in.nombreActa},"
			+ "ID_USUARIO_MODIFICA = #{in.idUsuario} ,"
			+ "REF_DOCUMENTO_BENEFICIARIO=#{in.documento} "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante} "
			+ " AND ID_PERSONA = #{in.idPersona}")
	public int actualizarContratanteDocumento(@Param("in") ActualizarBeneficiarioDTO persona);

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
			" CURRENT_TIMESTAMP())")
	@Options(useGeneratedKeys = true, keyProperty = "out.idPersona", keyColumn = "ID_PERSONA")
	public int insertaPersona(@Param("out") AgregarBeneficiarioDTO persona);

	@Update(value = ""
			+ "UPDATE SVT_CONTRATANTE_BENEFICIARIOS  "
			+ "SET  "
			+ "FEC_ACTUALIZACION = CURRENT_TIMESTAMP(), "
			+ "IND_ACTIVO = 1, "
			+ "ID_USUARIO_MODIFICA = #{in.idUsuario} ,"
			+ "IND_INE_BENEFICIARIO = #{in.validaIne} ,"
			+ "REF_UBICACION_INE_BENEFICIARIO = #{in.nombreIne}, "
			+ "IND_ACTA_NACIMIENTO  = #{in.validaActa},"
			+ "REF_UBICACION_ACTA_NACIMIENTO=#{in.nombreActa},"
			+ "REF_DOCUMENTO_BENEFICIARIO=#{in.documento} "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = (SELECT cb.ID_CONTRATANTE_BENEFICIARIOS \r\n" + //
			"from SVT_CONTRATANTE_BENEFICIARIOS cb\r\n" + //
			"WHERE cb.ID_CONTRA_PAQ_CONVENIO_PF= (\r\n" + //
			"SELECT pc.ID_CONTRA_PAQ_CONVENIO_PF \r\n" + //
			"FROM SVT_CONVENIO_PF pf\r\n" + //
			"join SVT_CONTRA_PAQ_CONVENIO_PF pc ON pc.ID_CONVENIO_PF=pf.ID_CONVENIO_PF\r\n" + //
			"WHERE pf.ID_CONVENIO_PF = #{in.idConvenio})) "
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
			" FROM  SVT_CONTRA_PAQ_CONVENIO_PF  where ID_CONVENIO_PF = #{out.idConvenio} and ID_CONTRATANTE = #{out.idContratante}  limit 1 ),"
			+
			"#{out.idParentesco}, " +
			"#{out.idPersona}, " +
			"#{out.idUsuario}, " +
			"CURRENT_TIMESTAMP(), " +
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
			+ "FEC_ACTUALIZACION = CURRENT_TIMESTAMP(), "
			+ "ID_USUARIO_MODIFICA = #{in.idUsuario} ,"
			+ "IND_ACTIVO = 0 "
			+ "WHERE ID_CONTRATANTE_BENEFICIARIOS = #{in.idContratante} "
			+ " AND ID_PERSONA = #{in.idPersona}")
	public int desactivarBeneficiario(@Param("in") ActualizarBeneficiarioDTO persona);

	@Select(value = "SELECT c.ID_CONTRATANTE AS idContratante,  " +
			" c.ID_PERSONA AS idPersona, c.ID_DOMICILIO AS idDomicilio, " +
			" ifnull(p.CVE_RFC,'') AS rfc, p.CVE_CURP AS curp, p.CVE_NSS AS nss,  " +
			" p.NOM_PERSONA AS nombre, p.NOM_PRIMER_APELLIDO AS primerApellido, " +
			" p.NOM_SEGUNDO_APELLIDO AS segundoApellido, " +
			" ifnull(c.CVE_MATRICULA,'') as matricula," +
			" p.NUM_SEXO AS idSexo,  " +
			" case when p.NUM_SEXO = 1 then 'Mujer' " +
			" when p.NUM_SEXO = 2 then 'Hombre' " +
			" ELSE 'Otro' END sexo, " +
			" ifnull(p.REF_OTRO_SEXO,'') AS otroSexo,  " +
			" DATE_FORMAT(p.FEC_NAC,'%d-%m-%Y') AS fechaNacimiento, " +
			" p.ID_ESTADO AS idEstado, " +
			" p.REF_TELEFONO AS telefono, " +
			" p.REF_CORREO AS correo, " +
			" p.TIP_PERSONA AS tipoPersona, " +
			" e.DES_ESTADO AS estado ," +
			" c.ID_DOMICILIO AS idDomicilio," +
			"(SELECT d.DES_DELEGACION FROM SVC_VELATORIO v " +
			"JOIN SVC_DELEGACION d ON d.ID_DELEGACION= v.ID_DELEGACION " +
			"WHERE v.ID_VELATORIO = #{in.idVelatorio}) AS delegacion ," +
			" DATE_FORMAT(DATE_ADD( CURDATE(), INTERVAL 1 DAY),'%d-%m-%Y') AS fecha " +
			" FROM SVC_CONTRATANTE c " +
			" JOIN SVC_PERSONA p ON p.ID_PERSONA = c.ID_PERSONA " +
			" LEFT JOIN SVC_ESTADO e ON e.ID_ESTADO= p.ID_ESTADO " +
			" WHERE c.ID_CONTRATANTE=  #{in.idContratante} ")
	public Map<String, Object> datosPersonalesContratante(@Param("in") AgregarBeneficiarioDTO persona);

}
