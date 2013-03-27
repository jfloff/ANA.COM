package pt.ist.ca.applicationserver;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Properties;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.service.GetCAPublicKeyService;
import pt.ist.ca.service.GetOperatorRevokedCertificateListService;
import pt.ist.ca.service.RevokeCertificateService;
import pt.ist.ca.service.SignCertificateService;
import pt.ist.ca.shared.data.Data;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.OperatorAndKeyDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.ca.shared.dto.PublicKeyDto;
import pt.ist.ca.shared.stubs.CertificateAuthorityPortType;
import pt.ist.ca.shared.stubs.CertificateListType;
import pt.ist.ca.shared.stubs.CertificateType;
import pt.ist.ca.shared.stubs.OperatorAndKeyType;
import pt.ist.ca.shared.stubs.OperatorType;
import pt.ist.ca.shared.stubs.PublicKeyType;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;

@javax.jws.WebService(endpointInterface = "pt.ist.ca.shared.stubs.CertificateAuthorityPortType", wsdlLocation = "/ca.wsdl", name = "CertificateAuthorityPortType", portName = "CertificateAuthorityPort", targetNamespace = "http://pt.ist.ca.essd.0403", serviceName = "CertificateAuthorityService")
public class CertificateAuthorityWebService implements CertificateAuthorityPortType {

    public static void init(final String caName, final String file) {
        System.out.println(".............STARTING Certificate Authority " + caName + " SERVER...........");

        // initializes the Fenix Framework
        try {

            FenixFramework.initialize(new Config() {
                {
                    dbAlias = "/tmp/db" + caName;
                    domainModelPath = "/tmp/ca.dml";
                    repositoryType = RepositoryType.BERKELEYDB;
                    rootClass = CertificateAuthority.class;
                }
            });

            Properties properties = Data.readPropertiesFile(file);
            String publicPath = properties.getProperty("publicKeyPath") + "CApublic.txt";
            String privatePath = properties.getProperty("privateKeyPath") + "CAprivate.txt";
            PublicKey pubKey = Data.readPublicKeys(publicPath);
            PrivateKey privKey = Data.readPrivateKeys(privatePath);

            boolean committed = false;
            try {
                Transaction.begin();
                CertificateAuthority ca = FenixFramework.getRoot();
                ca.addKeys(pubKey, privKey);
                Transaction.commit();
                committed = true;
            } finally {
                if (!committed) {
                    Transaction.abort();
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to initialize the Certificate Authority server.\n");
        }
    }

    @Override
    public CertificateType signCertificate(OperatorAndKeyType signCertificateInput) {

        OperatorAndKeyDto dto = new OperatorAndKeyDto(signCertificateInput.getOperatorID(), signCertificateInput.getPublicKey(), 20);
        SignCertificateService service = new SignCertificateService(dto);
        CertificateType certType = new CertificateType();
        try {
            service.execute();
            CertificateDto cert = service.getSignCertificateServiceResult();
            certType.setSerialID(cert.getSerialID());
            certType.setOperatorID(cert.getOperatorID());
            certType.setPublicKey(cert.getPublicKey());
            certType.setStartDate(Data.convertDateToString(cert.getStartDate()));
            certType.setEndDate(Data.convertDateToString(cert.getEndDate()));
            certType.setSignature(cert.getSignature());
            return certType;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void revokeCertificate(OperatorType revokeCertificateInput) {

        OperatorDto dto = new OperatorDto(revokeCertificateInput.getOperatorID());
        RevokeCertificateService service = new RevokeCertificateService(dto);

        try {
            service.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CertificateListType getOperatorRevokedCertificateList(OperatorType getOperatorRevokedCertificateListInput) {

        OperatorDto dto = new OperatorDto(getOperatorRevokedCertificateListInput.getOperatorID());
        GetOperatorRevokedCertificateListService service = new GetOperatorRevokedCertificateListService(dto);
        CertificateListType certListType = new CertificateListType();
        try {
            service.execute();

            // fill in dto arrayList
            for (CertificateDto certificate : service.getGetRevokedListServiceResult().getCertificateList()) {
                CertificateType certificateType = new CertificateType();
                certificateType.setSerialID(certificate.getSerialID());
                certificateType.setOperatorID(certificate.getOperatorID());
                certificateType.setPublicKey(certificate.getPublicKey());
                certificateType.setStartDate(Data.convertDateToString(certificate.getStartDate()));
                certificateType.setEndDate(Data.convertDateToString(certificate.getEndDate()));
                certificateType.setSignature(certificate.getSignature());
                certListType.getCertificateList().add(certificateType);
            }

            return certListType;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PublicKeyType getCAPublicKey() {

        GetCAPublicKeyService service = new GetCAPublicKeyService();
        PublicKeyType keyType = new PublicKeyType();
        try {
            service.execute();
            PublicKeyDto keyDto = service.getGetCAServerPublicKeyServiceResult();
            keyType.setPublicKey(keyDto.getPublicKey());
            return keyType;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
