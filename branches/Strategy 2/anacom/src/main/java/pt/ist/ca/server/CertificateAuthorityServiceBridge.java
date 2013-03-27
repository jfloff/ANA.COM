package pt.ist.ca.server;

import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;

import org.apache.log4j.Logger;

import pt.ist.ca.service.bridge.CertificateAuthorityBridge;
import pt.ist.ca.shared.data.Data;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorAndKeyDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.ca.shared.dto.PublicKeyDto;
import pt.ist.ca.shared.stubs.CertificateAuthorityPortType;
import pt.ist.ca.shared.stubs.CertificateAuthorityService;
import pt.ist.ca.shared.stubs.CertificateListType;
import pt.ist.ca.shared.stubs.CertificateType;
import pt.ist.ca.shared.stubs.OperatorAndKeyType;
import pt.ist.ca.shared.stubs.OperatorType;
import pt.ist.ca.shared.stubs.PublicKeyType;


public class CertificateAuthorityServiceBridge implements CertificateAuthorityBridge {

    String organizationName = "CertificateAuthority";

    // UDDI Managers
    RegistryService registryService;
    BusinessQueryManager businessQueryManager;
    BusinessLifeCycleManager businessLifeCycleManager;


    public CertificateAuthorityServiceBridge() {

        // //////////////////////////////////////////////////////
        // Connection to UDDI Registry
        // //////////////////////////////////////////////////////
        try {
            ConnectionFactory connFactory = org.apache.ws.scout.registry.ConnectionFactoryImpl.newInstance();

            // Properties
            Properties props = new Properties();
            props.setProperty("scout.juddi.client.config.file", "uddi.xml");
            props.setProperty("javax.xml.registry.queryManagerURL", "http://localhost:8081/juddiv3/services/inquiry");
            props.setProperty("javax.xml.registry.lifeCycleManagerURL", "http://localhost:8081/juddiv3/services/publish");
            props.setProperty("javax.xml.registry.securityManagerURL", "http://localhost:8081/juddiv3/services/security");
            props.setProperty("scout.proxy.uddiVersion", "3.0");
            props.setProperty("scout.proxy.transportClass", "org.apache.juddi.v3.client.transport.JAXWSTransport");
            connFactory.setProperties(props);
            Connection connection = connFactory.createConnection();

            // Authentication
            PasswordAuthentication passwdAuth = new PasswordAuthentication("username", "password".toCharArray());
            Set<PasswordAuthentication> creds = new HashSet<PasswordAuthentication>();
            creds.add(passwdAuth);
            connection.setCredentials(creds);

            // Get Query Manager
            registryService = connection.getRegistryService();
            businessQueryManager = registryService.getBusinessQueryManager();
            businessLifeCycleManager = registryService.getBusinessLifeCycleManager();
        } catch (Exception e) {
            System.err.println("[jUDDI-ERROR] Error while connecting to jUDDI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private CertificateAuthorityPortType getPort() {

        try {

            // //////////////////////////////////////////////////////
            // Search for CA organization
            // //////////////////////////////////////////////////////

            Organization caOrg = null;

            Collection<String> findQualifiers = new ArrayList<String>();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);

            Collection<String> namePatterns = new ArrayList<String>();
            namePatterns.add("%" + organizationName + "%");

            // Make serch for organization name
            BulkResponse bulkResponse = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);
            Collection<Organization> searchedOrg = bulkResponse.getCollection();

            for (Organization org : searchedOrg) {
                if (org.getName().getValue().equals(organizationName)) {
                    Logger.getLogger(this.getClass()).info("Organization - " + org.getName().getValue());
                    caOrg = org;
                    break;
                }
            }

            // //////////////////////////////////////////////////////
            // Search for CA Service
            // //////////////////////////////////////////////////////
            String url = null;
            if (caOrg != null) {

                Collection<Service> findServices = caOrg.getServices();
                for (Service service : findServices) {
                    if (service.getName().getValue().equals("policia")) {
                        Logger.getLogger(this.getClass()).info("    Service - " + service.getName().getValue().toString());
                        // Get CA URL
                        Collection<ServiceBinding> bindingList = service.getServiceBindings();
                        for (ServiceBinding binding : bindingList) {
                            url = binding.getAccessURI();
                            System.out.println("URL: " + url);
                        }
                        break;
                    }
                }

            } else {
                System.err.println("[LOG]Anacom organization was not found in jUDDI!");
            }

            CertificateAuthorityService ca = new CertificateAuthorityService(new URL(url));
            CertificateAuthorityPortType port = ca.getCertificateAuthorityPort();
            return port;

        } catch (Exception e) {
            System.err.println("[jUDDI-ERROR] Error while getting replicas from jUDDI: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CertificateDto signCertificate(OperatorAndKeyDto dto) throws ParseException {

        CertificateAuthorityPortType port = getPort();
        OperatorAndKeyType operatorType = new OperatorAndKeyType();
        operatorType.setOperatorID(dto.getOperatorID());
        operatorType.setPublicKey(dto.getPublicKey());
        CertificateType certType = port.signCertificate(operatorType);
        CertificateDto certificateDto = new CertificateDto(certType.getSerialID(), certType.getOperatorID(), certType.getPublicKey(), Data.convertStringToDate(certType.getStartDate()), Data.convertStringToDate(certType.getEndDate()), certType.getSignature());

        return certificateDto;
    }

    @Override
    public void revokeCertificate(OperatorDto dto) {

        CertificateAuthorityPortType port = getPort();

        OperatorType operatorType = new OperatorType();

        operatorType.setOperatorID(dto.getOperatorID());

        port.revokeCertificate(operatorType);
    }


    @Override
    public CertificateListDto getOperatorRevokedCertificateList(OperatorDto dto) throws ParseException {

        CertificateAuthorityPortType port = getPort();

        OperatorType operatorType = new OperatorType();

        operatorType.setOperatorID(dto.getOperatorID());

        CertificateListType certListType = port.getOperatorRevokedCertificateList(operatorType);

        CertificateListDto certListDto = new CertificateListDto();

        for (CertificateType cert : certListType.getCertificateList())
            certListDto.add(cert.getSerialID(), cert.getOperatorID(), cert.getPublicKey(), Data.convertStringToDate(cert.getStartDate()), Data.convertStringToDate(cert.getEndDate()), cert.getSignature());

        return certListDto;
    }


    @Override
    public PublicKeyDto getCAPublicKey() {

        CertificateAuthorityPortType port = getPort();

        PublicKeyType publicType = port.getCAPublicKey();

        PublicKeyDto publicDto = new PublicKeyDto(publicType.getPublicKey());

        return publicDto;
    }
}
