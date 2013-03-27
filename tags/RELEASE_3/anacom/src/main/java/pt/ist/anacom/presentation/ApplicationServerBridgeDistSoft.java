package pt.ist.anacom.presentation;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.OperatorDto;
import pt.ist.anacom.shared.dto.PhoneDto;
import pt.ist.anacom.shared.dto.PhoneReceivedSMSListDto;
import pt.ist.anacom.shared.dto.PhonesAndBalanceListDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidBalanceOperationException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorDoesNotExistException;
import pt.ist.anacom.shared.exception.OperatorNullNameException;
import pt.ist.anacom.shared.exception.OperatorWithWrongPrefixException;
import pt.ist.anacom.shared.exception.PhoneAlreadyExistsException;
import pt.ist.anacom.shared.exception.PhoneIsBUSYException;
import pt.ist.anacom.shared.exception.PhoneIsOFFException;
import pt.ist.anacom.shared.exception.PhoneNumberIncorrectException;
import pt.ist.anacom.shared.exception.PhonePrefixDoesNotMatchException;
import pt.ist.anacom.shared.exception.PrefixDoesNotExistException;
import pt.ist.anacom.shared.stubs.AnacomPortType;
import pt.ist.anacom.shared.stubs.AnacomService;
import pt.ist.anacom.shared.stubs.BalanceDtoType;
import pt.ist.anacom.shared.stubs.InsuficientBalanceRemoteException;
import pt.ist.anacom.shared.stubs.InvalidBalanceOperationRemoteException;
import pt.ist.anacom.shared.stubs.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.OperatorAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.OperatorDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.OperatorDtoType;
import pt.ist.anacom.shared.stubs.OperatorExceptionType;
import pt.ist.anacom.shared.stubs.OperatorNullNameRemoteException;
import pt.ist.anacom.shared.stubs.OperatorPrefixElementType;
import pt.ist.anacom.shared.stubs.OperatorWithWrongPrefixRemoteException;
import pt.ist.anacom.shared.stubs.PhoneAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.PhoneAndBalanceListDtoType;
import pt.ist.anacom.shared.stubs.PhoneDtoType;
import pt.ist.anacom.shared.stubs.PhoneIsBUSYRemoteException;
import pt.ist.anacom.shared.stubs.PhoneIsOFFRemoteException;
import pt.ist.anacom.shared.stubs.PhoneNumberElementType;
import pt.ist.anacom.shared.stubs.PhoneNumberIncorrectRemoteException;
import pt.ist.anacom.shared.stubs.PhonePrefixDoesNotMatchRemoteException;
import pt.ist.anacom.shared.stubs.PhonePrefixDoesNotMatchRemoteExceptionType;
import pt.ist.anacom.shared.stubs.PrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.PrefixElementType;
import pt.ist.anacom.shared.stubs.SMSDtoType;

public class ApplicationServerBridgeDistSoft implements ApplicationServerBridge {

	private static final int prefixLength = 2;

	private AnacomPortType getPort(String operatorPrefix) {

		AnacomService anacom = new AnacomService();
		AnacomPortType port = anacom.getAnacomPort();

		String endpointURL = "http://localhost:8080/" + operatorPrefix + "/"
				+ operatorPrefix;
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				endpointURL);

