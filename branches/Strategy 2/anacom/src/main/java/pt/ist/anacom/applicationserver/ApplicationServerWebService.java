package pt.ist.anacom.applicationserver;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Properties;

import org.apache.log4j.Logger;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.service.CancelRegisteredPhoneService;
import pt.ist.anacom.service.CleanAnacomService;
import pt.ist.anacom.service.GetBalanceAndPhoneListService;
import pt.ist.anacom.service.GetBalanceService;
import pt.ist.anacom.service.GetLastMadeCommunicationService;
import pt.ist.anacom.service.GetPhoneStateService;
import pt.ist.anacom.service.GetReplicaVersionService;
import pt.ist.anacom.service.GetSMSPhoneReceivedListService;
import pt.ist.anacom.service.IncreaseBalanceService;
import pt.ist.anacom.service.ProcessEndDestinationVoiceCallService;
import pt.ist.anacom.service.ProcessEndSourceVoiceCallService;
import pt.ist.anacom.service.ProcessReceiveSMSService;
import pt.ist.anacom.service.ProcessSendSMSService;
import pt.ist.anacom.service.ProcessStartSourceVoiceCallService;
import pt.ist.anacom.service.RegisterNewOperatorService;
import pt.ist.anacom.service.RegisterNewPhoneService;
import pt.ist.anacom.service.SetPhoneStateService;
import pt.ist.anacom.service.SetReplicaVersionService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.data.Data;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.ReplicaVersionDto;
import pt.ist.anacom.shared.dto.SMSDto;
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
import pt.ist.anacom.shared.stubs.server.BalanceAndPhoneListType;
import pt.ist.anacom.shared.stubs.server.BalanceAndPhoneType;
import pt.ist.anacom.shared.stubs.server.BalanceLimitExceededRemoteException;
import pt.ist.anacom.shared.stubs.server.BalanceType;
import pt.ist.anacom.shared.stubs.server.CommunicationDurationType;
import pt.ist.anacom.shared.stubs.server.CommunicationType;
import pt.ist.anacom.shared.stubs.server.InsuficientBalanceRemoteException;
import pt.ist.anacom.shared.stubs.server.InvalidDurationRemoteException;
import pt.ist.anacom.shared.stubs.server.InvalidTaxRemoteException;
import pt.ist.anacom.shared.stubs.server.LastCommunicationType;
import pt.ist.anacom.shared.stubs.server.NegativeBalanceValueRemoteException;
import pt.ist.anacom.shared.stubs.server.NoActiveCommunicationRemoteException;
import pt.ist.anacom.shared.stubs.server.NoCommunicationsMadeYetRemoteException;
import pt.ist.anacom.shared.stubs.server.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.server.OperatorDetailedType;
import pt.ist.anacom.shared.stubs.server.OperatorNameAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.server.OperatorNameAndPrefixType;
import pt.ist.anacom.shared.stubs.server.OperatorNameType;
import pt.ist.anacom.shared.stubs.server.OperatorNullNameRemoteException;
import pt.ist.anacom.shared.stubs.server.OperatorPrefixAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.server.OperatorPrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.server.OperatorPrefixWrongLengthRemoteException;
import pt.ist.anacom.shared.stubs.server.OperatorSimpleType;
import pt.ist.anacom.shared.stubs.server.PhoneAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.server.PhoneAndOperatorPrefixDoNotMatchRemoteException;
import pt.ist.anacom.shared.stubs.server.PhoneAndStateType;
import pt.ist.anacom.shared.stubs.server.PhoneDetailedType;
import pt.ist.anacom.shared.stubs.server.PhoneNumberAndOperatorPrefixType;
import pt.ist.anacom.shared.stubs.server.PhoneNumberWrongLengthRemoteException;
import pt.ist.anacom.shared.stubs.server.PhoneSimpleType;
import pt.ist.anacom.shared.stubs.server.PhoneStateRemoteException;
import pt.ist.anacom.shared.stubs.server.SMSInvalidTextRemoteException;
import pt.ist.anacom.shared.stubs.server.SMSPhoneReceivedListType;
import pt.ist.anacom.shared.stubs.server.SMSType;
import pt.ist.anacom.shared.stubs.server.StateType;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;

