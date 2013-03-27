package pt.ist.anacom.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Phone extends Phone_Base {
	
	public enum State{
		ON,
		SILENCE,
		BUSY,
		OFF
	}
	
    public Phone(int nr) {
        super();
        this.setNr(nr);
        this.setBalance(new BigDecimal(0));
    }

    @Override
    public String toString(){
    	return "N. Telemovel: " + this.getNr() + " Saldo: " + this.getBalance().doubleValue();
    }
    
    public void setBalance(double balance){
    	this.setBalance(new BigDecimal(balance));
    }
    
    /**
     * 
     * Get the Phone prefix.
     * 
     * @return Returns the Phone prefix.
     */
    public int getPrefix() {
    	return Integer.parseInt(this.getNr().toString().substring(0, Anacom.prefixLength));  	
    }
    
    /* ====================================================================
       =============================SMS Operations=========================
       ==================================================================== */
 
    /** Store a sent SMS.
	  * 
	  * @param sms
	  * 		SMS to be added
	  */
    public void SMSSent(SMS sms) { addCommunication(sms); }
	
	/** Store a received SMS.
	  * 
	  * @param sms
	  * 		SMS to be added
	  */
	public void SMSReceived(SMS sms) { addCommunication(sms); }
	
	 /** Gets all the messages this phone has.
	  * 
	  * @return an ArrayList of all SMS's.
	  */
	public ArrayList<SMS> getAllSMS(){
		ArrayList<SMS> allSMS = new ArrayList<SMS>();
		for(Communication communication : getCommunication())
			if(communication instanceof SMS)
				allSMS.add((SMS)communication);
		return allSMS;
	}
	
	 /** Gets all the messages this phone has. 
	  *  (Does not callgetAllSMS because it had to search in the lists twice)
	  * 
	  * @return an ArrayList of all SMS's.
	  */
	public ArrayList<SMS> getReceivedSMS(){
		ArrayList<SMS> receivedSMS = new ArrayList<SMS>();
		SMS aux = null;
		for(Communication communication : receivedSMS)
			if(communication instanceof SMS){
				aux = (SMS) communication;
				if(aux.getNrDest() != getNr())
					receivedSMS.add(aux);
			}
		return receivedSMS;
	}
		
	 /** Gets all the messages this phone has.
	  *  (Does not call getAllSMS because it had to search in the lists twice).
	  * 
	  * @return an ArrayList of all SMS's.
	  */
	public ArrayList<SMS> getSentSMS(){
		ArrayList<SMS> receivedSMS = new ArrayList<SMS>();
		SMS aux = null;
		for(Communication communication : receivedSMS)
			if(communication instanceof SMS){
				aux = (SMS) communication;
				if(aux.getNrSource() != getNr())
					receivedSMS.add(aux);
			}
		return receivedSMS;
	}
		
		
	 /* ====================================================================
	    ===========================Voice Operations=========================
	    ==================================================================== */
	
	/** Store a sent voice call.
	  * 
	  * @param voice
	  * 		Voice call to be added
	  */
	public void voiceSent(Voice voice) { addCommunication(voice); }
	
	/** Store a received voiceCall.
	  * 
	  * @param voice
	  * 		Voice call to be added
	  */
		public void voiceReceived(Voice voice) { addCommunication(voice); }
	
	 /** Gets all the voice calls this phone has. 
	  *  (Does not call allVoice because it had to search in the lists twice)
	  * 
	  * @return an ArrayList of all the voice calls received.
	  */
	public ArrayList<Voice> getAllVoiceCalls(){
		ArrayList<Voice> allVoiceCalls = new ArrayList<Voice>();
		for(Communication communication : getCommunication())
			if(communication instanceof Voice)
				allVoiceCalls.add((Voice)communication);
		return allVoiceCalls;
	}
		
	 /** Gets all the received voice calls this phone has. 
	  *  (Does not call getAllVoiceCalls because it had to search in the lists twice)
	  * 
	  * @return an ArrayList of all the voice calls received.
	  */
	public ArrayList<Voice> getReceivedVoiceCalls(){
		ArrayList<Voice> receivedVoiceCalls = new ArrayList<Voice>();
		Voice aux = null;
		for(Communication communication : receivedVoiceCalls)
			if(communication instanceof Voice){
				aux = (Voice) communication;
				if(aux.getNrDest() != getNr())
					receivedVoiceCalls.add(aux);
			}
		return receivedVoiceCalls;
	}
		
	 /** Gets all the voice calls this phone has.
	  *  (Does not call getAllVoiceCalls because it had to search in the lists twice).
	  * 
	  * @return an ArrayList of all the voice calls made.
	  */
	public ArrayList<Voice> getSentVoiceCalls(){
		ArrayList<Voice> receivedVoiceCalls = new ArrayList<Voice>();
		Voice aux = null;
		for(Communication communication : receivedVoiceCalls)
			if(communication instanceof Voice){
				aux = (Voice) communication;
				if(aux.getNrSource() != getNr())
					receivedVoiceCalls.add(aux);
			}
		return receivedVoiceCalls;
	}
		
	 /* ====================================================================
	===========================Video Operations=========================
	==================================================================== */
	
	/** Store a sent video call.
	  * 
	  * @param video
	  * 		Video call to be added
	  */
	public void videoSent(Video video) { addCommunication(video); }
	
	/** Store a received video call.
	  * 
	  * @param video
	  * 		Video call to be added
	  */
	public void videoReceived(Video video) { addCommunication(video); }
	 
}
