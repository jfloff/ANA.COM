package pt.ist.anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StartVoiceCallPanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Label destinationPhoneNumberLabel;

    private final TextBox destinationPhoneNumberTextBox;

    private final Button startVoiceCallButton;

    private final Label errorLabel;

    public StartVoiceCallPanel() {
        GWT.log("presentationserver.client.view.StartVoiceCallPanel::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.destinationPhoneNumberLabel = new Label("Destination Phone Number:");

        this.destinationPhoneNumberTextBox = new TextBox();

        this.startVoiceCallButton = new Button("Start Voice Call");
        this.startVoiceCallButton.setStyleName("getBalanceButton");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.verticalPanel.add(this.destinationPhoneNumberLabel);
        this.verticalPanel.add(this.destinationPhoneNumberTextBox);
        this.verticalPanel.add(this.startVoiceCallButton);
        this.verticalPanel.add(this.errorLabel);
        this.add(this.verticalPanel);

    }

    public TextBox getDestinationPhoneNumberTextBox() {
        return this.destinationPhoneNumberTextBox;
    }

    public Button getStartVoiceCallButton() {
        return this.startVoiceCallButton;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }
}
