package pt.ist.ca.service;

import pt.ist.ca.domain.Certificate;
import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.domain.CertificateINFO;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.OperatorAndKeyDto;
import pt.ist.fenixframework.FenixFramework;

public class SignCertificateService extends CertificateAuthorityService {

    private OperatorAndKeyDto operatorAndKeyDto;
    private CertificateDto certificateDto;

    public SignCertificateService(OperatorAndKeyDto dto) {
        this.operatorAndKeyDto = dto;
    }

    @Override
    public final void dispatch() throws Exception {

        CertificateAuthority ca = FenixFramework.getRoot();
        Certificate cert = ca.generateCertificate(this.operatorAndKeyDto.getOperatorID(), this.operatorAndKeyDto.getPublicKey(), this.operatorAndKeyDto.getValidity());
        CertificateINFO certInfo = cert.getCertificateINFO();
        certificateDto = new CertificateDto(certInfo.getSerialID(), certInfo.getOperatorID(), certInfo.getPublicKey(), certInfo.getStartDate(), certInfo.getEndDate(), cert.getSignature());
    }

    public CertificateDto getSignCertificateServiceResult() {
        return this.certificateDto;
    }
}
