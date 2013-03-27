package pt.ist.anacom.service.test;

import java.util.ArrayList;
import java.util.Set;

import junit.framework.TestCase;
import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.NewGenPhone;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.domain.Phone.State;
import pt.ist.anacom.domain.SMS;
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
			anacom.addOperator(new Operator(prefix, operatorName, 1, 1, 1, 1));
			Transaction.commit();
			committed = true;
		} finally {
			if (!committed) {
				Transaction.abort();
				fail("Could not add operator: " + operatorName);
			}
		}
	}

	protected void addPhone(String operatorName, String number, int balance, State state) {
		boolean committed = false;
		try {
			Transaction.begin();
			Anacom anacom = FenixFramework.getRoot();
			NewGenPhone phone = new NewGenPhone(number, balance);
			if (state != null)
				phone.setState(state);
			anacom.addPhone(operatorName, phone);
			Transaction.commit();
			committed = true;
		} finally {
			if (!committed) {
				Transaction.abort();
				fail("Could not add phone: " + number + "to operator " + operatorName);
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

	protected boolean checkSMS(String number_phone, String smsText) {
		boolean committed = false;
		boolean res = false;

		try {
			Transaction.begin();
			Anacom anacom = FenixFramework.getRoot();
			ArrayList<SMS> allSMS;
			if ((allSMS = anacom.getPhone(number_phone).getAllSMS()) != null) {
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

	protected boolean checkOperator(String operatorName) {
		boolean committed = false;
		boolean res = false;

		try {
			Transaction.begin();
			Anacom anacom = FenixFramework.getRoot();
			if (anacom.getOperatorByString(operatorName) != null)
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
}
