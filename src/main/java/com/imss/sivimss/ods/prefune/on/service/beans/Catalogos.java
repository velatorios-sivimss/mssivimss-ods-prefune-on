package com.imss.sivimss.ods.prefune.on.service.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ods.prefune.on.utils.SelectQueryUtil;

@Service
public class Catalogos {

    private String query;

    private static final Logger log = LoggerFactory.getLogger(Catalogos.class);

    public String consultarParentesco() {
        SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
        selectQueryUtil.select("SO.ID_PARENTESCO AS idParentesco, SO.DES_PARENTESCO AS nombreParentesco")
                .from("SVC_PARENTESCO SO ")
                .orderBy("SO.DES_PARENTESCO ASC ");

        query = selectQueryUtil.build();
        log.info("consultarParentesco: {}", query);

        return query;
    }

    public String consultarPromotores() {
        SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
        selectQueryUtil
                .select("SP.ID_PROMOTOR AS value", "SP.NUM_EMPLEDO AS numEmpleado",
                        "CONCAT (SP.NOM_PROMOTOR , ' ' , SP.NOM_PAPELLIDO , ' ' , SP.NOM_SAPELLIDO) AS label")
                .from("SVT_PROMOTOR SP");
        query = selectQueryUtil.build();
        log.info("consultarPromotores: {}", this.query);
        return query;
    }

    public String obtenerPaquetes(Integer idVelatorio) {
        SelectQueryUtil selectQueryUtilPaquete = new SelectQueryUtil();
        SelectQueryUtil selectQueryUtilUnionPaqueteRegion = new SelectQueryUtil();
        SelectQueryUtil selectQueryUtilUnionPaqueteVelatorio = new SelectQueryUtil();
        SelectQueryUtil selectQueryUtilUnionPaqueteServicio = new SelectQueryUtil();
        SelectQueryUtil selectQueryUtilUnionPaqueteArticulo = new SelectQueryUtil();

        selectQueryUtilUnionPaqueteVelatorio
                .select("SP.ID_PAQUETE", "SP.REF_PAQUETE_NOMBRE", "SP.REF_PAQUETE_DESCRIPCION","SP.MON_PRECIO")
                .from("SVT_PAQUETE SP")
                .innerJoin("SVT_PAQUETE_VELATORIO SPV", "SP.ID_PAQUETE=SPV.ID_PAQUETE")
                .where("SP.IND_ACTIVO = 1")
                .and("SP.IND_ONLINE = 1")
                .and("SP.IND_PA = 0")
                .and("SPV.ID_VELATORIO = " + idVelatorio);

        selectQueryUtilUnionPaqueteRegion
                .select("SP.ID_PAQUETE", "SP.REF_PAQUETE_NOMBRE", "SP.REF_PAQUETE_DESCRIPCION","SP.MON_PRECIO")
                .from("SVT_PAQUETE SP")
                .where("SP.IND_ACTIVO = 1 ")
                .and("SP.IND_ONLINE = 1")
                .and("SP.IND_PA = 0")
                .and("SP.IND_REGION =1");

        selectQueryUtilUnionPaqueteServicio.select("SPS.ID_PAQUETE")
                .from("SVT_PROVEEDOR SP")
                .innerJoin("SVT_CONTRATO SC", "SC.ID_PROVEEDOR = SP.ID_PROVEEDOR")
                .innerJoin("SVT_CONTRATO_SERVICIO SCS", "SCS.ID_CONTRATO = SC.ID_CONTRATO")
                .innerJoin("SVT_SERVICIO SS", "SS.ID_SERVICIO = SCS.ID_SERVICIO")
                .innerJoin("SVT_PAQUETE_SERVICIO SPS", "SPS.ID_SERVICIO = SS.ID_SERVICIO")
                .where("SP.IND_ACTIVO =1 ")
                .and("SPS.IND_ACTIVO = 1")
                .and("SP.ID_TIPO_PROVEEDOR =1")
                .and("SC.FEC_FIN_VIG >= CURRENT_TIMESTAMP()")
                .and("SP.FEC_VIGENCIA >= CURRENT_TIMESTAMP()")
                .and("SC.IND_ACTIVO =1");

        selectQueryUtilUnionPaqueteArticulo.select("DISTINCT SPA.ID_PAQUETE")
                .from("SVT_INVENTARIO_ARTICULO STI")
                .innerJoin("SVT_ARTICULO STA", "STA.ID_ARTICULO =STI.ID_ARTICULO")
                .innerJoin("SVC_CATEGORIA_ARTICULO SCA",
                        "SCA.ID_CATEGORIA_ARTICULO = STA.ID_CATEGORIA_ARTICULO")
                .innerJoin("SVT_PAQUETE_ARTICULO SPA",
                        "SPA.ID_CATEGORIA_ARTICULO = SCA.ID_CATEGORIA_ARTICULO")
                .where("STI.IND_ESTATUS = 0")
                .and("STI.ID_TIPO_ASIGNACION_ART IN (1,3)");

        String queryPaqueteRegion = selectQueryUtilUnionPaqueteVelatorio
                .union(selectQueryUtilUnionPaqueteRegion);

        selectQueryUtilPaquete
                .select("PAQUETES.ID_PAQUETE AS idPaquete", "PAQUETES.REF_PAQUETE_NOMBRE AS nomPaquete",
                        "PAQUETES.REF_PAQUETE_DESCRIPCION AS descPaquete","PAQUETES.MON_PRECIO AS costoPaquete")
                .from("(" + queryPaqueteRegion + ") PAQUETES");

        query = selectQueryUtilPaquete.build();
        log.info("obtenerPaquetes: {}", query);

        return query;
    }

