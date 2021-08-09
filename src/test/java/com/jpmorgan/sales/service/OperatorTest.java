package com.jpmorgan.sales.service;

import org.junit.Test;

import java.math.BigDecimal;

import static com.jpmorgan.sales.model.Operation.ADD;
import static com.jpmorgan.sales.model.Operation.MULTIPLY;
import static com.jpmorgan.sales.model.Operation.SUBTRACT;
import static org.assertj.core.api.Assertions.assertThat;

public class OperatorTest {

    private BigDecimal ten = new BigDecimal("10");
    private BigDecimal twenty = new BigDecimal("20");

    @Test
    public void add() {
        assertThat(ADD.calculate(ten, twenty)).isEqualByComparingTo(new BigDecimal("30"));
    }

    @Test
    public void subtract() {
        assertThat(SUBTRACT.calculate(ten, twenty)).isEqualByComparingTo(new BigDecimal("-10"));
    }

    @Test
    public void multiply() {
        assertThat(MULTIPLY.calculate(ten, twenty)).isEqualByComparingTo(new BigDecimal("200"));
    }

}
