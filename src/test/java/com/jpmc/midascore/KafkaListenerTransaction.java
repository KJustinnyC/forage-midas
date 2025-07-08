package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;


public class KafkaListenerTransaction {

    private final TransactionService transactionService;

    public KafkaListenerTransaction(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(Transaction transaction) {
        System.out.println("Received transaction: " + transaction);
        transactionService.processTransaction(transaction);
    }
}

