package pt.ist.anacom.presentationserver.client.view;

import pt.ist.anacom.shared.dto.SMSDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SMSReceivedListPanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Button receivedSMSButton;

    private final FlexTable table;

    private final Label errorLabel;

    public SMSReceivedListPanel() {

        GWT.log("presentationserver.client.view.SMSReceivedListPanel::constructor()");

        this.receivedSMSButton = new Button("ReceivedSMSButton");
        this.receivedSMSButton.setStyleName("receivedSMSButton");

        this.table = new FlexTable();
        // format table main features:
        this.table.addStyleName("smsTable");
        // add header row:
        this.table.setText(0, 0, "Source Number");
        this.table.setText(0, 1, "SMS Body");
        // add style to row:
        this.table.getRowFormatter().addStyleName(0, "smsTableHeader");

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.verticalPanel = new VerticalPanel();

        this.verticalPanel.add(this.receivedSMSButton);
        this.verticalPanel.add(this.table);
        this.verticalPanel.add(this.errorLabel);
        this.add(this.verticalPanel);

    }

    public Button getReceivedSMSButton() {
        return this.receivedSMSButton;
    }

    public FlexTable getTable() {
        return this.table;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }

    public void clearTable() {
        GWT.log("presentationserver.client.view.SMSReceivedListPanel::clearTable()");
        for (int i = table.getRowCount() - 1; i > 0; i--)
            table.removeRow(i);
    }

    public void add(SMSDto sms) {
        GWT.log("presentationserver.client.view.SMSReceivedListPanel::add(" + sms + ")");
        // get the number of the next row:
        int row = this.table.getRowCount();

        // add name and phone number (and set style from CSS)
        this.table.setText(row, 0, sms.getSourceNumber());
        this.table.getCellFormatter().addStyleName(row, 0, "smsTableNumberCell");
        this.table.setText(row, 1, sms.getMessage());
        this.table.getCellFormatter().addStyleName(row, 1, "smsTableTextCell");

    }
}
