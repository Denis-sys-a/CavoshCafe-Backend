package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.mappers;

import com.cavosh.api_cafe.modules.pedidos.domain.model.DetallePedido;
import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.entities.DetallePedidoEntity;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.entities.PedidoEntity;
import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import com.cavosh.api_cafe.modules.promociones.domain.model.Promocion;
import com.cavosh.api_cafe.modules.promociones.infrastructure.adapters.out.persistence.entities.CodigoPromocionalEntity;
import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;
import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.out.persistence.entities.SucursalEntity;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Direccion;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities.DireccionEntity;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Conversión entre el dominio {@link Pedido} y sus entidades JPA.
 *
 * <p>
 * Escritura: las relaciones (usuario, sucursal, dirección, promoción) se
 * enlazan con
 * {@code EntityManager#getReference}, es decir, solo con su ID: no hay SELECT
 * adicional
 * ni se modifican esas entidades.
 *
 * <p>
 * Lectura: usuario, sucursal, dirección y promoción se convierten a
 * proyecciones de
 * dominio de solo lectura. Nunca se expone la contraseña del usuario.
 *
 * <p>
 * Errores de datos incompletos se reportan como
 * {@link IllegalArgumentException} con un
 * mensaje explícito, en lugar de dejar que fallen como violaciones de
 * restricción en BD.
 */
@Component
public class PedidoPersistenceMapper {

    private static final String PREFIJO_NUMERO = "CVS-";
    private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LONGITUD_SUFIJO = 6;
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final SecureRandom RANDOM = new SecureRandom();

    @PersistenceContext
    private EntityManager entityManager;

    // ------------------------------------------------------------------
    // Dominio -> entidad
    // ------------------------------------------------------------------

    /**
     * Vuelca el dominio sobre la entidad (nueva o ya persistida). El número de
     * pedido se
     * asigna una sola vez, al crear; subtotal y descuento se derivan de los
     * detalles.
     */
    public void aplicarDominio(PedidoEntity entity, Pedido pedido) {
        if (entity.getNumeroPedido() == null) {
            entity.setNumeroPedido(generarNumeroPedido());
        }

        Long usuarioId = idPersistido(pedido.getUsuario() == null ? null : pedido.getUsuario().getId(),
                "El pedido requiere un usuario ya registrado");
        Long sucursalId = idPersistido(pedido.getSucursal() == null ? null : pedido.getSucursal().getId(),
                "El pedido requiere una sucursal ya registrada");
        if (pedido.getMetodoEntrega() == null) {
            throw new IllegalArgumentException("El pedido requiere un método de entrega");
        }

        entity.setUsuario(entityManager.getReference(UsuarioEntity.class, usuarioId));
        entity.setSucursal(entityManager.getReference(SucursalEntity.class, sucursalId));
        entity.setDireccion(pedido.getDireccion() == null ? null
                : entityManager.getReference(DireccionEntity.class, idPersistido(pedido.getDireccion().getId(),
                        "La dirección del pedido debe estar ya registrada")));
        entity.setCodigoPromocional(pedido.getCodigoPromocional() == null ? null
                : entityManager.getReference(CodigoPromocionalEntity.class,
                        idPersistido(pedido.getCodigoPromocional().getId(),
                                "El código promocional del pedido debe estar ya registrado")));
        entity.setMetodoEntrega(pedido.getMetodoEntrega());
        if (pedido.getEstado() != null) {
            entity.setEstado(pedido.getEstado());
        }

        List<DetallePedidoEntity> detalles = sincronizarDetalles(entity, pedido.getDetalles());

        BigDecimal subtotal = detalles.stream()
                .map(DetallePedidoEntity::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = pedido.getTotal() != null ? dinero(pedido.getTotal()) : subtotal;
        entity.setSubtotal(subtotal);
        entity.setTotal(total);
        entity.setDescuento(subtotal.subtract(total).max(BigDecimal.ZERO));
    }

    /**
     * Reutiliza los detalles ya persistidos (mismo id) para actualizarlos, crea los
     * nuevos y
     * deja fuera —para que orphanRemoval los elimine— los que ya no vengan en el
     * dominio.
     */
    private List<DetallePedidoEntity> sincronizarDetalles(PedidoEntity entity, List<DetallePedido> detalles) {
        Map<Long, DetallePedidoEntity> existentes = new HashMap<>();
        for (DetallePedidoEntity existente : entity.getDetalles()) {
            if (existente.getId() != null) {
                existentes.put(existente.getId(), existente);
            }
        }

        List<DetallePedidoEntity> resultado = new ArrayList<>();
        if (detalles != null) {
            for (DetallePedido detalle : detalles) {
                DetallePedidoEntity destino = detalle.getId() == null ? null : existentes.get(detalle.getId());
                if (destino == null) {
                    destino = new DetallePedidoEntity();
                }
                aplicarDetalle(destino, detalle);
                resultado.add(destino);
            }
        }
        entity.reemplazarDetalles(resultado);
        return resultado;
    }

    private void aplicarDetalle(DetallePedidoEntity entity, DetallePedido detalle) {
        Producto producto = detalle.getProducto();
        if (producto == null || producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("Cada detalle del pedido requiere un producto con nombre");
        }
        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad del producto '" + producto.getNombre() + "' debe ser mayor a cero");
        }
        BigDecimal precioUnitario = detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario()
                : producto.getPrecio();
        if (precioUnitario == null) {
            throw new IllegalArgumentException(
                    "El producto '" + producto.getNombre() + "' no tiene precio unitario");
        }
        BigDecimal subtotal = detalle.getSubtotal() != null ? detalle.getSubtotal()
                : precioUnitario.multiply(BigDecimal.valueOf(detalle.getCantidad()));

        entity.setProductoId(producto.getId());
        entity.setNombreProducto(producto.getNombre());
        entity.setCantidad(detalle.getCantidad());
        entity.setPrecioUnitario(dinero(precioUnitario));
        entity.setSubtotal(dinero(subtotal));
    }

    // ------------------------------------------------------------------
    // Entidad -> dominio
    // ------------------------------------------------------------------

    public Pedido aDominio(PedidoEntity entity) {
        // El pago (tabla 'pagos') lo gestiona el módulo de pagos: aquí no se carga.
        return Pedido.builder()
                .id(entity.getId())
                .numeroPedido(entity.getNumeroPedido())
                .usuario(aUsuario(entity.getUsuario()))
                .sucursal(aSucursal(entity.getSucursal()))
                .direccion(aDireccion(entity.getDireccion()))
                .metodoEntrega(entity.getMetodoEntrega())
                .codigoPromocional(aPromocion(entity.getCodigoPromocional()))
                .estado(entity.getEstado())
                .total(entity.getTotal())
                .fechaCreacion(entity.getCreatedAt())
                .detalles(entity.getDetalles().stream().map(this::aDetalle).toList())
                .build();
    }

    private DetallePedido aDetalle(DetallePedidoEntity entity) {
        // Se reconstruye el producto desde la "foto" guardada en el pedido (puede no
        // existir ya en catálogo).
        Producto producto = Producto.builder()
                .id(entity.getProductoId())
                .nombre(entity.getNombreProducto())
                .precio(entity.getPrecioUnitario())
                .build();
        return DetallePedido.builder()
                .id(entity.getId())
                .producto(producto)
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .subtotal(entity.getSubtotal())
                .build();
    }

    /** Proyección segura: sin contraseña ni datos de autenticación. */
    private Usuario aUsuario(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        return Usuario.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getCorreo())
                .telefono(entity.getTelefono())
                .authProvider(entity.getAuthProvider())
                .isVerified(entity.isVerified())
                .build();
    }

    private Sucursal aSucursal(SucursalEntity entity) {
        if (entity == null) {
            return null;
        }
        return Sucursal.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .horarioAtencion(formatearHorario(entity.getHoraApertura(), entity.getHoraCierre()))
                .activa(entity.isActivo())
                .build();
    }

    private Direccion aDireccion(DireccionEntity entity) {
        if (entity == null) {
            return null;
        }
        return Direccion.builder()
                .id(entity.getId())
                .etiqueta(entity.getEtiqueta())
                // Filas anteriores a la migración no tienen calle: se usa el texto completo.
                .calle(entity.getCalle() != null ? entity.getCalle() : entity.getDireccion())
                .ciudad(entity.getCiudad())
                .distrito(entity.getDistrito())
                .referencia(entity.getReferencia())
                .esPrincipal(entity.isPredeterminada())
                .build();
    }

    private Promocion aPromocion(CodigoPromocionalEntity entity) {
        if (entity == null) {
            return null;
        }
        return Promocion.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .descuento(entity.getValorDescuento())
                .tipoDescuento(entity.getTipoDescuento())
                .fechaInicio(entity.getValidoDesde())
                .fechaFin(entity.getValidoHasta())
                .activo(entity.isActivo())
                .usoMaximo(entity.getUsoMaximo())
                .usosActuales(entity.getUsosActuales())
                .usoMaximoPorUsuario(entity.getUsoMaximoPorUsuario())
                .build();
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------

    private static Long idPersistido(Long id, String mensajeError) {
        if (id == null) {
            throw new IllegalArgumentException(mensajeError);
        }
        return id;
    }

    private static BigDecimal dinero(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private static String formatearHorario(LocalTime apertura, LocalTime cierre) {
        if (apertura == null || cierre == null) {
            return null;
        }
        return HORA.format(apertura) + " - " + HORA.format(cierre);
    }

    /**
     * Formato {@code CVS-yyyyMMdd-XXXXXX} (19 caracteres; la columna admite 20 y es
     * UNIQUE).
     */
    private static String generarNumeroPedido() {
        StringBuilder numero = new StringBuilder(PREFIJO_NUMERO)
                .append(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
                .append('-');
        for (int i = 0; i < LONGITUD_SUFIJO; i++) {
            numero.append(ALFABETO.charAt(RANDOM.nextInt(ALFABETO.length())));
        }
        return numero.toString();
    }
}
