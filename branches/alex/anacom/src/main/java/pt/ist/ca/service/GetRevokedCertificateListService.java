package pt.ist.ca.service;

import java.io.IOException;
import java.util.List;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.shared.CertificateContents;
import pt.ist.shared.SignedCertificate;

public class GetRevokedCertificateListService extends
		CertificateAuthorityService {

	CertificateListDto certificateListDto;

	public GetRevokedCertificateListService() {
	}

	@Override
	public void dispatch() throws IOException {
		CertificateAuthority ca = FenixFramework.getRoot();
		List<SignedCertificate> revokedList = ca.getRevokedCertificates();
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
