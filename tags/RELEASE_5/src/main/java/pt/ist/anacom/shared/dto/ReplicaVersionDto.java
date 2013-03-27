package pt.ist.anacom.shared.dto;

public class ReplicaVersionDto {

    private int version = 0;

    public ReplicaVersionDto(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
