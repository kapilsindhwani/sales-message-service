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
        List<Product> sum = ProductService.sumProducts(Arrays.asList(
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
        List<Product> sum = ProductService.sumProducts(Arrays.asList(
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
        List<Product> adj = ProductService.adjustProducts(
                new ProductAdjustment("apple", new BigDecimal(5), Operation.MULTIPLY),
                Arrays.asList(
                        new Product("apple", new BigDecimal(10), 1),
                        new Product("banana", new BigDecimal(20), 2),
                        new Product("cherry", new BigDecimal(30), 3)
                )
        );

        assertThat(adj).hasSize(3).containsExactly(
                new Product("apple", new BigDecimal(50), 1),
                new Product("banana", new BigDecimal(20), 2),
                new Product("cherry", new BigDecimal(30), 3)
        );
    }

    @Test
    public void testAdjustOnMultipleProducts() {
        List<Product> adj = ProductService.adjustProducts(
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

        assertThat(adj).hasSize(6).containsExactly(
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
        List<Product> products = Arrays.asList(
                new Product("apple", new BigDecimal(10), 1),
                new Product("banana", new BigDecimal(20), 2),
                new Product("cherry", new BigDecimal(30), 3),
                new Product("apple", new BigDecimal(1), 1),
                new Product("banana", new BigDecimal(2), 2),
                new Product("cherry", new BigDecimal(3), 3)
        );

        List<Product> adj = ProductService.adjustProducts(new ProductAdjustment("apple", new BigDecimal(5), Operation.ADD), products);
        List<Product> adj2 = ProductService.adjustProducts(new ProductAdjustment("banana", new BigDecimal(5), Operation.SUBTRACT), adj);
        List<Product> adj3 = ProductService.adjustProducts(new ProductAdjustment("cherry", new BigDecimal(5), Operation.MULTIPLY), adj2);

        assertThat(adj3).hasSize(6).containsExactly(
                new Product("apple", new BigDecimal(15), 1),
                new Product("banana", new BigDecimal(15), 2),
                new Product("cherry", new BigDecimal(150), 3),
                new Product("apple", new BigDecimal(6), 1),
                new Product("banana", new BigDecimal(-3), 2),
                new Product("cherry", new BigDecimal(15), 3)
        );
    }

}
