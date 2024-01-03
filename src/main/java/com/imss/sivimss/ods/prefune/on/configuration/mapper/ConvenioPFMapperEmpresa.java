package com.imss.sivimss.ods.prefune.on.configuration.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.imss.sivimss.ods.prefune.on.model.request.AgregarConvenioEmpresaDTO;

public interface ConvenioPFMapperEmpresa {

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
			"WHERE v.ID_VELATORIO = #{datos.idVelatorio}), " +
			"(SELECT DATE_ADD( CURDATE(), INTERVAL 1 DAY)), " +
			"(select DATE_ADD(DATE_ADD( CURDATE(), interval 1 year),interval 1 DAY)), " +
			"(DATE_FORMAT(NOW(), '%H:%i')), " +
			"#{datos.idVelatorio}, " +
			"1, " +
			"0, " +
			"#{datos.idPromotor}, " +
			"1, " +
			"#{datos.idUsuario}, " +
			" CURRENT_DATE()) ")
	@Options(useGeneratedKeys = true, keyProperty = "datos.idConvenioPF", keyColumn = "ID_CONVENIO_PF")
	public int agregarConvenioPFEmpresa(@Param("datos") AgregarConvenioEmpresaDTO datos);

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
	public int agregarDomicilio(@Param("datos") AgregarConvenioEmpresaDTO datos);

	@Insert(value = "INSERT INTO SVT_EMPRESA_CONVENIO_PF " +
			"( " +
			"REF_NOMBRE, " +
			"REF_RAZON_SOCIAL," +
			"CVE_RFC, " +
			"ID_PAIS, " +
			"ID_DOMICILIO," +
			"REF_TELEFONO, " +
			"REF_CORREO," +
			"ID_CONVENIO_PF," +
			"ID_USUARIO_ALTA," +
			"FEC_ALTA)  " +
			"values " +
			"( " +
			" #{datos.nombre}, " +
			" #{datos.razonSocial}, " +
			" #{datos.rfcEmpresa}, " +
			" #{datos.idPais}, " +
			" #{datos.idDomicilio}, " +
			" #{datos.telefono}, " +
			" #{datos.correo}, " +
			" #{datos.idConvenioPF}, " +
			" #{datos.idUsuario}, " +
			" CURRENT_DATE() " +
			")")
	@Options(useGeneratedKeys = true, keyProperty = "datos.idEmpresaConvenioPF", keyColumn = "ID_EMPRESA_CONVENIO_PF")
	public int agregarEmpresaConvenioPF(@Param("datos") AgregarConvenioEmpresaDTO datos);

	@Select(value = "SELECT DES_FOLIO AS folio " +
			"FROM SVT_CONVENIO_PF " +
			"WHERE ID_CONVENIO_PF = #{datos.idConvenioPF}")
	public Map<String, Object> folioConvenio(@Param("datos") AgregarConvenioEmpresaDTO datos);

	// se terminan los datos por empresa

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
	public int agregarContratante(@Param("datos") AgregarConvenioEmpresaDTO datos);

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
	public int agregarContratoConvenioPaquete(@Param("datos") AgregarConvenioEmpresaDTO datos);

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
	public int agregaDocumentacion(@Param("datos") AgregarConvenioEmpresaDTO datos);

}
