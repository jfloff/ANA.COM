package pt.ist.anacom.replication.vote;

import java.util.ArrayList;
import java.util.Set;

import pt.ist.anacom.shared.stubs.client.LastCommunicationType;

public class LastCommunicationTypeVoteList extends VoteList {

    public LastCommunicationTypeVoteList(ArrayList<Integer> versionsList, ArrayList<Object> detailedTypeList, Set<String> responseURLList) {
        super(versionsList, detailedTypeList, responseURLList);
    }

    @Override
    public void addVote(int version, Object response) {
        LastCommunicationType lastCommunicationType = (LastCommunicationType) response;
        // Search vote
        for (Vote vote : voteList) {
            LastCommunicationType castedResponse = (LastCommunicationType) vote.getResponse();
            if (sameCommunication(lastCommunicationType, castedResponse)) {
                // If found, incremente counter
                vote.setCounter(vote.getCounter() + 1);
                return;
            }
        }

        // If not found add a new one Vote
        voteList.add(new Vote(version, 1, response));
    }

    private boolean sameCommunication(LastCommunicationType communication1, LastCommunicationType communication2) {
        return communication1.getDestinationPhoneNumber().equals(communication2.getDestinationPhoneNumber())
                && communication1.getLength() == communication2.getLength() && communication1.getType() == communication2.getType()
                && communication1.getCost() == communication2.getCost();
    }

}
