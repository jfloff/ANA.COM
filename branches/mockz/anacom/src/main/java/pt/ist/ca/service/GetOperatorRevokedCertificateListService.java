package pt.ist.ca.service;

import java.io.IOException;
import java.util.List;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.shared.CertificateContents;
import pt.ist.shared.SignedCertificate;

public class GetOperatorRevokedCertificateListService extends CertificateAuthorityService {

    OperatorDto operatorDto;
    CertificateListDto certificateListDto;

    public GetOperatorRevokedCertificateListService(OperatorDto dto) {
        this.operatorDto = dto;
    }

    @Override
    public void dispatch() throws IOException {
        CertificateAuthority ca = FenixFramework.getRoot();
        List<SignedCertificate> revokedList = ca.getRevokedCertificates(operatorDto.getOperatorID());
        this.certificateListDto = new CertificateListDto();

        for (SignedCertificate cert : revokedList) {
            CertificateContents contents = cert.getCertificateContents();
            certificateListDto.add(contents, cert.getSignature());
        }
    }

    public CertificateListDto getGetRevokedListServiceResult() {
        return certificateListDto;
    }
}
