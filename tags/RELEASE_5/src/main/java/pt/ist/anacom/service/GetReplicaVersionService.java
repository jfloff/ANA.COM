package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.ReplicaVersionDto;
import pt.ist.fenixframework.FenixFramework;

public class GetReplicaVersionService extends AnacomService {

    private ReplicaVersionDto resultReplicaVersionDto;

    public GetReplicaVersionService() {

    }

    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();
        Integer replicaVersion = anacom.getReplicaVersion();
        resultReplicaVersionDto = new ReplicaVersionDto(replicaVersion);
    }

    public ReplicaVersionDto getReplicaVersionServiceResult() {
        return this.resultReplicaVersionDto;
    }
}
