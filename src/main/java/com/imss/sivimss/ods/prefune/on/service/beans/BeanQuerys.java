package com.imss.sivimss.ods.prefune.on.service.beans;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;



@Service
public class BeanQuerys {

	public String queryGetArticulos() {
		return "SELECT DISTINCT "
				+ "    SA.ID_ARTICULO AS idArticulo, "
				+ "    SA.REF_ARTICULO AS nombreArticulo "
				+ "FROM "
				+ "    SVT_ARTICULO SA "
				+ "INNER JOIN SVT_INVENTARIO_ARTICULO STI ON "
				+ "    SA.ID_ARTICULO = STI.ID_ARTICULO AND STI.IND_ESTATUS NOT IN(2, 3) AND STI.ID_INVE_ARTICULO NOT IN( "
				+ "    SELECT "
				+ "        IFNULL(STP.ID_INVE_ARTICULO, 0) "
				+ "    FROM "
				+ "        SVC_DETALLE_CARAC_PRESUP_TEMP STP "
				+ "    WHERE "
				+ "        STP.IND_ACTIVO = 1 AND DATE_FORMAT(STP.TIM_ALTA, 'YY-MM-DD') = DATE_FORMAT(CURRENT_TIMESTAMP(), 'YY-MM-DD') AND TIMESTAMPDIFF( "
				+ "            MINUTE, "
				+ "            DATE_ADD(STP.TIM_ALTA, INTERVAL 4 HOUR), "
				+ "            CURRENT_TIMESTAMP()) <= 0 "
				+ "        ) AND STI.ID_INVE_ARTICULO NOT IN( "
				+ "        SELECT "
				+ "            IFNULL(SDCP.ID_INVE_ARTICULO, 0) "
				+ "        FROM "
				+ "            SVC_DETALLE_CARAC_PRESUP SDCP "
				+ "        WHERE "
				+ "            SDCP.IND_ACTIVO = 1 "
				+ "    ) "
				+ "INNER JOIN SVT_ORDEN_ENTRADA SOE2 ON "
				+ "    SOE2.ID_ODE = STI.ID_ODE "
				+ "INNER JOIN SVT_CONTRATO SC ON "
				+ "    SC.ID_CONTRATO = SOE2.ID_CONTRATO "
				+ "INNER JOIN SVT_CONTRATO_ARTICULOS SCA ON "
				+ "    SCA.ID_CONTRATO = SC.ID_CONTRATO AND STI.ID_ARTICULO = SCA.ID_ARTICULO "
				+ "WHERE "
				+ "    STI.ID_TIPO_ASIGNACION_ART = 1 AND SA.ID_CATEGORIA_ARTICULO = 1 AND STI.ID_VELATORIO = 1 "
				+ "UNION "
				+ "SELECT DISTINCT "
				+ "    SA.ID_ARTICULO AS idArticulo, "
				+ "    SA.REF_ARTICULO AS nombreArticulo "
				+ "FROM "
				+ "    SVT_ARTICULO SA "
				+ "INNER JOIN SVT_INVENTARIO_ARTICULO STI ON "
				+ "    SA.ID_ARTICULO = STI.ID_ARTICULO AND STI.IND_ESTATUS NOT IN(2, 3) AND STI.ID_INVE_ARTICULO NOT IN( "
				+ "    SELECT "
				+ "        IFNULL(STP.ID_INVE_ARTICULO, 0) "
				+ "    FROM "
				+ "        SVC_DETALLE_CARAC_PRESUP_TEMP STP "
				+ "    WHERE "
				+ "        STP.IND_ACTIVO = 1 AND DATE_FORMAT(STP.TIM_ALTA, 'YY-MM-DD') = DATE_FORMAT(CURRENT_TIMESTAMP(), 'YY-MM-DD') AND TIMESTAMPDIFF( "
				+ "            MINUTE, "
				+ "            DATE_ADD(STP.TIM_ALTA, INTERVAL 4 HOUR), "
				+ "            CURRENT_TIMESTAMP()) <= 0 "
				+ "        ) AND STI.ID_INVE_ARTICULO IN( "
				+ "        SELECT "
				+ "            IFNULL(SDCP.ID_INVE_ARTICULO, 0) "
				+ "        FROM "
				+ "            SVC_DETALLE_CARAC_PRESUP SDCP "
				+ "        INNER JOIN SVC_ATAUDES_DONADOS STA ON "
				+ "            STA.ID_INVE_ARTICULO = SDCP.ID_INVE_ARTICULO "
				+ "        INNER JOIN SVT_INVENTARIO_ARTICULO STAI ON "
				+ "            STAI.ID_INVE_ARTICULO = SDCP.ID_INVE_ARTICULO "
				+ "        WHERE "
				+ "            SDCP.IND_ACTIVO = 1 AND STAI.ID_TIPO_ASIGNACION_ART = 1 "
				+ "    ) "
				+ "INNER JOIN SVT_ORDEN_ENTRADA SOE2 ON "
				+ "    SOE2.ID_ODE = STI.ID_ODE "
				+ "INNER JOIN SVT_CONTRATO SC ON "
				+ "    SC.ID_CONTRATO = SOE2.ID_CONTRATO "
				+ "INNER JOIN SVT_CONTRATO_ARTICULOS SCA ON "
				+ "    SCA.ID_CONTRATO = SC.ID_CONTRATO AND STI.ID_ARTICULO = SCA.ID_ARTICULO "
				+ "WHERE "
				+ "    SA.ID_CATEGORIA_ARTICULO = 1 AND STI.ID_VELATORIO = 1";
		
	}

	public String queryTodosArticulos() {
		return "SELECT "
				+ "    SA.ID_ARTICULO AS idArticulo, "
				+ "    SA.REF_ARTICULO AS nombreArticulo "
				+ "FROM "
				+ "    SVT_ARTICULO SA "
				;
	}
	
}
