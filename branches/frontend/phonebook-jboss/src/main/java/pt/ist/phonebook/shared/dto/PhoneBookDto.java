package pt.ist.phonebook.shared.dto;

import java.util.List;

public class PhoneBookDto {

    private List<ContactDetailedDto> contactDtoList;

    public PhoneBookDto(List<ContactDetailedDto> contactDtoList) {
        this.contactDtoList = contactDtoList;
    }

    public List<ContactDetailedDto> getContacts() {
        return this.contactDtoList;
    }

}
