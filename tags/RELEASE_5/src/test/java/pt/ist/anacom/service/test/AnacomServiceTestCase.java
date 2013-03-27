package pt.ist.anacom.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Communication;
import pt.ist.anacom.domain.OldGenPhone;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.domain.SMS;
import pt.ist.anacom.domain.Voice;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;

public class AnacomServiceTestCase extends TestCase {

    static {
        if (FenixFramework.getConfig() == null) {
            FenixFramework.initialize(new Config() {
                {
                    dbAlias = "test-db";
                    domainModelPath = "src/main/dml/anacom.dml";
                    repositoryType = RepositoryType.BERKELEYDB;
                    rootClass = Anacom.class;
                }
            });
        }
    }

    protected AnacomServiceTestCase(String msg) {
        super(msg);
    }

    protected AnacomServiceTestCase() {
        super();
    }

    protected void setUp() {
        cleanAnacom();
    }

    protected void cleanAnacom() {
        boolean committed = false;
        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            Set<Operator> allOperators = anacom.getOperatorSet();
            allOperators.clear();
            Transaction.commit();
            committed = true;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not clean anacom");
            }
        }
    }

    protected void addOperator(String prefix, String operatorName, int bonus) {
        boolean committed = false;
        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            anacom.addOperator(prefix, operatorName, 1, 1, 1, 1, bonus);
            Transaction.commit();
            committed = true;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not add operator: " + operatorName);
            }
        }
    }

    protected void addPhone(String operatorPrefix, String number, int balance, AnacomData.PhoneType phoneGen, AnacomData.State state) {
        boolean committed = false;
        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            anacom.addPhone(operatorPrefix, number, phoneGen);
            Phone phone = anacom.getPhone(number);
            phone.setBalance(balance);
            if (state != null)
                phone.setState(state);
            Transaction.commit();
            committed = true;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not add phone: " + number + "to operator " + operatorPrefix);
            }
        }
    }
    
    /* ALTERAR CONSOANTE A CHAMADA FOR FEITA NO DOMINIO */
    protected void startVoiceCall(String senderNumber, String receiverNumber) {
        boolean committed = false;
        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            anacom.startSourceVoiceCall(senderNumber, receiverNumber);
            anacom.startDestinationVoiceCall(senderNumber, receiverNumber);
            Transaction.commit();
            committed = true;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not make voice call from: " + senderNumber + "to " + receiverNumber);
            }
        }
    }

    protected void sendSMS(String senderNumber, String receiverNumber, String message) {
        boolean committed = false;
        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            Phone senderPhone = anacom.getPhone(senderNumber);
            Phone receiverPhone = anacom.getPhone(receiverNumber);
            SMS sms = new SMS(senderNumber, receiverNumber, message);
            senderPhone.addSentSMS(sms);
            receiverPhone.addReceivedSMS(sms);
            Transaction.commit();
            committed = true;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not send sms [ + " + message + "] from: " + senderNumber + "to " + receiverNumber);
            }
        }
    }

    protected boolean checkIncreasedBalance(String number, int balance, int amountIncreased) {

        boolean committed = false;
        boolean res = false;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();

            if (balance + amountIncreased == anacom.getPhone(number).getBalance())
                res = true;

            Transaction.commit();
            committed = true;
            return res;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check balance");
            }
        }
    }

    
    protected boolean checkExistsCommunication(String phoneNumber) {
    	boolean committed = false;
        boolean res = false;
			
		try{
			Transaction.begin();
			Anacom anacom = FenixFramework.getRoot();
			
			Phone phone = anacom.getPhone(phoneNumber);
			if(phone.getActiveCommunication() != null)
				res = true;		
			
			Transaction.commit();
            committed = true;
            return res;
			
		} finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check active communication: " + phoneNumber);
            }
        }	
	}
    
    protected boolean checkPhoneBusyState(String phoneSourceNumber) {
    	boolean committed = false;
        boolean res = false;
			
		try{
			Transaction.begin();
			Anacom anacom = FenixFramework.getRoot();
			
			Phone phoneSource = anacom.getPhone(phoneSourceNumber);
			
			if(phoneSource.getStateType() == AnacomData.State.BUSY)
				res = true;		
			
			Transaction.commit();
            committed = true;
            return res;
			
		} finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check state: " + phoneSourceNumber);
            }
        }	
	}
    
    protected boolean checkSMS(String phoneNumber, String smsText) {
        boolean committed = false;
        boolean res = false;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            ArrayList<SMS> allSMS;

            if ((allSMS = anacom.getPhone(phoneNumber).getAllSMS()) != null) {
                if (allSMS.get(0).getMessage().equals(smsText)) {
                    res = true;
                }
            }
            Transaction.commit();
            committed = true;
            return res;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check sms");
            }
        }
    }

    protected boolean checkCorrectFinalBalance(String phoneSourceNumber, String phoneDestinationNumber, int duration, int phoneBalance) {
        boolean committed = false;
        boolean res = false;
        int calculatedBalance, finalBalance;

        try {
        	Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
           
            Phone phoneSource = anacom.getPhone(phoneSourceNumber);
            Phone phoneDestination = anacom.getPhone(phoneDestinationNumber);

            Operator phoneSourceOperator = phoneSource.getOperator();
            Operator phoneDestinationOperator = phoneDestination.getOperator();
            
            if(phoneSourceOperator.getPrefix() == phoneDestinationOperator.getPrefix())
            	calculatedBalance = phoneBalance - phoneSourceOperator.getPlan().calcCostVoice(duration, true);
            else
            	calculatedBalance = phoneBalance - phoneSourceOperator.getPlan().calcCostVoice(duration, false);

            finalBalance = phoneSource.getBalance();

            if(finalBalance == calculatedBalance)
            	res = true;
            
            Transaction.commit();
            committed = true;
            return res;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check final balance");
            }
        }
    }
    
    
    protected boolean checkPreviousState(String phoneSourceNumber, AnacomData.State state) {
        boolean committed = false;
        boolean res = false;

        try {
        	Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
           Phone phone = anacom.getPhone(phoneSourceNumber);
           
           if(phone.getStateType() == state)
        	   res = true;
           
            Transaction.commit();
            committed = true;
            return res;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check preivous state");
            }
        }
    }
    
    
    
    protected int getPhoneBalance(String number) {

        boolean committed = false;
        int balance = 0;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            balance = anacom.getPhone(number).getBalance();
            Transaction.commit();
            committed = true;
            return balance;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check balance");
            }
        }
    }

    protected boolean checkOperator(String operatorPrefix) {
        boolean committed = false;
        boolean res = false;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            if (anacom.hasOperatorByPrefix(operatorPrefix) != null)
                res = true;
            Transaction.commit();
            committed = true;
            return res;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check operator");
            }
        }
    }
    
    protected boolean checkPhone(String operator, String phoneNumber) {
    	 boolean committed = false;
         boolean res = false;

         
         try {
             Transaction.begin();
             Anacom anacom = FenixFramework.getRoot();
             if (anacom.getOperator().get(0).getPhoneByNumber(phoneNumber) != null)
                 res = true;
             Transaction.commit();
             committed = true;
             return res;
         } finally {
             if (!committed) {
                 Transaction.abort();
                 fail("Could not check operator");
             }
         }
  	}
    
    protected boolean checkCancelPhone() {
    	 boolean committed = false;
         boolean res = false;

         
         try {
             Transaction.begin();
             Anacom anacom = FenixFramework.getRoot();
             if (anacom.getOperator().get(0).getPhone().isEmpty())
                 res = true;
             Transaction.commit();
             committed = true;
             return res;
         } finally {
             if (!committed) {
                 Transaction.abort();
                 fail("Could not check operator");
             }
         }
	}


    protected boolean checkReceivedSMSList(String phoneNumber, ArrayList<SMSDto> smsReceivedList) {
        boolean committed = false;
        boolean res = true;
        boolean found = false;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();

            ArrayList<SMS> smsList = anacom.getPhone(phoneNumber).getReceivedSMS();

            for (SMSDto smsDto : smsReceivedList) {

                SMS smsAux = new SMS(null, null, smsDto.getMessage());
                smsAux.setSourcePhoneNumber(smsDto.getSourcePhoneNumber());
                smsAux.setDestinationPhoneNumber(smsDto.getDestinationPhoneNumber());
                found = false;
                for (SMS sms : smsList)
                    if (sms.equals(smsAux)) {
                        found = true;
                    }

                if (!found) {
                    res = false;
                    break;
                }
            }

            Transaction.commit();
            committed = true;
            return res;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check sms list");
            }
        }
    }
    
    protected boolean checkBalanceAndPhoneList(String operator_prefix,ArrayList<BalanceAndPhoneDto> phoneList) {
    	boolean committed = false;
    	boolean res = true;
    	boolean found = false;

    	try {
    		Transaction.begin();
    		Anacom anacom = FenixFramework.getRoot();

    		List<Phone> balancePhoneList = anacom.getBalanceAndPhoneList(operator_prefix);

    		for (BalanceAndPhoneDto balancePhoneDto : phoneList) {

    			Phone phoneAux = new OldGenPhone(balancePhoneDto.getPhoneNumber(),balancePhoneDto.getBalance());
    			
    			found = false;
    			for (Phone phone : balancePhoneList){
    				if (phone.equals(phoneAux)) {
    					found = true;
    				}
    			}

    			if (!found) {
    				res = false;
    				break;
    			}
    		}

    		Transaction.commit();
    		committed = true;
    		return res;
    	} finally {
    		if (!committed) {
    			Transaction.abort();
    			fail("Could not check sms list");
    		}
    	}
    }


    protected boolean checkLastCommunication(String sourceNumber, LastCommunicationDto lastCommunication) {
        boolean committed = false;
        boolean res = false;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            Phone source = anacom.getPhone(sourceNumber);
            Communication communication = source.getPhoneLastMadeCommunication();

            if (communication.getDestinationPhoneNumber() == lastCommunication.getDestinationPhoneNumber()
                    && communication.getCost() == lastCommunication.getCost() && communication.getType() == lastCommunication.getCommunicationType()
                    && communication.getLength() == lastCommunication.getLength())
                res = true;

            Transaction.commit();
            committed = true;
            return res;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not check last communication");
            }
        }
    }

    protected AnacomData.State getPhoneState(String number) {
        boolean committed = false;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            AnacomData.State state = anacom.getPhone(number).getStateType();
            Transaction.commit();
            committed = true;
            return state;
        } finally {
            if (!committed) {
                Transaction.abort();
                fail("Could not get phone state");
            }
        }

    }

}
