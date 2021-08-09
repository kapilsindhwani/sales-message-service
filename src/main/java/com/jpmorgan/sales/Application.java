package com.jpmorgan.sales;

import com.jpmorgan.sales.publisher.SalesMessagePublisher;

public class Application {

    public static void main(String... args) {
        new SalesMessagePublisher().start();
    }
}
