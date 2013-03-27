package pt.ist.anacom.replication.vote;

import java.util.ArrayList;
import java.util.Set;

import pt.ist.anacom.shared.stubs.client.StateType;

public class StateTypeVoteList extends VoteList {

    public StateTypeVoteList(ArrayList<Integer> versionsList, ArrayList<Object> detailedTypeList, Set<String> responseURLList) {
        super(versionsList, detailedTypeList, responseURLList);
    }
    
    @Override
    public void addVote(int version, Object response) {
        StateType stateType = (StateType) response;
        // Search vote
        for (Vote vote : voteList) {
            StateType castedResponse = (StateType) vote.getResponse();
            if (castedResponse.getPhoneState() == stateType.getPhoneState()) {
                // If found, incremente counter
                vote.setCounter(vote.getCounter() + 1);
                return;
            }
        }

        // If not found add a new one Vote
        voteList.add(new Vote(version, 1, response));
    }
    
}
