package com.acme.payments.processing;

import com.acme.payments.domain.dto.PaymentRequest;
import com.acme.payments.exceptions.PaymentValidationException;
import com.acme.payments.infra.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Business service that applies posting rules.
 * - Validate account existence
 * - Check balance
 * - Debit/Credit accounts
 * - Return a pacs.002-like response
 */
@Service
public class PaymentService {

    private final AccountService accountService;

    public PaymentService(AccountService accountService) {
        this.accountService = accountService;
    }

    public String executePayment(PaymentRequest request) {
        // Step 1: Validate accounts
        if (!accountService.accountExists(request.debtorAccount())) {
            throw new PaymentValidationException(
                    "DEBTOR_NOT_FOUND",
                    "Debtor account does not exist: " + request.debtorAccount()
            );
        }
        if (!accountService.accountExists(request.creditorAccount())) {
            throw new PaymentValidationException(
                    "CREDITOR_NOT_FOUND",
                    "Creditor account does not exist: " + request.creditorAccount()
            );
        }

        // Step 2: Check sufficient balance
        BigDecimal balance = accountService.getBalance(request.debtorAccount());
        if (balance.compareTo(request.amount()) < 0) {
            throw new PaymentValidationException(
                    "INSUFFICIENT_FUNDS",
                    "Insufficient funds for account: " + request.debtorAccount()
            );
        }

        // Step 3: Debit and credit
        accountService.debit(request.debtorAccount(), request.amount());
        accountService.credit(request.creditorAccount(), request.amount());

        // Step 4: Generate simple pacs.002-like acknowledgment
        return """
            <pacs.002>
                <TransactionId>%s</TransactionId>
                <Status>ACCP</Status>
                <Message>Payment successfully processed</Message>
            </pacs.002>
            """.formatted(request.transactionId());
    }
}
