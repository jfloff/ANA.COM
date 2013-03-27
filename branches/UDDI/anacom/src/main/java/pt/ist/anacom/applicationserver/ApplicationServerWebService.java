package pt.ist.anacom.applicationserver;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.service.CancelRegisteredPhoneService;
import pt.ist.anacom.service.GetBalanceAndPhoneListService;
import pt.ist.anacom.service.GetBalanceService;
import pt.ist.anacom.service.GetLastMadeCommunicationService;
import pt.ist.anacom.service.GetPhoneStateService;
import pt.ist.anacom.service.GetSMSPhoneReceivedListService;
import pt.ist.anacom.service.IncreaseBalanceService;
import pt.ist.anacom.service.ProcessReceiveSMSService;
import pt.ist.anacom.service.ProcessSendSMSService;
import pt.ist.anacom.service.RegisterNewOperatorService;
import pt.ist.anacom.service.RegisterNewPhoneService;
import pt.ist.anacom.service.SetPhoneStateService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidTaxException;
import pt.ist.anacom.shared.exception.NegativeBalanceException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorNullNameException;
import pt.ist.anacom.shared.exception.OperatorPrefixAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.OperatorWithWrongPrefixException;
import pt.ist.anacom.shared.exception.PhoneAlreadyExistsException;
import pt.ist.anacom.shared.exception.PhoneIsBUSYException;
import pt.ist.anacom.shared.exception.PhoneIsOFFException;
import pt.ist.anacom.shared.exception.PhoneNumberIncorrectException;
import pt.ist.anacom.shared.exception.PhonePrefixDoesNotMatchException;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;
import pt.ist.anacom.shared.stubs.AnacomPortType;
import pt.ist.anacom.shared.stubs.BalanceAndPhoneNumberListType;
import pt.ist.anacom.shared.stubs.BalanceAndPhoneNumberType;
import pt.ist.anacom.shared.stubs.BalanceLimitExceededRemoteException;
import pt.ist.anacom.shared.stubs.BalanceType;
import pt.ist.anacom.shared.stubs.CommunicationType;
import pt.ist.anacom.shared.stubs.InsuficientBalanceRemoteException;
import pt.ist.anacom.shared.stubs.InvalidTaxRemoteException;
import pt.ist.anacom.shared.stubs.NegativeBalanceRemoteException;
import pt.ist.anacom.shared.stubs.NoCommunicationsMadeYetRemoteException;
import pt.ist.anacom.shared.stubs.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.OperatorDetailedType;
import pt.ist.anacom.shared.stubs.OperatorNameAndPrefixType;
import pt.ist.anacom.shared.stubs.OperatorNullNameRemoteException;
import pt.ist.anacom.shared.stubs.OperatorPrefixAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.OperatorPrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.OperatorPrefixType;
import pt.ist.anacom.shared.stubs.OperatorWithWrongPrefixRemoteException;
import pt.ist.anacom.shared.stubs.PhoneAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.PhoneDetailedType;
import pt.ist.anacom.shared.stubs.PhoneIsBUSYRemoteException;
import pt.ist.anacom.shared.stubs.PhoneIsOFFRemoteException;
import pt.ist.anacom.shared.stubs.PhoneNumberAndStateType;
import pt.ist.anacom.shared.stubs.PhoneNumberIncorrectRemoteException;
import pt.ist.anacom.shared.stubs.PhoneNumberType;
import pt.ist.anacom.shared.stubs.PhonePrefixDoesNotMatchExceptionType;
import pt.ist.anacom.shared.stubs.PhonePrefixDoesNotMatchRemoteException;
import pt.ist.anacom.shared.stubs.PhoneStateType;
import pt.ist.anacom.shared.stubs.SMSInvalidTextRemoteException;
import pt.ist.anacom.shared.stubs.SMSPhoneReceivedListType;
import pt.ist.anacom.shared.stubs.SMSType;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.Config.RepositoryType;
import pt.ist.fenixframework.FenixFramework;

