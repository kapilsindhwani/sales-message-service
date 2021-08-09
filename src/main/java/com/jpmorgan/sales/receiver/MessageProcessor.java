package com.jpmorgan.sales.receiver;

/**
 * Represents an interface that process the message.
 * This is a functional interface whose functional method is process().
 */
@FunctionalInterface
public interface MessageProcessor {
    void process();
}