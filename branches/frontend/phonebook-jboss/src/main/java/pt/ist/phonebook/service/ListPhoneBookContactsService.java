package pt.ist.phonebook.service;

import java.util.List;
import java.util.ArrayList;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.PhoneBook;
import pt.ist.phonebook.domain.Contact;

import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.PhoneBookDto;

public class ListPhoneBookContactsService extends PhoneBookService {

    private PhoneBookDto result;

    public final void dispatch() {
        PhoneBook pb = FenixFramework.getRoot();
        List<ContactDetailedDto> contactDtoList = new ArrayList<ContactDetailedDto>();
        for (Contact contact : pb.getContactSet()) {
            ContactDetailedDto view = new ContactDetailedDto(contact.getName(), contact.getPhoneNumber());
            contactDtoList.add(view);
        }
        this.result = new PhoneBookDto(contactDtoList);
    }

    public final PhoneBookDto getContactList() {
        return this.result;
    }
}
