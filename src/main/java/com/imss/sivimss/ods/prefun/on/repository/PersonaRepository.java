package com.imss.sivimss.ods.prefun.on.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.imss.sivimss.ods.prefun.on.model.entity.PersonaEntity;
import com.imss.sivimss.ods.prefun.on.model.request.Persona;

public interface PersonaRepository  extends JpaRepository<PersonaEntity, Integer> {

	@Query(value = "SELECT sp.NOM_PERSONA AS nombre, sp.NOM_PRIMER_APELLIDO AS primerApellido, sp.NOM_SEGUNDO_APELLIDO AS segundoApellido"
			+ ", (YEAR(CURDATE()) - YEAR (sp.FEC_NAC)) AS edad, sp.CVE_RFC AS rfc, sp.REF_CORREO AS correo FROM SVC_PERSONA sp "
			+ " ORDER BY sp.ID_PERSONA ASC "
			, nativeQuery = true)
	List<Map<String, Object>>  buscarPersonas();
	
	@Query(value = "SELECT sp.NOM_PERSONA AS nombre, sp.NOM_PRIMER_APELLIDO AS primerApellido, sp.NOM_SEGUNDO_APELLIDO AS segundoApellido"
			+ ", (YEAR(CURDATE()) - YEAR (sp.FEC_NAC)) AS edad, sp.CVE_RFC AS rfc, sp.REF_CORREO AS correo FROM SVC_PERSONA sp "
			+ " WHERE sp.ID_PERSONA = ?1 "
			+ " ORDER BY sp.ID_PERSONA ASC"
			, nativeQuery = true)
	List<Map<String, Object>>  buscarPersonaById(Integer idDelegacion);

	@Query(value = "SELECT sp.NOM_PERSONA AS nombre, sp.NOM_PRIMER_APELLIDO AS primerApellido, sp.NOM_SEGUNDO_APELLIDO AS segundoApellido"
			+ ", (YEAR(CURDATE()) - YEAR (sp.FEC_NAC)) AS edad, sp.CVE_RFC AS rfc, sp.REF_CORREO AS correo FROM SVC_PERSONA sp "
			+ " ORDER BY sp.ID_PERSONA ASC "
			,countQuery = "SELECT count(*) FROM SVC_PERSONA sv "
			, nativeQuery = true)
	List<Map<String, Object>>  buscarVelatorioPaginado(Pageable pageable);
	
	@Query(value = "SELECT COUNT(*) AS conteo FROM (SELECT sp.NOM_PERSONA AS nombre, sp.NOM_PRIMER_APELLIDO AS primerApellido, sp.NOM_SEGUNDO_APELLIDO AS segundoApellido"
			+ ", (YEAR(CURDATE()) - YEAR (sp.FEC_NAC)) AS edad, sp.CVE_RFC AS rfc, sp.REF_CORREO AS correo FROM SVC_PERSONA sp "
			+ " ORDER BY sp.ID_PERSONA ASC) tem"
			, nativeQuery = true)
	List<Map<String, Object>>  buscarVelatorioPaginadoTotal();

	@Query(value = "SELECT sv.ID_VELATORIO AS idVelatorio, sv.DES_VELATORIO AS nombreVelatorio FROM SVC_VELATORIO sv WHERE sv.ID_DELEGACION = ?1 ORDER BY sv.ID_VELATORIO ASC",
			countQuery = "SELECT count(*) FROM SVC_VELATORIO sv WHERE sv.ID_DELEGACION = ?1"
			, nativeQuery = true)
	List<Map<String, Object>>  buscarVelatorioFiltroPag(Pageable pageable, Integer idDelegacion);
	
	@Query(value = "SELECT COUNT(*) AS conteo FROM (SELECT sv.ID_VELATORIO AS idVelatorio, sv.DES_VELATORIO AS nombreVelatorio FROM SVC_VELATORIO sv  WHERE sv.ID_DELEGACION = ?1 ) tem"
			, nativeQuery = true)
	List<Map<String, Object>>  buscarVelatorioFiltroPagTotal(Integer idDelegacion);
	
	@Modifying
	@Transactional
	@Query(
	  value = " INSERT INTO SVC_PERSONA "
	  		+ " (CVE_RFC, CVE_CURP, CVE_NSS, NOM_PERSONA, NOM_PRIMER_APELLIDO, NOM_SEGUNDO_APELLIDO, NUM_SEXO, REF_OTRO_SEXO, FEC_NAC, ID_PAIS, ID_ESTADO, REF_TELEFONO, REF_TELEFONO_FIJO, REF_CORREO, TIP_PERSONA, ID_USUARIO_ALTA, FEC_ALTA, NUM_INE) "
	  		+ " VALUES(:#{#persona.cveRFC},:#{#persona.cveCURP},:#{#persona.cveNSS},:#{#persona.nomPersona},:#{#persona.primerApellido},:#{#persona.segundoApellido},:#{#persona.numSexo},:#{#persona.otroSexo},:#{#persona.fecNac},:#{#persona.idPais},:#{#persona.idEstado},:#{#persona.telefono},:#{#persona.telefonoFijo},:#{#persona.correo},:#{#persona.tipoPersona}"
	  		+ ", :idUsuarioAlta, CURDATE(),:#{#persona.numINE})",
	  		nativeQuery = true)
	void guardarPersona(@Param("persona") Persona persona,
			@Param("idUsuarioAlta") Integer idUsuarioAlta);
	

	@Modifying
	@Transactional
	@Query( value = " UPDATE  SVC_PERSONA SET "
			+ " CVE_RFC = :#{#persona.cveRFC}, CVE_CURP = :#{#persona.cveCURP},CVE_NSS = :#{#persona.cveNSS},NOM_PERSONA = :#{#persona.nomPersona}, NOM_PRIMER_APELLIDO = :#{#persona.primerApellido}, NOM_SEGUNDO_APELLIDO = :#{#persona.segundoApellido} "
			+ ", NUM_SEXO = :#{#persona.numSexo}, REF_OTRO_SEXO = :#{#persona.otroSexo}, FEC_NAC = :#{#persona.fecNac}, ID_PAIS = :#{#persona.idPais}, ID_ESTADO = :#{#persona.idEstado}, REF_TELEFONO =:#{#persona.telefono} "
			+ ", REF_TELEFONO_FIJO = :#{#persona.telefonoFijo}, REF_CORREO = :#{#persona.correo}, TIP_PERSONA = :#{#persona.tipoPersona} "
			+ ", ID_USUARIO_MODIFICA = :idUsuarioModifica, FEC_ACTUALIZACION = CURDATE(), NUM_INE = :#{#persona.numINE}"
			+ " WHERE ID_PERSONA = :#{#persona.idPersona}"
				  		,nativeQuery = true)
	void actualizarPersona(@Param("persona") Persona persona ,@Param("idUsuarioModifica") Integer idUsuarioModifica);
	

	@Modifying
	@Transactional
	@Query(
	  value = " UPDATE SVC_PERSONA SET CVE_CURP = null,CVE_NSS = null , FEC_BAJA = CURDATE(), ID_USUARIO_BAJA = :idUsuarioBaja "
	  		+ "WHERE ID_PERSONA = :idPersona",
	  		nativeQuery = true)
	void borrarPersona(@Param("idPersona") Integer idPersona,@Param("idUsuarioBaja") Integer idUsuarioBaja);
}
