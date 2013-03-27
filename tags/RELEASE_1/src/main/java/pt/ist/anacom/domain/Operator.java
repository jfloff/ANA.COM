package pt.ist.anacom.domain;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import pt.ist.anacom.exception.PhoneAlreadyExistsException;

public class Operator extends Operator_Base {
    
    public Operator() {
        super();
    }
    
    public Operator(Integer prefix, String name) {
        super();
        setPrefix(prefix);
        setName(name);
    } 
    
    @Override
    public void addPhone(Phone phone)  {
    	try{
			if (hasPhone(phone.getNr()))
			    throw new PhoneAlreadyExistsException(phone, "Operator", "addPhone");
			super.addPhone(phone);
    	}catch(PhoneAlreadyExistsException e) {System.err.println(e.getMessage());}
    }
    
    @Override
    public String toString(){
 	   return getName() + " (" + getPrefix() + ")" + " has " + getPhoneCount() + " Phones.";
    }

    public boolean equals(Operator operator){
    	return (operator.getPrefix() == this.getPrefix()) || 
			   (operator.getName().equals(this.getName()));
    }
    
    public boolean hasPhone(Integer nr) {
    	return getPhoneByNr(nr) != null;
    }
    
    /*
     *  -------------------
     *  - Phones  Methods -
     *  -------------------
 	*/
    
    /**
	 * Search a phone by his number
	 *
	 * @param nr The phone number to find
	 * 
	 */
     private Phone getPhoneByNr(int nr) {
		for(Phone phone : getPhoneSet())
		    if(phone.getNr() == nr)
		    	return phone;
		return null;
     }    
    
    /**
     * Print all the Phones objects that belongs to the operator.
     *
     */
     public void printPhones() {
    	for(Phone phone: getPhoneSet())
    		System.out.println(phone);
     }
    
    /**
     * Calculate the cost of a SMS communication.
     *
     * @param SMSText The SMS text
     * @return The cost of the SMS
     */
     public Double calcCostSMS(String SMSText) {
    	 
    	BigDecimal taxSMS = getTaxSMS();
      	int length = SMSText.length();
    	double tax = taxSMS.doubleValue();
    	double cost = tax * (((length-1)/100)+1);
    	 
		return cost;
    	 
     }
     
     /**
      * Calculate the Voice cost.
      *
      * @param SMSText The SMS text
      * @return The cost of the SMS
      */
      public Double calcCostVoice(DateTime time) {
     	 
     	
     	 return null;
      }
      
      /**
       * Calculate the SMS cost.
       *
       * @param SMSText The SMS text
       * @return The cost of the SMS
       */
       public Double calcCostVideo(DateTime time) {
      	
    	   
    	   return null;
       }
    
}
