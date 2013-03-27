package pt.ist.ca.service.bridge;

import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorCertificateInfoDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.ca.shared.dto.PublicKeyDto;
import pt.ist.ca.shared.exception.CertificateDoesNotExistException;
import pt.ist.ca.shared.exception.EmptyOperatorException;
import pt.ist.ca.shared.exception.EmptyPublicKeyException;
import pt.ist.ca.shared.exception.InvalidValidityException;
import pt.ist.ca.shared.exception.UnableToProvideServiceException;

public interface CertificateAuthorityBridge {

	public CertificateDto signCertificate(OperatorCertificateInfoDto dto)
			throws UnableToProvideServiceException, EmptyOperatorException,
			EmptyPublicKeyException, InvalidValidityException;

	public void revokeCertificate(OperatorDto dto)
			throws CertificateDoesNotExistException;

	public PublicKeyDto getCAPublicKey();

	public CertificateListDto getRevokedCertificateList()
			throws UnableToProvideServiceException;

}
