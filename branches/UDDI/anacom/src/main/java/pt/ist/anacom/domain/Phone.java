package pt.ist.anacom.domain;

import java.util.ArrayList;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.NegativeBalanceException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
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

        switch (state) {
        case OFF:
            super.setState(getStateOFF());
            break;
        case SILENCE:
            super.setState(getStateSILENCE());
            break;
        case ON:
            super.setState(getStateON());
            break;
        case BUSY:
            super.setState(getStateBUSY());
            break;
        }
    }

    public AnacomData.State getStateType() {

        return this.getState().getStateType();
    }

    @Override
    public void setPhoneNumber(String number) {
        if (!(number.length() == AnacomData.NUMBER_LENGTH))
            throw new PhoneNumberIncorrectException(number);
        super.setPhoneNumber(number);
    }

    @Override
    public String toString() {
        return "N. Telemovel: " + this.getPhoneNumber() + " Saldo: " + this.getBalance() + " - " + getAllSMS();
    }

    /**
     * Get the Phone prefix.
     * 
     * @return Returns the Phone prefix.
     */
    public String getPrefix() {
        return this.getPhoneNumber().substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    public void increaseBalanceBy(int valueToIncrease) {
        int balance = getBalance();

        if ((balance + valueToIncrease) > AnacomData.MAX_BALANCE)
            throw new BalanceLimitExceededException(this.getPhoneNumber(), this.getBalance());

        if (valueToIncrease < 0)
            throw new NegativeBalanceException(this.getPhoneNumber());

        // Bonus formula
        Operator op = this.getOperator();
        int taxBonus = op.getPlan().getTaxBonus(); //
        balance += (valueToIncrease * (101 + taxBonus)) / 100; // saldo += valueToincrease
                                                               // * (1+bonus%)

        // if balance exceed balance limit set balance with maximum amount
        if (balance > AnacomData.MAX_BALANCE)
            setBalance(AnacomData.MAX_BALANCE);
        else
            setBalance(balance);
    }

    public void decreaseBalanceBy(int valueToDecrease) {
        int balance = getBalance();

        if (valueToDecrease < 0)
            throw new NegativeBalanceException(this.getPhoneNumber());

        if (valueToDecrease > balance)
            throw new InsuficientBalanceException(this.getPhoneNumber(), balance);

        setBalance(balance - valueToDecrease);
    }

    public Communication getLastCommunicationMade() {
        /* Have not made any communications */
        if (getCommunicationSent().isEmpty())
            throw new NoCommunicationsMadeYetException(this.getPhoneNumber());

        return getCommunicationSent().get(getCommunicationSentCount() - 1);
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

    public void startMadeVoiceCall() {
        if (this.getState().canStartMadeVoiceCall())
            setState(AnacomData.State.BUSY);
        else
            getState().throwStateException(getPhoneNumber());
    }

    public void startReceivedVoiceCall() {
        if (this.getState().canStartReceivedVoiceCall())
            setState(AnacomData.State.BUSY);
        else
            getState().throwStateException(getPhoneNumber());
    }


    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!! To make it go to prev State (going to ON everytime)
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public void endMadeVoiceCall() {
        setState(AnacomData.State.ON);

    }

    public void endReceivedVoiceCall() {
        setState(AnacomData.State.ON);
    }

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
