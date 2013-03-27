package pt.ist.anacom.presentationserver.client;

import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateAndPhoneDto;
import pt.ist.anacom.shared.dto.StateDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AnacomServiceAsync {

    void initBridge(String serverType, AsyncCallback<Void> callback);

    void getPhoneBalance(PhoneSimpleDto phoneDto, AsyncCallback<BalanceDto> callback);

    void increaseBalance(BalanceAndPhoneDto incBalDto, AsyncCallback<Void> callback);

    void getPhoneState(PhoneSimpleDto phoneDto, AsyncCallback<StateDto> callback);

    void setPhoneState(StateAndPhoneDto setStateDto, AsyncCallback<Void> callback);

    void sendSMS(SMSDto SMSDto, AsyncCallback<Void> callback);

    void getSMSPhoneReceivedList(PhoneSimpleDto phoneDto, AsyncCallback<SMSPhoneReceivedListDto> callback);
}
