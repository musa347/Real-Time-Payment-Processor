package com.acme.payments.validation;

import com.acme.payments.exceptions.InvalidIso20022MessageException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;

@Component
public class Iso20022XsdValidator {

    private final Schema pacs008Schema;

    public Iso20022XsdValidator() {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            pacs008Schema = schemaFactory.newSchema(
                    new ClassPathResource("xsd/iso20022/pacs.008.001.08.xsd").getFile()
            );
        } catch (SAXException | IOException e) {
            throw new InvalidIso20022MessageException("Failed to load pacs.008 XSD schema", "SCHEMA_LOAD_ERROR", e);
        }
    }

    public void validatePacs008(String xml) {
        try {
            Validator validator = pacs008Schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
        } catch (Exception e) {
            switch (e) {
                case SAXParseException sax ->
                        throw new InvalidIso20022MessageException(
                                "XSD validation failed at line " + sax.getLineNumber() +
                                        ", column " + sax.getColumnNumber() + ": " + sax.getMessage(),
                                "XSD_VALIDATION_FAILED", sax
                        );
                case IOException io ->
                        throw new InvalidIso20022MessageException(
                                "I/O error during XML validation: " + io.getMessage(),
                                "XSD_IO_ERROR", io
                        );
                case SAXException sax ->
                        throw new InvalidIso20022MessageException(
                                "General SAX error during XML validation: " + sax.getMessage(),
                                "XSD_SAX_ERROR", sax
                        );
                default ->
                        throw new InvalidIso20022MessageException(
                                "Unexpected error during XML validation: " + e.getMessage(),
                                "XSD_UNKNOWN_ERROR", e
                        );
            }
        }
    }
}
