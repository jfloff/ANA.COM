package pt.ist.ca.server;

import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.log4j.Logger;

import pt.ist.anacom.shared.security.EntityINFO;
import pt.ist.ca.service.bridge.CertificateAuthorityBridge;
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
import pt.ist.ca.shared.stubs.CertificateAuthorityPortType;
import pt.ist.ca.shared.stubs.CertificateAuthorityService;
import pt.ist.ca.shared.stubs.CertificateDoesNotExistRemoteException;
import pt.ist.ca.shared.stubs.CertificateListType;
import pt.ist.ca.shared.stubs.CertificateType;
import pt.ist.ca.shared.stubs.EmptyOperatorRemoteException;
import pt.ist.ca.shared.stubs.EmptyPublicKeyRemoteException;
import pt.ist.ca.shared.stubs.InvalidValidityRemoteException;
import pt.ist.ca.shared.stubs.OperatorCertificateInfoType;
import pt.ist.ca.shared.stubs.OperatorType;
import pt.ist.ca.shared.stubs.PublicKeyType;
import pt.ist.ca.shared.stubs.UnableToProvideServiceRemoteException;
import pt.ist.ca.shared.stubs.VoidType;

public class CertificateAuthorityServiceBridge implements CertificateAuthorityBridge {

    String organizationName = "CertificateAuthority";
    EntityINFO entityINFO;

    // UDDI Managers
    RegistryService registryService;
    BusinessQueryManager businessQueryManager;
    BusinessLifeCycleManager businessLifeCycleManager;

    public CertificateAuthorityServiceBridge(EntityINFO entityINFO) {

        this.entityINFO = entityINFO;
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

    public void setSecurityHandlers(CertificateAuthorityPortType port) {
        Binding binding = ((BindingProvider) port).getBinding();
        List<Handler> securityHandlerList = new ArrayList<Handler>();
        securityHandlerList.add(new ClientCAHandler(this.entityINFO));
        binding.setHandlerChain(securityHandlerList);
    }

    @Override
    public CertificateDto signCertificate(OperatorCertificateInfoDto dto) throws EmptyOperatorException,
            EmptyPublicKeyException,
            InvalidValidityException,
            UnableToProvideServiceException {

        CertificateAuthorityPortType port = getPort();
        OperatorCertificateInfoType operatorType = new OperatorCertificateInfoType();
        operatorType.setOperatorID(dto.getOperatorID());
        operatorType.setPublicKey(dto.getPublicKey());
        operatorType.setValidity(dto.getValidity());
        CertificateType certType = null;
        try {
            certType = port.signCertificate(operatorType);
        } catch (UnableToProvideServiceRemoteException e) {
            throw new UnableToProvideServiceException();
        } catch (EmptyPublicKeyRemoteException e) {
            throw new EmptyPublicKeyException();
        } catch (EmptyOperatorRemoteException e) {
            throw new EmptyOperatorException();
        } catch (InvalidValidityRemoteException e) {
            throw new InvalidValidityException(e.getFaultInfo().getValidity());
        }

        CertificateDto certificateDto = new CertificateDto(certType.getCertificateContents(), certType.getSignature());

        return certificateDto;
    }

    @Override
    public void revokeCertificate(OperatorDto dto) throws CertificateDoesNotExistException {

        CertificateAuthorityPortType port = getPort();
        setSecurityHandlers(port);

        OperatorType operatorType = new OperatorType();

        operatorType.setOperatorID(dto.getOperatorID());

        try {
            port.revokeCertificate(operatorType);
        } catch (CertificateDoesNotExistRemoteException e) {
            throw new CertificateDoesNotExistException(e.getFaultInfo().getOperatorID());
        }
    }

    @Override
    public CertificateListDto getRevokedCertificateList() throws UnableToProvideServiceException {

        CertificateAuthorityPortType port = getPort();
        setSecurityHandlers(port);

        CertificateListType certListType;
        try {
            certListType = port.getRevokedCertificateList(new VoidType());
        } catch (UnableToProvideServiceRemoteException e) {
            throw new UnableToProvideServiceException();
        }

        CertificateListDto certListDto = new CertificateListDto();

        for (CertificateType cert : certListType.getCertificateList())
            certListDto.add(cert.getCertificateContents(), cert.getSignature());

        return certListDto;
    }

    @Override
    public PublicKeyDto getCAPublicKey() {
        CertificateAuthorityPortType port = getPort();

        // setSecurityHandlers(port);

        PublicKeyType publicType = port.getCAPublicKey(new VoidType());

        PublicKeyDto publicDto = new PublicKeyDto(publicType.getPublicKey());

        return publicDto;

    }
}
