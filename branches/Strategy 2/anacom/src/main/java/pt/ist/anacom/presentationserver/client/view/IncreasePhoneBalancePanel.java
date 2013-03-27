package pt.ist.anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IncreasePhoneBalancePanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Label valueLabel;

    private final TextBox valueTextBox;

    private final Button increasePhoneBalanceButton;

    private final Label errorLabel;

    public IncreasePhoneBalancePanel() {
        GWT.log("presentationserver.client.view.IncreasePhoneBalancePanel::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.valueLabel = new Label("Value to Increase:");

        this.valueTextBox = new TextBox();

        this.increasePhoneBalanceButton = new Button("Increase Phone Balance");
        this.increasePhoneBalanceButton.setStyleName("getBalanceButton");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.verticalPanel.add(this.valueLabel);
        this.verticalPanel.add(this.valueTextBox);
        this.verticalPanel.add(this.increasePhoneBalanceButton);
        this.verticalPanel.add(this.errorLabel);
        this.add(this.verticalPanel);

    }

    public TextBox getValueTextBox() {
        return this.valueTextBox;
    }

    public Button getIncreasePhoneBalanceButton() {
        return this.increasePhoneBalanceButton;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }
}
