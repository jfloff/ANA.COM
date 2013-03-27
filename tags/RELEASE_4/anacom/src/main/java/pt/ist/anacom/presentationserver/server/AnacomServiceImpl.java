package pt.ist.anacom.presentationserver.server;

import pt.ist.anacom.applicationserver.DatabaseBootstrap;
import pt.ist.anacom.presentationserver.client.AnacomService;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateAndPhoneDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidBalanceOperationException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneIsBUSYException;
import pt.ist.anacom.shared.exception.PhoneIsOFFException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AnacomServiceImpl extends RemoteServiceServlet implements AnacomService {

    private static final long serialVersionUID = 1L;

    private static ApplicationServerBridge bridge = null;
    private static boolean databasedInitialized = false;

    public void initBridge(String serverType) {
        if (serverType.equals("ES+SD")) {
            bridge = new ApplicationServerBridgeDistSoft();
        } else if (serverType.equals("ES-only")) {
            bridge = new ApplicationServerBridgeSoftEng();
            if (!databasedInitialized) {
                DatabaseBootstrap.init();
                databasedInitialized = true;
            }
        } else {
            throw new RuntimeException("Servlet parameter error: ES+SD nor ES-only");
        }
    }

    @Override
    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {
        return bridge.getPhoneBalance(phoneDto);
    }

    @Override
    public void increaseBalance(BalanceAndPhoneDto incBalDto) throws OperatorPrefixDoesNotExistException,
            NoSuchPhoneException,
            InvalidBalanceOperationException {
        bridge.increaseBalance(incBalDto);
    }

    @Override
    public StateDto getPhoneState(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {
        return bridge.getPhoneState(phoneDto);
    }

    @Override
    public void setPhoneState(StateAndPhoneDto setStateDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {
        bridge.setPhoneState(setStateDto);
    }

    @Override
    public void sendSMS(SMSDto SMSDto) throws OperatorPrefixDoesNotExistException,
            NoSuchPhoneException,
            InsuficientBalanceException,
            InvalidBalanceOperationException,
            PhoneIsOFFException,
            PhoneIsBUSYException {
        bridge.sendSMS(SMSDto);
    }

    @Override
    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {
        return bridge.getSMSPhoneReceivedList(phoneDto);
    }
}
