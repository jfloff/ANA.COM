package pt.ist.ca.service;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.ca.shared.exception.CertificateDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class RevokeCertificateService extends CertificateAuthorityService {

    OperatorDto operatorDto;

    public RevokeCertificateService(OperatorDto dto) {
        this.operatorDto = dto;
    }

    @Override
    public void dispatch() throws CertificateDoesNotExistException {
        CertificateAuthority ca = FenixFramework.getRoot();
        ca.revokeCertificate(operatorDto.getOperatorID());
    }

}
