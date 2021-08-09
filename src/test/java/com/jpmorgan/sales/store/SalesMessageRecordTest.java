package com.jpmorgan.sales.store;

import com.jpmorgan.sales.model.Product;
import com.jpmorgan.sales.model.Operation;
import com.jpmorgan.sales.model.ProductAdjustment;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesMessageRecordTest {

    @Test
    public void testProductMessageRecord() {
        SalesMessageMemoryStore storage = new SalesMessageMemoryStore();

        Product a = storage.saveProduct("A", BigDecimal.ONE, 1);
        Product b = storage.saveProduct("B", BigDecimal.ONE, 1);
        assertThat(storage.getProducts()).containsOnly(a, b);
    }

    @Test
    public void testAdjustmentMessageRecord() {
        SalesMessageMemoryStore storage = new SalesMessageMemoryStore();

        ProductAdjustment aAdj = storage.saveAdjustments("A", BigDecimal.ONE, Operation.MULTIPLY);
        ProductAdjustment bAdj = storage.saveAdjustments("B", BigDecimal.ONE, Operation.MULTIPLY);
        assertThat(storage.getAdjustments()).containsOnly(aAdj, bAdj);
    }

    @Test
    public void testMessageCount() {
        SalesMessageMemoryStore storage = new SalesMessageMemoryStore();

        storage.saveProduct("A", BigDecimal.ONE, 1);
        storage.saveProduct("A", BigDecimal.ONE, 1);
        storage.saveAdjustments("A", BigDecimal.ONE, Operation.MULTIPLY);
        assertThat(storage.getNumberOfMessages()).isEqualTo(3);
    }
}