    public String obtenerCaracteristicasPaquete(Integer idPaquete) {
        SelectQueryUtil selectQueryUtilServicio = new SelectQueryUtil();
        selectQueryUtilServicio.select(
                "SS.DES_SERVICIO AS concepto");
        selectQueryUtilServicio.from("SVT_PAQUETE SP")
                .innerJoin("SVT_PAQUETE_SERVICIO SPS", "SPS.ID_PAQUETE = SP.ID_PAQUETE")
                .leftJoin("SVT_SERVICIO SS", "SS.ID_SERVICIO =SPS.ID_SERVICIO")
                .innerJoin("SVC_TIPO_SERVICIO STPS", "SS.ID_TIPO_SERVICIO = STPS.ID_TIPO_SERVICIO")
                .where("SP.ID_PAQUETE = :idPaquete")
                .and("SPS.IND_ACTIVO = 1")
                .setParameter("idPaquete", idPaquete);

        SelectQueryUtil selectQueryUtilArticulo = new SelectQueryUtil();
        selectQueryUtilArticulo.select("SCA.DES_CATEGORIA_ARTICULO AS concepto");

        selectQueryUtilArticulo.from("SVT_PAQUETE SP")
                .innerJoin("SVT_PAQUETE_ARTICULO SPA", "SP.ID_PAQUETE= SPA.ID_PAQUETE ")
                .leftJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = SPA.ID_CATEGORIA_ARTICULO")
                .where("SP.ID_PAQUETE = :idPaquete")
                .and("SPA.IND_ACTIVO = 1")
                .setParameter("idPaquete", idPaquete);

        query = selectQueryUtilServicio.union(selectQueryUtilArticulo);
        log.info(query);

        return query;
    }

    public String consultarPaises() {
        SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
        selectQueryUtil.select("SP.ID_PAIS AS value", "SP.DES_PAIS AS label")
                .from("SVC_PAIS SP ")
                .where("ifnull(SP.DES_PAIS,'') <> ''")
                .orderBy("SP.DES_PAIS ASC ");

        query = selectQueryUtil.build();
        log.info("consultarPaises: {}", query);

        return query;
    }
    
    public String consultarEstado() {
        SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
        selectQueryUtil.select("SP.ID_ESTADO AS value", "SP.DES_ESTADO AS label")
                .from("SVC_ESTADO SP ")
                .where("ifnull(SP.DES_ESTADO,'') <> ''")
                .orderBy("SP.DES_ESTADO ASC ");

        query = selectQueryUtil.build();
        log.info("consultarEstado: {}", query);

        return query;
    }

}
