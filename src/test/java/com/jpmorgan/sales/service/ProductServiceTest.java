package com.jpmorgan.sales.service;

import com.jpmorgan.sales.model.Operation;
import com.jpmorgan.sales.model.Product;
import com.jpmorgan.sales.model.ProductAdjustment;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest {

    @Test
    public void testSumOnUniqueProduct() {
        var sum = ProductService.sumProducts(Arrays.asList(
                new Product("apple", new BigDecimal(10), 1),
                new Product("banana", new BigDecimal(20), 2),
                new Product("cherry", new BigDecimal(30), 3)
        ));

        assertThat(sum).hasSize(3).containsExactly(
                new Product("apple", new BigDecimal(10), 1),
                new Product("banana", new BigDecimal(40), 2),
                new Product("cherry", new BigDecimal(90), 3)
        );
    }

    @Test
    public void testSumOnMultipleProducts() {
        var sum = ProductService.sumProducts(Arrays.asList(
                new Product("apple", new BigDecimal(10), 1),
                new Product("banana", new BigDecimal(20), 2),
                new Product("cherry", new BigDecimal(30), 3),
                new Product("apple", new BigDecimal(1), 1),
                new Product("banana", new BigDecimal(2), 2),
                new Product("cherry", new BigDecimal(3), 3)
        ));

        assertThat(sum).hasSize(3).containsExactly(
                new Product("apple", new BigDecimal(11), 2),
                new Product("banana", new BigDecimal(44), 4),
                new Product("cherry", new BigDecimal(99), 6)
        );
    }


    @Test
    public void testAdjustOnUniqueProduct() {
        var adjustProducts = ProductService.adjustProducts(
                new ProductAdjustment("apple", new BigDecimal(5), Operation.MULTIPLY),
                Arrays.asList(
                        new Product("apple", new BigDecimal(10), 1),
                        new Product("banana", new BigDecimal(20), 2),
                        new Product("cherry", new BigDecimal(30), 3)
                )
        );

        assertThat(adjustProducts).hasSize(3).containsExactly(
                new Product("apple", new BigDecimal(50), 1),
                new Product("banana", new BigDecimal(20), 2),
                new Product("cherry", new BigDecimal(30), 3)
        );
    }

    @Test
    public void testAdjustOnMultipleProducts() {
        var adjustProducts = ProductService.adjustProducts(
                new ProductAdjustment("apple", new BigDecimal(5), Operation.MULTIPLY),
                Arrays.asList(
                        new Product("apple", new BigDecimal(10), 1),
                        new Product("banana", new BigDecimal(20), 2),
                        new Product("cherry", new BigDecimal(30), 3),
                        new Product("apple", new BigDecimal(1), 1),
                        new Product("banana", new BigDecimal(2), 2),
                        new Product("cherry", new BigDecimal(3), 3)
                )
        );

        assertThat(adjustProducts).hasSize(6).containsExactly(
                new Product("apple", new BigDecimal(50), 1),
                new Product("banana", new BigDecimal(20), 2),
                new Product("cherry", new BigDecimal(30), 3),
                new Product("apple", new BigDecimal(5), 1),
                new Product("banana", new BigDecimal(2), 2),
                new Product("cherry", new BigDecimal(3), 3)
        );
    }

    @Test
    public void testMultipleAdjustOnMultipleProducts() {
        var products = Arrays.asList(
                new Product("apple", new BigDecimal(10), 1),
                new Product("banana", new BigDecimal(20), 2),
                new Product("cherry", new BigDecimal(30), 3),
                new Product("apple", new BigDecimal(1), 1),
                new Product("banana", new BigDecimal(2), 2),
                new Product("cherry", new BigDecimal(3), 3)
        );

        var adjustedApple = ProductService.adjustProducts(new ProductAdjustment("apple", new BigDecimal(5), Operation.ADD), products);
        var adjustedBanana = ProductService.adjustProducts(new ProductAdjustment("banana", new BigDecimal(5), Operation.SUBTRACT), adjustedApple);
        var adjustedCherry = ProductService.adjustProducts(new ProductAdjustment("cherry", new BigDecimal(5), Operation.MULTIPLY), adjustedBanana);

        assertThat(adjustedCherry).hasSize(6).containsExactly(
                new Product("apple", new BigDecimal(15), 1),
                new Product("banana", new BigDecimal(15), 2),
                new Product("cherry", new BigDecimal(150), 3),
                new Product("apple", new BigDecimal(6), 1),
                new Product("banana", new BigDecimal(-3), 2),
                new Product("cherry", new BigDecimal(15), 3)
        );
    }

}
