package com.ss.prodsel.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record SaleRequestDTO(
  @NotNull(message = "Product ID is mandatory")
  @Positive(message = "Product ID should a positive value") Integer productId,
  @NotNull(message = "Quantity is mandatory")
  @Positive(message = "Quantity should a positive value") Integer quantity,
  @NotNull(message = "Sale date is mandatory")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  LocalDate saleDate) {
}
