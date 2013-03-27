package pt.ist.anacom.service;

import java.util.Set;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.fenixframework.FenixFramework;

public class CleanAnacomService extends AnacomService {

    public CleanAnacomService() {
    }

    @Override
    public void dispatch() throws AnacomException {
        Anacom anacom = FenixFramework.getRoot();
        Set<Operator> allOperators = anacom.getOperatorSet();
        allOperators.clear();
        System.out.println("OPERADORES: " + anacom.getOperatorSet());
    }

}
