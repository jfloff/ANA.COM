package pt.ist.anacom.domain;
	
import pt.ist.anacom.exception.AnacomException;
import pt.ist.anacom.exception.OperatorAlreadyExistsException;
import pt.ist.anacom.exception.PrefixDoesNotExistException;
	
public class Anacom extends Anacom_Base {
	
	/*
	 *  -------------------
	 *      Data Values
	 *  -------------------
	 */
	
	public static final int prefixLength = 2;
	
	public enum PSP{}
	
	/*
	 *  -------------------
	 *     Anacom Class
	 *  -------------------
	 */
	
	public  Anacom() {
	    super();
	}
	     	    
	/*
	 *  -------------------
	 *    Operator Methods
	 *  -------------------
	 */
	
	 @Override
	 public void addOperator(Operator operator) throws OperatorAlreadyExistsException {
		if(hasOperator(operator))
			throw new OperatorAlreadyExistsException(operator, "Anacom", "addOperator");
		super.addOperator(operator);
	 }
   
	 public boolean hasOperator(Operator operator){
	   for(Operator aux : this.getOperatorSet())
		   if(operator.equals(aux))
			   return true;
	   return false;
	 }
	 
	 /**
	  * Prints all the Operators information.
	  *
	  */
	 public void printOperators() {
		 for(Operator operator : getOperator()) {
			   System.out.println(operator);
			   operator.printPhones();
		 }		 
	 }
	
	/**
	 * Return the operator responsible by a specific prefix
	 *
	 * @param prefix The prefix of the operator
	 * @return The operator responsible
	 */
	 public Operator getOperatorByPrefix(int prefix){
	  for(Operator operator: getOperatorSet())
		  if(operator.getPrefix() == prefix)
			  return operator;
	
	 	return null;
	 }
	
	/**
	 * Return the operator with a specific name
	 *
	 * @param operatorName The name of the operator
	 * @return The operator with that name
	 */
	 public Operator getOperatorByString(String operatorName){
	   for(Operator operator: getOperatorSet())
		   if(operator.getName().equals(operatorName))
			   return operator;
	
	   return null;
	 }
   
	/*
	 *  -------------------
	 *    Phones  Methods
	 *  -------------------
	 */
		
   /**
	 * Add a phone to the correct operator responsible by his prefix
	 *
	 * @param phone The phone object to be added
	 * 
	 */
	public void addPhone(Phone phone) {
		try{
			int prefix = phone.getPrefix();
			Operator operator = getOperatorByPrefix(prefix); 
			if(operator == null)
				throw new PrefixDoesNotExistException(prefix, "Anacom", "addPhone");
			operator.addPhone(phone);
		} catch (AnacomException e) { System.err.println(e.getMessage()); }
	}
}
