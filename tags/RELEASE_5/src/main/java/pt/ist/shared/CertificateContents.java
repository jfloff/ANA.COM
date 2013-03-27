package pt.ist.shared;

import java.io.Serializable;

import org.joda.time.DateTime;

public class CertificateContents implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String serialID;
    private String operatorID;
    private String publicKey;
    private DateTime startDate;
    private DateTime endDate;

    public CertificateContents(String serialID, String operatorID, String publicKey, DateTime startDate, DateTime endDate) {
        this.serialID = serialID;
        this.operatorID = operatorID;
        this.publicKey = publicKey;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getSerialID() {
        return serialID;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CertificateContents) {
            CertificateContents cert = (CertificateContents) object;
            return (this.serialID.equals(cert.getSerialID()) && this.operatorID.equals(cert.getOperatorID())
                    && this.publicKey.equals(cert.getPublicKey()) && this.startDate.equals(cert.getStartDate()) && this.endDate.equals(cert.getEndDate()));
        }
        return false;
    }

    @Override
    public String toString() {
        return "CertificateContents [serialID=" + serialID + ", operatorID=" + operatorID + ", publicKey=" + publicKey + ", startDate=" + startDate
                + ", endDate=" + endDate + "]";
    }
}
