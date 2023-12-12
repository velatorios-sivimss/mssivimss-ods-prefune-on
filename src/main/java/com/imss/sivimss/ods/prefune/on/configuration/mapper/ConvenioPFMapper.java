package com.imss.sivimss.ods.prefune.on.configuration.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

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
			"SELECT COUNT(SP.ID_CONVENIO_PF +1) " +
			"FROM SVT_CONVENIO_PF SP),6,'0'))  " +
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
			"1, " +
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

}
