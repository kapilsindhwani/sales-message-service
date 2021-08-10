package com.jpmorgan.sales.receiver;

import com.jpmorgan.sales.model.Product;
import com.jpmorgan.sales.model.Operation;
import com.jpmorgan.sales.report.MockSalesMessageReporter;
import com.jpmorgan.sales.store.SalesMessageMemoryStore;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class SalesMessageReceiverTest {

    @Test
    public void whenMessageIsReceivedThenProductIsPersisted() {
        SalesMessageReceiver salesMessageReceiver = getMessageReceiver(5, 5);
        salesMessageReceiver.receiveProduct("A", BigDecimal.ONE);
        assertThat(salesMessageReceiver.getStorage().getNumberOfMessages()).isEqualTo(1);
    }

    @Test
    public void when10MessagesAreReceivedThenReportIsPublished() {
        testReportProducts(30, 10, 3);
    }

    @Test(expected = IllegalStateException.class)
    public void whenMaxMessageIsReceivedThenThrowException() {
        int max = 1;
        SalesMessageMemoryStore storage = new SalesMessageMemoryStore();
        MockSalesMessageReporter report = new MockSalesMessageReporter();
        SalesMessageReceiver receiver = new SalesMessageReceiver(storage, report, 10, max);

        receiver.receiveProduct("A", BigDecimal.ONE);
        try {
            receiver.receiveAdjustment("A", BigDecimal.ONE, Operation.MULTIPLY);
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("No more messages allowed!");
            assertThat(receiver.hasReachedMaximumNumberOfMessages()).isTrue();
            assertThat(storage.getProducts()).hasSize(1);
            assertThat(storage.getAdjustments()).hasSize(0);
            assertThat(storage.getNumberOfMessages()).isEqualTo(1);
            assertThat(report.getNumberOfAdjustmentReports()).isEqualTo(1);
            throw e;
        }

        fail("Last message should fail.");
    }

    @Test
    public void whenMaximumMessageIsNotReceivedThenMessageIsPersisted() {
        SalesMessageReceiver receiver = getMessageReceiver(0, 0);
        IntStream.range(0, 5).forEach(count -> receiver.receiveProduct("A", BigDecimal.ONE));
        assertThat(receiver.getStorage().getNumberOfMessages()).isEqualTo(5);
    }

    @Test
    public void whenProductAdjustmentMessageIsReceivedThenPriceIsAdjusted() {
        SalesMessageReceiver r = getMessageReceiver(0, 0);

        r.receiveProduct("A", BigDecimal.TEN);
        r.receiveProduct("A", BigDecimal.ONE, 2);
        r.receiveProduct("B", BigDecimal.TEN);
        r.receiveAdjustment("A", BigDecimal.ONE, Operation.ADD);
        r.receiveProduct("A", BigDecimal.TEN);
        r.receiveAdjustment("A", BigDecimal.ONE, Operation.ADD);

        assertThat(r.getStorage().getProducts()).containsExactly(
           new Product("A", new BigDecimal(12), 1),
           new Product("A", new BigDecimal(3), 2),
           new Product("B", new BigDecimal(10), 1),
           new Product("A", new BigDecimal(11), 1)
        );
    }

    @Test
    public void testProductReportEdgeCases() {
        testReportProducts(9, 10, 0);
        testReportProducts(10, 10, 1);
        testReportProducts(11, 10, 1);
        testReportProducts(19, 10, 1);
        testReportProducts(20, 10, 2);
        testReportProducts(21, 10, 2);
    }

    @Test
    public void testAlwaysReportProducts() {
        testReportProducts(30, 0, 30);
    }

    private void testReportProducts(Integer messages, Integer reportFreq, Integer expectedReportCount) {
        SalesMessageMemoryStore storage = new SalesMessageMemoryStore();
        MockSalesMessageReporter report = new MockSalesMessageReporter();
        SalesMessageReceiver receiver = new SalesMessageReceiver(storage, report, reportFreq, 50);

        IntStream.range(0, messages).forEach(count -> receiver.receiveProduct("A", BigDecimal.ONE));

        assertThat(report.getNumberOfProductReports()).isEqualTo(expectedReportCount);
        assertThat(storage.getNumberOfMessages()).isEqualTo(messages);
    }

    @Test
    public void testAdjustmentReportWhenReachedTheMaximum() {
        SalesMessageMemoryStore storage = new SalesMessageMemoryStore();
        MockSalesMessageReporter report = new MockSalesMessageReporter();
        SalesMessageReceiver receiver = new SalesMessageReceiver(storage, report, 1, 1);

        receiver.receiveProduct("A", BigDecimal.ONE);
        assertThat(report.getNumberOfAdjustmentReports()).isEqualTo(1);
        assertThat(receiver.getStorage().getNumberOfMessages()).isEqualTo(1);
        assertThat(receiver.hasReachedMaximumNumberOfMessages()).isTrue();
    }

    @Test
    public void testMaximumMessage() {
        int max = 3;
        SalesMessageMemoryStore storage = new SalesMessageMemoryStore();
        MockSalesMessageReporter report = new MockSalesMessageReporter();
        SalesMessageReceiver receiver = new SalesMessageReceiver(storage, report, 10, max);

        receiver.receiveProduct("A", BigDecimal.ONE);
        receiver.receiveProduct("A", BigDecimal.ONE, 1);
        receiver.receiveAdjustment("A", BigDecimal.ONE, Operation.MULTIPLY);

        assertThat(storage.getProducts()).hasSize(2);
        assertThat(storage.getAdjustments()).hasSize(1);
        assertThat(storage.getNumberOfMessages()).isEqualTo(3);
        assertThat(report.getNumberOfAdjustmentReports()).isEqualTo(1);
        assertThat(receiver.hasReachedMaximumNumberOfMessages()).isTrue();
    }

    private SalesMessageReceiver getMessageReceiver(Integer reportFreq, Integer maxMsg) {
        SalesMessageMemoryStore storage = new SalesMessageMemoryStore();
        MockSalesMessageReporter report = new MockSalesMessageReporter();
        return new SalesMessageReceiver(storage, report, reportFreq, maxMsg);
    }
}
