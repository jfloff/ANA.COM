package pt.ist.anacom.domain;

import pt.ist.anacom.shared.exception.SMSInvalidTextException;

public class SMS extends SMS_Base {

    public SMS(String text) {
        super();
        if (text == null)
            throw new SMSInvalidTextException();
        this.setMessage(text);
    }

    @Override
    public String toString() {
        return "SMS: " + getMessage();
    }


    public boolean equals(SMS sms) {
        return super.getMessage().equals(sms.getMessage()) && super.getNrDest().equals(sms.getNrDest())
                && super.getNrSource().equals(sms.getNrSource());
    }
}
