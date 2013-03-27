package pt.ist.phonebook.replication;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

import pt.ist.phonebook.service.bridge.ApplicationServerBridge;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;
import pt.ist.phonebook.shared.dto.PhoneBookDto;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;
import pt.ist.phonebook.shared.exception.NameAlreadyExistsException;
import pt.ist.phonebook.shared.stubs.client.ContactDetailedReqType;
import pt.ist.phonebook.shared.stubs.client.ContactDoesNotExistException_Exception;
import pt.ist.phonebook.shared.stubs.client.ContactSimpleReqType;
import pt.ist.phonebook.shared.stubs.client.NameAlreadyExistsException_Exception;
import pt.ist.phonebook.shared.stubs.client.PhoneBookApplicationServerPortType;
import pt.ist.phonebook.shared.stubs.client.Phonebook;

@SuppressWarnings("unchecked")
public class FrontEnd implements ApplicationServerBridge {

    // TODO Have this name global
    String organizationName = "anacom3";

    // Quorum counter
    public static int quorumCounter = 0;
    private final int quorumLength = 3;
    private final int replicaNumber = 4;

    // Responses
    private static ConcurrentHashMap<String, ContactDetailedReqType> contactDetailedResponses = new ConcurrentHashMap<String, ContactDetailedReqType>();

    public static void addContactDetailedResponses(String replicaURL, ContactDetailedReqType replicaResponse) {
        contactDetailedResponses.put(replicaURL, replicaResponse);
    }


    // UDDI Managers
    RegistryService registryService;
    BusinessQueryManager businessQueryManager;
    BusinessLifeCycleManager businessLifeCycleManager;

