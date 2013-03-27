package pt.ist.ca.service;

import java.io.IOException;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.OperatorCertificateInfoDto;
import pt.ist.ca.shared.exception.EmptyOperatorException;
import pt.ist.ca.shared.exception.EmptyPublicKeyException;
import pt.ist.ca.shared.exception.InvalidValidityException;
import pt.ist.ca.shared.exception.UnableToProvideServiceException;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.shared.CertificateContents;
import pt.ist.shared.SignedCertificate;

public class SignCertificateService extends CertificateAuthorityService {

	private OperatorCertificateInfoDto operatorCertificateInfoDto;
	private CertificateDto certificateDto;

	public SignCertificateService(OperatorCertificateInfoDto dto) {
		this.operatorCertificateInfoDto = dto;
	}

	@Override
	public final void dispatch() throws EmptyOperatorException,
			EmptyPublicKeyException, InvalidValidityException,
			UnableToProvideServiceException, IOException {

		CertificateAuthority ca = FenixFramework.getRoot();
		SignedCertificate cert = ca.generateCertificate(
				this.operatorCertificateInfoDto.getOperatorID(),
				this.operatorCertificateInfoDto.getPublicKey(),
				this.operatorCertificateInfoDto.getValidity());
		CertificateContents certInfo = cert.getCertificateContents();
		certificateDto = new CertificateDto(certInfo, cert.getSignature());
	}

	public CertificateDto getSignCertificateServiceResult() {
		return this.certificateDto;
	}
}
