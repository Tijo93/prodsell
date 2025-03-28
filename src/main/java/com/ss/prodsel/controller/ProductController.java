package com.ss.prodsel.controller;

import com.ss.prodsel.config.security.IsAdmin;
import com.ss.prodsel.dto.request.ProductAddDTO;
import com.ss.prodsel.dto.request.ProductUpdateDTO;
import com.ss.prodsel.dto.response.ProductResponseDTO;
import com.ss.prodsel.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static com.ss.prodsel.controller.util.ValidationErrorCodes.nonPositiveProductID;

@RestController
@RequestMapping("/api/products")
@Validated
@Tag(name = "Products")
public class ProductController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
  private final ProductService productService;

  public ProductController(final ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void addProduct(@Valid @RequestBody final ProductAddDTO dto) {
    LOGGER.info("Adding product: {}", dto.name());
    productService.addProduct(dto);
  }

  @GetMapping
  @Operation(summary = "Retrieve all products")
  public List<ProductResponseDTO> getProducts(@PageableDefault final Pageable pageable) {
    LOGGER.info("Retrieving all products");
    return productService.getAllProducts(pageable);
  }

  @GetMapping("/{productId}")
  public ProductResponseDTO getProduct(@Min(value = 1, message = nonPositiveProductID) @PathVariable final int productId) {
    return productService.getProductById(productId);
  }

  @PutMapping("/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @IsAdmin
  public void updateProduct(@Min(value = 1, message = nonPositiveProductID) @PathVariable final int productId,
                            @Valid @RequestBody final ProductUpdateDTO updateDTO) {
    productService.updateProduct(productId, updateDTO);
  }

  @DeleteMapping("/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @IsAdmin
  public void deleteProduct(@Min(value = 1, message = nonPositiveProductID) @PathVariable final int productId) {
    productService.deleteProduct(productId);
  }

  @GetMapping("/revenue")
  public double getTotalRevenue() {
    return productService.getTotalRevenue();
  }

  @GetMapping("/revenue/{productId}")
  public double getProductRevenue(@Min(value = 1, message = nonPositiveProductID) @PathVariable final int productId) {
    return productService.getRevenueByProduct(productId);
  }

  @GetMapping(value = "/export", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<Resource> downloadPDF() throws JRException, IOException {
    final var resource = productService.exportProductsAsPdf();
    return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_PDF)
      .contentLength(resource.contentLength())
      .header(HttpHeaders.CONTENT_DISPOSITION,
        ContentDisposition.attachment().filename("product-report.pdf").build().toString())
      .body(resource);
  }
}
