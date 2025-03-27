package com.ss.prodsel.service;

import com.ss.prodsel.dto.request.ProductAddDTO;
import com.ss.prodsel.dto.request.ProductUpdateDTO;
import com.ss.prodsel.dto.response.ProductResponseDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.util.List;

public interface ProductService {

  void addProduct(ProductAddDTO product);

  ProductResponseDTO getProductById(int productId);

  List<ProductResponseDTO> getAllProducts(Pageable pageable);

  void updateProduct(int productId, ProductUpdateDTO updateDTO);

  void deleteProduct(int productId);

  double getTotalRevenue();

  double getRevenueByProduct(int productId);

  Resource exportProductsAsPdf() throws FileNotFoundException, JRException;
}
