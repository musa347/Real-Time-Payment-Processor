package com.acme.payments.infra;

import com.acme.payments.domain.PaymentRequest;
import com.acme.payments.validation.Iso20022XsdValidator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Converts ISO20022 pacs.008 XML into a structured PaymentRequest domain object.
 * */

@Component
public class Iso20022Parser {

    private final Iso20022XsdValidator validator;

    public Iso20022Parser(Iso20022XsdValidator validator) {
        this.validator = validator;
    }

    public  Mono<PaymentRequest> parse(String xml) {
        return Mono.fromCallable(() -> {

            validator.validatePacs008(xml);

        JAXBContext jaxbContext = JAXBContext.newInstance(PaymentRequest.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Pacs008Stub pacs008Stub =(Pacs008Stub) unmarshaller.unmarshal(new StringReader(xml));

        return new PaymentRequest(
                UUID.randomUUID(), // or map from XML BizMsgIdr
                pacs008Stub.getDebtorAccount(),
                pacs008Stub.getCreditorAccount(),
                new BigDecimal(pacs008Stub.getAmount()),
                pacs008Stub.getCurrency()
                    );
        });
    }}