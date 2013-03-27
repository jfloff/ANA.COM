package pt.ist.anacom.applicationserver;

import pt.ist.anacom.shared.stubs.AnacomPortType;
import pt.ist.anacom.shared.stubs.BalanceDtoType;
import pt.ist.anacom.shared.stubs.InsuficientBalanceRemoteException;
import pt.ist.anacom.shared.stubs.InvalidBalanceOperationRemoteException;
import pt.ist.anacom.shared.stubs.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.OperatorAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.OperatorDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.OperatorDtoType;
import pt.ist.anacom.shared.stubs.OperatorNullNameRemoteException;
import pt.ist.anacom.shared.stubs.OperatorWithWrongPrefixRemoteException;
import pt.ist.anacom.shared.stubs.PhoneAlreadyExistsRemoteException;
import pt.ist.anacom.shared.stubs.PhoneAndBalanceListDtoType;
import pt.ist.anacom.shared.stubs.PhoneDtoType;
import pt.ist.anacom.shared.stubs.PhoneIsBUSYRemoteException;
import pt.ist.anacom.shared.stubs.PhoneIsOFFRemoteException;
import pt.ist.anacom.shared.stubs.PhoneNumberIncorrectRemoteException;
import pt.ist.anacom.shared.stubs.PhonePrefixDoesNotMatchRemoteException;
import pt.ist.anacom.shared.stubs.PrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.SMSDtoType;

// import javax.annotation.PostConstruct;

// dtoType to dtoz

// //////////////////////////////////
// // ISTO ESTA MAL QUE SE FARTA ////
// //////////////////////////////////



@javax.jws.WebService(endpointInterface = "pt.ist.anacom.shared.stubs.AnacomPortType", wsdlLocation = "/anacom.wsdl", name = "AnacomPortType", portName = "AnacomPort", targetNamespace = "http://pt.ist.anacom.essd.0403", serviceName = "AnacomService")
public class ApplicationServerWebService implements AnacomPortType {

    @Override
    public void receiveSMS(SMSDtoType receiveSMSInput) throws NoSuchPhoneRemoteException,
            PhoneIsOFFRemoteException,
            PrefixDoesNotExistRemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerOperator(OperatorDtoType registerOperatorInput) throws OperatorNullNameRemoteException,
            OperatorWithWrongPrefixRemoteException,
            OperatorAlreadyExistsRemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerPhone(PhoneDtoType registerPhoneInput) throws PhonePrefixDoesNotMatchRemoteException,
            PhoneNumberIncorrectRemoteException,
            PhoneAlreadyExistsRemoteException,
            OperatorDoesNotExistRemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelRegisterPhone(PhoneDtoType cancelRegisterPhoneInput) throws NoSuchPhoneRemoteException, PrefixDoesNotExistRemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendSMS(SMSDtoType sendSMSInput) throws NoSuchPhoneRemoteException,
            InvalidBalanceOperationRemoteException,
            PhoneIsOFFRemoteException,
            PhoneIsBUSYRemoteException,
            PrefixDoesNotExistRemoteException,
            InsuficientBalanceRemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public BalanceDtoType getPhoneBalance(BalanceDtoType getPhoneBalanceInput) throws NoSuchPhoneRemoteException, PrefixDoesNotExistRemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PhoneAndBalanceListDtoType getPhonesBalanceList(PhoneAndBalanceListDtoType getPhonesBalanceListInput) throws OperatorDoesNotExistRemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void increasePhoneBalance(BalanceDtoType increasePhoneBalanceInput) throws NoSuchPhoneRemoteException,
            InvalidBalanceOperationRemoteException,
            PrefixDoesNotExistRemoteException {
        // TODO Auto-generated method stub

    }



