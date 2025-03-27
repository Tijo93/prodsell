package com.ss.prodsel.controller;

import com.ss.prodsel.config.security.IsAdmin;
import com.ss.prodsel.dto.request.SaleRequestDTO;
import com.ss.prodsel.service.SaleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sales")
@IsAdmin
public class SaleController {
  private final SaleService saleService;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public SaleController(final SaleService saleService) {
    this.saleService = saleService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void addSales(@RequestBody @Valid List<SaleRequestDTO> salesData) {
    saleService.addSales(salesData);
  }
}
