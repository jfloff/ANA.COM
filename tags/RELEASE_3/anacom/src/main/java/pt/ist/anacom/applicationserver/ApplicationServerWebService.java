package pt.ist.anacom.applicationserver;

//import javax.annotation.PostConstruct;
import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.service.ProcessCancelRegisteredPhoneService;
import pt.ist.anacom.service.ProcessGetBalanceService;
import pt.ist.anacom.service.ProcessGetPhonesBalanceListService;
import pt.ist.anacom.service.ProcessIncBalanceService;
import pt.ist.anacom.service.ProcessReceiveSMSService;
import pt.ist.anacom.service.ProcessRegisterNewOperatorService;
import pt.ist.anacom.service.ProcessRegisterNewPhoneService;
import pt.ist.anacom.service.ProcessSendSMSService;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.OperatorDto;
import pt.ist.anacom.shared.dto.PhoneDto;
import pt.ist.anacom.shared.dto.PhonesAndBalanceListDto;
import pt.ist.anacom.shared.dto.SMSDto;
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
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;

//dtoType to dtoz

@javax.jws.WebService(endpointInterface = "pt.ist.anacom.shared.stubs.AnacomPortType", wsdlLocation = "/anacom.wsdl", name = "AnacomPortType", portName = "AnacomPort", targetNamespace = "http://pt.ist.anacom.essd.0403", serviceName = "AnacomService")
public class ApplicationServerWebService implements AnacomPortType {

	public static void init(final String operatorName) {
		System.out.println(".............STARTING Anacom " + operatorName + " SERVER...........");

		// initializes the Fenix Framework
		try {

			FenixFramework.initialize(new Config() {
				{
					dbAlias = "/tmp/db" + operatorName;
					domainModelPath = "/tmp/anacom.dml";
					repositoryType = RepositoryType.BERKELEYDB;
					rootClass = Anacom.class;
				}
			});

		} catch (Exception e) {
			System.out.println("Failed to initialize the operator server.\n");
		}
	}
	
	@Override
	public void registerOperator(OperatorDtoType registerOperatorInput) throws OperatorNullNameRemoteException, OperatorWithWrongPrefixRemoteException, OperatorAlreadyExistsRemoteException {
		try {
			OperatorDto dto = new OperatorDto(registerOperatorInput.getOperatorPrefix(), registerOperatorInput.getOperatorPrefix(), registerOperatorInput.getTax(),
					registerOperatorInput.getTaxVoice(), registerOperatorInput.getTaxSMS(), registerOperatorInput.getTaxVideo());

			ProcessRegisterNewOperatorService op = new ProcessRegisterNewOperatorService(dto);
			op.execute();

		} catch (OperatorNullNameException e) {
			PrefixElementType remoteExc = new PrefixElementType();
			remoteExc.setOperatorPrefix(e.getPrefix());
			throw new OperatorNullNameRemoteException("Operator Exception", remoteExc);
		}

		catch (OperatorWithWrongPrefixException e) {
			OperatorExceptionType remoteExc = new OperatorExceptionType();
			remoteExc.setOperatorName(e.getName());
			remoteExc.setOperatorPrefix(e.getOperatorPrefix());
			throw new OperatorWithWrongPrefixRemoteException("Operator Exception", remoteExc);
		}

		catch (OperatorAlreadyExistsException e) {
			OperatorExceptionType remoteExc = new OperatorExceptionType();
			remoteExc.setOperatorName(e.getOperatorName());
			remoteExc.setOperatorPrefix(e.getPrefix());
			throw new OperatorAlreadyExistsRemoteException("Operator Exception", remoteExc);
		}
	}

