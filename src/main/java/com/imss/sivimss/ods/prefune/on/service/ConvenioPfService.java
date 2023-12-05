package com.imss.sivimss.ods.prefune.on.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.imss.sivimss.ods.prefune.on.model.request.ActualizarBeneficiarioDTO;
import com.imss.sivimss.ods.prefune.on.model.request.AgregarBeneficiarioDTO;
import com.imss.sivimss.ods.prefune.on.model.request.Paginado;
import com.imss.sivimss.ods.prefune.on.model.request.PdfDto;
import com.imss.sivimss.ods.prefune.on.utils.Response;

public interface ConvenioPfService {

	public Response<Object> consultaMiConvenio(Paginado paginado, Integer idContratante, Authentication authentication)
			throws IOException;

	public Response<Object> renovarConvenio(String idConvenio, Authentication authentication);

	public Response<Object> consultaDetalleConvenio(Integer idConvenio, Authentication authentication)
			throws IOException;

	public Response<Object> generarPDF(PdfDto request, Authentication authentication) throws IOException;

	public Response<Object> actualizarBeneficiario(ActualizarBeneficiarioDTO request, Authentication authentication);

	public Response<Object>consultarCurpRfc(@RequestBody JsonNode datos,Authentication authentication) throws IOException;


	public Response<Object> altaBeneficiario(AgregarBeneficiarioDTO request, Authentication authentication)
			throws IOException;

}
