package pt.ist.anacom.presentationserver.client.view;

import pt.ist.anacom.shared.data.AnacomData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SetPhoneStatePanel extends DecoratorPanel {

    private final VerticalPanel verticalPanel;

    private final Button setStateButton;

    private final Label errorLabel;

    private final ListBox listBox;

    public SetPhoneStatePanel() {
        GWT.log("presentationserver.client.view.SetPhoneState::constructor()");

        this.verticalPanel = new VerticalPanel();

        this.setStateButton = new Button("Set Phone State");
        this.setStateButton.setStyleName("setStateButton");

        this.listBox = new ListBox();
        this.listBox.addItem(AnacomData.State.ON.name());
        this.listBox.addItem(AnacomData.State.OFF.name());
        this.listBox.addItem(AnacomData.State.SILENCE.name());
        this.listBox.addItem(AnacomData.State.BUSY.name());
        this.listBox.setVisibleItemCount(1);

        this.errorLabel = new Label();
        this.errorLabel.setText("");
        this.errorLabel.setStyleName("labelError");

        this.verticalPanel.add(this.listBox);
        this.verticalPanel.add(this.setStateButton);
        this.verticalPanel.add(this.errorLabel);
        this.add(this.verticalPanel);

    }

    public ListBox getListBox() {
        return listBox;
    }

    public Button getStateButton() {
        return this.setStateButton;
    }

    public Label getErrorLabel() {
        return this.errorLabel;
    }
}
