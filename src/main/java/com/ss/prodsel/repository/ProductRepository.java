package com.ss.prodsel.repository;

import com.ss.prodsel.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  boolean existsByNameIgnoreCase(final String name);

  @Query(value = """
    SELECT COALESCE(SUM(s.quantity * p.price), 0) AS total_revenue
    FROM product p JOIN sale s ON s.product_id = p.id;
    """, nativeQuery = true)
  double totalRevenue();

  @Query(value = """
    SELECT COALESCE(SUM(s.quantity * p.price), 0) AS total_revenue
    FROM product p
    JOIN sale s ON s.product_id = p.id
    WHERE p.id = :productId
    """, nativeQuery = true)
  double totalRevenueForProduct(final @Param("productId") int productId);

  @Query(value = """
    SELECT p.id, p.name, p.description, p.quantity, p.price, coalesce(p.price*s.quantity, 0) AS revenue FROM product p LEFT JOIN sale s ON p.id=s.product_id
    """, nativeQuery = true)
  List<Object[]> getProductStatistics();
}
