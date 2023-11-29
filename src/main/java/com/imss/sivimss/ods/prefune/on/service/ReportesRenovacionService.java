package com.imss.sivimss.ods.prefune.on.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ods.prefune.on.model.request.PdfDto;
import com.imss.sivimss.ods.prefune.on.utils.Response;

public interface ReportesRenovacionService {

	Response<Object> generarDoc(PdfDto pdfDto, Authentication authentication) throws Throwable;

	Response<?> generarHojaAfilicion(PdfDto pdfDto, Authentication authentication) throws Throwable;

	Response<?> generarDocPlanAnterior(PdfDto pdfDto, Authentication authentication) throws IOException;

}