@javax.jws.WebService(endpointInterface = "pt.ist.anacom.shared.stubs.AnacomPortType", wsdlLocation = "/anacom.wsdl", name = "AnacomPortType", portName = "AnacomPort", targetNamespace = "http://pt.ist.anacom.essd.0403", serviceName = "AnacomService")
public class ApplicationServerWebService implements AnacomPortType {

    public static void init(final String operatorName) {
        System.out.println(".............STARTING Anacom " + operatorName + " SERVER...........");

        // initializes the Fenix Framework
        try {

            FenixFramework.initialize(new Config() {
                {
                    dbAlias = "/tmp/db" + operatorName;
                    domainModelPath = "/tmp/anacom.dml";
                    repositoryType = RepositoryType.BERKELEYDB;
                    rootClass = Anacom.class;
                }
            });

        } catch (Exception e) {
            System.out.println("Failed to initialize the operator server.\n");
        }
    }

    @Override
    public void registerOperator(OperatorDetailedType registerOperatorInput) throws OperatorNullNameRemoteException,
            OperatorWithWrongPrefixRemoteException,
            OperatorPrefixAlreadyExistsRemoteException,
            InvalidTaxRemoteException {

        try {
            OperatorDetailedDto dto = new OperatorDetailedDto(registerOperatorInput.getOperatorPrefix(), registerOperatorInput.getOperatorName(),
                    registerOperatorInput.getTax(), registerOperatorInput.getTaxVoice(), registerOperatorInput.getTaxSMS(),
                    registerOperatorInput.getTaxVideo(), registerOperatorInput.getTaxBonus());

            RegisterNewOperatorService service = new RegisterNewOperatorService(dto);
            service.execute();
        } catch (OperatorNullNameException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getPrefix());
            throw new OperatorNullNameRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorWithWrongPrefixException e) {
            OperatorNameAndPrefixType remoteExc = new OperatorNameAndPrefixType();
            remoteExc.setOperatorName(e.getName());
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorWithWrongPrefixRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixAlreadyExistsException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getPrefix());
            throw new OperatorPrefixAlreadyExistsRemoteException(e.getMessage(), remoteExc);
        } catch (InvalidTaxException e) {
            throw new InvalidTaxRemoteException(e.getMessage());
        }
    }

    @Override
    public void registerPhone(PhoneDetailedType registerPhoneInput) throws PhonePrefixDoesNotMatchRemoteException,
            PhoneAlreadyExistsRemoteException,
            OperatorPrefixDoesNotExistRemoteException,
            PhoneNumberIncorrectRemoteException {

        try {
            PhoneDetailedDto dto = new PhoneDetailedDto(registerPhoneInput.getOperatorPrefix(), registerPhoneInput.getPhoneNumber(),
                    AnacomData.convertIntToPhoneTypeEnum(registerPhoneInput.getPhoneGen()));
            RegisterNewPhoneService service = new RegisterNewPhoneService(dto);
            service.execute();
        } catch (PhonePrefixDoesNotMatchException e) {
            PhonePrefixDoesNotMatchExceptionType remoteExc = new PhonePrefixDoesNotMatchExceptionType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            remoteExc.setOperatorName(e.getOperatorName());
            remoteExc.setPhonePrefix(e.getPhonePrefix());
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new PhonePrefixDoesNotMatchRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneAlreadyExistsException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new PhoneAlreadyExistsRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneNumberIncorrectException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new PhoneNumberIncorrectRemoteException(e.getMessage(), remoteExc);
        }

    }

    @Override
    public void increasePhoneBalance(BalanceAndPhoneNumberType increasePhoneBalanceInput) throws BalanceLimitExceededRemoteException,
            NoSuchPhoneRemoteException,
            NegativeBalanceRemoteException,
            OperatorPrefixDoesNotExistRemoteException {
        try {
            BalanceAndPhoneDto dto = new BalanceAndPhoneDto(increasePhoneBalanceInput.getPhoneNumber(), increasePhoneBalanceInput.getBalanceValue());
            IncreaseBalanceService service = new IncreaseBalanceService(dto);
            service.execute();
        } catch (NoSuchPhoneException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (NegativeBalanceException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NegativeBalanceRemoteException(e.getMessage(), remoteExc);
        } catch (BalanceLimitExceededException e) {
            BalanceAndPhoneNumberType remoteExc = new BalanceAndPhoneNumberType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            remoteExc.setBalanceValue(e.getPhoneBalance());
            throw new BalanceLimitExceededRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public void cancelRegisterPhone(PhoneNumberType cancelRegisterPhoneInput) throws NoSuchPhoneRemoteException,
            OperatorPrefixDoesNotExistRemoteException {

        try {
            PhoneSimpleDto dto = new PhoneSimpleDto(cancelRegisterPhoneInput.getPhoneNumber());
            CancelRegisteredPhoneService service = new CancelRegisteredPhoneService(dto);
            service.execute();
        } catch (NoSuchPhoneException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public void setPhoneState(PhoneNumberAndStateType setPhoneStateInput) throws NoSuchPhoneRemoteException,
            OperatorPrefixDoesNotExistRemoteException {
        try {
            PhoneAndStateDto dto = new PhoneAndStateDto(setPhoneStateInput.getPhoneNumber(),
                    AnacomData.convertIntToStateEnum(setPhoneStateInput.getPhoneState()));
            SetPhoneStateService service = new SetPhoneStateService(dto);
            service.execute();
        } catch (NoSuchPhoneException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public PhoneStateType getPhoneState(PhoneNumberType getPhoneStateInput) throws NoSuchPhoneRemoteException,
            OperatorPrefixDoesNotExistRemoteException {

        try {
            PhoneSimpleDto dto = new PhoneSimpleDto(getPhoneStateInput.getPhoneNumber());
            GetPhoneStateService service = new GetPhoneStateService(dto);
            service.execute();
            StateDto state = service.getPhoneStateServiceResult();
            PhoneStateType phoneState = new PhoneStateType();
            phoneState.setPhoneState(state.getState().ordinal());
            return phoneState;
        } catch (NoSuchPhoneException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public BalanceType getPhoneBalance(PhoneNumberType getPhoneBalanceInput) throws NoSuchPhoneRemoteException,
            OperatorPrefixDoesNotExistRemoteException {

        try {
            PhoneSimpleDto dto = new PhoneSimpleDto(getPhoneBalanceInput.getPhoneNumber());
            GetBalanceService phone = new GetBalanceService(dto);
            phone.execute();
            BalanceDto balanceDto = phone.getBalanceServiceResult();
            BalanceType balance = new BalanceType();
            balance.setBalanceValue(balanceDto.getBalance());
            return balance;
        } catch (NoSuchPhoneException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public BalanceAndPhoneNumberListType getBalanceAndPhoneList(OperatorPrefixType getBalanceAndPhoneListInput) throws OperatorPrefixDoesNotExistRemoteException {
        try {

            OperatorSimpleDto operatorDto = new OperatorSimpleDto(getBalanceAndPhoneListInput.getOperatorPrefix());
            // Execute service
            GetBalanceAndPhoneListService service = new GetBalanceAndPhoneListService(operatorDto);
            service.execute();
            BalanceAndPhoneNumberListType phoneAndBalanceListType = new BalanceAndPhoneNumberListType();

            // fill in dto arrayList
            for (BalanceAndPhoneDto balanceAndPhoneDto : service.getBalanceAndPhoneListServiceResult().getPhoneList()) {

                BalanceAndPhoneNumberType balanceAndPhoneType = new BalanceAndPhoneNumberType();

                balanceAndPhoneType.setPhoneNumber(balanceAndPhoneDto.getPhoneNumber());
                balanceAndPhoneType.setBalanceValue(balanceAndPhoneDto.getBalance());

                phoneAndBalanceListType.getPhoneList().add(balanceAndPhoneType);
            }

            return phoneAndBalanceListType;

        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public CommunicationType getLastMadeCommunication(PhoneNumberType getLastMadeCommunicationInput) throws NoSuchPhoneRemoteException,
            OperatorPrefixDoesNotExistRemoteException,
            NoCommunicationsMadeYetRemoteException {
        try {
            PhoneSimpleDto phoneDto = new PhoneSimpleDto(getLastMadeCommunicationInput.getPhoneNumber());
            GetLastMadeCommunicationService service = new GetLastMadeCommunicationService(phoneDto);
            service.execute();
            CommunicationDto communicationDto = service.getLastMadeCommunicationServiceResult();

            CommunicationType communicationType = new CommunicationType();
            communicationType.setType(communicationDto.getCommunicationType().ordinal());
            communicationType.setDestinationPhoneNumber(communicationDto.getDestinationPhoneNumber());
            communicationType.setCost(communicationDto.getCost());
            communicationType.setLength(communicationDto.getLength());
            return communicationType;
        } catch (NoSuchPhoneException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (NoCommunicationsMadeYetException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NoCommunicationsMadeYetRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public SMSPhoneReceivedListType getSMSPhoneReceivedList(PhoneNumberType getSMSPhoneReceivedListInput) throws NoSuchPhoneRemoteException,
            OperatorPrefixDoesNotExistRemoteException {
        try {

            PhoneSimpleDto phoneDto = new PhoneSimpleDto(getSMSPhoneReceivedListInput.getPhoneNumber());

            // Execute service
            GetSMSPhoneReceivedListService service = new GetSMSPhoneReceivedListService(phoneDto);
            service.execute();
            SMSPhoneReceivedListType smsPhoneReceivedListType = new SMSPhoneReceivedListType();

            // fill in dto arrayList
            for (SMSDto smsDto : service.getSMSPhoneReceivedListServiceResult().getSmsList()) {

                SMSType smsType = new SMSType();

                smsType.setSourcePhoneNumber(smsDto.getSourceNumber());
                smsType.setDestinationPhoneNumber(smsDto.getDestinationNumber());
                smsType.setMessage(smsDto.getMessage());

                smsPhoneReceivedListType.getSmsList().add(smsType);
            }

            return smsPhoneReceivedListType;
        } catch (NoSuchPhoneException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        }
    }

    @Override
    public void sendSMS(SMSType sendSMSInput) throws NoSuchPhoneRemoteException,
            NegativeBalanceRemoteException,
            OperatorPrefixDoesNotExistRemoteException,
            SMSInvalidTextRemoteException,
            InsuficientBalanceRemoteException,
            PhoneIsOFFRemoteException,
            PhoneIsBUSYRemoteException {

        try {
            SMSDto smsDto = new SMSDto(sendSMSInput.getSourcePhoneNumber(), sendSMSInput.getDestinationPhoneNumber(), sendSMSInput.getMessage());
            ProcessSendSMSService sendService = new ProcessSendSMSService(smsDto);
            ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(smsDto);
            sendService.execute();
            receiveService.execute();
        } catch (NoSuchPhoneException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getNumber());
            throw new NoSuchPhoneRemoteException(e.getMessage(), remoteExc);
        } catch (OperatorPrefixDoesNotExistException e) {
            OperatorPrefixType remoteExc = new OperatorPrefixType();
            remoteExc.setOperatorPrefix(e.getOperatorPrefix());
            throw new OperatorPrefixDoesNotExistRemoteException(e.getMessage(), remoteExc);
        } catch (NegativeBalanceException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new NegativeBalanceRemoteException(e.getMessage(), remoteExc);
        } catch (InsuficientBalanceException e) {
            BalanceAndPhoneNumberType remoteExc = new BalanceAndPhoneNumberType();
            remoteExc.setBalanceValue(e.getPhoneBalance());
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new InsuficientBalanceRemoteException(e.getMessage(), remoteExc);
        } catch (SMSInvalidTextException e) {
            throw new SMSInvalidTextRemoteException(e.getMessage());
        } catch (PhoneIsOFFException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new PhoneIsOFFRemoteException(e.getMessage(), remoteExc);
        } catch (PhoneIsBUSYException e) {
            PhoneNumberType remoteExc = new PhoneNumberType();
            remoteExc.setPhoneNumber(e.getPhoneNumber());
            throw new PhoneIsBUSYRemoteException(e.getMessage(), remoteExc);
        }
    }
}
