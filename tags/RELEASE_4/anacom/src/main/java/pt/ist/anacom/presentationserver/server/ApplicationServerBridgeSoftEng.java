package pt.ist.anacom.presentationserver.server;

import jvstm.Atomic;
import pt.ist.anacom.service.CancelRegisteredPhoneService;
import pt.ist.anacom.service.GetBalanceAndPhoneListService;
import pt.ist.anacom.service.GetBalanceService;
import pt.ist.anacom.service.GetPhoneStateService;
import pt.ist.anacom.service.GetSMSPhoneReceivedListService;
import pt.ist.anacom.service.IncreaseBalanceService;
import pt.ist.anacom.service.ProcessReceiveSMSService;
import pt.ist.anacom.service.ProcessSendSMSService;
import pt.ist.anacom.service.RegisterNewOperatorService;
import pt.ist.anacom.service.RegisterNewPhoneService;
import pt.ist.anacom.service.SetPhoneStateService;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateAndPhoneDto;
import pt.ist.anacom.shared.dto.StateDto;

public class ApplicationServerBridgeSoftEng implements ApplicationServerBridge {

    @Override
    public void registerPhone(PhoneDetailedDto dto) {
        RegisterNewPhoneService service = new RegisterNewPhoneService(dto);
        service.execute();
    }

    @Override
    public void registerOperator(OperatorDetailedDto operatorDto) {
        RegisterNewOperatorService service = new RegisterNewOperatorService(operatorDto);
        service.execute();
    }

    @Override
    public void increaseBalance(BalanceAndPhoneDto incBalDto) {
        IncreaseBalanceService incBalService = new IncreaseBalanceService(incBalDto);
        incBalService.execute();
    }

    @Override
    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) {
        GetBalanceService getBalService = new GetBalanceService(phoneDto);
        getBalService.execute();
        return getBalService.getBalanceServiceResult();
    }

    @Override
    public void cancelRegisteredPhone(PhoneSimpleDto phoneDto) {
        CancelRegisteredPhoneService cancelRegister = new CancelRegisteredPhoneService(phoneDto);
        cancelRegister.execute();
    }

    @Override
    public StateDto getPhoneState(PhoneSimpleDto phoneDto) {
        GetPhoneStateService getPhoneStateService = new GetPhoneStateService(phoneDto);
        getPhoneStateService.execute();
        return getPhoneStateService.getPhoneStateServiceResult();
    }

    @Override
    public void setPhoneState(StateAndPhoneDto setStateDto) {
        SetPhoneStateService setPhoneStatusService = new SetPhoneStateService(setStateDto);
        setPhoneStatusService.execute();
    }

    @Override
    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto) {
        GetBalanceAndPhoneListService getBalanceAndPhoneListService = new GetBalanceAndPhoneListService(operatorDto);
        getBalanceAndPhoneListService.execute();
        return getBalanceAndPhoneListService.getBalanceAndPhoneListServiceResult();
    }

    @Atomic
    @Override
    public void sendSMS(SMSDto SMSDto) {
        ProcessSendSMSService sendService = new ProcessSendSMSService(SMSDto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(SMSDto);
        sendService.dispatch();
        receiveService.dispatch();
    }

    @Override
    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) {
        GetSMSPhoneReceivedListService getSMSPhoneReceivedListService = new GetSMSPhoneReceivedListService(phoneDto);
        getSMSPhoneReceivedListService.execute();
        return getSMSPhoneReceivedListService.getSMSPhoneReceivedListServiceResult();
    }
}
