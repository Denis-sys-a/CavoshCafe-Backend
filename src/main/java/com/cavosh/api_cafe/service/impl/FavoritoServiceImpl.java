package com.cavosh.api_cafe.service.impl;

import com.cavosh.api_cafe.dto.ProductResponseDTO;
import com.cavosh.api_cafe.entity.Favorito;
import com.cavosh.api_cafe.entity.Product;
import com.cavosh.api_cafe.entity.Usuario;
import com.cavosh.api_cafe.exception.DuplicateFavoriteException;
import com.cavosh.api_cafe.exception.ResourceNotFoundException;
import com.cavosh.api_cafe.repository.FavoritoRepository;
import com.cavosh.api_cafe.repository.ProductRepository;
import com.cavosh.api_cafe.repository.UsuarioRepository;
import com.cavosh.api_cafe.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void agregarAFavoritos(Long usuarioId, Long productoId) {
        if (favoritoRepository.existsByUsuarioIdAndProductoId(usuarioId, productoId)) {
            throw new DuplicateFavoriteException("Este producto ya está en tus favoritos");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        Product producto = productRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        Favorito favorito = Favorito.builder()
                .usuario(usuario)
                .producto(producto)
                .build();

        favoritoRepository.save(favorito);
    }

    @Override
    @Transactional
    public void eliminarDeFavoritos(Long usuarioId, Long productoId) {
        favoritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Este producto no está en tus favoritos"));

        favoritoRepository.deleteByUsuarioIdAndProductoId(usuarioId, productoId);
    }

    @Override
    public List<ProductResponseDTO> obtenerFavoritosPorUsuario(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId).stream()
                .map(favorito -> toProductResponseDTO(favorito.getProducto()))
                .collect(Collectors.toList());
    }

    private ProductResponseDTO toProductResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .basePrice(product.getBasePrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .tipoProducto(product.getProductType())
                .build();
    }
}
