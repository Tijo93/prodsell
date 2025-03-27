package com.ss.prodsel.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "sale")
public class Sale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @ManyToOne
  private Product product;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private LocalDate saleDate;

  public Sale(final Product product, final Integer quantity, final LocalDate saleDate) {
    this.product = product;
    this.quantity = quantity;
    this.saleDate = saleDate;
  }

  public Sale() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(final Product product) {
    this.product = product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(final Integer quantity) {
    this.quantity = quantity;
  }

  public LocalDate getSaleDate() {
    return saleDate;
  }

  public void setSaleDate(final LocalDate saleDate) {
    this.saleDate = saleDate;
  }
}
