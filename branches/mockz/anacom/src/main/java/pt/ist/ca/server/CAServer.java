package pt.ist.ca.server;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.service.bridge.CertificateAuthorityBridge;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorCertificateInfoDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.ca.shared.dto.PublicKeyDto;
import pt.ist.ca.shared.exception.CertificateAuthorityException;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.shared.SecurityData;

public class CAServer {

    public static CertificateAuthorityBridge serviceBridge = null;

    private static boolean notInitialized = true;

    public void init() {

        // serviceBridge = new CertificateAuthorityServiceBridge("TESTE");

        if (notInitialized) {
            FenixFramework.initialize(new Config() {
                {
                    dbAlias = "test-db";
                    domainModelPath = "src/main/dml/ca.dml";
                    repositoryType = RepositoryType.BERKELEYDB;
                    rootClass = CertificateAuthority.class;
                }
            });
        }
        notInitialized = false;
    }

    private void ourMain() {
        KeyPair pair;
        try {
            pair = SecurityData.generateKeys("RSA", 1024);
            CertificateDto dto = signCertificateCommand("", SecurityData.encode64(pair.getPublic().getEncoded()), 20);
            System.out.println("CERTIFICATE " + dto.toString());

            revokeCertificateCommand("Server1");
            System.out.println(getOperatorRevokedCertificateListCommand("Server1").getCertificateList());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private CertificateListDto getOperatorRevokedCertificateListCommand(String operatorID) {
        try {
            OperatorDto dto = new OperatorDto(operatorID);
            CertificateListDto listDto = null;
            listDto = serviceBridge.getOperatorRevokedCertificateList(dto);
            return listDto;
        } catch (CertificateAuthorityException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void revokeCertificateCommand(String operatorID) {
        try {
            OperatorDto dto = new OperatorDto(operatorID);
            serviceBridge.revokeCertificate(dto);
        } catch (CertificateAuthorityException e) {
            System.err.println(e.getMessage());
        }
    }

    private PublicKeyDto getCAPublicKeyCommand() {
        PublicKeyDto dto = serviceBridge.getCAPublicKey();
        return dto;
    }

    private CertificateDto signCertificateCommand(String operatorID, String publicKey, int validity) {

        try {
            OperatorCertificateInfoDto dto = new OperatorCertificateInfoDto(operatorID, publicKey, validity);
            return serviceBridge.signCertificate(dto);
        } catch (CertificateAuthorityException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void main(final String[] args) {

        CAServer ps = new CAServer();
        // ps.init();
        // serviceBridge = new CertificateAuthorityServiceBridge();
        ps.ourMain();
    }
}
