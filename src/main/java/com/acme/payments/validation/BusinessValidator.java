package com.acme.payments.validation;

import com.acme.payments.exceptions.BusinessRuleViolationException;
import com.acme.payments.infra.AccountRepository;
import com.acme.payments.infra.InstitutionRegistry;
import com.acme.payments.infra.RedisService;
import org.springframework.stereotype.Component;

@Component
public class BusinessValidator {

    private final AccountRepository accountRepository;
    private final InstitutionRegistry institutionRegistry;
    private final RedisService redisService;

    public BusinessValidator(AccountRepository accountRepository,
                             InstitutionRegistry institutionRegistry,
                             RedisService redisService) {
        this.accountRepository = accountRepository;
        this.institutionRegistry = institutionRegistry;
        this.redisService = redisService;
    }

    public void validatePayment(String transactionId, String debtorAccount, String creditorAccount,
                                String debtorBic, String creditorBic, double amount) {
        // 1. Idempotency
        if (redisService.isDuplicate(transactionId)) {
            throw new BusinessRuleViolationException("Duplicate transaction detected", "IDEMPOTENCY_ERROR");
        }

        // 2. Institution validation
        if (!institutionRegistry.isValid(debtorBic)) {
            throw new BusinessRuleViolationException("Debtor institution not registered: " + debtorBic, "INVALID_DEBTOR");
        }
        if (!institutionRegistry.isValid(creditorBic)) {
            throw new BusinessRuleViolationException("Creditor institution not registered: " + creditorBic, "INVALID_CREDITOR");
        }

        // 3. Balance check
        double balance = accountRepository.getBalance(debtorAccount);
        if (balance < amount) {
            throw new BusinessRuleViolationException("Insufficient funds in account: " + debtorAccount, "INSUFFICIENT_FUNDS");
        }

        // If all checks pass, mark transaction as processed (idempotency key in Redis)
        redisService.markProcessed(transactionId);
    }
}
