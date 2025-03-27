package com.ss.prodsel.service.impl;

import com.ss.prodsel.dto.request.ProductAddDTO;
import com.ss.prodsel.dto.request.ProductUpdateDTO;
import com.ss.prodsel.dto.response.ProductResponseDTO;
import com.ss.prodsel.dto.response.ProductStatistics;
import com.ss.prodsel.exception.DuplicateProductException;
import com.ss.prodsel.exception.ProductNotFoundException;
import com.ss.prodsel.mapper.ProductMapper;
import com.ss.prodsel.repository.ProductRepository;
import com.ss.prodsel.service.ProductService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
  private static final String PRODUCT_NOT_FOUND = "Product with the given ID not found.";
  private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  public ProductServiceImpl(final ProductRepository productRepository, final ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  /**
   * Add a new product
   *
   * @param productAddDTO ProductAddDTO object
   */
  @Override
  @Transactional
  public void addProduct(final ProductAddDTO productAddDTO) {
    LOGGER.info("Adding product: {}", productAddDTO.name());
    if (productRepository.existsByNameIgnoreCase(productAddDTO.name())) {
      throw new DuplicateProductException("Product already exists");
    }
    productRepository.save(productMapper.toEntity(productAddDTO));
  }

  /**
   * Get a product by ID
   *
   * @param productId ID of the product to get
   * @return ProductResponseDTO
   */
  @Override
  public ProductResponseDTO getProductById(final int productId) {
    LOGGER.info("Retrieving product with ID: {}", productId);
    return productRepository.findById(productId)
      .map(productMapper::toResponse)
      .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
  }

  /**
   * Get all products
   *
   * @param pageable Pageable object
   * @return List of products
   */
  @Override
  public List<ProductResponseDTO> getAllProducts(final Pageable pageable) {
    LOGGER.info("Retrieving all products");
    return productMapper.toResponseList(productRepository.findAll(pageable).getContent());
  }

  /**
   * Updates an existing product.
   *
   * @param productId the ID of the product to update
   * @param updateDTO the data transfer object containing the updated product information
   */
  @Override
  @Transactional
  public void updateProduct(final int productId, final ProductUpdateDTO updateDTO) {
    final var product = productRepository.findById(productId)
      .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    productRepository.save(productMapper.toUpdatedEntity(product, updateDTO));
    LOGGER.info("Product with ID: {} updated", productId);
  }

  /**
   * Delete a product
   *
   * @param productId ID of the product to delete
   */
  @Override
  @Transactional
  public void deleteProduct(final int productId) {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
    }
    productRepository.deleteById(productId);
    LOGGER.info("Product with ID: {} deleted", productId);
  }

  /**
   * Get total revenue for all products
   *
   * @return total revenue
   */
  @Override
  public double getTotalRevenue() {
    LOGGER.info("Calculating total revenue");
    return productRepository.totalRevenue();
  }

  /**
   * Calculate revenue for a product
   *
   * @param productId ID of the product to get revenue for
   * @return Revenue for the product
   */
  @Override
  public double getRevenueByProduct(final int productId) {
    LOGGER.info("Calculating revenue for product with ID: {}", productId);
    return productRepository.totalRevenueForProduct(productId);
  }

  /**
   * Export products as PDF
   *
   * @return Resource containing the PDF file
   * @throws FileNotFoundException if the file is not found
   * @throws JRException           if an error occurs during JasperReports processing
   */
  @Override
  public Resource exportProductsAsPdf() throws FileNotFoundException, JRException {
    final List<ProductStatistics> products = productRepository.getProductStatistics();
    final var file = ResourceUtils.getFile("classpath:jasper/products2.jrxml");
    final JasperReport report = JasperCompileManager.compileReport(file.getAbsolutePath());
    JRSaver.saveObject(report, "ProductReport.jasper");
    final Map<String, Object> params = Map.of("ReportTitle", "Products Report");
    final var jasperPrint = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(products));
    return new ByteArrayResource(JasperExportManager.exportReportToPdf(jasperPrint));
  }
}
