package pt.ist.phonebook.applicationserver;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.JAXRException;
import javax.xml.registry.JAXRResponse;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;

import pt.ist.fenixframework.pstm.repository.Repository;

public class ApplicationServerInitListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

        System.out.println("\n.........Entrou no context destroyed listener\n");
        Repository rep = Repository.getRepository();
        rep.closeRepository();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            
            String operatorPrefix = arg0.getServletContext().getInitParameter("operatorPrefix");
            String replicaId = arg0.getServletContext().getInitParameter("replicaId");
            
            System.out.println("[jUDDI] Initializing Replica " + operatorPrefix + " - " + replicaId);

            // Initialize WebService
            ApplicationServerWebService.init(operatorPrefix, replicaId);
            
            // //////////////////////////////////////////////////////
            // Connection to UDDI Registry
            // //////////////////////////////////////////////////////
            
            ConnectionFactory connFactory = org.apache.ws.scout.registry.ConnectionFactoryImpl.newInstance();


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
            RegistryService rs = connection.getRegistryService();
            BusinessQueryManager businessQueryManager = rs.getBusinessQueryManager();
            BusinessLifeCycleManager businessLifeCycleManager = rs.getBusinessLifeCycleManager();


            // //////////////////////////////////////////////////////
            // Search for organization
            // //////////////////////////////////////////////////////

            Collection<String> findQualifiers = new ArrayList<String>();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);

            String organizationName = "anacom3";
            String serviceName = operatorPrefix + "-" + replicaId;
            String bindingDescription = "OperatorPrefix: " + operatorPrefix + " ReplicaId:" + replicaId;
            String bindingURL = "http://localhost:8080/" + operatorPrefix + "-" + replicaId + "/" + operatorPrefix + "?wsdl";

            Organization newOrg = null;
            Collection<String> namePatterns = new ArrayList<String>();
            namePatterns.add("%" + organizationName + "%");

            // Make serch for organization name
            BulkResponse response = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);
            Collection<Organization> searchedOrg = response.getCollection();

            for (Organization org : searchedOrg) {
                if (org.getName().getValue().equals(organizationName)) {
                    System.out.println("[jUDDI] Organization \"" + org.getName().getValue() + "\" was found");
                    newOrg = org;
                    break;
                }
            }

            // //////////////////////////////////////////////////////
            // Registry entities in UDDI
            // //////////////////////////////////////////////////////

            // If organization does not exist
            if (newOrg == null) {
                // Add organization
                System.out.println("[jUDDI] Creating a new organization \"" + organizationName + "\"");
                newOrg = businessLifeCycleManager.createOrganization(organizationName);
            }

            // If services does not exist
            if (!serviceAlreadyExists(serviceName, newOrg.getServices())) {

                // Add services if needed
                Service service = businessLifeCycleManager.createService(serviceName);
                service.setDescription(businessLifeCycleManager.createInternationalString(serviceName));

                System.out.println("[jUDDI] Adding new service \"" + service.getName().getValue() + "\" to \"" + newOrg.getName().getValue());
                newOrg.addService(service);

                // Binding Service
                ServiceBinding serviceBinding = businessLifeCycleManager.createServiceBinding();
                serviceBinding.setDescription(businessLifeCycleManager.createInternationalString(bindingDescription));
                serviceBinding.setValidateURI(false);
                serviceBinding.setAccessURI(bindingURL);
                service.addServiceBinding(serviceBinding);

                Collection<Organization> newOrgList = new ArrayList<Organization>();
                newOrgList.add(newOrg);

                // Save new organization list
                BulkResponse br = businessLifeCycleManager.saveOrganizations(newOrgList);

                if (br.getStatus() == JAXRResponse.STATUS_SUCCESS) {
                    System.out.println("[jUDDI] Service \"" + serviceName + "\" successfully registered");
                } else {
                    System.err.println("[jUDDI-ERROR] Error while registering service \"" + serviceName + "\" in \"" + organizationName);
                }

            }
            
            System.out.println("[jUDDI] Replica " + operatorPrefix + " - " + replicaId + " ready!");

        } catch (JAXRException e) {
            System.err.println("[jUDDI-ERROR] Error while initializing server: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.println("[jUDDI-ERROR] Error while initializing server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean serviceAlreadyExists(String serviceName, Collection<Service> services) throws JAXRException {
        for (Service service : services)
            if (service.getName().getValue().equals(serviceName))
                return true;
        return false;
    }
}
