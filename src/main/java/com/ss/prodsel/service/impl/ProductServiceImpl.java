package com.ss.prodsel.service.impl;

import com.ss.prodsel.dto.request.ProductAddDTO;
import com.ss.prodsel.dto.request.ProductUpdateDTO;
import com.ss.prodsel.dto.response.ProductResponseDTO;
import com.ss.prodsel.exception.DuplicateProductException;
import com.ss.prodsel.exception.ProductNotFoundException;
import com.ss.prodsel.mapper.ProductMapper;
import com.ss.prodsel.repository.ProductRepository;
import com.ss.prodsel.service.ProductService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.util.JRSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
  private static final String PRODUCT_NOT_FOUND = "Product with the given ID not found.";
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  public ProductServiceImpl(final ProductRepository productRepository, final ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  /**
   * @param product
   */
  @Override
  @Transactional
  public void addProduct(final ProductAddDTO product) {
    if (productRepository.existsByNameIgnoreCase(product.name())) {
      throw new DuplicateProductException("Product already exists");
    }
    productRepository.save(productMapper.toEntity(product));
  }

  /**
   * @param productId
   * @return
   */
  @Override
  public ProductResponseDTO getProductById(final int productId) {
    return productRepository.findById(productId)
      .map(productMapper::toResponse)
      .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
  }

  /**
   * @param pageable
   * @return
   */
  @Override
  public List<ProductResponseDTO> getAllProducts(final Pageable pageable) {
    return productMapper.toResponseList(productRepository.findAll(pageable).getContent());
  }

  /**
   * @param productId
   * @param updateDTO
   */
  @Override
  @Transactional
  public void updateProduct(final int productId, final ProductUpdateDTO updateDTO) {
    final var product = productRepository.findById(productId)
      .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    productRepository.save(productMapper.toUpdatedEntity(product, updateDTO));
  }

  /**
   * @param productId
   */
  @Override
  @Transactional
  public void deleteProduct(final int productId) {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
    }
    productRepository.deleteById(productId);
  }

  /**
   * @return
   */
  @Override
  public double getTotalRevenue() {
    return productRepository.totalRevenue();
  }

  /**
   * @param productId
   * @return
   */
  @Override
  public double getRevenueByProduct(final int productId) {
    return productRepository.totalRevenueForProduct(productId);
  }

  /**
   * @return
   */
  @Override
  public Resource exportProductsAsPdf() throws FileNotFoundException, JRException {
    final File file = ResourceUtils.getFile("classpath:jasper/products.jrxml");
    final var report = JasperCompileManager.compileReport(file.getAbsolutePath());
    JRSaver.saveObject(report, "ProductReport.jasper");
    final var jasperPrint = JasperFillManager.fillReport(report, null, new JREmptyDataSource());
    return new ByteArrayResource(JasperExportManager.exportReportToPdf(jasperPrint));
  }
}
