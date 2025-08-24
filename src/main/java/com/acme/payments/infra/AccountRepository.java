package com.acme.payments.infra;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountRepository {
    private final Map<String, Double> balances = new HashMap<>();

    public AccountRepository() {
        balances.put("ACC123", 1000.0);
        balances.put("ACC456", 50.0);
    }

    public double getBalance(String accountId) {
        return balances.getOrDefault(accountId, 0.0);
    }
}
