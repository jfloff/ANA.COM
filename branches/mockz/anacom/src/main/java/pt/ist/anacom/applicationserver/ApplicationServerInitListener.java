package pt.ist.anacom.applicationserver;

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

import org.apache.log4j.Logger;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.fenixframework.pstm.repository.Repository;

public class ApplicationServerInitListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    
        System.out.println("\n.........Entrou no context destroyed listener\n");
        pt.ist.fenixframework.pstm.TransactionChangeLogs.finalizeTransactionSystem();
        Repository rep = Repository.getRepository();
        rep.closeRepository();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            // Parameters
            String operatorPrefix = arg0.getServletContext().getInitParameter("operatorPrefix");
            String replicaId = arg0.getServletContext().getInitParameter("replicaId");
            String getRealPath = arg0.getServletContext().getRealPath("/WEB-INF/config/build.properties");

            Logger.getLogger(this.getClass()).info("Starting Operator " + operatorPrefix + " with id " + replicaId);

            // Initialize WebService
            ApplicationServerWebService.init(operatorPrefix, replicaId, getRealPath);

            Logger.getLogger(this.getClass()).info("Replica " + operatorPrefix + " - " + replicaId + " was initialized");

            ConnectionFactory connFactory = org.apache.ws.scout.registry.ConnectionFactoryImpl.newInstance();

            // //////////////////////////////////////////////////////
            // Connection to UDDI Registry
            // //////////////////////////////////////////////////////

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

            String serviceName = operatorPrefix + "-" + replicaId;
            String bindingDescription = "OperatorPrefix: " + operatorPrefix + " ReplicaId:" + replicaId;
            String bindingURL = "http://localhost:8080/" + operatorPrefix + "-" + replicaId + "/" + operatorPrefix + "?wsdl";

            Organization newOrg = null;
            Collection<String> namePatterns = new ArrayList<String>();
            namePatterns.add("%" + AnacomData.ORG_NAME + "%");

            // Make serch for organization name
            BulkResponse response = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);
            Collection<Organization> searchedOrg = response.getCollection();

            for (Organization org : searchedOrg) {
                if (org.getName().getValue().equals(AnacomData.ORG_NAME)) {
                    Logger.getLogger(this.getClass()).info("Organization \"" + org.getName().getValue() + "\" was found");
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
                Logger.getLogger(this.getClass()).info("Creating a new organization \"" + AnacomData.ORG_NAME + "\"");
                newOrg = businessLifeCycleManager.createOrganization(AnacomData.ORG_NAME);
            }

            // If services does not exist
            if (!serviceAlreadyExists(serviceName, newOrg.getServices())) {

                // Add services if needed
                Service service = businessLifeCycleManager.createService(serviceName);
                service.setDescription(businessLifeCycleManager.createInternationalString(serviceName));

                Logger.getLogger(this.getClass()).info("Adding new service \"" + service.getName().getValue() + "\" to \""
                        + newOrg.getName().getValue());
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

                Logger.getLogger(this.getClass()).info("Replica " + operatorPrefix + " - " + replicaId + " ready!");

                if (br.getStatus() == JAXRResponse.STATUS_SUCCESS) {
                    Logger.getLogger(this.getClass()).info("Service \"" + serviceName + "\" successfully registered");
                } else {
                    System.err.println("[jUDDI-ERROR] Error while registering service \"" + serviceName + "\" in \"" + AnacomData.ORG_NAME);
                }

            }

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
