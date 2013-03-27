package pt.ist.ca.shared.dto;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;

public class CertificateDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String serialID;
    private String operatorID;
    private String publicKey;
    private DateTime startDate;
    private DateTime endDate;
    private String signature;

    public CertificateDto() {

    }

    public CertificateDto(String serialID, String operatorID, String publicKey, DateTime startDate, DateTime endDate, String signature) {
        this.serialID = serialID;
        this.operatorID = operatorID;
        this.publicKey = publicKey;
        this.startDate = startDate;
        this.endDate = endDate;
        this.signature = signature;

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

    public String getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return "CertificateDto [serialID=" + serialID + ", operatorID=" + operatorID + ", publicKey=" + publicKey + ", startDate=" + startDate + ", endDate=" + endDate + ", signature=" + signature + "]";
    }
}
