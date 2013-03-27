package pt.ist.phonebook.service;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.Contact;
import pt.ist.phonebook.domain.PhoneBook;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;

public class SearchContactService extends PhoneBookService {

    private String token;
    private List<ContactSimpleDto> result;

    public SearchContactService(String token) {
        this.token = token;
    }

    public final void dispatch() {
        PhoneBook phoneBook = FenixFramework.getRoot();
        ;

        List<Contact> matchingContacts = phoneBook.searchContact(this.token);
        List<ContactSimpleDto> matchingContactDtoList = new ArrayList<ContactSimpleDto>();
        for (Contact contact : matchingContacts) {
            matchingContactDtoList.add(new ContactSimpleDto(contact.getName()));
        }
        this.result = matchingContactDtoList;
    }

    public final List<ContactSimpleDto> getResult() {
        return this.result;
    }
}
