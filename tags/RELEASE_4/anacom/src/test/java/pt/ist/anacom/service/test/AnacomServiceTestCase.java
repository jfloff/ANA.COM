package pt.ist.anacom.service.test;

import java.util.ArrayList;
import java.util.Set;

import junit.framework.TestCase;
import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.domain.SMS;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.Config.RepositoryType;
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

    protected void addOperator(String prefix, String operatorName) {
        boolean committed = false;
        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            anacom.addOperator(prefix, operatorName, 1, 1, 1, 1);
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

    protected void sendSMS(String senderNumber, String receiverNumber, String message) {
        boolean committed = false;
        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            Phone senderPhone = anacom.getPhone(senderNumber);
            Phone receiverPhone = anacom.getPhone(receiverNumber);
            SMS sms = new SMS(message);
            sms.setNrSource(senderNumber);
            sms.setNrDest(receiverNumber);
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

    protected boolean checkSMS(String phoneNumber, String smsText) {
        boolean committed = false;
        boolean res = false;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();
            ArrayList<SMS> allSMS;
            if ((allSMS = anacom.getPhone(phoneNumber).getAllSMS()) != null) {
                if (allSMS.get(0).getMessage().equals(smsText))
                    res = true;
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


    protected boolean checkReceivedSMSList(String phoneNumber, ArrayList<SMSDto> smsReceivedList) {
        boolean committed = false;
        boolean res = true;
        boolean found = false;

        try {
            Transaction.begin();
            Anacom anacom = FenixFramework.getRoot();

            ArrayList<SMS> smsList = anacom.getPhone(phoneNumber).getReceivedSMS();
            for (SMSDto smsDto : smsReceivedList) {

                SMS smsAux = new SMS(smsDto.getMessage());
                smsAux.setNrSource(smsDto.getSourceNumber());
                smsAux.setNrDest(smsDto.getDestinationNumber());
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
}
