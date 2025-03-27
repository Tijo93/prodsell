package com.ss.prodsel.service.impl;

import com.ss.prodsel.dto.request.SaleRequestDTO;
import com.ss.prodsel.entity.Sale;
import com.ss.prodsel.repository.ProductRepository;
import com.ss.prodsel.repository.SaleRepository;
import com.ss.prodsel.service.SaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {
  private final SaleRepository saleRepository;
  private final ProductRepository productRepository;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public SaleServiceImpl(final SaleRepository saleRepository, final ProductRepository productRepository) {
    this.saleRepository = saleRepository;
    this.productRepository = productRepository;
  }

  /**
   * @param saleRequest
   */
  @Override
  public void addSales(final List<SaleRequestDTO> saleRequest) {
    List<Sale> sales = new ArrayList<>(saleRequest.size());
    Sale sale;
    for (final var saleRequestDTO : saleRequest) {
      sale = new Sale();
      sale.setProduct(productRepository.getReferenceById(saleRequestDTO.productId()));
      sale.setQuantity(saleRequestDTO.quantity());
      sale.setSaleDate(saleRequestDTO.saleDate());
      sales.add(sale);
    }
    saleRepository.saveAll(sales);
  }
}
