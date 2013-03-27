package pt.ist.anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SendSMSPanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Label phoneDestNumberLabel;

    private final TextBox phoneDestNumberTextBox;

    private final Label smsLabel;

    private final TextArea smsTextBox;

    private final Button sendSMSButton;

    private final Label errorLabel;

    public SendSMSPanel() {
        GWT.log("presentationserver.client.view.SendSMSPanel::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.phoneDestNumberLabel = new Label("Destination Phone Number:");
        this.phoneDestNumberTextBox = new TextBox();

        this.smsLabel = new Label("SMS Text:");
        this.smsTextBox = new TextArea();
        smsTextBox.setCharacterWidth(30);
        smsTextBox.setVisibleLines(10);

        this.sendSMSButton = new Button("Send SMS!");
        // getBalanceButton.setStyleName("getBalanceButton");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.verticalPanel.add(this.phoneDestNumberLabel);
        this.verticalPanel.add(this.phoneDestNumberTextBox);
        this.verticalPanel.add(this.smsLabel);
        this.verticalPanel.add(this.smsTextBox);
        this.verticalPanel.add(this.sendSMSButton);
        this.verticalPanel.add(this.errorLabel);
        this.add(this.verticalPanel);

    }

    public TextBox getPhoneDestNumberTextBox() {
        return phoneDestNumberTextBox;
    }

    public TextArea getSmsTextBox() {
        return smsTextBox;
    }

    public Button getSendSMSButton() {
        return sendSMSButton;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }
}