    // public static void init(final String operatorName) {
    // System.out.println(".............STARTING Anacom " + operatorName +
    // " SERVER...........");
    //
    // // initializes the Fenix Framework
    // try {
    //
    // FenixFramework.initialize(new Config() {
    // {
    // dbAlias = "/tmp/db" + operatorName;
    // domainModelPath = "/tmp/anacom.dml";
    // repositoryType = RepositoryType.BERKELEYDB;
    // rootClass = Anacom.class;
    // }
    // });
    //
    // } catch (Exception e) {
    // System.out.println("Failed to initialize the operator server.\n");
    // }
    // }
    //
    // @Override
    // public void registerOperator(OperatorDtoType registerOperatorInput) throws
    // OperatorNullNameRemoteException,
    // OperatorWithWrongPrefixRemoteException,
    // OperatorAlreadyExistsRemoteException {
    // try {
    // OperatorDto dto = new OperatorDto(registerOperatorInput.getOperatorPrefix(),
    // registerOperatorInput.getOperatorPrefix(),
    // registerOperatorInput.getTax(), registerOperatorInput.getTaxVoice(),
    // registerOperatorInput.getTaxSMS(),
    // registerOperatorInput.getTaxVideo());
    //
    // RegisterNewOperatorService op = new RegisterNewOperatorService(dto);
    // op.execute();
    //
    // } catch (OperatorNullNameException e) {
    // PrefixElementType remoteExc = new PrefixElementType();
    // remoteExc.setOperatorPrefix(e.getPrefix());
    // throw new OperatorNullNameRemoteException("Operator Exception", remoteExc);
    // }
    //
    // catch (OperatorWithWrongPrefixException e) {
    // OperatorExceptionType remoteExc = new OperatorExceptionType();
    // remoteExc.setOperatorName(e.getName());
    // remoteExc.setOperatorPrefix(e.getOperatorPrefix());
    // throw new OperatorWithWrongPrefixRemoteException("Operator Exception", remoteExc);
    // }
    //
    // catch (OperatorPrefixAlreadyExistsException e) {// VIT
    // OperatorExceptionType remoteExc = new OperatorExceptionType();
    // remoteExc.setOperatorPrefix(e.getPrefix());
    // throw new OperatorAlreadyExistsRemoteException("Operator Exception", remoteExc);
    // }
    // }
    //
    // @Override
    // public void registerPhone(PhoneDtoType registerPhoneInput) throws
    // PhonePrefixDoesNotMatchRemoteException,
    // PhoneNumberIncorrectRemoteException,
    // PhoneAlreadyExistsRemoteException,
    // OperatorDoesNotExistRemoteException {
    //
    // try {
    // DetailedPhoneDto dto = new DetailedPhoneDto(registerPhoneInput.getOperatorPrefix(),
    // registerPhoneInput.getNumber());
    // RegisterNewPhoneService phone = new RegisterNewPhoneService(dto);
    // phone.execute();
    // }
    //
    // catch (PhoneNumberIncorrectException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getNumber());
    // throw new PhoneNumberIncorrectRemoteException("Phone Exception", remoteExc);
    // }
    //
    // catch (PhoneAlreadyExistsException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getPhoneNumber());
    // throw new PhoneAlreadyExistsRemoteException("Phone Exception", remoteExc);
    // }
    //
    // catch (OperatorDoesNotExistException e) {
    // OperatorPrefixElementType remoteExc = new OperatorPrefixElementType();
    // remoteExc.setOperatorPrefix(e.getPrefix());
    // throw new OperatorDoesNotExistRemoteException("Operator Exception", remoteExc);
    // }
    // }
    //
    // @Override
    // public void cancelRegisterPhone(PhoneDtoType cancelRegisterPhoneInput) throws
    // NoSuchPhoneRemoteException, PrefixDoesNotExistRemoteException {
    // try {
    // DetailedPhoneDto dto = new
    // DetailedPhoneDto(cancelRegisterPhoneInput.getOperatorPrefix(),
    // cancelRegisterPhoneInput.getNumber());
    // CancelRegisteredPhoneService phone = new CancelRegisteredPhoneService(dto);
    // phone.execute();
    // } catch (NoSuchPhoneException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getNumber());
    // throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
    // }
    //
    // catch (PrefixDoesNotExistException e) {
    //
    // PrefixElementType remoteExc = new PrefixElementType();
    // remoteExc.setOperatorPrefix(e.getOperatorPrefix());
    // throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
    // }
    // }
    //
    // @Override
    // public void receiveSMS(SMSDtoType receiveSMSInput) throws
    // NoSuchPhoneRemoteException,
    // PhoneIsOFFRemoteException,
    // PrefixDoesNotExistRemoteException {
    //
    // try {
    // SMSDto dto = new SMSDto(receiveSMSInput.getNrSource(), receiveSMSInput.getNrDest(),
    // receiveSMSInput.getText());
    // ProcessReceiveSMSService receiveSMSService = new ProcessReceiveSMSService(dto);
    // receiveSMSService.execute();
    //
    // } catch (NoSuchPhoneException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getNumber());
    // throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
    // }
    //
    // catch (PhoneIsOFFException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getPhoneNr());
    // throw new PhoneIsOFFRemoteException("Phone Exception", remoteExc);
    // }
    //
    // catch (PrefixDoesNotExistException e) {
    // PrefixElementType remoteExc = new PrefixElementType();
    // remoteExc.setOperatorPrefix(e.getOperatorPrefix());
    // throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
    // }
    // }
    //
    // @Override
    // public void sendSMS(SMSDtoType sendSMSInput) throws NoSuchPhoneRemoteException,
    // InvalidBalanceOperationRemoteException,
    // PhoneIsOFFRemoteException,
    // PhoneIsBUSYRemoteException,
    // PrefixDoesNotExistRemoteException,
    // InsuficientBalanceRemoteException {
    //
    // try {
    // SMSDto dto = new SMSDto(sendSMSInput.getNrSource(), sendSMSInput.getNrDest(),
    // sendSMSInput.getText());
    // ProcessSendSMSService sendSMSService = new ProcessSendSMSService(dto);
    // sendSMSService.execute();
    //
    // } catch (NoSuchPhoneException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getNumber());
    // throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
    // }
    //
    // catch (InvalidBalanceOperationException e) {
    // BalanceDtoType remoteExc = new BalanceDtoType();
    // remoteExc.setNumber(e.getPhoneNumber());
    // remoteExc.setBalance(e.getPhoneBalance());
    // throw new InvalidBalanceOperationRemoteException("Balance Exception", remoteExc);
    // }
    //
    // catch (PhoneIsOFFException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getPhoneNr());
    // throw new PhoneIsOFFRemoteException("Phone Exception", remoteExc);
    // }
    //
    // catch (PhoneIsBUSYException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getPhoneNr());
    // throw new PhoneIsBUSYRemoteException("Phone Exception", remoteExc);
    // }
    //
    // catch (PrefixDoesNotExistException e) {
    // PrefixElementType remoteExc = new PrefixElementType();
    // remoteExc.setOperatorPrefix(e.getOperatorPrefix());
    // throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
    //
    // } catch (InsuficientBalanceException e) {
    // BalanceDtoType remoteExc = new BalanceDtoType();
    // remoteExc.setNumber(e.getPhoneNumber());
    // remoteExc.setBalance(e.getPhoneBalance());
    // throw new InsuficientBalanceRemoteException("Balance Exception", remoteExc);
    // }
    // }
    //
    // @Override
    // public BalanceDtoType getPhoneBalance(BalanceDtoType newBalance) throws
    // NoSuchPhoneRemoteException, PrefixDoesNotExistRemoteException {
    //
    // try {
    // SimplePhoneDto dto = new SimplePhoneDto(newBalance.getNumber());
    // GetBalanceService balanceService = new GetBalanceService(dto);
    // balanceService.execute();
    // BalanceDto balanceDto = balanceService.getBalanceServiceResult();
    //
    // newBalance.setBalance(balanceDto.getBalance());
    //
    // return newBalance;
    //
    // } catch (NoSuchPhoneException e) {
    // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // remoteExc.setPhoneNumber(e.getNumber());
    // throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
    // } catch (PrefixDoesNotExistException e) {
    // PrefixElementType remoteExc = new PrefixElementType();
    // remoteExc.setOperatorPrefix(e.getOperatorPrefix());
    // throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
    // }
    //
    // }
    //
    // @Override
    // public PhoneAndBalanceListDtoType getPhonesBalanceList(PhoneAndBalanceListDtoType
    // phoneAndBalanceListDtoType) throws OperatorDoesNotExistRemoteException {
    //
    // try {
    //
    // // Execute service
    // PhonesAndBalanceListDto dto = new
    // PhonesAndBalanceListDto(phoneAndBalanceListDtoType.getOperatorPrefix());
    // GetPhonesBalanceListService service = new GetPhonesBalanceListService(dto);
    // service.execute();
    //
    // // fill in dto arrayList
    // for (BalanceDto balanceDto : dto.getPhoneList()) {
    //
    // BalanceDtoType newBalanceDtoType = new BalanceDtoType();
    //
    // newBalanceDtoType.setBalance(balanceDto.getBalance());
    //
    // phoneAndBalanceListDtoType.getPhoneList().add(newBalanceDtoType);
    // }
    //
    // return phoneAndBalanceListDtoType;
    //
    // } catch (OperatorDoesNotExistException e) {
    // OperatorPrefixElementType remoteExc = new OperatorPrefixElementType();
    // remoteExc.setOperatorPrefix(e.getPrefix());
    // throw new OperatorDoesNotExistRemoteException("Operator Exception", remoteExc);
    // }
    //
    // }
    //
    // @Override
    // public void increasePhoneBalance(BalanceDtoType increasePhoneBalanceInput) throws
    // NoSuchPhoneRemoteException,
    // PrefixDoesNotExistRemoteException,
    // InvalidBalanceOperationRemoteException {
    //
    // // try {
    // //
    // // BalanceDto dto = new BalanceDto(increasePhoneBalanceInput.getBalance());
    // // IncreaseBalanceService service = new IncreaseBalanceService(dto);
    // // service.execute();
    // // }
    // //
    // // catch (NoSuchPhoneException e) {
    // // PhoneNumberElementType remoteExc = new PhoneNumberElementType();
    // // remoteExc.setPhoneNumber(e.getNumber());
    // // throw new NoSuchPhoneRemoteException("Phone Exception", remoteExc);
    // // }
    // //
    // // catch (PrefixDoesNotExistException e) {
    // // PrefixElementType remoteExc = new PrefixElementType();
    // // remoteExc.setOperatorPrefix(e.getOperatorPrefix());
    // // throw new PrefixDoesNotExistRemoteException("Prefix Exception", remoteExc);
    // // } catch (InvalidBalanceOperationException e) {
    // // BalanceDtoType remoteExc = new BalanceDtoType();
    // // remoteExc.setBalance(e.getPhoneBalance());
    // // remoteExc.setNumber(e.getPhoneNumber());
    // // throw new InvalidBalanceOperationRemoteException("Prefix Exception",
    // // remoteExc);
    // // }
    // }
}
