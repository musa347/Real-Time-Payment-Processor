package com.acme.payments.infra;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RedisService {
    private final Set<String> processedTransactions = new HashSet<>();

    public boolean isDuplicate(String transactionId) {
        return processedTransactions.contains(transactionId);
    }

    public void markProcessed(String transactionId) {
        processedTransactions.add(transactionId);
    }
}
