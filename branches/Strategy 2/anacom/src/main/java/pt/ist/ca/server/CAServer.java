package pt.ist.ca.server;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.service.bridge.CertificateAuthorityBridge;
import pt.ist.ca.shared.data.Data;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorAndKeyDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.ca.shared.dto.PublicKeyDto;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.Config.RepositoryType;
import pt.ist.fenixframework.FenixFramework;

public class CAServer {

    public static CertificateAuthorityBridge serviceBridge = null;

    private static boolean notInitialized = true;

    public void init() {

        serviceBridge = new CertificateAuthorityServiceBridge();

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
            pair = Data.generateKeys("RSA", 1024);
            CertificateDto dto = signCertificateCommand("Server1", Data.encode64(pair.getPublic().getEncoded()), 20);
            System.out.println("CERTIFICATE " + dto.toString());

            revokeCertificateCommand("Server1");
            System.out.println(getOperatorRevokedCertificateListCommand("Server1").getCertificateList());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private CertificateListDto getOperatorRevokedCertificateListCommand(String operatorID) {
        OperatorDto dto = new OperatorDto(operatorID);
        CertificateListDto listDto = null;
        try {
            listDto = serviceBridge.getOperatorRevokedCertificateList(dto);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return listDto;
    }

    private void revokeCertificateCommand(String operatorID) {
        OperatorDto dto = new OperatorDto(operatorID);
        serviceBridge.revokeCertificate(dto);
    }

    private PublicKeyDto getCAPublicKeyCommand() {
        PublicKeyDto dto = serviceBridge.getCAPublicKey();
        return dto;
    }

    private CertificateDto signCertificateCommand(String operatorID, String publicKey, long validity) {

        OperatorAndKeyDto dto = new OperatorAndKeyDto(operatorID, publicKey, validity);

        try {
            return serviceBridge.signCertificate(dto);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void main(final String[] args) {

        CAServer ps = new CAServer();
        // ps.init();
        serviceBridge = new CertificateAuthorityServiceBridge();
        ps.ourMain();
    }
}
