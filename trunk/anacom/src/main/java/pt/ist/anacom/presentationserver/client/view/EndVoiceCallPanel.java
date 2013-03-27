package pt.ist.anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EndVoiceCallPanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Label destinationPhoneNumberLabel;

    private final TextBox destinationPhoneNumberTextBox;

    private final Button endVoiceCallButton;

    private final Label errorLabel;

    public EndVoiceCallPanel() {
        GWT.log("presentationserver.client.view.EndVoiceCallPanel::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.destinationPhoneNumberLabel = new Label("Destination Phone Number:");

        this.destinationPhoneNumberTextBox = new TextBox();

        this.endVoiceCallButton = new Button("End Voice Call");
        this.endVoiceCallButton.setStyleName("getBalanceButton");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.verticalPanel.add(this.destinationPhoneNumberLabel);
        this.verticalPanel.add(this.destinationPhoneNumberTextBox);
        this.verticalPanel.add(this.endVoiceCallButton);
        this.verticalPanel.add(this.errorLabel);
        this.add(this.verticalPanel);

    }

    public TextBox getDestinationPhoneNumberTextBox() {
        return this.destinationPhoneNumberTextBox;
    }

    public Button getEndVoiceCallButton() {
        return this.endVoiceCallButton;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }
}
