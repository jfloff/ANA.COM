package pt.ist.anacom.replication.vote;

import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;

import pt.ist.anacom.shared.stubs.client.BalanceAndPhoneListType;

public class BalanceAndPhoneListTypeVoteList extends VoteList {

    public BalanceAndPhoneListTypeVoteList(ArrayList<Integer> versionsList, ArrayList<Object> detailedTypeList, Set<String> responseURLList) {
        super(versionsList, detailedTypeList, responseURLList);
    }

    @Override
    public void addVote(int version, Object response) {
        BalanceAndPhoneListType balanceAndPhoneListType = (BalanceAndPhoneListType) response;
        // Search vote
        for (Vote vote : voteList) {
            BalanceAndPhoneListType castedResponse = (BalanceAndPhoneListType) vote.getResponse();
            Logger.getLogger(this.getClass()).info("[TEST] Comparing -> " + castedResponse + "\n[TEST] withOther -> " + balanceAndPhoneListType);
            if (!castedResponse.getPhoneList().containsAll(balanceAndPhoneListType.getPhoneList())) {
                Logger.getLogger(this.getClass()).info("SAME!");
                // If found, incremente counter
                vote.setCounter(vote.getCounter() + 1);
                return;
            }
        }

        // If not found add a new one Vote
        voteList.add(new Vote(version, 1, response));
    }

}
