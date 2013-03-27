package pt.ist.anacom.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class SMSPhoneReceivedListDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private ArrayList<SMSDto> smsList;

    public SMSPhoneReceivedListDto() {
        this.smsList = new ArrayList<SMSDto>();
    }

    public void add(String sourceNumber, String destinationNumber, String text) {
        this.smsList.add(new SMSDto(sourceNumber, destinationNumber, text));
    }

    public ArrayList<SMSDto> getSmsList() {
        return this.smsList;
    }

    @Override
    public String toString() {
        return this.smsList.toString();
    }

}
