package com.figuamba.prueba.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.figuamba.prueba.dto.MovimientoRequest;
import com.figuamba.prueba.exception.EntityNotFoundException;
import com.figuamba.prueba.exception.InsufficientBalanceException;
import com.figuamba.prueba.model.Cuenta;
import com.figuamba.prueba.model.Movimiento;
import com.figuamba.prueba.repository.CuentaRepository;
import com.figuamba.prueba.repository.MovimientoRepository;
import com.figuamba.prueba.utils.EnumMensajes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoService {

	private static String MOVIMIENTO_DEBITO = "DEBITO";

	@Autowired
	private MovimientoRepository movimientoRepository;

	@Autowired
	private CuentaRepository cuentaRepository;

	public List<Movimiento> listarMovimientos() {
		return movimientoRepository.findAll();
	}

	public List<Movimiento> listarMovimientoPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
		return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin);
	}

	public Movimiento obtenerMovimiento(Long id) {
		return movimientoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(EnumMensajes.REGISTRO_NO_ENCONTRADO.getMensaje()));
	}

	@Transactional
	public Movimiento guardarMovimiento(Movimiento Movimiento) {
		try {
			return movimientoRepository.save(Movimiento);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public Movimiento actualizarMovimiento(Movimiento Movimiento) {
		if (movimientoRepository.existsById(Movimiento.getId())) {
			return movimientoRepository.save(Movimiento);
		} else {
			throw new EntityNotFoundException(EnumMensajes.REGISTRO_NO_ENCONTRADO.getMensaje() + Movimiento.getId());
		}
	}

	public void eliminarMovimiento(Long id) {
		movimientoRepository.deleteById(id);
	}

	@Transactional
	public Movimiento createMovimiento(MovimientoRequest movimientoReq) throws InsufficientBalanceException {
		Cuenta cuenta = cuentaRepository.findById(movimientoReq.getCuenta_id())
				.orElseThrow(() -> new RuntimeException(EnumMensajes.CUENTA_NO_ENCONTRADA.getMensaje()));

		 Movimiento ultimoMovimiento = movimientoRepository.findFirstByCuentaOrderByIdDesc(cuenta);
		 BigDecimal saldoAnterior = (ultimoMovimiento != null) ? ultimoMovimiento.getSaldo() : cuenta.getSaldoInicial();

		if (movimientoReq.getTipoMovimiento().equalsIgnoreCase(MOVIMIENTO_DEBITO))
			movimientoReq.setValor(movimientoReq.getValor().negate());

		BigDecimal nuevoSaldo = saldoAnterior.add(movimientoReq.getValor());
		nuevoSaldo = nuevoSaldo.setScale(2, RoundingMode.HALF_UP);
		if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
			throw new InsufficientBalanceException(EnumMensajes.SALDO_NO_DISPONIBLE.getMensaje());
		}

		cuentaRepository.save(cuenta);

		Movimiento movimientoNuevo = new Movimiento();
		movimientoNuevo.setFecha(LocalDate.now());
		movimientoNuevo.setTipoMovimiento(movimientoReq.getTipoMovimiento());
		movimientoNuevo.setValor(movimientoReq.getValor());
		movimientoNuevo.setSaldo(nuevoSaldo);
		movimientoNuevo.setCuenta(cuenta);
		return movimientoRepository.save(movimientoNuevo);
	}

}