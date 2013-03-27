package pt.ist.anacom.domain;

public class SMS extends SMS_Base {
    
    public  SMS() {
        super();
    }
    
    @Override
    public String toString(){
    	return "SMS: " + getMessage();
    }
    
}
