package com.imss.sivimss.ods.prefune.on.configuration.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.imss.sivimss.ods.prefune.on.model.request.ActualizarBeneficiarioDTO;
import com.imss.sivimss.ods.prefune.on.model.request.AgregarConvenioPersonaDTO;

public interface ConvenioPFMapper {

	@Insert(value = "INSERT INTO SVT_CONVENIO_PF ( " +
			" DES_FOLIO,  " +
			" FEC_INICIO, " +
			" FEC_VIGENCIA,  " +
			" TIM_HORA, " +
			" ID_VELATORIO, " +
			" ID_TIPO_PREVISION, " +
			" IND_TIPO_CONTRATACION, " +
			" ID_PROMOTOR, " +
			" ID_ESTATUS_CONVENIO, " +
			" ID_USUARIO_ALTA, " +
			" FEC_ALTA) " +
			"  VALUES ( " +
			"( " +
			"SELECT CONCAT( " +
			"LEFT(v.DES_VELATORIO, 3),'-', LPAD(( " +
			"SELECT COUNT(SP.ID_CONVENIO_PF) + 1 " +
			"FROM SVT_CONVENIO_PF SP" +
			" WHERE SP.ID_VELATORIO = #{datos.idVelatorio}),6,'0'))  " +
			"FROM SVC_VELATORIO v " +
			"JOIN SVT_PAQUETE p ON p.ID_PAQUETE = #{datos.idPaquete} " +
			"WHERE v.ID_VELATORIO = #{datos.idVelatorio}), " +
			"(SELECT DATE_ADD( CURDATE(), INTERVAL 1 DAY)), " +
			"(select DATE_ADD(DATE_ADD( CURDATE(), interval 1 year),interval 1 DAY)), " +
			"(DATE_FORMAT(NOW(), '%H:%i')), " +
			"#{datos.idVelatorio}, " +
			"1, " +
			"#{datos.idTipoContratacion}, " +
			"#{datos.idPromotor}, " +
			"5, " +
			"#{datos.idUsuario}, " +
			" CURRENT_DATE()) ")
	@Options(useGeneratedKeys = true, keyProperty = "datos.idConvenioPF", keyColumn = "ID_CONVENIO_PF")
	public int agregarConvenioPF(@Param("datos") AgregarConvenioPersonaDTO datos);

	@Insert(value = "INSERT INTO SVT_DOMICILIO " +
			" (" +
			" REF_CALLE, " +
			" NUM_EXTERIOR, " +
			" NUM_INTERIOR, " +
			" REF_CP,  " +
			" REF_COLONIA, " +
			" REF_MUNICIPIO,  " +
			" REF_ESTADO,  " +
			" ID_USUARIO_ALTA,  " +
			" FEC_ALTA)  " +
			" VALUES  " +
			" ( " +
			" #{datos.calle}, " +
			" #{datos.noExterior}, " +
			" #{datos.noInterior}, " +
			" #{datos.cp}, " +
			" #{datos.colonia}, " +
			" #{datos.municipio}, " +
			" #{datos.estado}, " +
			" #{datos.idUsuario}, " +
			" CURRENT_DATE() " +
			" )  ")
	@Options(useGeneratedKeys = true, keyProperty = "datos.idDomicilio", keyColumn = "ID_DOMICILIO")
	public int agregarDomicilio(@Param("datos") AgregarConvenioPersonaDTO datos);

	@Update(value = ""
			+ "UPDATE SVT_DOMICILIO  "
			+ "SET  "
			+ "FEC_ACTUALIZACION = CURRENT_DATE(), "
			+ "ID_USUARIO_MODIFICA = #{in.idUsuario} ," +
			" REF_CALLE = #{in.calle} , " +
			" NUM_EXTERIOR= #{in.noExterior} , " +
			" NUM_INTERIOR = #{in.noInterior} , " +
			" REF_CP = #{in.cp} ,  " +
			" REF_COLONIA = #{in.colonia} , " +
			" REF_MUNICIPIO = #{in.municipio} ,  " +
			" REF_ESTADO = #{in.estado}   "
			+ " WHERE ID_DOMICILIO = #{in.idDomicilio} ")
	public int updateDomicilio(@Param("in") AgregarConvenioPersonaDTO persona);

