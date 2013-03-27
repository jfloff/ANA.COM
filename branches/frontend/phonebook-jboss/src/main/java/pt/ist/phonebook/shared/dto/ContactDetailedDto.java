package pt.ist.phonebook.shared.dto;

public class ContactDetailedDto extends ContactSimpleDto {

    private Integer phoneNumber;

    public ContactDetailedDto(String name, Integer phoneNumber) {
        super(name);
        this.phoneNumber = phoneNumber;
    }

    public Integer getPhoneNumber() {
        return this.phoneNumber;
    }

}
