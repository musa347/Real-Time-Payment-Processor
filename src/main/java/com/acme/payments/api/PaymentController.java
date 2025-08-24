package com.acme.payments.api;

import com.acme.payments.domain.dto.PaymentRequest;
import com.acme.payments.processing.PaymentOrchestrator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentOrchestrator orchestrator;

    public PaymentController(PaymentOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> processPayment(@RequestBody String xmlPayload) {
        PaymentRequest request = new PaymentRequest(
                "TXN-" + System.currentTimeMillis(),  // temporary tx id
                "ACC123",
                "ACC456",
                "BANKAUS33",
                "BANKGB22",
                new BigDecimal("200.00"),
                xmlPayload
        );
        return orchestrator.processPayment(request);
    }
}
