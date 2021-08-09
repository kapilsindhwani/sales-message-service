package com.jpmorgan.sales.model;

import java.math.BigDecimal;

/**
 * Operation to ADD, SUBTRACT or MULTIPLY numbers
 */
public enum Operation {
    ADD {
        @Override
        public BigDecimal calculate(BigDecimal a, BigDecimal b) {
            return a.add(b);
        }
    },
    SUBTRACT {
        @Override
        public BigDecimal calculate(BigDecimal a, BigDecimal b) {
            return a.subtract(b);
        }
    },
    MULTIPLY {
        @Override
        public BigDecimal calculate(BigDecimal a, BigDecimal b) {
            return a.multiply(b);
        }
    };

    public abstract BigDecimal calculate(BigDecimal a, BigDecimal b);
}