	@Override
	public void registerPhone(PhoneDtoType registerPhoneInput) throws PhonePrefixDoesNotMatchRemoteException, PhoneNumberIncorrectRemoteException,
			PhoneAlreadyExistsRemoteException, OperatorDoesNotExistRemoteException {

		try {
			PhoneDto dto = new PhoneDto(registerPhoneInput.getOperatorPrefix(), registerPhoneInput.getNumber());
			ProcessRegisterNewPhoneService phone = new ProcessRegisterNewPhoneService(dto);
			phone.execute();
		} catch (PhonePrefixDoesNotMatchException e) {
			PhonePrefixDoesNotMatchRemoteExceptionType remoteExc = new PhonePrefixDoesNotMatchRemoteExceptionType();
			remoteExc.setOperatorName(e.getOperatorName());
			remoteExc.setOperatorPrefix(e.getOperatorPrefix());
			remoteExc.setPhoneNumber(e.getPhoneNumber());
			remoteExc.setPhonePrefix(e.getPhonePrefix());
			throw new PhonePrefixDoesNotMatchRemoteException("Phone Exception", remoteExc);
		}

		catch (PhoneNumberIncorrectException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getNumber());
			throw new PhoneNumberIncorrectRemoteException("Phone Exception", remoteExc);
		}

