package com.jpmorgan.sales.publisher;

import org.junit.Test;

public class SalesMessagePublisherTest {

    @Test
    public void whenFileIsLoadedItIsPublishedSuccessfully() {
        new SalesMessagePublisher().start();
    }

}