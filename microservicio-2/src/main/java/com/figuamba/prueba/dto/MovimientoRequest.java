package com.figuamba.prueba.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoRequest {

	private String tipoMovimiento;

	private BigDecimal valor;

	private Long cuenta_id;
}
