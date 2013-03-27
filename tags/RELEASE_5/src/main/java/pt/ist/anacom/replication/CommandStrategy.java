package pt.ist.anacom.replication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.log4j.Logger;

import pt.ist.anacom.presentationserver.handlers.ClientHandler;
import pt.ist.anacom.replication.quorumresponse.QuorumResponse;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.CouldNotSetStateWhileCommunicationActiveException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.InvalidTaxException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorNameAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorNullNameException;
import pt.ist.anacom.shared.exception.OperatorPrefixAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.OperatorPrefixWrongLengthException;
import pt.ist.anacom.shared.exception.PhoneAlreadyExistsException;
import pt.ist.anacom.shared.exception.PhoneAndOperatorPrefixDoNotMatchException;
import pt.ist.anacom.shared.exception.PhoneNumberWrongLengthException;
import pt.ist.anacom.shared.exception.PhoneStateException;
import pt.ist.anacom.shared.exception.ReplicasDownProtocolException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.AnacomService;
import pt.ist.anacom.shared.stubs.client.BalanceLimitExceededRemoteException;
import pt.ist.anacom.shared.stubs.client.CouldNotSetStateWhileCommunicationActiveRemoteException;
import pt.ist.anacom.shared.stubs.client.InsuficientBalanceRemoteException;
import pt.ist.anacom.shared.stubs.client.InvalidDurationRemoteException;
import pt.ist.anacom.shared.stubs.client.InvalidTaxRemoteException;
import pt.ist.anacom.shared.stubs.client.NegativeBalanceValueRemoteException;
import pt.ist.anacom.shared.stubs.client.NoActiveCommunicationRemoteException;
import pt.ist.anacom.shared.stubs.client.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorNameAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorNullNameRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorPrefixAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorPrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorPrefixWrongLengthRemoteException;
import pt.ist.anacom.shared.stubs.client.PhoneAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.client.PhoneAndOperatorPrefixDoNotMatchRemoteException;
import pt.ist.anacom.shared.stubs.client.PhoneNumberWrongLengthRemoteException;
import pt.ist.anacom.shared.stubs.client.PhoneStateRemoteException;

@SuppressWarnings("unchecked")
public abstract class CommandStrategy {

    // Managers
    protected final ArrayList<String> urlList;
    protected final BusinessQueryManager businessQueryManager;

    // Used by Quorums
    protected int nextVersion = 0;
    public static AtomicInteger quorumCounter;

    // Defined by GetReplicas(prefix)
    private int quorumLength = 0;
    private int replicaLength = 0;
    protected final String prefix;

    // Responses
    protected ArrayList<Object> handlerList;
    private final ArrayList<String> replicasDown = new ArrayList<String>();

    public CommandStrategy(String prefix, BusinessQueryManager businessQueryManager) throws MalformedURLException {
        this.businessQueryManager = businessQueryManager;
        this.urlList = getReplicaURL(prefix);
        this.nextVersion = getReplicaNextVersion(urlList);
        this.handlerList = new ArrayList<Object>();
        quorumCounter = new AtomicInteger(0);
        this.prefix = prefix;
        if (System.getProperty("mock", "false").equals("true")) {
            Logger.getLogger(this.getClass()).info("==========================================");
            Logger.getLogger(this.getClass()).info("===============> jMOCK?? <================");
            Logger.getLogger(this.getClass()).info("==========================================");
            quorumCounter.set(4);
            quorumLength = 3;
            replicaLength = 4;
        }
    }

    public abstract void sendToReplicas();

    public abstract void addResponse(String url, Object responseType);

    public abstract void execute();

