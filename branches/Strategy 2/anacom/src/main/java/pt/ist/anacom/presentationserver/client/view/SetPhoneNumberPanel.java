package pt.ist.anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SetPhoneNumberPanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Label phoneNumberLabel;

    private final TextBox phoneNumberTextBox;

    private final Button setPhoneNumberButton;

    private final Label finalPhoneNumberLabel;

    private final Label errorLabel;

    public SetPhoneNumberPanel() {
        GWT.log("presentationserver.client.view.SetPhoneNumberPanel::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.phoneNumberLabel = new Label("Phone Number:");

        this.phoneNumberTextBox = new TextBox();

        this.setPhoneNumberButton = new Button("Set Phone Number");
        this.setPhoneNumberButton.setStyleName("setPhoneNumberButton");

        this.finalPhoneNumberLabel = new Label();
        this.finalPhoneNumberLabel.setText("");
        this.finalPhoneNumberLabel.setStyleName("h1");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.verticalPanel.add(this.phoneNumberLabel);
        this.verticalPanel.add(this.phoneNumberTextBox);
        this.verticalPanel.add(this.setPhoneNumberButton);
        this.verticalPanel.add(this.errorLabel);
        this.verticalPanel.add(this.finalPhoneNumberLabel);

        this.add(this.verticalPanel);
    }

    public TextBox getPhoneNumberTextBox() {
        return this.phoneNumberTextBox;
    }

    public Button getSetPhoneNumberButton() {
        return this.setPhoneNumberButton;
    }

    public Label getFinalPhoneNumberLabel() {
        return this.finalPhoneNumberLabel;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }
}
