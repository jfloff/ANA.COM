package pt.ist.anacom.presentationserver.server;

import pt.ist.anacom.service.CancelRegisteredPhoneService;
import pt.ist.anacom.service.CleanAnacomService;
import pt.ist.anacom.service.GetBalanceAndPhoneListService;
import pt.ist.anacom.service.GetBalanceService;
import pt.ist.anacom.service.GetLastMadeCommunicationService;
import pt.ist.anacom.service.GetPhoneStateService;
import pt.ist.anacom.service.GetSMSPhoneReceivedListService;
import pt.ist.anacom.service.IncreaseBalanceService;
import pt.ist.anacom.service.ProcessEndDestinationVoiceCallService;
import pt.ist.anacom.service.ProcessEndSourceVoiceCallService;
import pt.ist.anacom.service.ProcessReceiveSMSService;
import pt.ist.anacom.service.ProcessSendSMSService;
import pt.ist.anacom.service.ProcessStartDestinationVoiceCallService;
import pt.ist.anacom.service.ProcessStartSourceVoiceCallService;
import pt.ist.anacom.service.RegisterNewOperatorService;
import pt.ist.anacom.service.RegisterNewPhoneService;
import pt.ist.anacom.service.SetPhoneStateService;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
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
import pt.ist.fenixframework.pstm.Transaction;

public class ApplicationServerBridgeSoftEng implements ApplicationServerBridge {

    @Override
    public void cleanDomain(OperatorSimpleDto dto) {
        CleanAnacomService service = new CleanAnacomService();
        service.execute();
    }

    @Override
    public void registerOperator(OperatorDetailedDto operatorDto) throws OperatorPrefixAlreadyExistsException,
            OperatorPrefixWrongLengthException,
            OperatorNullNameException,
            InvalidTaxException,
            OperatorNameAlreadyExistsException {
        RegisterNewOperatorService service = new RegisterNewOperatorService(operatorDto);
        service.execute();
    }

    @Override
    public void registerPhone(PhoneDetailedDto phoneDto) throws PhoneAlreadyExistsException,
            PhoneAndOperatorPrefixDoNotMatchException,
            OperatorPrefixDoesNotExistException,
            PhoneNumberWrongLengthException {
        RegisterNewPhoneService service = new RegisterNewPhoneService(phoneDto);
        service.execute();
    }

    @Override
    public void cancelRegisteredPhone(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        CancelRegisteredPhoneService cancelRegister = new CancelRegisteredPhoneService(phoneDto);
        cancelRegister.execute();
    }

    @Override
    public void increasePhoneBalance(BalanceAndPhoneDto incBalDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            BalanceLimitExceededException {
        IncreaseBalanceService incBalService = new IncreaseBalanceService(incBalDto);
        incBalService.execute();
    }

    @Override
    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        GetBalanceService getBalService = new GetBalanceService(phoneDto);
        getBalService.execute();
        return getBalService.getBalanceServiceResult();
    }

    @Override
    public StateDto getPhoneState(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        GetPhoneStateService getPhoneStateService = new GetPhoneStateService(phoneDto);
        getPhoneStateService.execute();
        return getPhoneStateService.getPhoneStateServiceResult();
    }

    @Override
    public void setPhoneState(PhoneAndStateDto setStateDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        SetPhoneStateService setPhoneStatusService = new SetPhoneStateService(setStateDto);
        setPhoneStatusService.execute();
    }

    @Override
    public LastCommunicationDto getPhoneLastMadeCommunication(PhoneSimpleDto phoneDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NoCommunicationsMadeYetException {
        GetLastMadeCommunicationService getLastMadeCommunicationService = new GetLastMadeCommunicationService(phoneDto);
        getLastMadeCommunicationService.execute();
        return getLastMadeCommunicationService.getLastMadeCommunicationServiceResult();
    }

    @Override
    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto) throws OperatorPrefixDoesNotExistException {
        GetBalanceAndPhoneListService getBalanceAndPhoneListService = new GetBalanceAndPhoneListService(operatorDto);
        getBalanceAndPhoneListService.execute();
        return getBalanceAndPhoneListService.getBalanceAndPhoneListServiceResult();
    }

    @Override
    public void sendSMS(SMSDto SMSDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            SMSInvalidTextException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException {

        ProcessSendSMSService sendService = new ProcessSendSMSService(SMSDto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(SMSDto);

        Transaction.begin();
        boolean txCommitted = false;
        try {
            // Transactional code area
            sendService.dispatch();
            receiveService.dispatch();
            // end
            Transaction.commit();
            txCommitted = true;
        } finally {
            if (!txCommitted) {
                Transaction.abort();
            }
        }
    }

    @Override
    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {
        GetSMSPhoneReceivedListService getSMSPhoneReceivedListService = new GetSMSPhoneReceivedListService(phoneDto);
        getSMSPhoneReceivedListService.execute();
        return getSMSPhoneReceivedListService.getSMSPhoneReceivedListServiceResult();
    }

    @Override
    public void startVoiceCall(CommunicationDto voiceCallDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException {

        ProcessStartSourceVoiceCallService sourceService = new ProcessStartSourceVoiceCallService(voiceCallDto);
        ProcessStartDestinationVoiceCallService destinationService = new ProcessStartDestinationVoiceCallService(voiceCallDto);

        Transaction.begin();
        boolean txCommitted = false;
        try {
            // Transactional code area
            sourceService.dispatch();
            destinationService.dispatch();
            // end
            Transaction.commit();
            txCommitted = true;
        } finally {
            if (!txCommitted) {
                Transaction.abort();
            }
        }
    }

    @Override
    public void endVoiceCall(CommunicationDurationDto voiceEndCallDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            InvalidDurationException,
            NoActiveCommunicationException {

        ProcessEndSourceVoiceCallService sourceService = new ProcessEndSourceVoiceCallService(voiceEndCallDto);
        ProcessEndDestinationVoiceCallService destinationService = new ProcessEndDestinationVoiceCallService(voiceEndCallDto);

        Transaction.begin();
        boolean txCommitted = false;
        try {
            // Transactional code area
            sourceService.dispatch();
            destinationService.dispatch();
            // end
            Transaction.commit();
            txCommitted = true;
        } finally {
            if (!txCommitted) {
                Transaction.abort();
            }
        }
    }

}
