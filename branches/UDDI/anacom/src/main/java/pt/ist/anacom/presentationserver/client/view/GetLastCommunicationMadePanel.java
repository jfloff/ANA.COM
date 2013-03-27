package pt.ist.anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GetLastCommunicationMadePanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Button getLastCommunicationMadeButton;

    private final Label errorLabel;

    private final FlexTable lastCommunicationMadeTable;

    public GetLastCommunicationMadePanel() {
        GWT.log("presentationserver.client.view.GetLastCommunicationMadePanel::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.getLastCommunicationMadeButton = new Button("Get Last Communication Made");
        this.getLastCommunicationMadeButton.setStyleName("getBalanceButton");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.lastCommunicationMadeTable = new FlexTable();

        this.verticalPanel.add(this.getLastCommunicationMadeButton);
        this.verticalPanel.add(this.errorLabel);
        this.verticalPanel.add(this.lastCommunicationMadeTable);

        this.add(this.verticalPanel);

    }

    public Button getLastCommunicationMadeButton() {
        return this.getLastCommunicationMadeButton;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }

    public FlexTable getLastCommunicationMadeTable() {
        return this.lastCommunicationMadeTable;
    }
}
