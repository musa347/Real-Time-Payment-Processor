package com.acme.payments.processing;

import com.acme.payments.domain.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessor {

    public String process(PaymentRequest request) {
        // TODO: Persist to DB, update ledger, produce Kafka event, etc.
        return "Payment " + request.transactionId() + " processed successfully";
    }
}