		catch (PhoneAlreadyExistsException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getPhoneNumber());
			throw new PhoneAlreadyExistsRemoteException("Phone Exception", remoteExc);
		}

		catch (OperatorDoesNotExistException e) {
			OperatorPrefixElementType remoteExc = new OperatorPrefixElementType();
			remoteExc.setOperatorPrefix(e.getPrefix());
			throw new OperatorDoesNotExistRemoteException("Operator Exception", remoteExc);
		}
	}

	@Override
	public void cancelRegisterPhone(PhoneDtoType cancelRegisterPhoneInput) throws NoSuchPhoneRemoteException, PrefixDoesNotExistRemoteException {
		try {
			PhoneDto dto = new PhoneDto(cancelRegisterPhoneInput.getOperatorPrefix(), cancelRegisterPhoneInput.getNumber());
			ProcessCancelRegisteredPhoneService phone = new ProcessCancelRegisteredPhoneService(dto);
			phone.execute();
		} catch (NoSuchPhoneException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getNumber());
			throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
		}

		catch (PrefixDoesNotExistException e) {

			PrefixElementType remoteExc = new PrefixElementType();
			remoteExc.setOperatorPrefix(e.getOperatorPrefix());
			throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
		}
	}
	
	@Override
	public void receiveSMS(SMSDtoType receiveSMSInput) throws NoSuchPhoneRemoteException, PhoneIsOFFRemoteException, PrefixDoesNotExistRemoteException {

		try {
			SMSDto dto = new SMSDto(receiveSMSInput.getNrSource(), receiveSMSInput.getNrDest(), receiveSMSInput.getText());
			ProcessReceiveSMSService receiveSMSService = new ProcessReceiveSMSService(dto);
			receiveSMSService.execute();

		} catch (NoSuchPhoneException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getNumber());
			throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
		}

		catch (PhoneIsOFFException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getPhoneNr());
			throw new PhoneIsOFFRemoteException("Phone Exception", remoteExc);
		}

		catch (PrefixDoesNotExistException e) {
			PrefixElementType remoteExc = new PrefixElementType();
			remoteExc.setOperatorPrefix(e.getOperatorPrefix());
			throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
		}
	}

	@Override
	public void sendSMS(SMSDtoType sendSMSInput) throws NoSuchPhoneRemoteException, InvalidBalanceOperationRemoteException, PhoneIsOFFRemoteException,
			PhoneIsBUSYRemoteException, PrefixDoesNotExistRemoteException, InsuficientBalanceRemoteException {

		try {
			SMSDto dto = new SMSDto(sendSMSInput.getNrSource(), sendSMSInput.getNrDest(), sendSMSInput.getText());
			ProcessSendSMSService sendSMSService = new ProcessSendSMSService(dto);
			sendSMSService.execute();
			
		} catch (NoSuchPhoneException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getNumber());
			throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
		}

		catch (InvalidBalanceOperationException e) {
			BalanceDtoType remoteExc = new BalanceDtoType();
			remoteExc.setNumber(e.getPhoneNumber());
			remoteExc.setBalance(e.getPhoneBalance());
			throw new InvalidBalanceOperationRemoteException("Balance Exception", remoteExc);
		}

		catch (PhoneIsOFFException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getPhoneNr());
			throw new PhoneIsOFFRemoteException("Phone Exception", remoteExc);
		}

		catch (PhoneIsBUSYException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getPhoneNr());
			throw new PhoneIsBUSYRemoteException("Phone Exception", remoteExc);
		}

		catch (PrefixDoesNotExistException e) {
			PrefixElementType remoteExc = new PrefixElementType();
			remoteExc.setOperatorPrefix(e.getOperatorPrefix());
			throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
			
		} catch (InsuficientBalanceException e) {
			BalanceDtoType remoteExc = new BalanceDtoType();
			remoteExc.setNumber(e.getPhoneNumber());
			remoteExc.setBalance(e.getPhoneBalance());
			throw new InsuficientBalanceRemoteException("Balance Exception", remoteExc);
		}
	}

	@Override
	public BalanceDtoType getPhoneBalance(BalanceDtoType newBalance) throws NoSuchPhoneRemoteException, PrefixDoesNotExistRemoteException {

		try {
			BalanceDto dto = new BalanceDto(newBalance.getNumber(), newBalance.getBalance());
			ProcessGetBalanceService balanceService = new ProcessGetBalanceService(dto);
			balanceService.execute();

			newBalance.setBalance(dto.getBalance());

			return newBalance;

		} catch (NoSuchPhoneException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getNumber());
			throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
		} catch (PrefixDoesNotExistException e) {
			PrefixElementType remoteExc = new PrefixElementType();
			remoteExc.setOperatorPrefix(e.getOperatorPrefix());
			throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
		}

	}

	@Override
	public PhoneAndBalanceListDtoType getPhonesBalanceList(PhoneAndBalanceListDtoType phoneAndBalanceListDtoType) throws OperatorDoesNotExistRemoteException {

		try {
			
			// Execute service
			PhonesAndBalanceListDto dto = new PhonesAndBalanceListDto(phoneAndBalanceListDtoType.getOperatorPrefix());
			ProcessGetPhonesBalanceListService service = new ProcessGetPhonesBalanceListService(dto);
			service.execute();
			
			//fill in dto arrayList
			for (BalanceDto balanceDto : dto.getPhoneList()) {
				
				BalanceDtoType newBalanceDtoType = new BalanceDtoType();
				
				newBalanceDtoType.setNumber(balanceDto.getNumber());
				newBalanceDtoType.setBalance(balanceDto.getBalance());
				
				phoneAndBalanceListDtoType.getPhoneList().add(newBalanceDtoType);
			}

			return phoneAndBalanceListDtoType;

		} catch (OperatorDoesNotExistException e) {
			OperatorPrefixElementType remoteExc = new OperatorPrefixElementType();
			remoteExc.setOperatorPrefix(e.getPrefix());
			throw new OperatorDoesNotExistRemoteException("Operator Exception", remoteExc);
		}

	}

	@Override
	public void increasePhoneBalance(BalanceDtoType increasePhoneBalanceInput) throws NoSuchPhoneRemoteException, PrefixDoesNotExistRemoteException, InvalidBalanceOperationRemoteException{

		try {

			BalanceDto dto = new BalanceDto(increasePhoneBalanceInput.getNumber(), increasePhoneBalanceInput.getBalance());
			ProcessIncBalanceService service = new ProcessIncBalanceService(dto);
			service.execute();
		}

		catch (NoSuchPhoneException e) {
			PhoneNumberElementType remoteExc = new PhoneNumberElementType();
			remoteExc.setPhoneNumber(e.getNumber());
			throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
		}

		catch (PrefixDoesNotExistException e) {
			PrefixElementType remoteExc = new PrefixElementType();
			remoteExc.setOperatorPrefix(e.getOperatorPrefix());
			throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
		}
		catch (InvalidBalanceOperationException e) {
			BalanceDtoType remoteExc = new BalanceDtoType();
			remoteExc.setBalance(e.getPhoneBalance());
			remoteExc.setNumber(e.getPhoneNumber());
			throw new InvalidBalanceOperationRemoteException("Prefix Exception", remoteExc);
		}
	}
}
