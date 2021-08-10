package com.jpmorgan.sales.model;

import java.math.BigDecimal;

/**
 * Immutable Product Adjustment
 */
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

    public String toString () {
        return "Applied "+operation+" to "+ type +" and price changed to "+value;
    }

}
