package pt.ist.anacom.domain;

import java.util.ArrayList;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.InvalidBalanceOperationException;
import pt.ist.anacom.shared.exception.PhoneNumberIncorrectException;
import pt.ist.anacom.shared.exception.UnsuportedOperationException;

public abstract class Phone extends Phone_Base {

    public Phone() {
        super();
        super.setStateOFF(new StateOFF());
        super.setStateON(new StateON());
        super.setStateBUSY(new StateBUSY());
        super.setStateSILENCE(new StateSILENCE());
        super.setState(getStateOFF());
        super.getState().setPhoneState(this);
    }

    public void setState(AnacomData.State state) {

        if (state == AnacomData.State.OFF)
            super.setState(getStateOFF());

        else if (state == AnacomData.State.ON)
            super.setState(getStateON());

        else if (state == AnacomData.State.SILENCE)
            super.setState(getStateSILENCE());

        else if (state == AnacomData.State.BUSY)
            super.setState(getStateBUSY());
    }

    public AnacomData.State getStateName() {

        State currentState = super.getState();

        if (currentState.compareStateName(getStateOFF()))
            return AnacomData.State.OFF;
        else if (currentState.compareStateName(getStateON()))
            return AnacomData.State.ON;
        else if (currentState.compareStateName(getStateBUSY()))
            return AnacomData.State.BUSY;
        else
            return AnacomData.State.SILENCE;

    }

    @Override
    public void setNr(String number) {
        if (!(number.length() == AnacomData.NUMBER_LENGTH))
            throw new PhoneNumberIncorrectException(number);
        super.setNr(number);
    }

    @Override
    public String toString() {
        return "N. Telemovel: " + this.getNr() + " Saldo: " + this.getBalance() + " - " + getAllSMS();
    }

    /**
     * Get the Phone prefix.
     * 
     * @return Returns the Phone prefix.
     */
    public String getPrefix() {
        return this.getNr().substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    public void increaseBalanceBy(int valueToIncrease) {
        int balance = getBalance();
        if (valueToIncrease < 0 || (balance += valueToIncrease) > 100)
            throw new InvalidBalanceOperationException(this.getNr(), this.getBalance());
        setBalance(balance);
    }

    public void decreaseBalanceBy(int valueToDecrease) {
        int balance = getBalance();
        if (balance < valueToDecrease || valueToDecrease < 0)
            throw new InvalidBalanceOperationException(this.getNr(), this.getBalance());
        setBalance(balance - valueToDecrease);
    }

    /*
     * ====================================================================
     * =============================SMS Operations=========================
     * ====================================================================
     */

    /**
     * Store a sent SMS.
     * 
     * @param sms SMS to be added
     */
    public void addSentSMS(SMS sms) {
        this.getState().sentSMS(sms);
    }

    /**
     * Store a received SMS.
     * 
     * @param sms SMS to be added
     */
    public void addReceivedSMS(SMS sms) {
        this.getState().receivedSMS(sms);
    }

    /**
     * Gets all the messages this phone has.
     * 
     * @return an ArrayList of all SMS's.
     */
    public ArrayList<SMS> getAllSMS() {
        ArrayList<SMS> allSMS = new ArrayList<SMS>();

        allSMS.addAll(getSentSMS());
        allSMS.addAll(getReceivedSMS());

        return allSMS;
    }

    /**
     * Gets all the messages this phone has. (Does not callgetAllSMS because it had to
     * search in the lists twice)
     * 
     * @return an ArrayList of all SMS's.
     */
    public ArrayList<SMS> getReceivedSMS() {
        ArrayList<SMS> receivedSMS = new ArrayList<SMS>();
        for (Communication communication : getCommunicationReceived())
            if (communication instanceof SMS) {
                receivedSMS.add((SMS) communication);
            }
        return receivedSMS;
    }

    /**
     * Gets all the messages this phone has. (Does not call getAllSMS because it had to
     * search in the lists twice).
     * 
     * @return an ArrayList of all SMS's.
     */
    public ArrayList<SMS> getSentSMS() {
        ArrayList<SMS> sentSMS = new ArrayList<SMS>();
        for (Communication communication : getCommunicationSent())
            if (communication instanceof SMS) {
                sentSMS.add((SMS) communication);
            }
        return sentSMS;
    }

    /*
     * ====================================================================
     * ===========================Voice Operations=========================
     * ====================================================================
     */

    /**
     * Store a sent voice call.
     * 
     * @param voice Voice call to be added
     */
    public void addSentVoice(Voice voice) {
        this.getState().sentVoice(voice);
    }

    /**
     * Store a received voice call.
     * 
     * @param voice Voice call to be added
     */
    public void addReceivedVoice(Voice voice) {
        this.getState().receivedVoice(voice);
    }

    /**
     * Gets all the voice calls this phone has. (Does not call allVoice because it had to
     * search in the lists twice)
     * 
     * @return an ArrayList of all the voice calls received.
     */
    public ArrayList<Voice> getAllVoiceCalls() {
        ArrayList<Voice> allVoiceCalls = new ArrayList<Voice>();
        allVoiceCalls.addAll(getSentVoiceCalls());
        allVoiceCalls.addAll(getReceivedVoiceCalls());

        return allVoiceCalls;
    }

    /**
     * Gets all the received voice calls this phone has. (Does not call getAllVoiceCalls
     * because it had to search in the lists twice)
     * 
     * @return an ArrayList of all the voice calls received.
     */
    public ArrayList<Voice> getReceivedVoiceCalls() {
        ArrayList<Voice> receivedVoiceCalls = new ArrayList<Voice>();
        for (Communication communication : getCommunicationReceived())
            if (communication instanceof Voice) {
                receivedVoiceCalls.add((Voice) communication);
            }
        return receivedVoiceCalls;
    }

    /**
     * Gets all the voice calls this phone has. (Does not call getAllVoiceCalls because it
     * had to search in the lists twice).
     * 
     * @return an ArrayList of all the voice calls made.
     */
    public ArrayList<Voice> getSentVoiceCalls() {
        ArrayList<Voice> sentVoiceCalls = new ArrayList<Voice>();
        for (Communication communication : getCommunicationSent())
            if (communication instanceof Voice) {
                sentVoiceCalls.add((Voice) communication);
            }
        return sentVoiceCalls;
    }

    /*
     * ====================================================================
     * ===========================Video Operations=========================
     * ====================================================================
     */

    /**
     * Store a sent video call.
     * 
     * @param video Video call to be added
     */
    public abstract void addSentVideo(Video video) throws UnsuportedOperationException;

    /**
     * Store a received video call.
     * 
     * @param video Video call to be added
     */
    public abstract void addReceivedVideo(Video video) throws UnsuportedOperationException;

}
