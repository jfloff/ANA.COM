package pt.ist.ca.domain;

import java.io.Serializable;

import org.joda.time.DateTime;

public class CertificateINFO extends CertificateINFO_Base implements Serializable {

    public CertificateINFO() {
        super();
    }

    public CertificateINFO(String serialID, String operatorID, String publicKey, long validity) {
        this.setSerialID(serialID);
        this.setOperatorID(operatorID);
        this.setPublicKey(publicKey);
        this.setStartDate(new DateTime());
        DateTime end = new DateTime();
        end.plus(validity);
        this.setEndDate(end);
    }

    @Override
    public String toString() {
        return "CertificateINFO [getSerialID()=" + getSerialID() + ", getOperatorID()=" + getOperatorID() + ", getPublicKey()=" + getPublicKey() + ", getStartDate()=" + getStartDate() + ", getEndDate()=" + getEndDate() + "]";
    }
}
