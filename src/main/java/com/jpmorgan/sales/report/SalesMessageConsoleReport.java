package com.jpmorgan.sales.report;

import com.jpmorgan.sales.service.ProductService;
import com.jpmorgan.sales.store.SalesMessageStore;

public class SalesMessageConsoleReport implements SalesMessageReport {

    private final SalesMessageStore storage;

    public SalesMessageConsoleReport(SalesMessageStore storage) {
        this.storage = storage;
    }

    @Override
    public void reportProducts() {
        System.out.println("\nProduct sales report:");
        ProductService.sumProducts(storage.getProducts())
                .forEach(System.out::println);
        System.out.println();
    }

    @Override
    public void reportAppliedAdjustments() {
        System.out.println("\nProduct adjustments:");
        storage.getAdjustments()
                .forEach(System.out::println);
        System.out.println();
    }

}