	@Insert(value = "INSERT INTO SVC_CONTRATANTE  " +
			" (  " +
			" ID_PERSONA, " +
			" CVE_MATRICULA, " +
			" ID_DOMICILIO, " +
			" FEC_ALTA, " +
			" ID_USUARIO_ALTA, " +
			" IND_ACTIVO) " +
			" VALUES ( " +
			" #{datos.idPersona}, " +
			" #{datos.cveMatricula}, " +
			" #{datos.idDomicilio}, " +
			" CURRENT_DATE(), " +
			" #{datos.idUsuario}, " +
			" 1 " +
			" )  ")
	@Options(useGeneratedKeys = true, keyProperty = "datos.idContratante", keyColumn = "ID_CONTRATANTE")
	public int agregarContratante(@Param("datos") AgregarConvenioPersonaDTO datos);

	@Insert(value = "INSERT INTO SVT_CONTRA_PAQ_CONVENIO_PF  " +
			" (ID_CONTRATANTE,   " +
			" ID_CONVENIO_PF,  " +
			" IND_ENFERMEDAD_PREXISTENTE,   " +
			" REF_OTRA_ENFERMEDAD,  " +
			" ID_PAQUETE,   " +
			" ID_USUARIO_ALTA,  " +
			" FEC_ALTA)  " +
			"  VALUES (  " +
			"#{datos.idContratante},  " +
			"#{datos.idConvenioPF},  " +
			"#{datos.idEnfermedad},  " +
			"#{datos.otraEnfermedad},  " +
			"#{datos.idPaquete},  " +
			"#{datos.idUsuario},  " +
			"CURRENT_DATE()  " +
			")  ")
	@Options(useGeneratedKeys = true, keyProperty = "datos.idContratoConvenioPaquete", keyColumn = "ID_CONTRA_PAQ_CONVENIO_PF")
	public int agregarContratoConvenioPaquete(@Param("datos") AgregarConvenioPersonaDTO datos);

	@Insert(value = "INSERT INTO SVC_VALIDA_DOCS_CONVENIO_PF " +
			" (" +
			" IND_INE_AFILIADO," +
			" REF_UBICACION_INE, " +
			" IND_CURP, " +
			" REF_UBICACION_CURP, " +
			" IND_RFC," +
			" REF_UBICACION_RFC,  " +
			" REF_DOC_INE_AFILIADO, " +
			" REF_DOC_CURP_AFILIADO, " +
			" REF_DOC_RFC_AFILIADO, " +
			" ID_CONVENIO_PF," +
			" ID_USUARIO_ALTA, " +
			" FEC_ALTA" +
			" )" +
			" VALUES " +
			" (" +
			" #{datos.validaIne}," +
			" #{datos.nombreIne}," +
			" #{datos.validaCurp}," +
			" #{datos.nombreCurp}," +
			" #{datos.validaRfc}," +
			" #{datos.nombreRfc}," +
			" #{datos.archivoIne}, " +
			" #{datos.archivoCurp}," +
			" #{datos.archivoRfc}," +
			" #{datos.idConvenioPF}," +
			" #{datos.idUsuario}," +
			" CURRENT_DATE()" +
			" ) ")
	@Options(useGeneratedKeys = true, keyProperty = "datos.idValidaDocumento", keyColumn = "ID_VALIDACION_DOCUMENTO")
	public int agregaDocumentacion(@Param("datos") AgregarConvenioPersonaDTO datos);

	@Select(value = "SELECT DES_FOLIO AS folio " +
			"FROM SVT_CONVENIO_PF " +
			"WHERE ID_CONVENIO_PF = #{datos.idConvenioPF}")
	public Map<String, Object> folioConvenio(@Param("datos") AgregarConvenioPersonaDTO datos);

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
			"#{out.idPais}, " +
			"#{out.telefono}, " +
			"#{out.correo}, " +
			"#{out.idUsuario}, " +
			" CURRENT_DATE())")
	@Options(useGeneratedKeys = true, keyProperty = "out.idPersona", keyColumn = "ID_PERSONA")
	public int agregarPersona(@Param("out") AgregarConvenioPersonaDTO persona);

	@Select(value = "SELECT  COUNT( cp.ID_CONVENIO_PF) as totalPersona " +
			"FROM SVT_CONTRA_PAQ_CONVENIO_PF  cp " +
			"join SVC_CONTRATANTE c ON c.ID_CONTRATANTE = cp.ID_CONTRATANTE " +
			"WHERE cp.ID_CONVENIO_PF = #{datos.idConvenioPF} " +
			"AND c.ID_PERSONA = #{datos.idPersona} " +
			"AND c.IND_ACTIVO = 1 ")
	public Map<String, Object> personaAgregada(@Param("datos") AgregarConvenioPersonaDTO datos);

}
