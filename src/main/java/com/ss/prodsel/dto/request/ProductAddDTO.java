package com.ss.prodsel.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductAddDTO(@NotBlank(message = "Product name should be given") String name,
                            String description,
                            @NotNull(message = "Price is mandatory")
                            @Min(value = 0, message = "Price should a positive value") Double price,
                            @NotNull(message = "Quantity is mandatory")
                            @Min(value = 0, message = "Quantity should a positive value") Integer quantity) {
}
