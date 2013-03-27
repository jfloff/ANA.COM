package pt.ist.anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GetPhoneStatePanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Button getStateButton;

    private final Label errorLabel;

    private final FlexTable stateTable;

    public GetPhoneStatePanel() {
        GWT.log("presentationserver.client.view.GetPhoneState::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.getStateButton = new Button("Get Phone State");
        this.getStateButton.setStyleName("getStateButton");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.stateTable = new FlexTable();

        this.verticalPanel.add(this.getStateButton);
        this.verticalPanel.add(this.errorLabel);
        this.verticalPanel.add(this.stateTable);

        this.add(this.verticalPanel);

    }

    public Button getStateButton() {
        return this.getStateButton;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }

    public FlexTable getStateTable() {
        return this.stateTable;
    }

}
