package com.imss.sivimss.ods.prefune.on.utils;

public class MensajeResponseUtil {


	private MensajeResponseUtil() {
		super();
	}

	public static Response<Object> mensajeResponseExterno(Response<Object> respuestaGenerado, String numeroMensaje,
			String numeroMensajeError) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 400) {
			respuestaGenerado.setCodigo(200);
			respuestaGenerado.setMensaje(numeroMensaje);
		} else if (codigo == 404 || codigo == 500) {
			respuestaGenerado.setMensaje(numeroMensajeError);
		}
		return respuestaGenerado;
	}
}
