package com.ss.prodsel.service;

import com.ss.prodsel.dto.request.SaleRequestDTO;

import java.util.List;

public interface SaleService {
  void addSales(List<SaleRequestDTO> saleRequest);
}
