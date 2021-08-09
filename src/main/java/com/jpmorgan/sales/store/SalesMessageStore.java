package com.jpmorgan.sales.store;

import com.jpmorgan.sales.model.Product;
import com.jpmorgan.sales.model.Operation;
import com.jpmorgan.sales.model.ProductAdjustment;

import java.math.BigDecimal;
import java.util.List;

public interface SalesMessageStore {
    Product saveProduct(String type, BigDecimal value, Integer quantity);

    void setProducts(List<Product> products);

    ProductAdjustment saveAdjustments(String type, BigDecimal value, Operation operation);

    List<Product> getProducts();

    List<ProductAdjustment> getAdjustments();

    Integer getNumberOfMessages();
}
