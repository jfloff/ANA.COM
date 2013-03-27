package pt.ist.anacom.domain;

public class Voice extends Voice_Base {
    
    public  Voice() {
        super();
    }
    
    @Override
    public String toString(){
    	return "Voice: " + getDuration();
    }
    
}