		return port;
	}
	
	public String getOperatorPrefix(String phoneNumber){
		
		return phoneNumber.substring(0,prefixLength);
	}

	@Override
	// APENAS NECESSARIO PARA A 3A ENTREGA DE ES!!!!
	public void getPhoneReceivedSMSList(PhoneReceivedSMSListDto dto) {
		// TODO Auto-generated method stub
	}

	@Override
	public void registerOperator(OperatorDto operatorDto) {

		AnacomPortType port = getPort(operatorDto.getOperatorPrefix());

		OperatorDtoType op = new OperatorDtoType();

		op.setName(operatorDto.getName());
		op.setOperatorPrefix(operatorDto.getOperatorPrefix());
		op.setTax(operatorDto.getTax());
		op.setTaxVoice(operatorDto.getTaxVoice());
		op.setTaxSMS(operatorDto.getTaxSMS());
		op.setTaxVideo(operatorDto.getTaxVideo());

		try {
			port.registerOperator(op);
			
		} catch (OperatorNullNameRemoteException e) {
			//Tested
			PrefixElementType exc = e.getFaultInfo();
			throw new OperatorNullNameException(exc.getOperatorPrefix());
		} catch (OperatorWithWrongPrefixRemoteException e) {
			//change?
			OperatorExceptionType exc = e.getFaultInfo();
			throw new OperatorWithWrongPrefixException(exc.getOperatorName(),
					exc.getOperatorName());
		} catch (OperatorAlreadyExistsRemoteException e) {
			//Tested
			OperatorExceptionType exc = e.getFaultInfo();
			throw new OperatorAlreadyExistsException(exc.getOperatorName(),
					exc.getOperatorName());
		}

	}

	@Override
	public void registerPhone(PhoneDto dto) {
		//Tested
		AnacomPortType port = getPort(dto.getOperatorPrefix());

		PhoneDtoType phone = new PhoneDtoType();

		phone.setNumber(dto.getPhoneNumber());
		phone.setOperatorPrefix(dto.getOperatorPrefix());

		try {
			port.registerPhone(phone);
		} catch (WebServiceException e) {
			//change?
			throw new CouldNotConnectException(
					getOperatorPrefix(dto.getPhoneNumber()));
		} catch (PhonePrefixDoesNotMatchRemoteException e) {
			//change?
			PhonePrefixDoesNotMatchRemoteExceptionType exc = e.getFaultInfo();
			throw new PhonePrefixDoesNotMatchException(exc.getOperatorPrefix(),
					exc.getOperatorPrefix(), exc.getPhoneNumber(),
					exc.getPhonePrefix());

		} catch (PhoneNumberIncorrectRemoteException e) {
			//Tested
			PhoneNumberElementType exc = e.getFaultInfo();
			throw new PhoneNumberIncorrectException(exc.getPhoneNumber());
		} catch (PhoneAlreadyExistsRemoteException e) {
			//Tested
			PhoneNumberElementType exc = e.getFaultInfo();
			throw new PhoneAlreadyExistsException(exc.getPhoneNumber());
		}

		catch (OperatorDoesNotExistRemoteException e) {
			//Tested
			OperatorPrefixElementType exc = e.getFaultInfo();
			throw new OperatorDoesNotExistException(exc.getOperatorPrefix());
		}
	}

	@Override
	public void sendSMS(SMSDto SMSDto) {

		AnacomPortType sendPort = getPort(getOperatorPrefix(SMSDto
				.getSourceNumber()));
		AnacomPortType receivePort = getPort(getOperatorPrefix(SMSDto
				.getDestinationNumber()));

		SMSDtoType SMSDtoType = new SMSDtoType();

		SMSDtoType.setNrDest(SMSDto.getDestinationNumber());
		SMSDtoType.setNrSource(SMSDto.getSourceNumber());
		SMSDtoType.setText(SMSDto.getMessage());

		boolean committed = false;

		try {
//			Transaction.begin();
			sendPort.sendSMS(SMSDtoType);
			receivePort.receiveSMS(SMSDtoType);
//			Transaction.commit();
//			committed = true;
		} 
		
		catch (NoSuchPhoneRemoteException e) {
			PhoneNumberElementType exc = e.getFaultInfo();
			throw new NoSuchPhoneException(exc.getPhoneNumber());
		}

		catch (InvalidBalanceOperationRemoteException e) {
			BalanceDtoType exc = e.getFaultInfo();
			throw new InvalidBalanceOperationException(exc.getNumber(),
					exc.getBalance());
		}

		catch (PhoneIsOFFRemoteException e) {
			PhoneNumberElementType exc = e.getFaultInfo();
			throw new PhoneIsOFFException(exc.getPhoneNumber());
		}

		catch (PhoneIsBUSYRemoteException e) {
			PhoneNumberElementType exc = e.getFaultInfo();
			throw new PhoneIsBUSYException(exc.getPhoneNumber());
		}

		catch (PrefixDoesNotExistRemoteException e) {
			PrefixElementType exc = e.getFaultInfo();
			throw new PrefixDoesNotExistException(exc.getOperatorPrefix());

		} 
		
		catch (InsuficientBalanceRemoteException e) {
			BalanceDtoType exc = e.getFaultInfo();
			throw new InsuficientBalanceException(exc.getNumber(),
					exc.getBalance());

		} 
//		finally {
//			if (!committed)
//				Transaction.abort();
//		}
	}

	@Override
	public void increaseBalance(BalanceDto balDto) {
		//Tested
		AnacomPortType port = getPort(getOperatorPrefix(balDto.getNumber()));

		BalanceDtoType balance = new BalanceDtoType();

		balance.setNumber(balDto.getNumber());
		balance.setBalance(balDto.getBalance());

		try {
			port.increasePhoneBalance(balance);
		} catch (NoSuchPhoneRemoteException e) {
			//Tested
			PhoneNumberElementType exc = e.getFaultInfo();
			throw new NoSuchPhoneException(exc.getPhoneNumber());

		} catch (PrefixDoesNotExistRemoteException e) {
			//Tested
			PrefixElementType exc = e.getFaultInfo();
			throw new PrefixDoesNotExistException(exc.getOperatorPrefix());
		} catch (InvalidBalanceOperationRemoteException e) {
			//Tested
			BalanceDtoType exc = e.getFaultInfo();
			throw new InvalidBalanceOperationException(exc.getNumber(),
					exc.getBalance());
		}
	}

	@Override
	public void getPhoneBalance(BalanceDto balanceDto) {

		AnacomPortType port = getPort(getOperatorPrefix(balanceDto.getNumber()));

		BalanceDtoType newBalance = new BalanceDtoType();

		newBalance.setNumber(balanceDto.getNumber());

		try {
			balanceDto
					.setBalance(port.getPhoneBalance(newBalance).getBalance());
		} catch (NoSuchPhoneRemoteException e) {
			//Tested
			PhoneNumberElementType exc = e.getFaultInfo();
			throw new NoSuchPhoneException(exc.getPhoneNumber());

		} catch (PrefixDoesNotExistRemoteException e) {
			//change?
			PrefixElementType exc = e.getFaultInfo();
			throw new PrefixDoesNotExistException(exc.getOperatorPrefix());
		}
	}

	@Override
	public void getPhonesBalanceList(PhonesAndBalanceListDto dto) {

		AnacomPortType port = getPort(dto.getOperatorPrefix());
									
		PhoneAndBalanceListDtoType phoneBalanceDtoType = new PhoneAndBalanceListDtoType();
		phoneBalanceDtoType.setOperatorPrefix(dto.getOperatorPrefix());

		try {
			phoneBalanceDtoType = port
					.getPhonesBalanceList(phoneBalanceDtoType);
			for (BalanceDtoType balanceDtoType : phoneBalanceDtoType
					.getPhoneList()) {
				dto.add(balanceDtoType.getNumber(), balanceDtoType.getBalance());
			}
		} catch (OperatorDoesNotExistRemoteException e) {
			OperatorPrefixElementType exc = e.getFaultInfo();
			throw new OperatorDoesNotExistException(exc.getOperatorPrefix());
		}
	}

	@Override
	public void cancelRegisteredPhone(PhoneDto dto) {
		AnacomPortType port = getPort(dto.getOperatorPrefix());

		PhoneDtoType phoneDto = new PhoneDtoType();

		phoneDto.setNumber(dto.getPhoneNumber());
		phoneDto.setOperatorPrefix(dto.getOperatorPrefix());

		try {
			port.cancelRegisterPhone(phoneDto);
		} catch (NoSuchPhoneRemoteException e) {
			//Tested
			PhoneNumberElementType exc = e.getFaultInfo();
			throw new NoSuchPhoneException(exc.getPhoneNumber());
		}

		catch (PrefixDoesNotExistRemoteException e) {
			//changed?
			PrefixElementType exc = e.getFaultInfo();
			throw new PrefixDoesNotExistException(exc.getOperatorPrefix());
		}
	}
}
