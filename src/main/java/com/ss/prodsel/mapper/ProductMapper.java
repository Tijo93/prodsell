package com.ss.prodsel.mapper;

import com.ss.prodsel.dto.request.ProductAddDTO;
import com.ss.prodsel.dto.request.ProductUpdateDTO;
import com.ss.prodsel.dto.response.ProductResponseDTO;
import com.ss.prodsel.entity.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(target = "id", ignore = true)
  Product toEntity(ProductAddDTO newProductData);

  @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
  List<ProductResponseDTO> toResponseList(List<Product> products);

  @Mapping(target = "id", ignore = true)
  Product toUpdatedEntity(@MappingTarget Product product, ProductUpdateDTO updateDTO);

  ProductResponseDTO toResponse(Product product);
}
