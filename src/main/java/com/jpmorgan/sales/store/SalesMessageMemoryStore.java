package com.jpmorgan.sales.store;

import com.jpmorgan.sales.model.Product;
import com.jpmorgan.sales.model.Operation;
import com.jpmorgan.sales.model.ProductAdjustment;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the messages in memory
 */
public class SalesMessageMemoryStore implements SalesMessageStore {

    private List<Product> products = new ArrayList<>();
    private List<ProductAdjustment> adjustments = new ArrayList<>();

    @Override
    public Product saveProduct(String type, BigDecimal value, Integer quantity) {
        var product = new Product(type, value, quantity);
        products.add(product);
        return product;
    }

    @Override
    public void setProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
    }

    @Override
    public ProductAdjustment saveAdjustments(String type, BigDecimal value, Operation operation) {
        var productAdjustment = new ProductAdjustment(type, value, operation);
        adjustments.add(productAdjustment);
        return productAdjustment;
    }

    @Override
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public List<ProductAdjustment> getAdjustments() {
        return Collections.unmodifiableList(adjustments);
    }

    @Override
    public Integer getNumberOfMessages() {
        return products.size() + adjustments.size();
    }

}
