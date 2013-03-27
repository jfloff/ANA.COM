package pt.ist.phonebook.service;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.Contact;
import pt.ist.phonebook.domain.PhoneBook;
import pt.ist.phonebook.shared.exception.NameAlreadyExistsException;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;

public class CreateNewContactService extends PhoneBookService {

    private ContactDetailedDto dto;

    public CreateNewContactService(ContactDetailedDto dto) {
        this.dto = dto;
    }

    public final void dispatch() throws NameAlreadyExistsException {
        PhoneBook phoneBook = FenixFramework.getRoot();
        phoneBook.addContact(new Contact(this.dto.getName(), this.dto.getPhoneNumber()));
    }
}
