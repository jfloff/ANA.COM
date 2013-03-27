package pt.ist.ca.service;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.shared.dto.PublicKeyDto;
import pt.ist.fenixframework.FenixFramework;

public class GetCAPublicKeyService extends CertificateAuthorityService {

    PublicKeyDto publicKeyDto;

    public GetCAPublicKeyService() {
    }

    @Override
    public void dispatch() {
        CertificateAuthority ca = FenixFramework.getRoot();
        String key = ca.getPublicKey();
        publicKeyDto = new PublicKeyDto(key);
    }

    public PublicKeyDto getGetCAServerPublicKeyServiceResult() {
        return publicKeyDto;
    }
}
