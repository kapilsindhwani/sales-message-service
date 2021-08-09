package com.jpmorgan.sales.receiver;

import com.jpmorgan.sales.model.Product;
import com.jpmorgan.sales.model.Operation;
import com.jpmorgan.sales.model.ProductAdjustment;
import com.jpmorgan.sales.report.SalesMessageReport;
import com.jpmorgan.sales.service.ProductService;
import com.jpmorgan.sales.store.SalesMessageStore;

import java.math.BigDecimal;
import java.util.List;

public class SalesMessageReceiver {

    private final SalesMessageStore salesMessageStore;
    private final SalesMessageReport salesMessageReporter;
    private final Integer reportFrequency;
    private final Integer maximumMessage;

    public SalesMessageReceiver(SalesMessageStore salesMessageStore, SalesMessageReport salesMessageReporter, Integer reportFrequency, Integer maximumMessage) {
        this.salesMessageStore = salesMessageStore;
        this.salesMessageReporter = salesMessageReporter;
        this.reportFrequency = reportFrequency;
        this.maximumMessage = maximumMessage;
    }

    public void receiveProduct(String type, BigDecimal value) {
        receiveProduct(type, value, 1);
    }

    public void receiveProduct(String type, BigDecimal value, Integer quantity) {
        receiveMessage(() -> salesMessageStore.saveProduct(type, value, quantity));
    }

    public void receiveAdjustment(String type, BigDecimal value, Operation operation) {
        receiveMessage(() -> {
            ProductAdjustment productAdjustment = salesMessageStore.saveAdjustments(type, value, operation);
            List<Product> adjustedProducts = ProductService.adjustProducts(productAdjustment, salesMessageStore.getProducts());
            salesMessageStore.setProducts(adjustedProducts);
        });
    }

    private void receiveMessage(MessageProcessor messageProcessor) {
        if (hasReachedMaximumNumberOfMessages()) {
            throw new IllegalStateException("No more messages allowed!");
        }
        messageProcessor.process();
        if (hasHitReportFrequency()) {
            salesMessageReporter.reportProducts();
        }
        if (hasReachedMaximumNumberOfMessages()) {
            salesMessageReporter.reportAppliedAdjustments();
        }
    }

    /**
     * Check if number of messages has reached the report threshold.
     *
     * @return Return true if report frequency is less than one, in this case a report should be taken after each message.
     *         Else return true each time when number of messages hits the frequency.
     */
    public boolean hasHitReportFrequency() {
        return reportFrequency < 1 || salesMessageStore.getNumberOfMessages() % reportFrequency == 0;
    }

    /**
     * Check if number of messages has reached the maximum number.
     *
     * @return Return false if maximum message is less than one, in this case any number of messages can be received.
     *         Else return true once number of messages reaches the upper limit.
     */
    public boolean hasReachedMaximumNumberOfMessages() {
        return maximumMessage > 0 && salesMessageStore.getNumberOfMessages() >= maximumMessage;
    }

    public SalesMessageStore getStorage() {
        return salesMessageStore;
    }

    public SalesMessageReport getReporter() {
        return salesMessageReporter;
    }


}
