package pt.ist.ca.domain;

import java.io.Serializable;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;


public class Certificate extends Certificate_Base implements Serializable {

    public Certificate() {
        super();
    }

    public Certificate(CertificateINFO certificate, String signature) {
        super();
        this.setCertificateINFO(certificate);
        this.setSignature(signature);
    }

    
    @Override
    public String toString() {
        return "Certificate [getSignature()=" + getSignature() + ", getCertificateINFO()=" + getCertificateINFO() + "]";
    }
}
