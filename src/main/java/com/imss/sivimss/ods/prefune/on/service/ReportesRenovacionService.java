package com.imss.sivimss.ods.prefune.on.service;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ods.prefune.on.model.request.PdfDto;
import com.imss.sivimss.ods.prefune.on.utils.Response;

public interface ReportesRenovacionService {

	Response<Object> generarDoc(PdfDto pdfDto, Authentication authentication) throws Throwable;

}
