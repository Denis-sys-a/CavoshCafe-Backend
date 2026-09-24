package com.cavosh.api_cafe.modules.promociones.application.usecase;

import com.cavosh.api_cafe.modules.promociones.domain.exception.CuponInvalidoException;
import com.cavosh.api_cafe.modules.promociones.domain.exception.PromocionNoEncontradaException;
import com.cavosh.api_cafe.modules.promociones.domain.model.Promocion;
import com.cavosh.api_cafe.modules.promociones.domain.repository.PromocionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Valida y aplica códigos promocionales, respetando:
 * - Vigencia (activo, fechaInicio/fechaFin)
 * - Límite global de usos (uso_maximo / usos_actuales)
 * - Límite de usos por usuario (uso_maximo_por_usuario)
 */
@Service
@RequiredArgsConstructor
public class ValidarCuponUseCase {

    private final PromocionRepository promocionRepository;

    /**
     * Valida que un código promocional pueda usarse por un usuario determinado.
     * No modifica el contador de usos: eso ocurre recién al concretar la compra,
     * ver {@link #registrarUso(Promocion)}.
     *
     * @param codigo                  código ingresado por el cliente
     * @param usosPreviosDelUsuario   cantidad de veces que ESE usuario ya usó este código
     *                                (calculado por el módulo de pedidos a partir de su historial)
     * @return la {@link Promocion} válida y lista para aplicarse
     */
    public Promocion validarCupon(String codigo, int usosPreviosDelUsuario) {
        Promocion promocion = promocionRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> new PromocionNoEncontradaException(
                        "No existe un código promocional con el valor: " + codigo));

        if (!promocion.esVálida()) {
            throw new CuponInvalidoException(
                    "El código promocional '" + codigo + "' está inactivo o fuera de su periodo de vigencia");
        }

        if (!promocion.tieneCupoGlobalDisponible()) {
            throw new CuponInvalidoException(
                    "El código promocional '" + codigo + "' alcanzó su límite máximo de usos");
        }

        if (!promocion.puedeUsarUsuario(usosPreviosDelUsuario)) {
            throw new CuponInvalidoException(
                    "Ya alcanzaste el límite de usos permitidos para el código promocional '" + codigo + "'");
        }

        return promocion;
    }

    /**
     * Incrementa el contador global de usos del cupón. Debe invocarse una única vez,
     * al concretar la compra (después de crear/confirmar el pedido), nunca durante
     * la sola validación.
     */
    public Promocion registrarUso(Promocion promocion) {
        promocion.setUsosActuales(promocion.getUsosActuales() + 1);
        return promocionRepository.guardar(promocion);
    }
}
