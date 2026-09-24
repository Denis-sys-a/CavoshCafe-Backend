package com.cavosh.api_cafe.modules.productos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.ArrayList;
import com.cavosh.api_cafe.modules.productos.domain.exception.OpcionProductoInvalidaException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagenUrl;
    private Boolean disponible;
    private Categoria categoria;

    @Builder.Default
    private List<OpcionProducto> opciones = new ArrayList<>();

    public BigDecimal calcularPrecioUnitario(Set<Long> opcionesValoresIds) {
        BigDecimal precioBase = precio != null ? precio : BigDecimal.ZERO;

        BigDecimal adicionalPorOpciones = obtenerOpcionesSeleccionadas(opcionesValoresIds).stream()
                .map(OpcionProducto::getPrecioAdicional)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return precioBase.add(adicionalPorOpciones);
    }

    public List<OpcionProducto> obtenerOpcionesSeleccionadas(Set<Long> opcionesValoresIds) {
        if (opcionesValoresIds == null || opcionesValoresIds.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, OpcionProducto> disponiblesPorId = opciones.stream()
                .collect(Collectors.toMap(OpcionProducto::getId, Function.identity()));

        List<Long> idsInvalidos = opcionesValoresIds.stream()
                .filter(id -> !disponiblesPorId.containsKey(id))
                .toList();

        if (!idsInvalidos.isEmpty()) {
            throw new OpcionProductoInvalidaException(
                    "Las opciones " + idsInvalidos + " no pertenecen al producto '" + nombre + "'");
        }

        return opcionesValoresIds.stream()
                .map(disponiblesPorId::get)
                .toList();
    }
}