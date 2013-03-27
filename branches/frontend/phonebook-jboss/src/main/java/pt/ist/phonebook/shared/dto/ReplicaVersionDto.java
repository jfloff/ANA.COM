package pt.ist.phonebook.shared.dto;

public class ReplicaVersionDto {

    private Integer version = 0;

    public ReplicaVersionDto(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
