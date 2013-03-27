package pt.ist.ca.shared.dto;

import java.io.IOException;
import java.io.Serializable;

import pt.ist.shared.CertificateContents;
import pt.ist.shared.SecurityData;

public class CertificateDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String certificate;
    private String signature;

    public CertificateDto() {

    }

    public CertificateDto(String certInfo, String signature) {
        this.certificate = certInfo;
        this.signature = signature;
    }

    public CertificateDto(CertificateContents certContents, String signature) throws IOException {
        byte[] certBytes = SecurityData.serialize(certContents);
        this.certificate = SecurityData.encode64(certBytes);
        this.signature = signature;
    }

    public String getCertificate() {
        return this.certificate;
    }

    public String getSignature() {
        return this.signature;
    }

    @Override
    public String toString() {
        return "CertificateDto [certificate=" + certificate + ", signature=" + signature + "]";
    }
}
