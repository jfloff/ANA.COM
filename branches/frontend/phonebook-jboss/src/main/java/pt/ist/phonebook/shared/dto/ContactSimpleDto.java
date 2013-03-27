package pt.ist.phonebook.shared.dto;

public class ContactSimpleDto {

    private String name;

    public ContactSimpleDto(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
