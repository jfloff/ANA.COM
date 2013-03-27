package pt.ist.ca.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;

import org.joda.time.DateTime;

public class CertificateListDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<CertificateDto> certificateList;

    public CertificateListDto() {
        this.certificateList = new ArrayList<CertificateDto>();
    }

    public void add(String serialID, String operatorID, String publicKey, DateTime startDate, DateTime endDate, String signature) {
        this.certificateList.add(new CertificateDto(serialID, operatorID, publicKey, startDate, endDate, signature));
    }

    public ArrayList<CertificateDto> getCertificateList() {
        return certificateList;
    }
}
