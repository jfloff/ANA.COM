package pt.ist.anacom.presentationserver.client;

import java.util.List;

import pt.ist.anacom.presentationserver.client.view.GetBalancePanel;
import pt.ist.anacom.presentationserver.client.view.GetLastCommunicationMadePanel;
import pt.ist.anacom.presentationserver.client.view.GetPhoneStatePanel;
import pt.ist.anacom.presentationserver.client.view.IncreasePhoneBalancePanel;
import pt.ist.anacom.presentationserver.client.view.SMSReceivedListPanel;
import pt.ist.anacom.presentationserver.client.view.SendSMSPanel;
import pt.ist.anacom.presentationserver.client.view.SetPhoneNumberPanel;
import pt.ist.anacom.presentationserver.client.view.SetPhoneStatePanel;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationOutDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateAndPhoneDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.CommunicationListIsEmptyException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidBalanceOperationException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneIsBUSYException;
import pt.ist.anacom.shared.exception.PhoneIsOFFException;

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

    private final SetPhoneNumberPanel setPhoneNumberPanel = new SetPhoneNumberPanel();
    private final GetBalancePanel getBalancePanel = new GetBalancePanel();
    private final IncreasePhoneBalancePanel incBalancePanel = new IncreasePhoneBalancePanel();
    private final SendSMSPanel sendSMSPanel = new SendSMSPanel();
    private final SMSReceivedListPanel receivedSMSPanel = new SMSReceivedListPanel();
    private final GetPhoneStatePanel getPhoneStatePanel = new GetPhoneStatePanel();
    private final SetPhoneStatePanel setPhoneStatePanel = new SetPhoneStatePanel();
    private final GetLastCommunicationMadePanel getLastCommunicationMadePanel = new GetLastCommunicationMadePanel();

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

        RootPanel phoneNumberPanel = RootPanel.get("setPhoneNumberContainer");
        phoneNumberPanel.add(setPhoneNumberPanel);
        setPhoneNumberPanel.setWidth("100%");

        this.setPhoneNumberPanel.getSetPhoneNumberButton().addClickHandler(new MyClickHandler());
        this.setPhoneNumberPanel.getPhoneNumberTextBox().addKeyUpHandler(new MyTypeHandler());

        // END setPhoneNumber

        // STT getBalance

        RootPanel balancePanel = RootPanel.get("balanceContainer");
        balancePanel.add(getBalancePanel);
        getBalancePanel.setWidth("100%");

        // GetBalance Handlers
        this.getBalancePanel.getGetBalanceButton().addClickHandler(new MyClickHandler());

        // END getBalance

        // STT incBalance

        RootPanel increaseBalancePanel = RootPanel.get("increaseBalanceContainer");
        increaseBalancePanel.add(incBalancePanel);
        incBalancePanel.setWidth("100%");

        // IncBalance Handlers
        this.incBalancePanel.getIncreasePhoneBalanceButton().addClickHandler(new MyClickHandler());
        this.incBalancePanel.getIncreasePhoneBalanceButton().addKeyUpHandler(new MyTypeHandler());

        // END incBalance

        // STT sendSMS

        RootPanel smsPanel = RootPanel.get("smsContainer");
        smsPanel.add(sendSMSPanel);
        sendSMSPanel.setWidth("100%");

        // SendSMS Handlers
        this.sendSMSPanel.getSendSMSButton().addClickHandler(new MyClickHandler());
        this.sendSMSPanel.getPhoneDestNumberTextBox().addKeyUpHandler(new MyTypeHandler());

        // END SendSMS

        // STT Received SMS List

        RootPanel smsReceivedPanel = RootPanel.get("receivedSMSListContainer");
        smsReceivedPanel.add(receivedSMSPanel);
        receivedSMSPanel.setWidth("100%");

        // ReceivedSMS Handlers
        this.receivedSMSPanel.getReceivedSMSButton().addClickHandler(new MyClickHandler());

        // END Received SMS List

        // STT GetState Panel
        RootPanel getStatePanel = RootPanel.get("getStateContainer");
        getStatePanel.add(getPhoneStatePanel);
        getPhoneStatePanel.setWidth("100%");

        // GetState Handlers
        this.getPhoneStatePanel.getStateButton().addClickHandler(new MyClickHandler());

        // END GetState Panel

        // STT SetState Panel
        RootPanel setStatePanel = RootPanel.get("setStateContainer");
        setStatePanel.add(setPhoneStatePanel);
        setPhoneStatePanel.setWidth("100%");

        // SetState Handlers
        this.setPhoneStatePanel.getStateButton().addClickHandler(new MyClickHandler());

        // END SetState Panel

        // STT getLastCommunicationMade

        RootPanel lastCommunicationMadePanel = RootPanel.get("getLastCommunicationMadeContainer");
        lastCommunicationMadePanel.add(getLastCommunicationMadePanel);
        getLastCommunicationMadePanel.setWidth("100%");

        // GetBalance Handlers
        this.getLastCommunicationMadePanel.getLastCommunicationMadeButton().addClickHandler(new MyClickHandler());

        // END getLastCommunicationMade

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

        // getPhoneStatePanel
        getPhoneStatePanel.getStateTable().removeAllRows();
        getPhoneStatePanel.getErrorLabel().setText("");

        // setPhoneStatePanel
        setPhoneStatePanel.getErrorLabel().setText("");
        setPhoneNumberPanel.getPhoneNumberTextBox().setText("");

        // getLastCommunicationMadePanel
        getLastCommunicationMadePanel.getLastCommunicationMadeTable().removeAllRows();
        getLastCommunicationMadePanel.getErrorLabel().setText("");
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
                    Window.alert("ERROR: Cannot show balance: " + caught.getMessage());
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
                } else if (caught instanceof InvalidBalanceOperationException) {
                    InvalidBalanceOperationException ex = (InvalidBalanceOperationException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR: Cannot increase balance: " + caught.getMessage());
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
                    Window.alert("ERROR: Cannot show phone state: " + caught.getMessage());
                }
            }
        });
    }

    public final void setPhoneState(AnacomData.State state) {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        final StateAndPhoneDto dto = new StateAndPhoneDto(phone, state);

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
                    Window.alert("ERROR: Cannot set phone state: " + caught.getMessage());
                }
            }
        });
    }

    public final void sendSMS(String phoneDestNumber, String text) {

        if (phone.equals("")) {
            setPhoneNumberPanel.getErrorLabel().setText("Please fill in Phone Number field.");
            return;
        }

        if (phoneDestNumber.equals("")) {
            sendSMSPanel.getErrorLabel().setText("Please fill all fields!");
            return;
        }

        sendSMSPanel.getErrorLabel().setText("");

        if (!phoneDestNumber.matches("[0-9]+")) {
            sendSMSPanel.getErrorLabel().setText("Invalid Destination Phone Number data.");
            return;
        }

        if (phoneDestNumber.length() > 9) {
            sendSMSPanel.getErrorLabel().setText("Destination Phone Number too long!");
            return;
        }

        final SMSDto dto = new SMSDto(phone, phoneDestNumber, text);

        rpcService.sendSMS(dto, new AsyncCallback<Void>() {
            public void onSuccess(Void response) {
                sendSMSPanel.getPhoneDestNumberTextBox().setText("");
                sendSMSPanel.getSmsTextBox().setText("");
                sendSMSPanel.getErrorLabel().setText("");
                Window.alert("SMS sent to '" + dto.getDestinationNumber() + "' with sucess!");
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
                } else if (caught instanceof InvalidBalanceOperationException) {
                    InvalidBalanceOperationException ex = (InvalidBalanceOperationException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof InsuficientBalanceException) {
                    InsuficientBalanceException ex = (InsuficientBalanceException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof PhoneIsBUSYException) {
                    PhoneIsBUSYException ex = (PhoneIsBUSYException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof PhoneIsOFFException) {
                    PhoneIsOFFException ex = (PhoneIsOFFException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR: Cannot increase balance: " + caught.getMessage());
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

                if (result.getSmsList().size() == 0)
                    receivedSMSPanel.getEmptyLabel().setText("Lista Vazia.");
                else
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
                } else if (caught instanceof InvalidBalanceOperationException) {
                    InvalidBalanceOperationException ex = (InvalidBalanceOperationException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof PhoneIsBUSYException) {
                    PhoneIsBUSYException ex = (PhoneIsBUSYException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof PhoneIsOFFException) {
                    PhoneIsOFFException ex = (PhoneIsOFFException) caught;
                    Window.alert(ex.getMessage());
                } else if (caught instanceof InsuficientBalanceException) {
                    InsuficientBalanceException ex = (InsuficientBalanceException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR: Cannot increase balance: " + caught.getMessage());
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

        rpcService.getLastMadeCommunication(dto, new AsyncCallback<CommunicationOutDto>() {

            @Override
            public void onSuccess(CommunicationOutDto result) {
                getLastCommunicationMadePanel.getLastCommunicationMadeTable().removeAllRows();
                getLastCommunicationMadePanel.getErrorLabel().setText("");

                getLastCommunicationMadePanel.getLastCommunicationMadeTable().setText(1,
                                                                                      0,
                                                                                      "Last Communication was a "
                                                                                              + result.getCommunicationType().name() + " to "
                                                                                              + result.getDestinationPhoneNumber() + " that costed "
                                                                                              + result.getCost() + " and had length of "
                                                                                              + result.getLength());
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
                } else if (caught instanceof CommunicationListIsEmptyException) {
                    CommunicationListIsEmptyException ex = (CommunicationListIsEmptyException) caught;
                    Window.alert(ex.getMessage());
                } else {
                    Window.alert("ERROR: Cannot show balance: " + caught.getMessage());
                }
            }
        });
    }
}
