package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class IncreaseBalanceService extends AnacomService {

    private BalanceAndPhoneDto incBalDto;

    public IncreaseBalanceService(BalanceAndPhoneDto incBalDto) {
        this.incBalDto = incBalDto;
    }

    public final void dispatch() throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            BalanceLimitExceededException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.increasePhoneBalance(incBalDto.getPhoneNumber(), incBalDto.getBalance());
    }
}