    public void waitQuorum() {
        Logger.getLogger(this.getClass()).info("[jUDDI] Waiting for " + quorumLength + " quorum responses");
        do {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Logger.getLogger(this.getClass()).info("[ERR] Exception occurred while waitQuorum!");
                e.printStackTrace();
            }
            Logger.getLogger(this.getClass()).info(".." + quorumCounter + "..");
        } while (quorumCounter.get() < quorumLength);
    }

    public void addHandler(Object handler) {
        handlerList.add(handler);
        Logger.getLogger(this.getClass()).info("ADDED: " + handler);
    }

    protected AnacomPortType getPortType(String url) {
        AnacomService anacom;
        try {
            anacom = new AnacomService(new URL(url));
            return anacom.getAnacomPort();
        } catch (MalformedURLException e) {
            Logger.getLogger(this.getClass()).info("[QUORUM] Malformed url \"" + url + "\"");
            return null;
        }

    }

    public int increaseQuorumCounter() {
        return quorumCounter.incrementAndGet();
    }

    protected ArrayList<String> getReplicaURL(String prefix) {

        // Lists
        ArrayList<Service> replicaList;
        ArrayList<String> urlList = new ArrayList<String>();

        // Get all replicas
        replicaList = getReplicas(prefix);

        if (replicaList.isEmpty())
            throw new OperatorPrefixDoesNotExistException(prefix);
        // Get all replicas URL
        Collection<ServiceBinding> bindingList = null;
        for (Service replica : replicaList) {
            try {
                bindingList = replica.getServiceBindings();
                for (ServiceBinding binding : bindingList)
                    urlList.add(binding.getAccessURI());
            } catch (JAXRException e) {
                Logger.getLogger(this.getClass()).info("Could not get Binding for replica " + replica);
            }
        }


        Logger.getLogger(this.getClass()).info("URLLIST: " + urlList);
        return urlList;
    }

    protected ArrayList<Service> getReplicas(String prefix) {

        try {

            // //////////////////////////////////////////////////////
            // Search for organization
            // //////////////////////////////////////////////////////

            Organization anacomOrg = null;
            ArrayList<Service> operatorReplicas = new ArrayList<Service>();

            Collection<String> findQualifiers = new ArrayList<String>();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);

            Collection<String> namePatterns = new ArrayList<String>();
            namePatterns.add("%" + AnacomData.ORG_NAME + "%");

            // Make serch for organization name
            BulkResponse bulkResponse = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);
            Collection<Organization> searchedOrg = bulkResponse.getCollection();

            for (Organization org : searchedOrg) {
                if (org.getName().getValue().equals(AnacomData.ORG_NAME)) {
                    Logger.getLogger(this.getClass()).info("[jUDDI] Organization - " + org.getName().getValue());
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
                        Logger.getLogger(this.getClass()).info("[jUDDI]     Service - " + service.getName().getValue().toString());

                        // Update replicas length
                        replicaLength++;
                    }
                }

                // Update quorum length
                quorumLength = Math.max(1, replicaLength - 1);

                Logger.getLogger(this.getClass()).info("QUORUM = " + quorumLength);

            } else {
                Logger.getLogger(this.getClass()).info("[jUDDI] Anacom organization was not found in jUDDI!");
            }

            return operatorReplicas;

        } catch (Exception e) {
            Logger.getLogger(this.getClass()).info("[jUDDI-ERROR] Error while getting replicas from jUDDI: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    protected int getReplicaNextVersion(ArrayList<String> urlList) throws MalformedURLException {

        int maxVersion = 0;
        int replicaVersion = 0;

        // Get replica max version
        for (String url : urlList) {

            Logger.getLogger(this.getClass()).info("CONTACTAR -> URL: " + url);
            URL replicaURL = new URL(url);

            try {
                AnacomService anacom = new AnacomService(replicaURL);
                AnacomPortType port = anacom.getAnacomPort();
                replicaVersion = port.getReplicaVersion();
            } catch (Exception e) {
                Logger.getLogger(this.getClass()).info("Replica " + url + " down!");
                replicasDown.add(url);
            }

            Logger.getLogger(this.getClass()).info("[jUDDI] Get Replica: " + url + " Version: " + replicaVersion);

            if (replicaVersion > maxVersion)
                maxVersion = replicaVersion;
        }


        if (replicasDown.size() > 1) {
            throw new ReplicasDownProtocolException(replicasDown);
        }

        urlList.removeAll(replicasDown);

        // Return max version plus one
        maxVersion++;
        return maxVersion;


    }

    public void checkExceptions() {

        int counter = 0;
        Throwable throwable = null;

        try {

            for (Object quorumResponse : handlerList) {
                QuorumResponse response = (QuorumResponse) quorumResponse;
                Logger.getLogger(this.getClass()).info("CASTED: " + response);
                if (response.getException() != null) {
                    counter++;
                    throwable = response.getException().getCause();
                }
            }

            if (counter >= quorumLength && throwable != null)
                throw throwable;

        } catch (OperatorNullNameRemoteException e) {
            throw new OperatorNullNameException(e.getFaultInfo().getOperatorPrefix());

        } catch (OperatorPrefixWrongLengthRemoteException e) {
            throw new OperatorPrefixWrongLengthException(e.getFaultInfo().getOperatorName(), e.getFaultInfo().getOperatorPrefix());
        } catch (OperatorPrefixAlreadyExistsRemoteException e) {
            throw new OperatorPrefixAlreadyExistsException(e.getFaultInfo().getOperatorPrefix());
        } catch (InvalidTaxRemoteException e) {
            throw new InvalidTaxException(e.getFaultInfo().getOperatorPrefix());

        } catch (OperatorNameAlreadyExistsRemoteException e) {
            throw new OperatorNameAlreadyExistsException(e.getFaultInfo().getOperatorName());

        } catch (PhoneAlreadyExistsRemoteException e) {
            throw new PhoneAlreadyExistsException(e.getFaultInfo().getPhoneNumber());

        } catch (PhoneAndOperatorPrefixDoNotMatchRemoteException e) {
            throw new PhoneAndOperatorPrefixDoNotMatchException(e.getFaultInfo().getOperatorPrefix(), e.getFaultInfo().getPhoneNumber());

        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            throw new OperatorPrefixDoesNotExistException(e.getFaultInfo().getOperatorPrefix());

        } catch (PhoneNumberWrongLengthRemoteException e) {
            throw new PhoneNumberWrongLengthException(e.getFaultInfo().getPhoneNumber());

        } catch (NoSuchPhoneRemoteException e) {
            throw new NoSuchPhoneException(e.getFaultInfo().getPhoneNumber());

        } catch (NegativeBalanceValueRemoteException e) {
            throw new NegativeBalanceValueException(e.getFaultInfo().getPhoneNumber(), e.getFaultInfo().getBalance());

        } catch (BalanceLimitExceededRemoteException e) {
            throw new BalanceLimitExceededException(e.getFaultInfo().getPhoneNumber(), e.getFaultInfo().getBalance());

        } catch (InsuficientBalanceRemoteException e) {
            throw new InsuficientBalanceException(e.getFaultInfo().getPhoneNumber(), e.getFaultInfo().getBalance());

        } catch (PhoneStateRemoteException e) {
            throw new PhoneStateException(e.getFaultInfo().getPhoneNumber(), AnacomData.convertIntToStateEnum(e.getFaultInfo().getPhoneState()));

        } catch (NoActiveCommunicationRemoteException e) {
            throw new NoActiveCommunicationException(e.getFaultInfo().getSourcePhoneNumber(), e.getFaultInfo().getDestinationPhoneNumber());

        } catch (InvalidDurationRemoteException e) {
            throw new InvalidDurationException(e.getFaultInfo().getSourcePhoneNumber(), e.getFaultInfo().getDestinationPhoneNumber());

        } catch (CouldNotSetStateWhileCommunicationActiveRemoteException e) {
            throw new CouldNotSetStateWhileCommunicationActiveException(e.getFaultInfo().getPhoneNumber());

        } catch (Throwable e) {
            Logger.getLogger(this.getClass()).info("Unkown exception");
            System.exit(-1);
        }
    }

    protected String getOperatorPrefix(String phoneNumber) {
        return phoneNumber.substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    public int getQuorumLength() {
        return quorumLength;
    }

    public int getReplicaLength() {
        return replicaLength;
    }

    public String getReplicasNotResponded(Set<String> responseURLList, ArrayList<String> replicaURLList) {
        for (String replicaURL : replicaURLList) {
            if (!responseURLList.contains(replicaURL)) {
                return replicaURL;
            }
        }
        return null;
    }

    // SECURITY
    public void setSecurityHandlers(AnacomPortType port) {
        Binding binding = ((BindingProvider) port).getBinding();
        List<Handler> securityHandlerList = new ArrayList<Handler>();
        securityHandlerList.add(new ClientHandler());
        binding.setHandlerChain(securityHandlerList);
    }

}
