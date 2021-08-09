package com.jpmorgan.sales.model;

import java.math.BigDecimal;

public class ProductAdjustment {

    private final String type;
    private final BigDecimal value;
    private final Operation operation;

    public ProductAdjustment(String type, BigDecimal value, Operation operation) {
        this.type = type;
        this.value = value;
        this.operation = operation;
    }

    public String getType() {
        return type;
    }

    public Product adjust(Product product) {
        return new Product(product.getType(), operation.calculate(product.getValue(), value) , product.getQuantity());
    }

    @Override
    public String toString() {
        return "ProductAdjustment{" +
                "type='" + type + '\'' +
                ", value=" + value +
                ", operation=" + operation +
                '}';
    }
}