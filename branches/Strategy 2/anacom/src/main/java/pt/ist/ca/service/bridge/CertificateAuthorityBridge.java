package pt.ist.ca.service.bridge;

import java.text.ParseException;

import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorAndKeyDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.ca.shared.dto.PublicKeyDto;

public interface CertificateAuthorityBridge {

    public CertificateDto signCertificate(OperatorAndKeyDto dto) throws ParseException;

    public void revokeCertificate(OperatorDto dto);

    public PublicKeyDto getCAPublicKey();

    public CertificateListDto getOperatorRevokedCertificateList(OperatorDto dto) throws ParseException;

}
