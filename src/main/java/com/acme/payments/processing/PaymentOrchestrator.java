package com.acme.payments.processing;

import com.acme.payments.validation.BusinessValidator;
import com.acme.payments.validation.Iso20022XsdValidator;
import com.acme.payments.domain.dto.PaymentRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Orchestrates ISO 20022 payment processing in reactive pipeline. xsd validation, business validation and
 * processing are executed in separate steps.
 */
@Service
public class PaymentOrchestrator {

    private final Iso20022XsdValidator xsdValidator;
    private final BusinessValidator businessValidator;
    private final PaymentService paymentService;

    public PaymentOrchestrator(Iso20022XsdValidator xsdValidator,
                               BusinessValidator businessValidator,
                               PaymentService paymentService) {
        this.xsdValidator = xsdValidator;
        this.businessValidator = businessValidator;
        this.paymentService = paymentService;
    }

    public Mono<String> processPayment(PaymentRequest request) {
        return Mono.fromCallable(() -> {
            xsdValidator.validatePacs008(request.xmlPayload());

            businessValidator.validatePayment(
                    request.transactionId(),
                    request.debtorAccount(),
                    request.creditorAccount(),
                    request.debtorBic(),
                    request.creditorBic(),
                    request.amount()
            );
            return paymentService.executePayment(request);
        });
    }
}