    public FrontEnd() {

        try {

            // //////////////////////////////////////////////////////
            // Connection to UDDI Registry
            // //////////////////////////////////////////////////////

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

    private ArrayList<Service> getReplicas(String prefix) {

        try {

            // //////////////////////////////////////////////////////
            // Search for organization
            // //////////////////////////////////////////////////////

            Organization anacomOrg = null;
            ArrayList<Service> operatorReplicas = new ArrayList<Service>();

            Collection<String> findQualifiers = new ArrayList<String>();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);

            Collection<String> namePatterns = new ArrayList<String>();
            namePatterns.add("%" + organizationName + "%");

            // Make serch for organization name
            BulkResponse bulkResponse = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);
            Collection<Organization> searchedOrg = bulkResponse.getCollection();

            for (Organization org : searchedOrg) {
                if (org.getName().getValue().equals(organizationName)) {
                    System.out.println("[jUDDI] Organization - " + org.getName().getValue());
                    anacomOrg = org;
                    break;
                }
            }

            // //////////////////////////////////////////////////////
            // Search for replicas
            // //////////////////////////////////////////////////////

            if (anacomOrg != null) {

                Collection<Service> findServices = anacomOrg.getServices();

                for (Service service : findServices) {
                    if (service.getName().getValue().startsWith(prefix)) {
                        operatorReplicas.add(service);
                        System.out.println("[jUDDI]     Service - " + service.getName().getValue().toString());
                    }
                }

            } else {
                System.err.println("[jUDDI] Anacom organization was not found in jUDDI!");
            }

            return operatorReplicas;

        } catch (Exception e) {
            System.err.println("[jUDDI-ERROR] Error while getting replicas from jUDDI: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<String> getReplicaURL(String prefix) throws JAXRException {

        // Lists
        ArrayList<Service> replicaList;
        ArrayList<String> urlList = new ArrayList<String>();

        // Get all replicas
        replicaList = getReplicas(prefix);

        // Get all replicas URL
        for (Service replica : replicaList) {
            Collection<ServiceBinding> bindingList = replica.getServiceBindings();
            for (ServiceBinding binding : bindingList) {
                urlList.add(binding.getAccessURI());
            }
        }

        return urlList;
    }

    public BigInteger getReplicaNextVersion(ArrayList<String> urlList) throws MalformedURLException {

        Integer maxVersion = 0;

        // Get replica max version
        for (String url : urlList) {

            Phonebook phonebook = new Phonebook(new URL(url));
            PhoneBookApplicationServerPortType port = phonebook.getPhoneBookApplicationServicePort();
            BigInteger replicaVersion = port.getReplicaVersion();

            System.out.println("[jUDDI] Replica: " + url + " Version: " + replicaVersion.intValue());

            if (replicaVersion.intValue() > maxVersion)
                maxVersion = replicaVersion.intValue();
        }

        // Return max version plus one
        maxVersion++;
        return new BigInteger("" + maxVersion);
    }

    //
    // WRITE
    //

    @Override
    public void createContact(ContactDetailedDto dto) {

        try {

            // TODO Vai buscar o prefixo ao DTO
            String prefix = dto.getPhoneNumber().toString().substring(0, 2);
            quorumCounter = 0;

            // AsyncHandlers List
            ArrayList<QuorumVersionResponse> quorumResponseList = new ArrayList<QuorumVersionResponse>();

            // Get all replicas URL
            ArrayList<String> urlList = getReplicaURL(prefix);

            // Get max version
            BigInteger nextVersion = getReplicaNextVersion(urlList);

            // Setup
            PhoneBookApplicationServerPortType port = null;

            ContactDetailedReqType contactDetailedRequest = new ContactDetailedReqType();
            contactDetailedRequest.setName(dto.getName());
            contactDetailedRequest.setPhoneNumber(new BigInteger("" + dto.getPhoneNumber()));
            contactDetailedRequest.setVersion(nextVersion);

            // Send request to all replicas
            for (String url : urlList) {

                port = getPortType(url);

                System.out.println("[QUORUM] createContactAsync(" + dto.getName() + ", " + dto.getPhoneNumber() + ", " + nextVersion + ")");

                // Async Replica request
                QuorumVersionResponse handlerResponse = new QuorumVersionResponse(url);
                quorumResponseList.add(handlerResponse);
                port.createContactAsync(contactDetailedRequest, handlerResponse);

            }

            System.out.println("[jUDDI] Waiting for quorum response");

            // Wait for Quorum acknowledge
            waitQuorum();
            
            System.out.println("[QUORUM] Finished waiting");
            System.out.println("[QUORUM] Checking for Exceptions...");

            // Checks if some handler had a exception
            checkExceptions(quorumResponseList);

        } catch (JAXRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //
    // GET
    //

    @Override
    public ContactDetailedDto getContact(ContactSimpleDto dto) {

        try {
            String prefix = "91";
            quorumCounter = 0;
            ArrayList<QuorumContactDetailedResponse> quorumResponseList = new ArrayList<QuorumContactDetailedResponse>();
            ArrayList<String> urlList = getReplicaURL(prefix);
            BigInteger nextVersion = getReplicaNextVersion(urlList);
            contactDetailedResponses.clear();
            PhoneBookApplicationServerPortType port = null;
            ContactSimpleReqType contactSimpleReqTypeRequest = new ContactSimpleReqType();
            contactSimpleReqTypeRequest.setName(dto.getName());
            contactSimpleReqTypeRequest.setVersion(nextVersion);

            for (String url : urlList) {
                System.out.println("[QUORUM] getContactAsynch(" + dto.getName() + ", " + nextVersion + ")");
                port = getPortType(url); // Get replica Port

                // Async Replica request
                QuorumContactDetailedResponse handlerResponse = new QuorumContactDetailedResponse(url);
                quorumResponseList.add(handlerResponse);
                port.getContactAsync(contactSimpleReqTypeRequest, handlerResponse);
            }

            // Wait for Quorum acknowledge
            waitQuorum();
            System.out.println("[QUORUM] Finished waiting");
            System.out.println("[QUORUM] Checking for Exceptions...");

            // TODO Provavelmente vai ter de contar o nº de excepcoes
            checkExceptions(quorumResponseList);

            System.out.println("[QUORUM] ...No Exceptions, voting for best response");

            // Create Vote List
            VoteList voteList = new VoteList(contactDetailedResponses.values(), contactDetailedResponses.keySet());


            /*
             * if (voteList.getVoteList().size() == 1) {
             * System.out.println("[QUORUM] Simulating 1 delay and 1 bysantine fault");
             * Vote vote = voteList.getVoteList().get(0); vote.setCounter(1);
             * voteList.addVote(vote.getVersion() - 1, vote.getResponse());
             * voteList.addVote(vote.getVersion() + 1, vote.getResponse()); }
             */

            System.out.println("[QUORUM] voteList -> " + voteList);

            // Get the best response based on Voting
            ContactDetailedReqType responseContacDetailedReqType = (ContactDetailedReqType) voteList.getBestResponse(quorumLength);

            System.out.println("[QUORUM] Response after bestResponse -> " + responseContacDetailedReqType);

            // Return best vote or find again with more replicas
            if (responseContacDetailedReqType == null) {

                // If we have all replicas
                if (voteList.getVoteList().size() == replicaNumber) {
                    System.out.println("[QUORUM] Got all responses from all servers");
                    ContactDetailedReqType contactReqType = (ContactDetailedReqType) voteList.getBestResponse(replicaNumber);
                    if (contactReqType == null) {
                        // TODO
                        System.out.println("[ERR] JA FOSTE!");
                        System.exit(-1);
                    }
                }

                else {
                    System.out.println("[EXTRA-QUORUM] Getting last server info");
                    String lastServerURL = getReplicasNotResponded(contactDetailedResponses.keySet(), urlList);
                    System.out.println("[EXTRA-QUORUM] Last Server : " + lastServerURL);

                    port = getPortType(lastServerURL);

                    System.out.println("[QUORUM] getContact(" + dto.getName() + ", " + nextVersion + ")");

                    // Sync Replica request
                    responseContacDetailedReqType = port.getContact(contactSimpleReqTypeRequest);

                    // Save vote
                    voteList.addVote(responseContacDetailedReqType.getVersion().intValue(), responseContacDetailedReqType);

                    // Vote again
                    responseContacDetailedReqType = (ContactDetailedReqType) voteList.getBestResponse(replicaNumber);

                    if (responseContacDetailedReqType == null) {
                        System.out.println("[ERR] JA FOSTE");
                        System.exit(-1);
                    }
                }
            }
            return new ContactDetailedDto(responseContacDetailedReqType.getName(), responseContacDetailedReqType.getPhoneNumber().intValue());

        } catch (ContactDoesNotExistException_Exception e) {
            throw new ContactDoesNotExistException(e.getFaultInfo().getName());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Basicamente devolve um url se ele estiver na urlList e não estiver na
     * replicaUrlList
     * 
     * @param responseURLList
     * @param replicaURLList
     * @return
     */
    private String getReplicasNotResponded(Set<String> responseURLList, ArrayList<String> replicaURLList) {
        for (String replicaURL : replicaURLList) {
            if (!responseURLList.contains(replicaURL)) {
                return replicaURL;
            }
        }
        return null;
    }

    @Override
    public List<ContactSimpleDto> search(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeContact(ContactSimpleDto dto) throws ContactDoesNotExistException {
        // TODO Auto-generated method stub

    }

    @Override
    public PhoneBookDto listContacts() {
        // TODO Auto-generated method stub
        return null;
    }

    public void listReplicas() {

        try {

            // ////////////////////////////////////////////////////
            // List all Organizations and Services
            // //////////////////////////////////////////////////////

            Collection<String> findQualifiers = new ArrayList<String>();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
            Collection<String> namePatterns = new ArrayList<String>();
            namePatterns.add("%" + organizationName + "%");

            // Make serch for organization name
            BulkResponse findResponse = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);

            if (findResponse.getStatus() == JAXRResponse.STATUS_SUCCESS) {
                Collection<Organization> findOrgs = findResponse.getCollection();

                for (Organization org : findOrgs) {
                    if (org != null) {
                        if (org.getName().getValue() != null) {
                            System.out.println("[jUDDI] Organization: " + org.getName().getValue().toString());

                            Collection<Service> findServices = org.getServices();
                            for (Service serv : findServices) {
                                System.out.println("[jUDDI]     -   Service: " + serv.getName().getValue().toString());

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    // NEW FUNQS

    private void checkExceptions(ArrayList<? extends QuorumResponse> quorumResponseList) {
        for (QuorumResponse quorumResponse : quorumResponseList)
            if (quorumResponse.getException() != null) {
                Exception exception = (Exception) quorumResponse.getException();
                if (exception instanceof ContactDoesNotExistException_Exception)
                    throw new ContactDoesNotExistException(((ContactDoesNotExistException_Exception) exception).getFaultInfo().getName());
                if (exception instanceof NameAlreadyExistsException_Exception)
                    throw new NameAlreadyExistsException(((NameAlreadyExistsException_Exception) exception).getFaultInfo().getDuplicateName());
                else {
                    System.out.println(exception.getMessage());
                    System.exit(-1);
                }
            }
    }
    
    private PhoneBookApplicationServerPortType getPortType(String url) throws MalformedURLException {
        Phonebook phonebook = new Phonebook(new URL(url));
        return phonebook.getPhoneBookApplicationServicePort();
    }

    private void waitQuorum() {
        System.out.print("[jUDDI] Waiting for " + quorumCounter + " quorum responses");
        while (quorumCounter < quorumLength) {
            System.out.print(".." + quorumCounter + "..");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("[ERR] Exception Occurred 1");
                e.printStackTrace();
            }
        }
    }

}
