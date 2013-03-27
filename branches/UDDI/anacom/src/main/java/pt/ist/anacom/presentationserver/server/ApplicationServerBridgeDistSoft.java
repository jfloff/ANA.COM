package pt.ist.anacom.presentationserver.server;

import javax.xml.ws.BindingProvider;

import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.dto.VoiceCallDto;
import pt.ist.anacom.shared.dto.VoiceEndCallDto;
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
import pt.ist.anacom.shared.stubs.AnacomService;
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

public class ApplicationServerBridgeDistSoft implements ApplicationServerBridge {

    private AnacomPortType getPort(String operatorPrefix) {

        AnacomService anacom = new AnacomService();
        AnacomPortType port = anacom.getAnacomPort();

        String endpointURL = "http://localhost:8080/" + operatorPrefix + "/" + operatorPrefix;
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

        return port;
    }

    public String getOperatorPrefix(String phoneNumber) {
        return phoneNumber.substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    @Override
    public void registerOperator(OperatorDetailedDto operatorDto) {

        AnacomPortType port = getPort(operatorDto.getOperatorPrefix());

        OperatorDetailedType op = new OperatorDetailedType();

        op.setOperatorName(operatorDto.getOperatorName());
        op.setOperatorPrefix(operatorDto.getOperatorPrefix());
        op.setTax(operatorDto.getOperatorTax());
        op.setTaxVoice(operatorDto.getOperatorTaxVoice());
        op.setTaxSMS(operatorDto.getOperatorTaxSMS());
        op.setTaxVideo(operatorDto.getOperatorTaxVideo());
        try {
            port.registerOperator(op);
        } catch (OperatorNullNameRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorNullNameException(exc.getOperatorPrefix());
        } catch (OperatorWithWrongPrefixRemoteException e) {
            OperatorNameAndPrefixType exc = e.getFaultInfo();
            throw new OperatorWithWrongPrefixException(exc.getOperatorName(), exc.getOperatorName());
        } catch (OperatorPrefixAlreadyExistsRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixAlreadyExistsException(exc.getOperatorPrefix());
        } catch (InvalidTaxRemoteException e) {
            throw new InvalidTaxException();
        }
    }

    @Override
    public void registerPhone(PhoneDetailedDto dto) {

        AnacomPortType port = getPort(dto.getOperatorPrefix());

        PhoneDetailedType phone = new PhoneDetailedType();

        phone.setPhoneNumber(dto.getPhoneNumber());
        phone.setOperatorPrefix(dto.getOperatorPrefix());
        phone.setPhoneGen(dto.getPhoneGen().ordinal());

        try {
            port.registerPhone(phone);
        } catch (PhoneAlreadyExistsRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new PhoneAlreadyExistsException(exc.getPhoneNumber());
        } catch (PhonePrefixDoesNotMatchRemoteException e) {
            PhonePrefixDoesNotMatchExceptionType exc = e.getFaultInfo();
            throw new PhonePrefixDoesNotMatchException(exc.getOperatorName(), exc.getOperatorPrefix(), exc.getPhoneNumber(), exc.getPhonePrefix());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        } catch (PhoneNumberIncorrectRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new PhoneNumberIncorrectException(exc.getPhoneNumber());
        }
    }

    @Override
    public void increaseBalance(BalanceAndPhoneDto incBalDto) {

        AnacomPortType port = getPort(getOperatorPrefix(incBalDto.getPhoneNumber()));
        BalanceAndPhoneNumberType balance = new BalanceAndPhoneNumberType();
        balance.setPhoneNumber(incBalDto.getPhoneNumber());
        balance.setBalanceValue(incBalDto.getBalance());
        try {
            port.increasePhoneBalance(balance);
        } catch (NoSuchPhoneRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoSuchPhoneException(exc.getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        } catch (NegativeBalanceRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NegativeBalanceException(exc.getPhoneNumber());
        } catch (BalanceLimitExceededRemoteException e) {
            BalanceAndPhoneNumberType exc = e.getFaultInfo();
            throw new BalanceLimitExceededException(exc.getPhoneNumber(), exc.getBalanceValue());
        }
    }

    @Override
    public void cancelRegisteredPhone(PhoneSimpleDto dto) {
        AnacomPortType port = getPort(dto.getOperatorPrefix());

        PhoneNumberType phoneDto = new PhoneNumberType();

        phoneDto.setPhoneNumber(dto.getPhoneNumber());

        try {
            port.cancelRegisterPhone(phoneDto);
        } catch (NoSuchPhoneRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoSuchPhoneException(exc.getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        }
    }

    @Override
    public void setPhoneState(PhoneAndStateDto dto) {

        AnacomPortType port = getPort(getOperatorPrefix(dto.getPhoneNumber()));

        PhoneNumberAndStateType phoneStateType = new PhoneNumberAndStateType();

        phoneStateType.setPhoneNumber(dto.getPhoneNumber());
        phoneStateType.setPhoneState(dto.getState().ordinal());

        try {
            port.setPhoneState(phoneStateType);
        } catch (NoSuchPhoneRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoSuchPhoneException(exc.getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        }
    }

    @Override
    public StateDto getPhoneState(PhoneSimpleDto phoneDto) {

        AnacomPortType port = getPort(getOperatorPrefix(phoneDto.getPhoneNumber()));

        PhoneNumberType phoneType = new PhoneNumberType();

        phoneType.setPhoneNumber(phoneDto.getPhoneNumber());

        try {
            PhoneStateType state = port.getPhoneState(phoneType);
            StateDto phoneState = new StateDto(AnacomData.convertIntToStateEnum(state.getPhoneState()));
            return phoneState;
        } catch (NoSuchPhoneRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoSuchPhoneException(exc.getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        }
    }

    @Override
    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) {

        AnacomPortType port = getPort(getOperatorPrefix(phoneDto.getPhoneNumber()));
        PhoneNumberType phoneType = new PhoneNumberType();
        phoneType.setPhoneNumber(phoneDto.getPhoneNumber());

        try {
            BalanceType balance = port.getPhoneBalance(phoneType);
            BalanceDto balanceDto = new BalanceDto(balance.getBalanceValue());
            return balanceDto;
        } catch (NoSuchPhoneRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoSuchPhoneException(exc.getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        }
    }

    @Override
    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto) {
        AnacomPortType port = getPort(operatorDto.getOperatorPrefix());

        OperatorPrefixType operatorType = new OperatorPrefixType();
        operatorType.setOperatorPrefix(operatorDto.getOperatorPrefix());

        try {
            BalanceAndPhoneNumberListType balanceAndPhoneList = port.getBalanceAndPhoneList(operatorType);
            BalanceAndPhoneListDto balanceAndPhoneListDto = new BalanceAndPhoneListDto();

            for (BalanceAndPhoneNumberType balanceAndPhoneNumberType : balanceAndPhoneList.getPhoneList()) {
                balanceAndPhoneListDto.add(balanceAndPhoneNumberType.getPhoneNumber(), balanceAndPhoneNumberType.getBalanceValue());
            }
            return balanceAndPhoneListDto;
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        }
    }

    @Override
    public CommunicationDto getLastMadeCommunication(PhoneSimpleDto phoneDto) {
        AnacomPortType port = getPort(getOperatorPrefix(phoneDto.getPhoneNumber()));
        PhoneNumberType phoneType = new PhoneNumberType();
        phoneType.setPhoneNumber(phoneDto.getPhoneNumber());
        try {
            CommunicationType communication = port.getLastMadeCommunication(phoneType);
            CommunicationDto communicationDto = new CommunicationDto(AnacomData.convertIntToCommunicationTypeEnum(communication.getType()),
                    communication.getDestinationPhoneNumber(), communication.getCost(), communication.getLength());
            return communicationDto;
        } catch (NoSuchPhoneRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoSuchPhoneException(exc.getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        } catch (NoCommunicationsMadeYetRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoCommunicationsMadeYetException(exc.getPhoneNumber());
        }
    }

    @Override
    public void sendSMS(SMSDto SMSDto) throws SMSInvalidTextException,
            OperatorPrefixDoesNotExistException,
            NoSuchPhoneException,
            NegativeBalanceException,
            InsuficientBalanceException,
            PhoneIsOFFException,
            PhoneIsBUSYException {

        AnacomPortType port = getPort(getOperatorPrefix(SMSDto.getSourceNumber()));

        SMSType smsDto = new SMSType();

        smsDto.setSourcePhoneNumber(SMSDto.getSourceNumber());
        smsDto.setDestinationPhoneNumber(SMSDto.getDestinationNumber());
        smsDto.setMessage(SMSDto.getMessage());

        try {
            port.sendSMS(smsDto);
        } catch (NoSuchPhoneRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoSuchPhoneException(exc.getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        } catch (NegativeBalanceRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NegativeBalanceException(exc.getPhoneNumber());
        } catch (SMSInvalidTextRemoteException e) {
            throw new SMSInvalidTextException();
        } catch (InsuficientBalanceRemoteException e) {
            BalanceAndPhoneNumberType exc = e.getFaultInfo();
            throw new InsuficientBalanceException(exc.getPhoneNumber(), exc.getBalanceValue());
        } catch (PhoneIsOFFRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new PhoneIsOFFException(exc.getPhoneNumber());
        } catch (PhoneIsBUSYRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new PhoneIsBUSYException(exc.getPhoneNumber());
        }
    }

    @Override
    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {
        AnacomPortType port = getPort(phoneDto.getOperatorPrefix());

        PhoneNumberType phoneType = new PhoneNumberType();
        phoneType.setPhoneNumber(phoneDto.getPhoneNumber());

        try {
            SMSPhoneReceivedListType smsPhoneReceivedList = port.getSMSPhoneReceivedList(phoneType);

            SMSPhoneReceivedListDto smsPhoneReceivedListDto = new SMSPhoneReceivedListDto();

            for (SMSType smsType : smsPhoneReceivedList.getSmsList()) {
                smsPhoneReceivedListDto.add(smsType.getSourcePhoneNumber(), smsType.getDestinationPhoneNumber(), smsType.getMessage());
            }
            return smsPhoneReceivedListDto;
        } catch (NoSuchPhoneRemoteException e) {
            PhoneNumberType exc = e.getFaultInfo();
            throw new NoSuchPhoneException(exc.getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            OperatorPrefixType exc = e.getFaultInfo();
            throw new OperatorPrefixDoesNotExistException(exc.getOperatorPrefix());
        }
    }

    @Override
    public void startVoiceCall(VoiceCallDto voiceCallDto) {
        // TODO Auto-generated method stub

    }

    @Override
    public void endVoiceCall(VoiceEndCallDto voiceEndCallDto) {
        // TODO Auto-generated method stub

    }

}
