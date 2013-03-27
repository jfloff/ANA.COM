package pt.ist.anacom.presentationserver.server;

import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
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
import javax.xml.registry.JAXRException;
import javax.xml.registry.JAXRResponse;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.commands.CancelRegisteredPhoneStrategy;
import pt.ist.anacom.replication.commands.EndReceiveVoiceCallStrategy;
import pt.ist.anacom.replication.commands.EndSendVoiceCallStrategy;
import pt.ist.anacom.replication.commands.GetBalanceAndPhoneListStrategy;
import pt.ist.anacom.replication.commands.GetLastCommunicationStrategy;
import pt.ist.anacom.replication.commands.GetPhoneBalanceStrategy;
import pt.ist.anacom.replication.commands.GetPhoneStateStrategy;
import pt.ist.anacom.replication.commands.GetSMSPhoneReceivedListStrategy;
import pt.ist.anacom.replication.commands.IncreaseBalanceStrategy;
import pt.ist.anacom.replication.commands.ReceivedSMSStrategy;
import pt.ist.anacom.replication.commands.RegisterOperatorStrategy;
import pt.ist.anacom.replication.commands.RegisterPhoneStrategy;
import pt.ist.anacom.replication.commands.SendSMSStrategy;
import pt.ist.anacom.replication.commands.SetPhoneStateStrategy;
import pt.ist.anacom.replication.commands.StartReceiveVoiceCallStrategy;
import pt.ist.anacom.replication.commands.StartSendVoiceCallStrategy;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.InvalidTaxException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
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
import pt.ist.anacom.shared.exception.SMSInvalidTextException;
import pt.ist.anacom.shared.stubs.server.AnacomPortType;
import pt.ist.anacom.shared.stubs.server.AnacomService;

public class ApplicationServerBridgeDistSoft implements ApplicationServerBridge {
    // UDDI Managers
    RegistryService registryService;
    BusinessQueryManager businessQueryManager;
    BusinessLifeCycleManager businessLifeCycleManager;

