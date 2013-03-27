package pt.ist.anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GetBalancePanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Button getBalanceButton;

    private final Label errorLabel;

    private final FlexTable balanceTable;

    public GetBalancePanel() {
        GWT.log("presentationserver.client.view.GetBalancePanel::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.getBalanceButton = new Button("Get Phone Balance");
        this.getBalanceButton.setStyleName("getBalanceButton");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.balanceTable = new FlexTable();

        this.verticalPanel.add(this.getBalanceButton);
        this.verticalPanel.add(this.errorLabel);
        this.verticalPanel.add(this.balanceTable);

        this.add(this.verticalPanel);

    }

    public Button getGetBalanceButton() {
        return this.getBalanceButton;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }

    public FlexTable getBalanceTable() {
        return this.balanceTable;
    }
}
