package pt.ist.anacom.presentationserver.server;

import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationOutDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateAndPhoneDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.dto.VoiceCallDto;
import pt.ist.anacom.shared.dto.VoiceEndCallDto;

public class ApplicationServerBridgeDistSoft implements ApplicationServerBridge {

    @Override
    public void registerPhone(PhoneDetailedDto dto) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerOperator(OperatorDetailedDto operatorDto) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendSMS(SMSDto SMSDto) {
        // TODO Auto-generated method stub

    }

    @Override
    public void increaseBalance(BalanceAndPhoneDto incBalDto) {
        // TODO Auto-generated method stub

    }

    @Override
    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void cancelRegisteredPhone(PhoneSimpleDto phoneDto) {
        // TODO Auto-generated method stub

    }

    @Override
    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StateDto getPhoneState(PhoneSimpleDto phoneDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPhoneState(StateAndPhoneDto dto) {
        // TODO Auto-generated method stub

    }

    @Override
    public CommunicationOutDto getLastMadeCommunication(PhoneSimpleDto phoneDto) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public void startVoiceCall(VoiceCallDto callDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endVoiceCall(VoiceEndCallDto voiceEndCallDto) {
		// TODO Auto-generated method stub
		
	}


}
// private AnacomPortType getPort(String operatorPrefix) {
//
// AnacomService anacom = new AnacomService();
// AnacomPortType port = anacom.getAnacomPort();
//
// String endpointURL = "http://localhost:8080/" + operatorPrefix + "/" +
// operatorPrefix;
// BindingProvider bp = (BindingProvider) port;
// bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
//
// return port;
// }
//
// public String getOperatorPrefix(String phoneNumber) {
// return phoneNumber.substring(AnacomEnum.PREFIX_POS, AnacomEnum.PREFIX_LENGTH);
// }
//
// @Override
// // APENAS NECESSARIO PARA A 2A ENTREGA DE SD!!!!
// public void getPhoneReceivedSMSList(PhoneReceivedSMSListDto dto) {
// // TODO Auto-generated method stub
// }
//
// @Override
// public void registerOperator(OperatorDto operatorDto) {
//
// AnacomPortType port = getPort(operatorDto.getOperatorPrefix());
//
// OperatorDtoType op = new OperatorDtoType();
//
// op.setName(operatorDto.getName());
// op.setOperatorPrefix(operatorDto.getOperatorPrefix());
// op.setTax(operatorDto.getTax());
// op.setTaxVoice(operatorDto.getTaxVoice());
// op.setTaxSMS(operatorDto.getTaxSMS());
// op.setTaxVideo(operatorDto.getTaxVideo());
//
// try {
// port.registerOperator(op);
//
// } catch (OperatorNullNameRemoteException e) {
// // Tested
// PrefixElementType exc = e.getFaultInfo();
// throw new OperatorNullNameException(exc.getOperatorPrefix());
// } catch (OperatorWithWrongPrefixRemoteException e) {
// // change?
// OperatorExceptionType exc = e.getFaultInfo();
// throw new OperatorWithWrongPrefixException(exc.getOperatorName(),
// exc.getOperatorName());
// } catch (OperatorAlreadyExistsRemoteException e) {
// // Tested
// OperatorExceptionType exc = e.getFaultInfo();
// throw new OperatorPrefixAlreadyExistsException(exc.getOperatorPrefix());// VIT
// }
//
// }
//
// @Override
// public void registerPhone(DetailedPhoneDto dto) {
// // Tested
// AnacomPortType port = getPort(dto.getOperatorPrefix());
//
// PhoneDtoType phone = new PhoneDtoType();
//
// phone.setNumber(dto.getPhoneNumber());
// phone.setOperatorPrefix(dto.getOperatorPrefix());
//
// try {
// port.registerPhone(phone);
// } catch (WebServiceException e) {
// // change?
// throw new CouldNotConnectException(getOperatorPrefix(dto.getPhoneNumber()));
// } catch (PhoneNumberIncorrectRemoteException e) {
// // Tested
// PhoneNumberElementType exc = e.getFaultInfo();
// throw new PhoneNumberIncorrectException(exc.getPhoneNumber());
// } catch (PhoneAlreadyExistsRemoteException e) {
// // Tested
// PhoneNumberElementType exc = e.getFaultInfo();
// throw new PhoneAlreadyExistsException(exc.getPhoneNumber());
// }
//
// catch (OperatorDoesNotExistRemoteException e) {
// // Tested
// OperatorPrefixElementType exc = e.getFaultInfo();
// throw new OperatorDoesNotExistException(exc.getOperatorPrefix());
// }
// }
//
// @Override
// public void sendSMS(SMSDto SMSDto) {
//
// AnacomPortType sendPort = getPort(getOperatorPrefix(SMSDto.getSourceNumber()));
// AnacomPortType receivePort =
// getPort(getOperatorPrefix(SMSDto.getDestinationNumber()));
//
// SMSDtoType SMSDtoType = new SMSDtoType();
//
// SMSDtoType.setNrDest(SMSDto.getDestinationNumber());
// SMSDtoType.setNrSource(SMSDto.getSourceNumber());
// SMSDtoType.setText(SMSDto.getMessage());
//
// boolean committed = false;
//
// try {
// // Transaction.begin();
// sendPort.sendSMS(SMSDtoType);
// receivePort.receiveSMS(SMSDtoType);
// // Transaction.commit();
// // committed = true;
// }
//
// catch (NoSuchPhoneRemoteException e) {
// PhoneNumberElementType exc = e.getFaultInfo();
// throw new NoSuchPhoneException(exc.getPhoneNumber());
// }
//
// catch (InvalidBalanceOperationRemoteException e) {
// BalanceDtoType exc = e.getFaultInfo();
// throw new InvalidBalanceOperationException(exc.getNumber(), exc.getBalance());
// }
//
// catch (PhoneIsOFFRemoteException e) {
// PhoneNumberElementType exc = e.getFaultInfo();
// throw new PhoneIsOFFException(exc.getPhoneNumber());
// }
//
// catch (PhoneIsBUSYRemoteException e) {
// PhoneNumberElementType exc = e.getFaultInfo();
// throw new PhoneIsBUSYException(exc.getPhoneNumber());
// }
//
// catch (PrefixDoesNotExistRemoteException e) {
// PrefixElementType exc = e.getFaultInfo();
// throw new PrefixDoesNotExistException(exc.getOperatorPrefix());
//
// }
//
// catch (InsuficientBalanceRemoteException e) {
// BalanceDtoType exc = e.getFaultInfo();
// throw new InsuficientBalanceException(exc.getNumber(), exc.getBalance());
//
// }
// // finally {
// // if (!committed)
// // Transaction.abort();
// // }
// }
//
// @Override
// public void increaseBalance(PhoneAndBalanceDto balDto) {
// // Tested
// // AnacomPortType port = getPort(getOperatorPrefix(balDto.getNumber()));
// //
// // BalanceDtoType balance = new BalanceDtoType();
// //
// // balance.setNumber(balDto.getNumber());
// // balance.setBalance(balDto.getBalance());
// //
// // try {
// // port.increasePhoneBalance(balance);
// // } catch (NoSuchPhoneRemoteException e) {
// // // Tested
// // PhoneNumberElementType exc = e.getFaultInfo();
// // throw new NoSuchPhoneException(exc.getPhoneNumber());
// //
// // } catch (PrefixDoesNotExistRemoteException e) {
// // // Tested
// // PrefixElementType exc = e.getFaultInfo();
// // throw new PrefixDoesNotExistException(exc.getOperatorPrefix());
// // } catch (InvalidBalanceOperationRemoteException e) {
// // // Tested
// // BalanceDtoType exc = e.getFaultInfo();
// // throw new InvalidBalanceOperationException(exc.getNumber(), exc.getBalance());
// // }
// }
//
// @Override
// public BalanceDto getPhoneBalance(SimplePhoneDto phoneDto) {
//
// // AnacomPortType port = getPort(getOperatorPrefix(balanceDto.getNumber()));
// //
// // BalanceDtoType newBalance = new BalanceDtoType();
// //
// // newBalance.setNumber(balanceDto.getNumber());
// //
// // try {
// // balanceDto.setBalance(port.getPhoneBalance(newBalance).getBalance());
// // } catch (NoSuchPhoneRemoteException e) {
// // // Tested
// // PhoneNumberElementType exc = e.getFaultInfo();
// // throw new NoSuchPhoneException(exc.getPhoneNumber());
// //
// // } catch (PrefixDoesNotExistRemoteException e) {
// // // change?
// // PrefixElementType exc = e.getFaultInfo();
// // throw new PrefixDoesNotExistException(exc.getOperatorPrefix());
// // }
//
// return null;
// }
//
// @Override
// public void getPhonesBalanceList(PhonesAndBalanceListDto dto) {
//
// AnacomPortType port = getPort(dto.getOperatorPrefix());
//
// PhoneAndBalanceListDtoType phoneBalanceDtoType = new PhoneAndBalanceListDtoType();
// phoneBalanceDtoType.setOperatorPrefix(dto.getOperatorPrefix());
//
// try {
// phoneBalanceDtoType = port.getPhonesBalanceList(phoneBalanceDtoType);
// for (BalanceDtoType balanceDtoType : phoneBalanceDtoType.getPhoneList()) {
// dto.add(balanceDtoType.getNumber(), balanceDtoType.getBalance());
// }
// } catch (OperatorDoesNotExistRemoteException e) {
// OperatorPrefixElementType exc = e.getFaultInfo();
// throw new OperatorDoesNotExistException(exc.getOperatorPrefix());
// }
// }
//
// @Override
// public void cancelRegisteredPhone(SimplePhoneDto dto) {
// AnacomPortType port = getPort(dto.getOperatorPrefix());
//
// PhoneDtoType phoneDto = new PhoneDtoType();
//
// phoneDto.setNumber(dto.getPhoneNumber());
// phoneDto.setOperatorPrefix(dto.getOperatorPrefix());
//
// try {
// port.cancelRegisterPhone(phoneDto);
// } catch (NoSuchPhoneRemoteException e) {
// // Tested
// PhoneNumberElementType exc = e.getFaultInfo();
// throw new NoSuchPhoneException(exc.getPhoneNumber());
// }
//
// catch (PrefixDoesNotExistRemoteException e) {
// // changed?
// PrefixElementType exc = e.getFaultInfo();
// throw new PrefixDoesNotExistException(exc.getOperatorPrefix());
// }
// }
//
// @Override
// public void getPhoneState(PhoneStateDto dto) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void setPhoneState(PhoneStateDto dto) {
// // TODO Auto-generated method stub
//
// }
