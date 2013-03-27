package pt.ist.anacom;

import jvstm.Atomic;
import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.exception.AnacomException;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;

public class Main {
		 
   public static void main(final String[] args) {	
	   FenixFramework.initialize(new Config() {{ 
           domainModelPath = "src/main/dml/anacom.dml";
           dbAlias = "db/dbAnacom";
           repositoryType = RepositoryType.BERKELEYDB;
           rootClass = Anacom.class;
       }});
       
	   System.out.println("Welcome to the Anacom application!");
	   
	   Transaction.begin();
	   Anacom anacom = FenixFramework.getRoot();
	   Transaction.commit();
	   
	   // Test Methods
	   createOperator(anacom);
	   registerPhones(anacom);
	   printAnacom(anacom);
	         
   }
   
   @Atomic
   static void createOperator(Anacom anacom) {
	   
	   Operator car1 = new Operator(98,"ES");
	   
	   try{
		   anacom.addOperator(car1);
	   }catch(AnacomException e) { System.err.println(e.getMessage()); }
	   
   }
   
   
   @Atomic
   static void registerPhones(Anacom anacom) {
	   
	   Phone p1 = new Phone(981234567);
	   Phone p2 = new Phone(981234567);
	   Phone p3 = new Phone(991234567);
   
	   p1.setBalance(2.6);
	   
	   try{
		   anacom.addPhone(p1);
		   anacom.addPhone(p2);
		   anacom.addPhone(p3);
	   }catch(AnacomException e) { System.err.println(e.getMessage()); }
   
   }
   
   
   @Atomic
   static void printAnacom(Anacom anacom) {
	   
	   System.out.println("=============\nComunications\n=============");
	   System.out.println(anacom.getCommunication());
	   System.out.println("========\nOperators\n========");
	   anacom.printOperators();
	   
   }
   
}