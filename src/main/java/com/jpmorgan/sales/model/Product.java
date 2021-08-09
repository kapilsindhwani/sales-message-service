package com.jpmorgan.sales.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final String type;
    private final BigDecimal value;
    private final Integer quantity;

    public Product(String type, BigDecimal value, Integer quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be bigger than zero!");
        this.type = type;
        this.value = value;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product that = (Product) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(value, that.value) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, quantity);
    }

    @Override
    public String toString() {
        return "Product{" +
                "type='" + type + '\'' +
                ", value=" + value +
                ", quantity=" + quantity +
                '}';
    }
}
