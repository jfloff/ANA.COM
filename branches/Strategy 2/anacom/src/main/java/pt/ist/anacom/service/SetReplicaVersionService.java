package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.ReplicaVersionDto;
import pt.ist.fenixframework.FenixFramework;

public class SetReplicaVersionService extends AnacomService {

    private final ReplicaVersionDto replicaVersionDto;

    public SetReplicaVersionService(ReplicaVersionDto replicaVersionDto) {
        this.replicaVersionDto = replicaVersionDto;
    }

    @Override
    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();
        anacom.setReplicaVersion(replicaVersionDto.getVersion());
        System.out.println("NOVA VERSAO: " + replicaVersionDto.getVersion());
    }
}
