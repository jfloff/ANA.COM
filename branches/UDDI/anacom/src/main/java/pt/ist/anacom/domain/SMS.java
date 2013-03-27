package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;

public class SMS extends SMS_Base {

    public SMS(String sourcePhoneNumber, String destinationPhoneNumber, String text) {
        super();
        super.setSourcePhoneNumber(sourcePhoneNumber);
        super.setDestinationPhoneNumber(destinationPhoneNumber);
        super.setCost(0);
        if (text == null)
            throw new SMSInvalidTextException();
        this.setMessage(text);
    }

    public SMS(String sourcePhoneNumber, String destinationPhoneNumber, String text, int cost) {
        super();
        super.setSourcePhoneNumber(sourcePhoneNumber);
        super.setDestinationPhoneNumber(destinationPhoneNumber);
        super.setCost(cost);
        if (text == null)
            throw new SMSInvalidTextException();
        this.setMessage(text);
    }

    @Override
    public String toString() {
        return "SMS: " + getMessage();
    }

    @Override
    public AnacomData.CommunicationType getType() {

        return AnacomData.CommunicationType.SMS;
    }

    @Override
    public int getLength() {
        return this.getMessage().length();
    }

    public boolean equals(SMS sms) {
        return super.getMessage().equals(sms.getMessage()) && super.getDestinationPhoneNumber().equals(sms.getDestinationPhoneNumber())
                && super.getSourcePhoneNumber().equals(sms.getSourcePhoneNumber());
    }
}
