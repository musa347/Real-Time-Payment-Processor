package com.acme.payments.infra;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class InstitutionRegistry {
    private final Set<String> validInstitutions = Set.of("BANKAUS33", "BANKGB22");

    public boolean isValid(String bic) {
        return validInstitutions.contains(bic);
    }
}
