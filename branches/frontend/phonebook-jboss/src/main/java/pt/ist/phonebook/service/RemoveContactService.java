package pt.ist.phonebook.service;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.PhoneBook;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;

public class RemoveContactService extends PhoneBookService {

    private ContactSimpleDto contact;

    public RemoveContactService(ContactSimpleDto contactName) {
        this.contact = contactName;
    }

    public final void dispatch() throws ContactDoesNotExistException {
        PhoneBook pb = FenixFramework.getRoot();
        pb.removeContact(this.contact.getName());
    }
}
