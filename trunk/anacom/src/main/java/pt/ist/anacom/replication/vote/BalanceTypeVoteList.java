package pt.ist.anacom.replication.vote;

import java.util.ArrayList;
import java.util.Set;

import pt.ist.anacom.shared.stubs.client.BalanceType;

public class BalanceTypeVoteList extends VoteList {

    public BalanceTypeVoteList(ArrayList<Integer> versionsList, ArrayList<Object> detailedTypeList, Set<String> responseURLList) {
     super(versionsList, detailedTypeList, responseURLList);
    }
    
    @Override
    public void addVote(int version, Object response) {
        BalanceType balanceType = (BalanceType) response;
        // Search vote
        for (Vote vote : voteList) {
            BalanceType castedResponse = (BalanceType) vote.getResponse();
            if (castedResponse.getBalance() == balanceType.getBalance()) {
                // If found, incremente counter
                vote.setCounter(vote.getCounter() + 1);
                return;
            }
        }

        // If not found add a new one Vote
        voteList.add(new Vote(version, 1, response));
    }
    
}
