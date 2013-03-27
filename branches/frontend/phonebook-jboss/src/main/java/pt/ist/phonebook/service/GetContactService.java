package pt.ist.phonebook.service;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.Contact;
import pt.ist.phonebook.domain.PhoneBook;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;

public class GetContactService extends PhoneBookService {

    private ContactSimpleDto dto;
    private ContactDetailedDto resultContactDetailedDto;

    public GetContactService(ContactSimpleDto dto) {
        this.dto = dto;
    }

    public final void dispatch() throws ContactDoesNotExistException {
        PhoneBook phoneBook = FenixFramework.getRoot();
        Contact contact = phoneBook.getPhoneNumberByName(this.dto.getName());
        resultContactDetailedDto = new ContactDetailedDto(contact.getName(), contact.getPhoneNumber());
    }

    public ContactDetailedDto getContactServiceResult() {
        return this.resultContactDetailedDto;
    }
}
