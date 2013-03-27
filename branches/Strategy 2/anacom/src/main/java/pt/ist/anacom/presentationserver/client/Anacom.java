package pt.ist.anacom.presentationserver.client;

import java.util.List;

import pt.ist.anacom.presentationserver.client.view.EndVoiceCallPanel;
import pt.ist.anacom.presentationserver.client.view.GetBalancePanel;
import pt.ist.anacom.presentationserver.client.view.GetLastCommunicationMadePanel;
import pt.ist.anacom.presentationserver.client.view.GetPhoneStatePanel;
import pt.ist.anacom.presentationserver.client.view.IncreasePhoneBalancePanel;
import pt.ist.anacom.presentationserver.client.view.SMSReceivedListPanel;
import pt.ist.anacom.presentationserver.client.view.SendSMSPanel;
import pt.ist.anacom.presentationserver.client.view.SetPhoneNumberPanel;
import pt.ist.anacom.presentationserver.client.view.SetPhoneStatePanel;
import pt.ist.anacom.presentationserver.client.view.StartVoiceCallPanel;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneStateException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Anacom implements EntryPoint {

    private static final String remoteServerType = "ES+SD";
    private static final String localServerType = "ES-only";

    private final AnacomServiceAsync rpcService = GWT.create(AnacomService.class);

    private final Label serverTypeLabel = new Label("Anacom");

    private String phone = "";
    private long startTime;

    private final SetPhoneNumberPanel setPhoneNumberPanel = new SetPhoneNumberPanel();
    private final GetBalancePanel getBalancePanel = new GetBalancePanel();
    private final IncreasePhoneBalancePanel incBalancePanel = new IncreasePhoneBalancePanel();
    private final SendSMSPanel sendSMSPanel = new SendSMSPanel();
    private final SMSReceivedListPanel receivedSMSPanel = new SMSReceivedListPanel();
    private final GetPhoneStatePanel getPhoneStatePanel = new GetPhoneStatePanel();
    private final SetPhoneStatePanel setPhoneStatePanel = new SetPhoneStatePanel();
    private final GetLastCommunicationMadePanel getLastCommunicationMadePanel = new GetLastCommunicationMadePanel();
    private final StartVoiceCallPanel startVoiceCallPanel = new StartVoiceCallPanel();
    private final EndVoiceCallPanel endVoiceCallPanel = new EndVoiceCallPanel();


    @Override
    public void onModuleLoad() {
        GWT.log("presentationserver.client.Anacom::onModuleLoad() - begin");

        // create label with mode type
        serverTypeLabel.setStyleName("h1");
        String serverType; // depends on type of running
        if (RootPanel.get(remoteServerType) != null) {
            GWT.log("presentationserver.client.Anacom::onModuleLoad() running on remote mode");
            serverTypeLabel.setText("Anacom - Remote mode");
            serverType = remoteServerType;
        } else { // default: local - even if it is misspelled
            GWT.log("presentationserver.client.Anacom::onModuleLoad() running on local mode");
            serverTypeLabel.setText("Anacom - Local mode");
            serverType = localServerType;
        }

        RootPanel typeRootPanel = RootPanel.get(serverType);
        typeRootPanel.add(serverTypeLabel);
        serverTypeLabel.setWidth("100%");

        // STT setPhoneNumber

        RootPanel phoneNumberRootPanel = RootPanel.get("setPhoneNumberContainer");
        phoneNumberRootPanel.add(setPhoneNumberPanel);
        setPhoneNumberPanel.setWidth("100%");

        this.setPhoneNumberPanel.getSetPhoneNumberButton().addClickHandler(new MyClickHandler());
        this.setPhoneNumberPanel.getPhoneNumberTextBox().addKeyUpHandler(new MyTypeHandler());

        // END setPhoneNumber

        // STT getBalance

        RootPanel balanceRootPanel = RootPanel.get("balanceContainer");
        balanceRootPanel.add(getBalancePanel);
        getBalancePanel.setWidth("100%");

        // GetBalance Handlers
        this.getBalancePanel.getGetBalanceButton().addClickHandler(new MyClickHandler());

        // END getBalance

        // STT incBalance

        RootPanel increaseBalanceRootPanel = RootPanel.get("increaseBalanceContainer");
        increaseBalanceRootPanel.add(incBalancePanel);
        incBalancePanel.setWidth("100%");

        // IncBalance Handlers
        this.incBalancePanel.getIncreasePhoneBalanceButton().addClickHandler(new MyClickHandler());

        // END incBalance

        // STT sendSMS

        RootPanel smsRootPanel = RootPanel.get("smsContainer");
        smsRootPanel.add(sendSMSPanel);
        sendSMSPanel.setWidth("100%");

        // SendSMS Handlers
        this.sendSMSPanel.getSendSMSButton().addClickHandler(new MyClickHandler());
        this.sendSMSPanel.getPhoneDestNumberTextBox().addKeyUpHandler(new MyTypeHandler());

        // END SendSMS

        // STT Received SMS List

        RootPanel smsReceivedRootPanel = RootPanel.get("receivedSMSListContainer");
        smsReceivedRootPanel.add(receivedSMSPanel);
        receivedSMSPanel.setWidth("100%");

        // ReceivedSMS Handlers
        this.receivedSMSPanel.getReceivedSMSButton().addClickHandler(new MyClickHandler());

        // END Received SMS List

        // STT GetState Panel
        RootPanel getStateRootPanel = RootPanel.get("getStateContainer");
        getStateRootPanel.add(getPhoneStatePanel);
        getPhoneStatePanel.setWidth("100%");

        // GetState Handlers
        this.getPhoneStatePanel.getStateButton().addClickHandler(new MyClickHandler());

        // END GetState Panel

        // STT SetState Panel
        RootPanel setStateRootPanel = RootPanel.get("setStateContainer");
        setStateRootPanel.add(setPhoneStatePanel);
        setPhoneStatePanel.setWidth("100%");

        // SetState Handlers
        this.setPhoneStatePanel.getStateButton().addClickHandler(new MyClickHandler());

        // END SetState Panel

        // STT getLastCommunicationMade

        RootPanel lastCommunicationMadeRootPanel = RootPanel.get("getLastCommunicationMadeContainer");
        lastCommunicationMadeRootPanel.add(getLastCommunicationMadePanel);
        getLastCommunicationMadePanel.setWidth("100%");

        // getLastCommunicationMade Handlers
        this.getLastCommunicationMadePanel.getLastCommunicationMadeButton().addClickHandler(new MyClickHandler());

        // END getLastCommunicationMade

        // STT startVoiceCallPanel

        RootPanel startVoiceCallRootPanel = RootPanel.get("startVoiceCallContainer");
        startVoiceCallRootPanel.add(startVoiceCallPanel);
        startVoiceCallPanel.setWidth("100%");

        // startVoiceCallPanel Handlers
        this.startVoiceCallPanel.getStartVoiceCallButton().addClickHandler(new MyClickHandler());
        this.startVoiceCallPanel.getDestinationPhoneNumberTextBox().addKeyUpHandler(new MyTypeHandler());

        // END startVoiceCallPanel

        // STT endVoiceCallPanel

        RootPanel endVoiceCallRootPanel = RootPanel.get("endVoiceCallContainer");
        endVoiceCallRootPanel.add(endVoiceCallPanel);
        endVoiceCallPanel.setWidth("100%");

        // startVoiceCallPanel Handlers
        this.endVoiceCallPanel.getEndVoiceCallButton().addClickHandler(new MyClickHandler());
        this.endVoiceCallPanel.getDestinationPhoneNumberTextBox().addKeyUpHandler(new MyTypeHandler());

        // END endVoiceCallPanel


        this.rpcService.initBridge(serverType, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.initBridge - done!");
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.initBridge");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                Window.alert("Not able to init aplication server bridge: " + caught.getMessage());
            }
        });

        GWT.log("presentationserver.client.Anacom::onModuleLoad() - done!");

    }

    // Create a generic handler that gets data and calls createContact
    class MyHandler {

        // Send the request to server
        protected void getBalanceRequest() {
            getPhoneBalance();
        }

        protected void setPhoneNumberRequest() {
            String number = setPhoneNumberPanel.getPhoneNumberTextBox().getValue();
            setPhoneNumber(number);
        }

        protected void incBalanceRequest() {
            String value = incBalancePanel.getValueTextBox().getValue();
            increasePhoneBalance(value);
        }

        protected void sendSMSRequest() {
            String destNumber = sendSMSPanel.getPhoneDestNumberTextBox().getValue();
            String text = sendSMSPanel.getSmsTextBox().getValue();
            sendSMS(destNumber, text);
        }

        protected void getReceivedSMSRequest() {
            getReceivedSMSList();
        }

        protected void getPhoneStateRequest() {
            getPhoneState();
        }

        protected void setPhoneStateRequest() {
            int index = setPhoneStatePanel.getListBox().getSelectedIndex();
            String state = setPhoneStatePanel.getListBox().getItemText(index);

            if (state.equals(AnacomData.State.ON.name()))
                setPhoneState(AnacomData.State.ON);
            else if (state.equals(AnacomData.State.OFF.name()))
                setPhoneState(AnacomData.State.OFF);
            else if (state.equals(AnacomData.State.BUSY.name()))
                setPhoneState(AnacomData.State.BUSY);
            else if (state.equals(AnacomData.State.SILENCE.name()))
                setPhoneState(AnacomData.State.SILENCE);
        }

        protected void getLastCommunicationMadeRequest() {
            getLastCommunicationMade();
        }

        protected void startVoiceCallRequest() {
            String value = startVoiceCallPanel.getDestinationPhoneNumberTextBox().getValue();
            startVoiceCall(value);
        }

        protected void endVoiceCallRequest() {
            String value = endVoiceCallPanel.getDestinationPhoneNumberTextBox().getValue();
            endVoiceCall(value);
        }
    }

    // Create a handler for clicks
    class MyClickHandler extends MyHandler implements ClickHandler {
        // Fired when the user clicks on the related button
        public void onClick(ClickEvent event) {
            // GWT.log("presentationserver.client.Anacom.MyClickHandler::onClick()");

            if (event.getSource().equals(getBalancePanel.getGetBalanceButton()))
                getBalanceRequest();

            else if (event.getSource().equals(setPhoneNumberPanel.getSetPhoneNumberButton()))
                setPhoneNumberRequest();

            else if (event.getSource().equals(incBalancePanel.getIncreasePhoneBalanceButton()))
                incBalanceRequest();

            else if (event.getSource().equals(sendSMSPanel.getSendSMSButton()))
                sendSMSRequest();

            else if (event.getSource().equals(receivedSMSPanel.getReceivedSMSButton()))
                getReceivedSMSRequest();

            else if (event.getSource().equals(getPhoneStatePanel.getStateButton()))
                getPhoneStateRequest();

            else if (event.getSource().equals(setPhoneStatePanel.getStateButton()))
                setPhoneStateRequest();

            else if (event.getSource().equals(getLastCommunicationMadePanel.getLastCommunicationMadeButton()))
                getLastCommunicationMadeRequest();

            else if (event.getSource().equals(startVoiceCallPanel.getStartVoiceCallButton()))
                startVoiceCallRequest();

            else if (event.getSource().equals(endVoiceCallPanel.getEndVoiceCallButton()))
                endVoiceCallRequest();
        }
    }

    // Create a handler for ENTER typing
    class MyTypeHandler extends MyHandler implements KeyUpHandler {
        // Fired when the user types in a field
        public void onKeyUp(KeyUpEvent event) {
            // GWT.log("presentationserver.client.Anacom.MyTypeHandler::onKeyUp() "
            // + "'" + event.getNativeKeyCode() + "'");
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                // GWT.log("presentationserver.client.Anacom.MyTypeHandler::onKeyUp()");
                if (event.getSource().equals(getBalancePanel))
                    getBalanceRequest();

                else if (event.getSource().equals(setPhoneNumberPanel))
                    setPhoneNumberRequest();

                else if (event.getSource().equals(incBalancePanel))
                    incBalanceRequest();

                else if (event.getSource().equals(sendSMSPanel))
                    sendSMSRequest();

                else if (event.getSource().equals(receivedSMSPanel))
                    getReceivedSMSRequest();

                else if (event.getSource().equals(getLastCommunicationMadePanel))
                    getLastCommunicationMadeRequest();

                else if (event.getSource().equals(startVoiceCallPanel))
                    startVoiceCallRequest();

                else if (event.getSource().equals(endVoiceCallPanel))
                    endVoiceCallRequest();
            }
        }
    }

    public final void clearAllPanels() {

        // getBalancePanel
        getBalancePanel.getBalanceTable().removeAllRows();
        getBalancePanel.getErrorLabel().setText("");

        // incBalancePanel
        incBalancePanel.getValueTextBox().setText("");
        incBalancePanel.getErrorLabel().setText("");

        // sendSMSPanel
        sendSMSPanel.getPhoneDestNumberTextBox().setText("");
        sendSMSPanel.getSmsTextBox().setText("");
        sendSMSPanel.getErrorLabel().setText("");
        sendSMSPanel.getEmptyLabel().setText("");

        // receivedSMSPanel
        receivedSMSPanel.clearTable();
        receivedSMSPanel.getEmptyLabel().setText("");

        // getPhoneStatePanel
        getPhoneStatePanel.getStateTable().removeAllRows();
        getPhoneStatePanel.getErrorLabel().setText("");

        // setPhoneStatePanel
        setPhoneStatePanel.getErrorLabel().setText("");
        setPhoneNumberPanel.getPhoneNumberTextBox().setText("");

        // getLastCommunicationMadePanel
        getLastCommunicationMadePanel.getLastCommunicationMadeTable().removeAllRows();
        getLastCommunicationMadePanel.getErrorLabel().setText("");

        // startVoiceCallCommunicationPanel
        startVoiceCallPanel.getDestinationPhoneNumberTextBox().setText("");
        startVoiceCallPanel.getErrorLabel().setText("");

        // endVoiceCallCommunicationPanel
        endVoiceCallPanel.getDestinationPhoneNumberTextBox().setText("");
        endVoiceCallPanel.getErrorLabel().setText("");
    }

    public final void setPhoneNumber(String phoneNumber) {

        clearAllPanels();

        if (phoneNumber.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        // if both are filled: clear warning and execute request!
        setPhoneNumberPanel.getErrorLabel().setText("");

        if (!phoneNumber.matches("[0-9]+")) {
            setPhoneNumberPanel.getErrorLabel().setText("Invalid Phone Number data.");
            return;
        }

        if (phoneNumber.length() > 9) {
            setPhoneNumberPanel.getErrorLabel().setText("Phone Number too long!");
            return; // no need to contact server - bad format number
        }

        // sets phone number
        phone = phoneNumber;

        setPhoneNumberPanel.getFinalPhoneNumberLabel().setText(phone);
    }

    public final void getPhoneBalance() {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        final PhoneSimpleDto dto = new PhoneSimpleDto(phone);

        rpcService.getPhoneBalance(dto, new AsyncCallback<BalanceDto>() {

            @Override
            public void onSuccess(BalanceDto result) {
                getBalancePanel.getBalanceTable().removeAllRows();
                getBalancePanel.getErrorLabel().setText("");
                getBalancePanel.getBalanceTable().setText(1, 0, "Balance is: " + result.getBalance());
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.getPhoneBalance");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }

    public final void increasePhoneBalance(String value) {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        if (value.equals("")) {
            incBalancePanel.getErrorLabel().setText("Please fill all fields!");
            return;
        }

        int newValue = Integer.parseInt(value);

        final BalanceAndPhoneDto dto = new BalanceAndPhoneDto(phone, newValue);

        rpcService.increaseBalance(dto, new AsyncCallback<Void>() {
            public void onSuccess(Void response) {
                incBalancePanel.getValueTextBox().setText("");
                incBalancePanel.getErrorLabel().setText("");
                Window.alert("Balance increased by '" + dto.getBalance() + "' with sucess!");
            }

            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.increasePhoneBalance");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof NegativeBalanceValueException) {
                    NegativeBalanceValueException ex = (NegativeBalanceValueException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof BalanceLimitExceededException) {
                    BalanceLimitExceededException ex = (BalanceLimitExceededException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }

    public final void getPhoneState() {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        final PhoneSimpleDto dto = new PhoneSimpleDto(phone);
        rpcService.getPhoneState(dto, new AsyncCallback<StateDto>() {

            @Override
            public void onSuccess(StateDto result) {
                getPhoneStatePanel.getStateTable().removeAllRows();
                getPhoneStatePanel.getErrorLabel().setText("");
                getPhoneStatePanel.getStateTable().setText(1, 0, "STATE IS " + result.getState());
            }

            public void onFailure(Throwable caught) {
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }

    public final void setPhoneState(AnacomData.State state) {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        final PhoneAndStateDto dto = new PhoneAndStateDto(phone, state);

        rpcService.setPhoneState(dto, new AsyncCallback<Void>() {
            public void onSuccess(Void response) {
                setPhoneNumberPanel.getErrorLabel().setText("");
                Window.alert("State changed to '" + dto.getState() + "' with success!");
            }

            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.SetPhoneState");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }

    public final void sendSMS(String destinationPhoneNumber, String text) {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        if (destinationPhoneNumber.equals("")) {
            sendSMSPanel.getErrorLabel().setText("Please fill all fields!");
            return;
        }

        sendSMSPanel.getErrorLabel().setText("");

        if (!destinationPhoneNumber.matches("[0-9]+")) {
            sendSMSPanel.getErrorLabel().setText("Invalid Destination Phone Number data.");
            return;
        }

        if (destinationPhoneNumber.length() > 9) {
            sendSMSPanel.getErrorLabel().setText("Destination Phone Number too long!");
            return;
        }

        final SMSDto dto = new SMSDto(phone, destinationPhoneNumber, text);

        rpcService.sendSMS(dto, new AsyncCallback<Void>() {
            public void onSuccess(Void response) {
                sendSMSPanel.getPhoneDestNumberTextBox().setText("");
                sendSMSPanel.getSmsTextBox().setText("");
                sendSMSPanel.getErrorLabel().setText("");
                Window.alert("SMS sent to '" + dto.getDestinationPhoneNumber() + "' with sucess!");
            }

            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.sendSMS");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof NegativeBalanceValueException) {
                    NegativeBalanceValueException ex = (NegativeBalanceValueException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof InsuficientBalanceException) {
                    InsuficientBalanceException ex = (InsuficientBalanceException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof PhoneStateException) {
                    PhoneStateException ex = (PhoneStateException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }

    public final void getReceivedSMSList() {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        final PhoneSimpleDto dto = new PhoneSimpleDto(phone);
        rpcService.getSMSPhoneReceivedList(dto, new AsyncCallback<SMSPhoneReceivedListDto>() {

            public final void showReceivedSMS(List<SMSDto> smsList) {
                if (smsList != null)
                    for (SMSDto dto : smsList)
                        receivedSMSPanel.add(dto);
            }

            @Override
            public void onSuccess(SMSPhoneReceivedListDto result) {
                receivedSMSPanel.clearTable();

                if (result.getSmsList().size() == 0) {
                    Window.alert("This number haven't received any sms yet.");
                    receivedSMSPanel.getEmptyLabel().setText("Lista Vazia.");
                } else
                    showReceivedSMS(result.getSmsList());
            }

            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.GetReceivedSMSList");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof NegativeBalanceValueException) {
                    NegativeBalanceValueException ex = (NegativeBalanceValueException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }

    public final void getLastCommunicationMade() {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        final PhoneSimpleDto dto = new PhoneSimpleDto(phone);

        rpcService.getLastMadeCommunication(dto, new AsyncCallback<LastCommunicationDto>() {

            @Override
            public void onSuccess(LastCommunicationDto result) {
                getLastCommunicationMadePanel.getLastCommunicationMadeTable().removeAllRows();
                getLastCommunicationMadePanel.getErrorLabel().setText("");

                getLastCommunicationMadePanel.getLastCommunicationMadeTable().setText(1, 0, "Last Communication was a " + result.getCommunicationType() + " to " + result.getDestinationPhoneNumber() + " that costed " + result.getCost() + " and had length of " + result.getLength());
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.getLastCommunicationMade");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");

                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof NoCommunicationsMadeYetException) {
                    NoCommunicationsMadeYetException ex = (NoCommunicationsMadeYetException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }

    public final void startVoiceCall(String destinationPhoneNumber) {
        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        if (destinationPhoneNumber.equals("")) {
            startVoiceCallPanel.getErrorLabel().setText("Please fill all fields!");
            return;
        }

        startVoiceCallPanel.getErrorLabel().setText("");

        if (!destinationPhoneNumber.matches("[0-9]+")) {
            startVoiceCallPanel.getErrorLabel().setText("Invalid Destination Phone Number data.");
            return;
        }

        if (destinationPhoneNumber.length() > 9) {
            startVoiceCallPanel.getErrorLabel().setText("Destination Phone Number too long!");
            return;
        }

        final CommunicationDto voiceDto = new CommunicationDto(phone, destinationPhoneNumber);

        rpcService.startVoiceCall(voiceDto, new AsyncCallback<Void>() {
            public void onSuccess(Void response) {
                startTime = AnacomData.getCurrentTime();
                startVoiceCallPanel.getDestinationPhoneNumberTextBox().setText("");
                startVoiceCallPanel.getErrorLabel().setText("");
                Window.alert("Voice call started to '" + voiceDto.getDestinationPhoneNumber() + "' with sucess!");
            }

            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.startVoiceCall");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof NegativeBalanceValueException) {
                    NegativeBalanceValueException ex = (NegativeBalanceValueException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof InsuficientBalanceException) {
                    InsuficientBalanceException ex = (InsuficientBalanceException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof PhoneStateException) {
                    PhoneStateException ex = (PhoneStateException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }

    public final void endVoiceCall(String destinationPhoneNumber) {
        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        if (destinationPhoneNumber.equals("")) {
            endVoiceCallPanel.getErrorLabel().setText("Please fill all fields!");
            return;
        }

        endVoiceCallPanel.getErrorLabel().setText("");

        if (!destinationPhoneNumber.matches("[0-9]+")) {
            endVoiceCallPanel.getErrorLabel().setText("Invalid Destination Phone Number data.");
            return;
        }

        if (destinationPhoneNumber.length() > 9) {
            endVoiceCallPanel.getErrorLabel().setText("Destination Phone Number too long!");
            return;
        }

        int duration = AnacomData.getCommunicationDuration(startTime);

        final CommunicationDurationDto voiceDto = new CommunicationDurationDto(phone, destinationPhoneNumber, duration);

        rpcService.endVoiceCall(voiceDto, new AsyncCallback<Void>() {
            public void onSuccess(Void response) {
                endVoiceCallPanel.getDestinationPhoneNumberTextBox().setText("");
                endVoiceCallPanel.getErrorLabel().setText("");
                Window.alert("Voice call to [" + voiceDto.getDestinationPhoneNumber() + "] end sucessfuly!");
            }

            public void onFailure(Throwable caught) {
                GWT.log("presentationserver.client.Anacom::onModuleLoad()::rpcService.endVoiceCall");
                GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
                if (caught instanceof NoSuchPhoneException) {
                    NoSuchPhoneException ex = (NoSuchPhoneException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof OperatorPrefixDoesNotExistException) {
                    OperatorPrefixDoesNotExistException ex = (OperatorPrefixDoesNotExistException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof NegativeBalanceValueException) {
                    NegativeBalanceValueException ex = (NegativeBalanceValueException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof InsuficientBalanceException) {
                    InsuficientBalanceException ex = (InsuficientBalanceException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof InvalidDurationException) {
                    InvalidDurationException ex = (InvalidDurationException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof NoActiveCommunicationException) {
                    NoActiveCommunicationException ex = (NoActiveCommunicationException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR:Unknown Exception: " + caught.getMessage());
                }
            }
        });
    }
}
