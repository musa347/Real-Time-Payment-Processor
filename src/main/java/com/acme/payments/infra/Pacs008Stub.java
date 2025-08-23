package com.acme.payments.infra;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Minimal stub for pacs.008 (instead of full XSD mapping).
 */
@XmlRootElement(name = "pacs.008")
public class Pacs008Stub {

    private String debtorAccount;
    private String transactionId;
    private String creditorAccount;
    private String amount;
    private String currency;

    @XmlElement(name = "DbtrAcct")
    public String getDebtorAccount() { return debtorAccount; }
    public void setDebtorAccount(String debtorAccount) { this.debtorAccount = debtorAccount; }

    @XmlElement(name = "CdtrAcct")
    public String getCreditorAccount() { return creditorAccount; }
    public void setCreditorAccount(String creditorAccount) { this.creditorAccount = creditorAccount; }

    @XmlElement(name = "InstdAmt")
    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    @XmlElement(name = "Ccy")
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
