package pt.ist.anacom.domain;

import java.util.ArrayList;

import pt.ist.anacom.shared.exception.InvalidBalanceOperationException;
import pt.ist.anacom.shared.exception.PhoneIsBUSYException;
import pt.ist.anacom.shared.exception.PhoneIsOFFException;
import pt.ist.anacom.shared.exception.PhoneNumberIncorrectException;
import pt.ist.anacom.shared.exception.UnsuportedOperationException;

public abstract class Phone extends Phone_Base {

	public enum State {
		MobileStateON, MobileStateSILENCE, MobileStateBUSY, MobileStateOFF
	}

	public Phone() {
		super();
	}

	@Override
	public void setNr(String number) {
		if (!(number.length() == Anacom.numberLength))
			throw new PhoneNumberIncorrectException(number);
		super.setNr(number);
	}

	@Override
	public String toString() {
		return "N. Telemovel: " + this.getNr() + " Saldo: " + this.getBalance() + " - " + getAllSMS();
	}

	/**
	 * 
	 * Get the Phone prefix.
	 * 
	 * @return Returns the Phone prefix.
	 */
	public String getPrefix() {
		return this.getNr().substring(0, Anacom.prefixLength);
	}

	public void increaseBalanceBy(int valueToIncrease) {
		int balance = getBalance();
		if ( valueToIncrease < 0 || (balance += valueToIncrease) > 100)
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
	 * @param sms
	 *            SMS to be added
	 */
	public void addSentSMS(SMS sms) {

		if (this.getState() == State.MobileStateOFF)
			throw new PhoneIsOFFException(this.getNr());

		if (this.getState().equals(State.MobileStateBUSY))
			throw new PhoneIsBUSYException(this.getNr());

		addCommunicationSent(sms);
	}

	/**
	 * Store a received SMS.
	 * 
	 * @param sms
	 *            SMS to be added
	 */
	public void addReceivedSMS(SMS sms) {

		if (this.getState() == State.MobileStateOFF)
			throw new PhoneIsOFFException(this.getNr());
		addCommunicationReceived(sms);
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
	 * Gets all the messages this phone has. (Does not callgetAllSMS because it
	 * had to search in the lists twice)
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
	 * Gets all the messages this phone has. (Does not call getAllSMS because it
	 * had to search in the lists twice).
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
	 * @param voice
	 *            Voice call to be added
	 */
	public void addSentVoice(Voice voice) {
		addCommunicationSent(voice);
	}

	/**
	 * Store a received voice call.
	 * 
	 * @param voice
	 *            Voice call to be added
	 */
	public void addReceivedVoice(Voice voice) {
		addCommunicationReceived(voice);
	}

	/**
	 * Gets all the voice calls this phone has. (Does not call allVoice because
	 * it had to search in the lists twice)
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
	 * Gets all the received voice calls this phone has. (Does not call
	 * getAllVoiceCalls because it had to search in the lists twice)
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
	 * Gets all the voice calls this phone has. (Does not call getAllVoiceCalls
	 * because it had to search in the lists twice).
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
	 * @param video
	 *            Video call to be added
	 */
	public abstract void addSentVideo(Video video) throws UnsuportedOperationException;

	/**
	 * Store a received video call.
	 * 
	 * @param video
	 *            Video call to be added
	 */
	public abstract void addReceivedVideo(Video video) throws UnsuportedOperationException;

}
