package pt.ist.ca.shared.dto;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import pt.ist.shared.CertificateContents;

public class CertificateListDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<CertificateDto> certificateList;

    public CertificateListDto() {
        this.certificateList = new ArrayList<CertificateDto>();
    }

    public void add(String certificateContents, String signature) {
        this.certificateList.add(new CertificateDto(certificateContents, signature));
    }

    public void add(CertificateContents certificateContents, String signature) throws IOException {
        this.certificateList.add(new CertificateDto(certificateContents, signature));
    }

    public ArrayList<CertificateDto> getCertificateList() {
        return certificateList;
    }

    @Override
    public String toString() {
        return "CertificateListDto [certificateList=" + certificateList + "]";
    }
}
