package com.jpmorgan.sales.report;

import com.jpmorgan.sales.service.ProductService;
import com.jpmorgan.sales.store.SalesMessageStore;

/**
 * Prints the report to console
 */
public class SalesMessageConsoleReport implements SalesMessageReport {

    private final SalesMessageStore storage;

    public SalesMessageConsoleReport(SalesMessageStore storage) {
        this.storage = storage;
    }

    @Override
    public void reportProducts() {
        System.out.println("After each 10 sales report");
        System.out.println(" ------------------------------------------- ");
        System.out.println("|            product|       value|  quantity|");
        System.out.println("|-------------------|------------|----------|");
        ProductService.sumProducts(storage.getProducts())
                .forEach(System.out::println);
        System.out.println();
    }

    @Override
    public void reportAppliedAdjustments() {
        System.out.println("50 processed messsages limit reached. Adjustments made so far:");
        storage.getAdjustments()
                .forEach(System.out::println);
        System.out.println();
    }

}
