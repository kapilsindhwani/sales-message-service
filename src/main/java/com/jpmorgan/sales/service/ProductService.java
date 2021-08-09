package com.jpmorgan.sales.service;

import com.jpmorgan.sales.model.Product;
import com.jpmorgan.sales.model.ProductAdjustment;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class ProductService {

    public static List<Product> sumProducts(List<Product> products) {
        return products.stream()
                .collect(groupingBy(Product::getType))
                .entrySet()
                .stream()
                .map(product -> sumProducts(product.getKey(), product.getValue()))
                .sorted(Comparator.comparing(Product::getType))
                .collect(toList());
    }

    private static Product sumProducts(String type, List<Product> products) {
        BigDecimal sum = products.stream()
                .map(product -> product.getValue().multiply(new BigDecimal(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer qty = products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
        return new Product(type, sum, qty);
    }


    public static List<Product> adjustProducts(ProductAdjustment adjustment, List<Product> products) {
        return products.stream()
                .map(product -> (product.getType().equals(adjustment.getType())) ? adjustment.adjust(product) : product)
                .collect(toList());
    }

}
