package pt.ist.anacom.domain;

import java.util.List;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorNullNameException;
import pt.ist.anacom.shared.exception.OperatorWithWrongPrefixException;
import pt.ist.anacom.shared.exception.PhoneAlreadyExistsException;

public class Operator extends Operator_Base {

    public Operator(String prefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo) {
        super();

        if (name == null || name.length() < 1)
            throw new OperatorNullNameException(prefix);
        if (prefix.length() != AnacomData.PREFIX_LENGTH)
            throw new OperatorWithWrongPrefixException(name, prefix);

        setPrefix(prefix);
        setName(name);
        setPlan(new Plan(tax, taxVoice, taxSMS, taxVideo));
    }

    @Override
    public void addPhone(Phone phone) {
        if (hasPhone(phone.getNr()))
            throw new PhoneAlreadyExistsException(phone.getNr());
        super.addPhone(phone);
    }

    @Override
    public String toString() {
        return getPrefix() + " (" + getPrefix() + ")" + " has " + getPhoneCount() + " Phones.";
    }

    public boolean conflicts(Operator operator) {

        return (operator.getPrefix().equals(this.getPrefix())) && (operator.getName().equals(this.getName()));
    }

    public boolean hasPhone(String nr) {
        return testGetPhoneByNr(nr) != null;
    }

    /*
     * ------------------- - Phones Methods - -------------------
     */

    public Phone testGetPhoneByNr(String nr) {
        for (Phone phone : getPhoneSet())
            if (phone.getNr().equals(nr))
                return phone;

        return null;
    }

    /**
     * Search a phone by his number
     * 
     * @param nr The phone number to find
     */
    public Phone getPhoneByNr(String nr) {

        Phone result = testGetPhoneByNr(nr);
        if (result == null)
            throw new NoSuchPhoneException(nr);

        return result;
    }


    public void removePhoneByNumber(String phoneNumber) {

        for (Phone phone : getPhoneSet())
            if (phone.getNr().equals(phoneNumber)) {
                removePhone(phone);
                return;
            }
    }

    public List<Phone> getPhoneList() {
        return this.getPhone();
    }

}
