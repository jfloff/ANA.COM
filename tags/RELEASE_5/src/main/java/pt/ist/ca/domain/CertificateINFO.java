package pt.ist.ca.domain;

import org.joda.time.DateTime;

public class CertificateINFO extends CertificateINFO_Base {

    public CertificateINFO() {
        super();
    }

    public CertificateINFO(String serialID, String operatorID, String publicKey, int validity) {
        this.setSerialID(serialID);
        this.setOperatorID(operatorID);
        this.setPublicKey(publicKey);
        DateTime date = new DateTime();
        this.setStartDate(date);
        DateTime end = date.plusMinutes(validity);
        this.setEndDate(end);
    }

    @Override
    public String toString() {
        return "CertificateINFO [getSerialID()=" + getSerialID() + ", getOperatorID()=" + getOperatorID() + ", getPublicKey()=" + getPublicKey()
                + ", getStartDate()=" + getStartDate() + ", getEndDate()=" + getEndDate() + "]";
    }
}
