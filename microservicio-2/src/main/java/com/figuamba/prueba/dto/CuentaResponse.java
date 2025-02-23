package com.figuamba.prueba.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.figuamba.prueba.model.Cuenta;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class CuentaResponse {

	@JsonProperty("cuenta(s)")
	private Cuenta cuenta;

	private String mensaje;

	@Data
	public static class Cuenta {
		private Long id;
		private String numeroCuenta;
		private String tipoCuenta;
		private BigDecimal saldoInicial;
		private Boolean estado;
		private Long clienteId;
		
	}
}