    public ApplicationServerBridgeDistSoft() {

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


    public String getOperatorPrefix(String phoneNumber) {
        return phoneNumber.substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
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
            namePatterns.add("%" + AnacomData.ORG_NAME + "%");

            // Make serch for organization name
            BulkResponse bulkResponse = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);
            Collection<Organization> searchedOrg = bulkResponse.getCollection();

            for (Organization org : searchedOrg) {
                if (org.getName().getValue().equals(AnacomData.ORG_NAME)) {
                    Logger.getLogger(this.getClass()).info("[ANACOM] Organization - " + org.getName().getValue());
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
                        Logger.getLogger(this.getClass()).info("[ANACOM]     Service - " + service.getName().getValue().toString());
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


        System.out.println("URLLIST: " + urlList);
        return urlList;
    }

    public int getReplicaNextVersion(ArrayList<String> urlList) throws MalformedURLException {

        int maxVersion = 0;

        // Get replica max version
        for (String url : urlList) {
            System.out.println("URL: " + url);
            AnacomService anacom = new AnacomService(new URL(url));
            AnacomPortType port = anacom.getAnacomPort();
            int replicaVersion = port.getReplicaVersion();

            Logger.getLogger(this.getClass()).info("[ANACOM] Get Replica: " + url + " Version: " + replicaVersion);

            if (replicaVersion > maxVersion)
                maxVersion = replicaVersion;
        }

        // Return max version plus one
        maxVersion++;
        return maxVersion;
    }


    public void listReplicas() {

        try {

            // ////////////////////////////////////////////////////
            // List all Organizations and Services
            // //////////////////////////////////////////////////////

            Collection<String> findQualifiers = new ArrayList<String>();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
            Collection<String> namePatterns = new ArrayList<String>();
            namePatterns.add("%" + AnacomData.ORG_NAME + "%");

            // Make serch for organization name
            BulkResponse findResponse = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);

            if (findResponse.getStatus() == JAXRResponse.STATUS_SUCCESS) {
                Collection<Organization> findOrgs = findResponse.getCollection();

                for (Organization org : findOrgs) {
                    if (org != null) {
                        if (org.getName().getValue() != null) {
                            Logger.getLogger(this.getClass()).info("[ANACOM] Organization: " + org.getName().getValue().toString());

                            Collection<Service> findServices = org.getServices();
                            for (Service serv : findServices) {
                                Logger.getLogger(this.getClass()).info("[ANACOM]     -   Service: " + serv.getName().getValue().toString());

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

    @Override
    public void cleanDomain(OperatorSimpleDto operatorDto) {
        try {

            // Get all replicas URL
            ArrayList<String> urlList = getReplicaURL(operatorDto.getOperatorPrefix());

            // Send request to all replicas
            for (String url : urlList) {

                Logger.getLogger(this.getClass()).info("[ANACOM] Sync cleanDomain : " + operatorDto.getOperatorPrefix() + " @" + url);

                AnacomService anacom = new AnacomService(new URL(url));
                AnacomPortType port = anacom.getAnacomPort();

                // Async Replica request
                Logger.getLogger(this.getClass()).info("[ANACOM] Servidor Limpo! Versao Inicial: "
                        + port.cleanDomain(operatorDto.getOperatorPrefix()));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void registerOperator(OperatorDetailedDto operatorDto) throws OperatorPrefixAlreadyExistsException,
            OperatorPrefixWrongLengthException,
            OperatorNullNameException,
            InvalidTaxException,
            OperatorNameAlreadyExistsException {

        try {
            RegisterOperatorStrategy registerOperatorStrategy = new RegisterOperatorStrategy(businessQueryManager, operatorDto,
                    operatorDto.getOperatorPrefix());
            registerOperatorStrategy.execute();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
    }

    @Override
    public void registerPhone(PhoneDetailedDto phoneDto) throws PhoneAlreadyExistsException,
            PhoneAndOperatorPrefixDoNotMatchException,
            OperatorPrefixDoesNotExistException,
            PhoneNumberWrongLengthException {
        try {
            RegisterPhoneStrategy registerPhoneStrategy = new RegisterPhoneStrategy(businessQueryManager, phoneDto, phoneDto.getOperatorPrefix());
            registerPhoneStrategy.execute();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
    }

    @Override
    public void cancelRegisteredPhone(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        try {
            CancelRegisteredPhoneStrategy cancelRegisteredPhoneStrategy = new CancelRegisteredPhoneStrategy(businessQueryManager, phoneDto,
                    phoneDto.getOperatorPrefix());
            cancelRegisteredPhoneStrategy.execute();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
    }

    @Override
    public void increasePhoneBalance(BalanceAndPhoneDto incBalDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            BalanceLimitExceededException {
        try {
            IncreaseBalanceStrategy increaseBalanceStrategy = new IncreaseBalanceStrategy(businessQueryManager, incBalDto,
                    getOperatorPrefix(incBalDto.getPhoneNumber()));
            increaseBalanceStrategy.execute();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
    }

    @Override
    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        try {
            GetPhoneBalanceStrategy getPhoneBalanceStrategy = new GetPhoneBalanceStrategy(businessQueryManager, phoneDto,
                    phoneDto.getOperatorPrefix());
            getPhoneBalanceStrategy.execute();

            return getPhoneBalanceStrategy.getBalance();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }

        return null;
    }

    @Override
    public StateDto getPhoneState(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        try {
            GetPhoneStateStrategy getPhoneStateStrategy = new GetPhoneStateStrategy(businessQueryManager, phoneDto, phoneDto.getOperatorPrefix());
            getPhoneStateStrategy.execute();

            return getPhoneStateStrategy.getState();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }

        return null;
    }

    @Override
    public void setPhoneState(PhoneAndStateDto setStateDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        try {
            SetPhoneStateStrategy setPhoneStateStrategy = new SetPhoneStateStrategy(businessQueryManager, setStateDto,
                    getOperatorPrefix(setStateDto.getPhoneNumber()));
            setPhoneStateStrategy.execute();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
    }

    @Override
    public LastCommunicationDto getPhoneLastMadeCommunication(PhoneSimpleDto phoneDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NoCommunicationsMadeYetException {

        try {
            GetLastCommunicationStrategy getLastCommunicationStrategy = new GetLastCommunicationStrategy(businessQueryManager, phoneDto,
                    getOperatorPrefix(phoneDto.getPhoneNumber()));
            getLastCommunicationStrategy.execute();

            return getLastCommunicationStrategy.getLastCommunication();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
        return null;
    }

    @Override
    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto) throws OperatorPrefixDoesNotExistException {
        try {
            GetBalanceAndPhoneListStrategy getBalanceAndPhoneListStrategy = new GetBalanceAndPhoneListStrategy(businessQueryManager, operatorDto, operatorDto.getOperatorPrefix());
            getBalanceAndPhoneListStrategy.execute();

            return getBalanceAndPhoneListStrategy.getBalanceAndPhoneList();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }


        return null;
    }

    @Override
    public void sendSMS(SMSDto smsDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            SMSInvalidTextException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException {
        try {
            SendSMSStrategy sendSMSStrategy = new SendSMSStrategy(businessQueryManager, smsDto, getOperatorPrefix(smsDto.getSourcePhoneNumber()));
            sendSMSStrategy.execute();
            ReceivedSMSStrategy receivedSMSStrategy = new ReceivedSMSStrategy(businessQueryManager, smsDto,
                    getOperatorPrefix(smsDto.getDestinationPhoneNumber()));
            receivedSMSStrategy.execute();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
    }

    @Override
    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {
        try {
            GetSMSPhoneReceivedListStrategy getSMSPhoneReceivedListStrategy = new GetSMSPhoneReceivedListStrategy(businessQueryManager, phoneDto,
                    getOperatorPrefix(phoneDto.getPhoneNumber()));
            getSMSPhoneReceivedListStrategy.execute();

            return getSMSPhoneReceivedListStrategy.getSMSPhoneReceivedList();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }

        return null;
    }

    @Override
    public void startVoiceCall(CommunicationDto voiceDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException {

        try {
            StartSendVoiceCallStrategy startSendVoiceCallStrategy = new StartSendVoiceCallStrategy(businessQueryManager, voiceDto,
                    getOperatorPrefix(voiceDto.getSourcePhoneNumber()));
            startSendVoiceCallStrategy.execute();
            StartReceiveVoiceCallStrategy startReceivedVoiceCallStrategy = new StartReceiveVoiceCallStrategy(businessQueryManager, voiceDto,
                    getOperatorPrefix(voiceDto.getDestinationPhoneNumber()));
            startReceivedVoiceCallStrategy.execute();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
    }

    @Override
    public void endVoiceCall(CommunicationDurationDto voiceDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            InvalidDurationException,
            NoActiveCommunicationException {

        try {
            EndSendVoiceCallStrategy endSendVoiceCallStrategy = new EndSendVoiceCallStrategy(businessQueryManager, voiceDto,
                    getOperatorPrefix(voiceDto.getSourcePhoneNumber()));
            endSendVoiceCallStrategy.execute();
            EndReceiveVoiceCallStrategy endReceivedVoiceCallStrategy = new EndReceiveVoiceCallStrategy(businessQueryManager, voiceDto,
                    getOperatorPrefix(voiceDto.getDestinationPhoneNumber()));
            endReceivedVoiceCallStrategy.execute();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException no BridgeDistSoft " + e.getMessage());
        }
    }
}
