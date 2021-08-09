package com.jpmorgan.sales.report;

public class MockSalesMessageReporter implements SalesMessageReport {

    private Integer callReportProducts = 0;
    private Integer callReportAdjustments = 0;

    @Override
    public void reportProducts() {
        callReportProducts++;
    }

    @Override
    public void reportAppliedAdjustments() {
        callReportAdjustments++;
    }

    public Integer getNumberOfProductReports() {
        return callReportProducts;
    }

    public Integer getNumberOfAdjustmentReports() {
        return callReportAdjustments;
    }
}
