package com.imss.sivimss.arquetipo.model.request;

import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class Paginado {
	
	@Min(value=0,message = "Pagina Minima 0")
	private Integer pagina;
	
	@Min(value=1,message = "Tamanio Minimo 1")
	private Integer tamanio;
	
}
