package pt.ist.ca.service;

import java.text.ParseException;
import java.util.List;

import pt.ist.ca.domain.Certificate;
import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.domain.CertificateINFO;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.fenixframework.FenixFramework;

public class GetOperatorRevokedCertificateListService extends CertificateAuthorityService {

    OperatorDto operatorDto;
    CertificateListDto certificateListDto;

    public GetOperatorRevokedCertificateListService(OperatorDto dto) {
        this.operatorDto = dto;
    }

    @Override
    public void dispatch() throws ParseException {
        CertificateAuthority ca = FenixFramework.getRoot();
        List<Certificate> revokedList = ca.getRevokedCertificates(operatorDto.getOperatorID());
        this.certificateListDto = new CertificateListDto();

        for (Certificate cert : revokedList) {
            CertificateINFO info = cert.getCertificateINFO();
            certificateListDto.add(info.getSerialID(), info.getOperatorID(), info.getPublicKey(), info.getStartDate(), info.getEndDate(), cert.getSignature());
        }
    }

    public CertificateListDto getGetRevokedListServiceResult() {
        return certificateListDto;
    }
}
