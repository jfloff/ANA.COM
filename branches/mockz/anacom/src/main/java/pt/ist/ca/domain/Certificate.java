package pt.ist.ca.domain;



public class Certificate extends Certificate_Base {

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
