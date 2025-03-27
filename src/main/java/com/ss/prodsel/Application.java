package com.ss.prodsel;

import com.ss.prodsel.entity.Product;
import com.ss.prodsel.entity.Sale;
import com.ss.prodsel.repository.ProductRepository;
import com.ss.prodsel.repository.SaleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
  private final ProductRepository productRepository;
  private final SaleRepository saleRepository;

  private Application(final ProductRepository productRepository, final SaleRepository saleRepository) {
    this.productRepository = productRepository;
    this.saleRepository = saleRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }


  /**
   * @param args
   */
  @Override
  public void run(final String... args) {
    saleRepository.deleteAll();
    productRepository.deleteAll();
    Product product1 = new Product("Prod1", "Sample prod1", 50.0, 300);
    Product product2 = new Product("Prod2", "Sample prod2", 150.0, 500);
    Product product3 = new Product("Prod3", "Sample prod3", 10.0, 3000);
    productRepository.saveAllAndFlush(List.of(product1, product2, product3));

    Sale sale1 = new Sale(product2, 30, LocalDate.of(2024, Month.SEPTEMBER, 30));
    Sale sale2 = new Sale(product2, 50, LocalDate.of(2025, Month.FEBRUARY, 20));
    saleRepository.saveAllAndFlush(List.of(sale1, sale2));
  }
}