@javax.jws.WebService(endpointInterface = "pt.ist.anacom.shared.stubs.server.AnacomPortType", wsdlLocation = "/anacom.wsdl", name = "AnacomPortType", portName = "AnacomPort", targetNamespace = "http://pt.ist.anacom.essd.0403", serviceName = "AnacomService")
@javax.jws.HandlerChain(file = "handler-chain.xml")
public class ApplicationServerWebService implements AnacomPortType {

    /* MUDAR?! O path é necessário para o Server Handler */
    public static String path;

    public static void init(final String operatorPrefix, final String replicaId, final String filePath) {
        System.out.println(".............STARTING Anacom " + operatorPrefix + " SERVER........... " + replicaId);

        // initializes the Fenix Framework
        try {

            FenixFramework.initialize(new Config() {
                {
                    dbAlias = "/tmp/db/" + operatorPrefix + "-" + replicaId;
                    domainModelPath = "/tmp/anacom.dml";
                    repositoryType = RepositoryType.BERKELEYDB;
                    rootClass = Anacom.class;
                }
            });

            path = filePath;
            Properties properties = Data.readPropertiesFile(filePath);
            String publicPath = properties.getProperty("publicKeyPath") + operatorPrefix + "-" + replicaId + "public.txt";
            String privatePath = properties.getProperty("privateKeyPath") + operatorPrefix + "-" + replicaId + "private.txt";
            PublicKey pubKey = Data.readPublicKeys(publicPath);
            PrivateKey privKey = Data.readPrivateKeys(privatePath);

            boolean committed = false;
            try {
                Transaction.begin();
                Anacom anacom = FenixFramework.getRoot();
                anacom.addKeys(pubKey, privKey);
                Transaction.commit();
                committed = true;
            } finally {
                if (!committed) {
                    Transaction.abort();
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to initialize the operator server.\n");
        }
    }

    @Override
    public int getReplicaVersion() {
        GetReplicaVersionService versionService = new GetReplicaVersionService();
        versionService.execute();
        ReplicaVersionDto versionDto = versionService.getReplicaVersionServiceResult();
        Integer replicaVersion = versionDto.getVersion();
        return replicaVersion;
    }

    public boolean replicaIsUpToDate(int newVersion) {
        if (newVersion > getReplicaVersion())
            return false;
        else
            return true;
    }


    @Override
    public int registerOperator(OperatorDetailedType registerOperatorInput) throws InvalidTaxRemoteException, OperatorNullNameRemoteException, OperatorPrefixWrongLengthRemoteException, OperatorPrefixAlreadyExistsRemoteException, OperatorNameAlreadyExistsRemoteException {

        try {

            if (registerOperatorInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] CRIAR OPERATOR NO SERVER: " + registerOperatorInput.getOperatorPrefix() + " version DTO: " + registerOperatorInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(registerOperatorInput.getVersion()));

                if (!replicaIsUpToDate(registerOperatorInput.getVersion())) {
                    // Create DTOs
                    OperatorDetailedDto dto = new OperatorDetailedDto(registerOperatorInput.getOperatorPrefix(), registerOperatorInput.getOperatorName(), registerOperatorInput.getTax(), registerOperatorInput.getTaxVoice(), registerOperatorInput.getTaxSMS(), registerOperatorInput.getTaxVideo(), registerOperatorInput.getTaxBonus());
                    // Run command
                    RegisterNewOperatorService service = new RegisterNewOperatorService(dto);
                    service.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(registerOperatorInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }
        } catch (OperatorNullNameException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorNullNameRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixWrongLengthException e) {
            OperatorNameAndPrefixType remoteExc = new OperatorNameAndPrefixType();
            remoteExc.setOperatorName(e.getOperatorName());
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixWrongLengthRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixAlreadyExistsException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixAlreadyExistsRemoteException(e.getMessage(), remoteExc);
        } catch (InvalidTaxException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new InvalidTaxRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorNameAlreadyExistsException e) {
            OperatorNameType remoteExc = new OperatorNameType();
            remoteExc.setOperatorName(e.getOperatorName());
            throw new OperatorNameAlreadyExistsRemoteException(e.getMessage(), remoteExc);
        }
        return 0;
    }

    @Override
    public int registerPhone(PhoneDetailedType registerPhoneInput) throws OperatorPrefixDoesNotExistRemoteException, PhoneAlreadyExistsRemoteException, PhoneAndOperatorPrefixDoNotMatchRemoteException, PhoneNumberWrongLengthRemoteException {

        try {

            if (registerPhoneInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] CRIAR CONTACTO NO SERVER: " + registerPhoneInput.getPhoneNumber() + " version DTO: " + registerPhoneInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(registerPhoneInput.getVersion()));

                if (!replicaIsUpToDate(registerPhoneInput.getVersion())) {

                    // Create DTOs
                    PhoneDetailedDto dto = new PhoneDetailedDto(registerPhoneInput.getOperatorPrefix(), registerPhoneInput.getPhoneNumber(), AnacomData.convertIntToPhoneTypeEnum(registerPhoneInput.getPhoneGen()));
                    // Run command
                    RegisterNewPhoneService service = new RegisterNewPhoneService(dto);
                    service.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(registerPhoneInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }
        } catch (PhoneAndOperatorPrefixDoNotMatchException e) {
            PhoneNumberAndOperatorPrefixType remoteExc = new PhoneNumberAndOperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new PhoneAndOperatorPrefixDoNotMatchRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneAlreadyExistsException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new PhoneAlreadyExistsRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneNumberWrongLengthException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new PhoneNumberWrongLengthRemoteException(e.getMessage(), remoteExc);
        }
        return 0;
    }

    @Override
    public int setPhoneState(PhoneAndStateType setPhoneStateInput) throws NoSuchPhoneRemoteException, OperatorPrefixDoesNotExistRemoteException {
        try {
            if (setPhoneStateInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] MUDAR ESTADO TELEMOVEL NO SERVER: " + setPhoneStateInput.getPhoneNumber() + " version DTO: " + setPhoneStateInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(setPhoneStateInput.getVersion()));

                if (!replicaIsUpToDate(setPhoneStateInput.getVersion())) {

                    // Create DTOs
                    PhoneAndStateDto dto = new PhoneAndStateDto(setPhoneStateInput.getPhoneNumber(), AnacomData.convertIntToStateEnum(setPhoneStateInput.getPhoneState()));
                    // Run command
                    SetPhoneStateService service = new SetPhoneStateService(dto);
                    service.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(setPhoneStateInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }

        return 0;
    }

    @Override
    public int cancelRegisteredPhone(PhoneSimpleType cancelRegisteredPhoneInput) throws NoSuchPhoneRemoteException, OperatorPrefixDoesNotExistRemoteException {

        try {

            if (cancelRegisteredPhoneInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] CANCELAR REGISTO TELEMOVEL NO SERVER: " + cancelRegisteredPhoneInput.getPhoneNumber() + " version DTO: " + cancelRegisteredPhoneInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(cancelRegisteredPhoneInput.getVersion()));

                if (!replicaIsUpToDate(cancelRegisteredPhoneInput.getVersion())) {

                    PhoneSimpleDto dto = new PhoneSimpleDto(cancelRegisteredPhoneInput.getPhoneNumber());
                    CancelRegisteredPhoneService service = new CancelRegisteredPhoneService(dto);
                    service.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(cancelRegisteredPhoneInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }

        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }

        return 0;
    }

    @Override
    public StateType getPhoneState(PhoneSimpleType getPhoneStateInput) throws NoSuchPhoneRemoteException, OperatorPrefixDoesNotExistRemoteException {
        try {
            PhoneSimpleDto dto = new PhoneSimpleDto(getPhoneStateInput.getPhoneNumber());
            GetPhoneStateService service = new GetPhoneStateService(dto);
            service.execute();
            StateDto state = service.getPhoneStateServiceResult();
            StateType phoneState = new StateType();
            phoneState.setPhoneState(state.getState().ordinal());
            return phoneState;
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public BalanceAndPhoneListType getBalanceAndPhoneList(OperatorSimpleType getBalanceAndPhoneListInput) throws OperatorPrefixDoesNotExistRemoteException {
        try {

            OperatorSimpleDto operatorDto = new OperatorSimpleDto(getBalanceAndPhoneListInput.getOperatorPrefix());
            // Execute service
            GetBalanceAndPhoneListService service = new GetBalanceAndPhoneListService(operatorDto);
            service.execute();
            BalanceAndPhoneListType phoneAndBalanceListType = new BalanceAndPhoneListType();

            // fill in dto arrayList
            for (BalanceAndPhoneDto balanceAndPhoneDto : service.getBalanceAndPhoneListServiceResult().getPhoneList()) {

                BalanceAndPhoneType balanceAndPhoneType = new BalanceAndPhoneType();

                balanceAndPhoneType.setPhoneNumber(balanceAndPhoneDto.getPhoneNumber());
                balanceAndPhoneType.setBalance(balanceAndPhoneDto.getBalance());

                phoneAndBalanceListType.getPhoneList().add(balanceAndPhoneType);
            }

            return phoneAndBalanceListType;

        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public SMSPhoneReceivedListType getSMSPhoneReceivedList(PhoneSimpleType getSMSPhoneReceivedListInput) throws NoSuchPhoneRemoteException, OperatorPrefixDoesNotExistRemoteException {
        try {

            PhoneSimpleDto phoneDto = new PhoneSimpleDto(getSMSPhoneReceivedListInput.getPhoneNumber());

            // Execute service
            GetSMSPhoneReceivedListService service = new GetSMSPhoneReceivedListService(phoneDto);
            service.execute();
            SMSPhoneReceivedListType smsPhoneReceivedListType = new SMSPhoneReceivedListType();

            // fill in dto arrayList
            for (SMSDto smsDto : service.getSMSPhoneReceivedListServiceResult().getSmsList()) {

                SMSType smsType = new SMSType();

                smsType.setSourcePhoneNumber(smsDto.getSourcePhoneNumber());
                smsType.setDestinationPhoneNumber(smsDto.getDestinationPhoneNumber());
                smsType.setMessage(smsDto.getMessage());

                smsPhoneReceivedListType.getSmsList().add(smsType);
            }

            return smsPhoneReceivedListType;
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public void sendSMS(SMSType sendSMSInput) throws NoSuchPhoneRemoteException, NegativeBalanceValueRemoteException, OperatorPrefixDoesNotExistRemoteException, PhoneStateRemoteException, SMSInvalidTextRemoteException, InsuficientBalanceRemoteException {

        try {
            SMSDto smsDto = new SMSDto(sendSMSInput.getSourcePhoneNumber(), sendSMSInput.getDestinationPhoneNumber(), sendSMSInput.getMessage());
            ProcessSendSMSService sendService = new ProcessSendSMSService(smsDto);
            sendService.execute();
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (NegativeBalanceValueException e) {
            BalanceAndPhoneType remoteExc = new BalanceAndPhoneType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalance(e.getBalance());
            throw new NegativeBalanceValueRemoteException(e.getMessage(), remoteExc);
        } catch (InsuficientBalanceException e) {
            BalanceAndPhoneType remoteExc = new BalanceAndPhoneType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalance(e.getPhoneBalance());
            throw new InsuficientBalanceRemoteException(e.getMessage(), remoteExc);
        } catch (SMSInvalidTextException e) {
            CommunicationType remoteExc = new CommunicationType();
            remoteExc.setDestinationPhoneNumber(e.getDestinationPhoneNumber());
            remoteExc.setSourcePhoneNumber(e.getSourcePhoneNumber());
            throw new SMSInvalidTextRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneStateException e) {
            PhoneAndStateType remoteExc = new PhoneAndStateType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setPhoneState(e.getState().ordinal());
            throw new PhoneStateRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public void receiveSMS(SMSType sendSMSInput) throws NoSuchPhoneRemoteException, OperatorPrefixDoesNotExistRemoteException, PhoneStateRemoteException, SMSInvalidTextRemoteException {

        try {
            SMSDto smsDto = new SMSDto(sendSMSInput.getSourcePhoneNumber(), sendSMSInput.getDestinationPhoneNumber(), sendSMSInput.getMessage());
            ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(smsDto);
            receiveService.execute();
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (SMSInvalidTextException e) {
            CommunicationType remoteExc = new CommunicationType();
            remoteExc.setDestinationPhoneNumber(e.getDestinationPhoneNumber());
            remoteExc.setSourcePhoneNumber(e.getSourcePhoneNumber());
            throw new SMSInvalidTextRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneStateException e) {
            PhoneAndStateType remoteExc = new PhoneAndStateType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setPhoneState(e.getState().ordinal());
            throw new PhoneStateRemoteException(e.getMessage(), remoteExc);
        }
    }


    @Override
    public int increasePhoneBalance(BalanceAndPhoneType increasePhoneBalanceInput) throws BalanceLimitExceededRemoteException, NoSuchPhoneRemoteException, NegativeBalanceValueRemoteException, OperatorPrefixDoesNotExistRemoteException {

        try {
            if (increasePhoneBalanceInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] AUMENTAR SALDO NO SERVER: " + increasePhoneBalanceInput.getPhoneNumber() + " version DTO: " + increasePhoneBalanceInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(increasePhoneBalanceInput.getVersion()));

                if (!replicaIsUpToDate(increasePhoneBalanceInput.getVersion())) {

                    // Create DTOs
                    BalanceAndPhoneDto dto = new BalanceAndPhoneDto(increasePhoneBalanceInput.getPhoneNumber(), increasePhoneBalanceInput.getBalance());

                    // Run command
                    IncreaseBalanceService service = new IncreaseBalanceService(dto);
                    service.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(increasePhoneBalanceInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (NegativeBalanceValueException e) {
            BalanceAndPhoneType remoteExc = new BalanceAndPhoneType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalance(e.getBalance());
            throw new NegativeBalanceValueRemoteException(e.getMessage(), remoteExc);
        } catch (BalanceLimitExceededException e) {
            BalanceAndPhoneType remoteExc = new BalanceAndPhoneType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalance(e.getPhoneBalance());
            throw new BalanceLimitExceededRemoteException(e.getMessage(), remoteExc);
        }
        return 0;
    }

    @Override
    public LastCommunicationType getPhoneLastMadeCommunication(PhoneSimpleType getPhoneLastMadeCommunicationInput) throws NoSuchPhoneRemoteException, NoCommunicationsMadeYetRemoteException, OperatorPrefixDoesNotExistRemoteException {
        try {
            PhoneSimpleDto phoneDto = new PhoneSimpleDto(getPhoneLastMadeCommunicationInput.getPhoneNumber());
            GetLastMadeCommunicationService service = new GetLastMadeCommunicationService(phoneDto);
            service.execute();
            LastCommunicationDto communicationDto = service.getLastMadeCommunicationServiceResult();

            LastCommunicationType communicationType = new LastCommunicationType();
            communicationType.setType(communicationDto.getCommunicationType().ordinal());
            communicationType.setDestinationPhoneNumber(communicationDto.getDestinationPhoneNumber());
            communicationType.setCost(communicationDto.getCost());
            communicationType.setLength(communicationDto.getLength());
            return communicationType;
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (NoCommunicationsMadeYetException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoCommunicationsMadeYetRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public int startSendVoiceCall(CommunicationType startSendVoiceCallInput) throws NoSuchPhoneRemoteException, NegativeBalanceValueRemoteException, OperatorPrefixDoesNotExistRemoteException, PhoneStateRemoteException, InsuficientBalanceRemoteException {
        try {
            if (startSendVoiceCallInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] COMECAR CHAMADA NO SERVER: " + startSendVoiceCallInput.getSourcePhoneNumber() + " version DTO: " + startSendVoiceCallInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(startSendVoiceCallInput.getVersion()));

                if (!replicaIsUpToDate(startSendVoiceCallInput.getVersion())) {

                    // Create DTOs
                    CommunicationDto voiceDto = new CommunicationDto(startSendVoiceCallInput.getSourcePhoneNumber(), startSendVoiceCallInput.getDestinationPhoneNumber());

                    // Run command
                    ProcessStartSourceVoiceCallService sourceService = new ProcessStartSourceVoiceCallService(voiceDto);
                    sourceService.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(startSendVoiceCallInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (NegativeBalanceValueException e) {
            BalanceAndPhoneType remoteExc = new BalanceAndPhoneType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalance(e.getBalance());
            throw new NegativeBalanceValueRemoteException(e.getMessage(), remoteExc);
        } catch (InsuficientBalanceException e) {
            BalanceAndPhoneType remoteExc = new BalanceAndPhoneType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalance(e.getPhoneBalance());
            throw new InsuficientBalanceRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneStateException e) {
            PhoneAndStateType remoteExc = new PhoneAndStateType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setPhoneState(e.getState().ordinal());
            throw new PhoneStateRemoteException(e.getMessage(), remoteExc);
        }
        return 0;
    }

    @Override
    public int startReceiveVoiceCall(CommunicationType startReceiveVoiceCallInput) throws NoSuchPhoneRemoteException, OperatorPrefixDoesNotExistRemoteException, PhoneStateRemoteException {
        try {
            if (startReceiveVoiceCallInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] COMECAR RECEBER CHAMADA NO SERVER: " + startReceiveVoiceCallInput.getSourcePhoneNumber() + " version DTO: " + startReceiveVoiceCallInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(startReceiveVoiceCallInput.getVersion()));

                if (!replicaIsUpToDate(startReceiveVoiceCallInput.getVersion())) {

                    // Create DTOs
                    CommunicationDto voiceDto = new CommunicationDto(startReceiveVoiceCallInput.getSourcePhoneNumber(), startReceiveVoiceCallInput.getDestinationPhoneNumber());

                    // Run command
                    ProcessStartSourceVoiceCallService destinationService = new ProcessStartSourceVoiceCallService(voiceDto);
                    destinationService.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(startReceiveVoiceCallInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneStateException e) {
            PhoneAndStateType remoteExc = new PhoneAndStateType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setPhoneState(e.getState().ordinal());
            throw new PhoneStateRemoteException(e.getMessage(), remoteExc);
        }
        return 0;
    }

    @Override
    public int endSendVoiceCall(CommunicationDurationType endSendVoiceCallInput) throws NoSuchPhoneRemoteException, NegativeBalanceValueRemoteException, NoActiveCommunicationRemoteException, OperatorPrefixDoesNotExistRemoteException, InvalidDurationRemoteException, InsuficientBalanceRemoteException {

        try {
            if (endSendVoiceCallInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] ACABAR CHAMADA NO SERVER: " + endSendVoiceCallInput.getSourcePhoneNumber() + " version DTO: " + endSendVoiceCallInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(endSendVoiceCallInput.getVersion()));

                if (!replicaIsUpToDate(endSendVoiceCallInput.getVersion())) {

                    // Create DTOs
                    CommunicationDurationDto voiceDurationDto = new CommunicationDurationDto(endSendVoiceCallInput.getSourcePhoneNumber(), endSendVoiceCallInput.getDestinationPhoneNumber(), endSendVoiceCallInput.getDuration());

                    // Run command
                    ProcessEndSourceVoiceCallService sourceService = new ProcessEndSourceVoiceCallService(voiceDurationDto);
                    sourceService.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(endSendVoiceCallInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (NegativeBalanceValueException e) {
            BalanceAndPhoneType remoteExc = new BalanceAndPhoneType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalance(e.getBalance());
            throw new NegativeBalanceValueRemoteException(e.getMessage(), remoteExc);
        } catch (InsuficientBalanceException e) {
            BalanceAndPhoneType remoteExc = new BalanceAndPhoneType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalance(e.getPhoneBalance());
            throw new InsuficientBalanceRemoteException(e.getMessage(), remoteExc);
        } catch (InvalidDurationException e) {
            CommunicationType remoteExc = new CommunicationType();
            remoteExc.setDestinationPhoneNumber(e.getDestinationPhoneNumber());
            remoteExc.setSourcePhoneNumber(e.getSourcePhoneNumber());
            throw new InvalidDurationRemoteException(e.getMessage(), remoteExc);
        } catch (NoActiveCommunicationException e) {
            CommunicationType remoteExc = new CommunicationType();
            remoteExc.setDestinationPhoneNumber(e.getDestinationPhoneNumber());
            remoteExc.setSourcePhoneNumber(e.getSourcePhoneNumber());
            throw new NoActiveCommunicationRemoteException(e.getMessage(), remoteExc);
        }

        return 0;
    }

    @Override
    public int endReceiveVoiceCall(CommunicationDurationType endReceiveVoiceCallInput) throws NoSuchPhoneRemoteException, NoActiveCommunicationRemoteException, OperatorPrefixDoesNotExistRemoteException, InvalidDurationRemoteException {


        try {
            if (endReceiveVoiceCallInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] ACABAR RECEBER CHAMADA NO SERVER: " + endReceiveVoiceCallInput.getSourcePhoneNumber() + " version DTO: " + endReceiveVoiceCallInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(endReceiveVoiceCallInput.getVersion()));

                if (!replicaIsUpToDate(endReceiveVoiceCallInput.getVersion())) {

                    // Create DTOs
                    CommunicationDurationDto voiceDurationDto = new CommunicationDurationDto(endReceiveVoiceCallInput.getSourcePhoneNumber(), endReceiveVoiceCallInput.getDestinationPhoneNumber(), endReceiveVoiceCallInput.getDuration());
                    // Run command
                    ProcessEndDestinationVoiceCallService destinationService = new ProcessEndDestinationVoiceCallService(voiceDurationDto);
                    destinationService.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(endReceiveVoiceCallInput.getVersion());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }
        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (InvalidDurationException e) {
            CommunicationType remoteExc = new CommunicationType();
            remoteExc.setDestinationPhoneNumber(e.getDestinationPhoneNumber());
            remoteExc.setSourcePhoneNumber(e.getSourcePhoneNumber());
            throw new InvalidDurationRemoteException(e.getMessage(), remoteExc);
        } catch (NoActiveCommunicationException e) {
            CommunicationType remoteExc = new CommunicationType();
            remoteExc.setDestinationPhoneNumber(e.getDestinationPhoneNumber());
            remoteExc.setSourcePhoneNumber(e.getSourcePhoneNumber());
            throw new NoActiveCommunicationRemoteException(e.getMessage(), remoteExc);
        }

        return 0;
    }

    @Override
    public String cleanDomain(String cleanDomainInput) {
        Logger.getLogger(this.getClass()).info("[ANACOM] CLEAN DOMAIN NO SERVER");

        CleanAnacomService service = new CleanAnacomService();
        service.execute();

        return "" + getReplicaVersion();
    }

    @Override
    public BalanceType getPhoneBalance(PhoneSimpleType getPhoneBalanceInput) throws NoSuchPhoneRemoteException, OperatorPrefixDoesNotExistRemoteException {
        try {

            if (getPhoneBalanceInput != null) {

                Logger.getLogger(this.getClass()).info("[ANACOM] GET SALDO NO SERVER: " + getPhoneBalanceInput.getPhoneNumber() + " version DTO: " + getPhoneBalanceInput.getVersion() + " version SERVER:" + getReplicaVersion() + " " + replicaIsUpToDate(getPhoneBalanceInput.getVersion()));

                // Create DTOs
                PhoneSimpleDto dto = new PhoneSimpleDto(getPhoneBalanceInput.getPhoneNumber());

                // Run command
                GetBalanceService phone = new GetBalanceService(dto);
                phone.execute();

                // Send DtoType back
                BalanceDto balanceDto = phone.getBalanceServiceResult();
                BalanceType balance = new BalanceType();
                balance.setBalance(balanceDto.getBalance());
                balance.setVersion(getReplicaVersion());
                return balance;

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
                return null;
            }

        } catch (NoSuchPhoneException e) {
            PhoneSimpleType remoteExc = new PhoneSimpleType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorSimpleType remoteExc = new OperatorSimpleType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }
}
