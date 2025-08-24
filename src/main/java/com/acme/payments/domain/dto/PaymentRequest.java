package com.acme.payments.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Domain DTO for payment request.
 * Immutable using Java 23 Records.
 */
public record PaymentRequest(
        String transactionId,
        String debtorAccount,
        String creditorAccount,
        String debtorBic,
        String creditorBic,
        double amount,
        String xmlPayload
) {}
