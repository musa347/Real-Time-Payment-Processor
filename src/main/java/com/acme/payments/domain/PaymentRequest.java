package com.acme.payments.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Domain-level representation of a payment request.
 * Extracted from pacs.008 XML using JAXB.
 */
public record PaymentRequest(
        UUID transactionId,
        String debtorAccount,
        String creditorAccount,
        BigDecimal amount,
        String currency
) {}
