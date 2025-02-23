package com.figuamba.prueba.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.figuamba.prueba.dto.MovimientoRequest;
import com.figuamba.prueba.model.Movimiento;
import com.figuamba.prueba.services.MovimientoService;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {
	@Autowired
	private MovimientoService movimientoService;

	@GetMapping
	public List<Movimiento> getAllMovimientos() {
		return movimientoService.listarMovimientos();
	}

	@GetMapping("/filtrar")
	public List<Movimiento> getAllMovimientosFecha(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
		return movimientoService.listarMovimientoPorFechas(fechaInicio, fechaFin);
	}

	@PostMapping
	public Movimiento createMovimiento(@RequestBody MovimientoRequest movimiento) {

		return movimientoService.createMovimiento(movimiento);
	}

}