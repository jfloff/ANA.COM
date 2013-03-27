package pt.ist.shared;

import java.io.Serializable;

public class SignedCertificate implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CertificateContents certificateContents;
    private String signature;

    public SignedCertificate(CertificateContents contents, String signature) {
        this.certificateContents = contents;
        this.signature = signature;
    }

    public CertificateContents getCertificateContents() {
        return certificateContents;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SignedCertificate) {
            SignedCertificate cert = (SignedCertificate) object;
            return (signature.equals(cert.getSignature())) && (certificateContents.equals(cert.getCertificateContents()));
        }
        return false;
    }

    @Override
    public String toString() {
        return "SignedCertificate [certificateContents=" + certificateContents + ", signature=" + signature + "]";
    }
}
