package pt.ist.anacom.replication.vote;

import java.util.ArrayList;
import java.util.Set;

import pt.ist.anacom.shared.stubs.client.SMSPhoneReceivedListType;

public class SMSPhoneReceivedListTypeVoteList extends VoteList {

    public SMSPhoneReceivedListTypeVoteList(ArrayList<Integer> versionsList, ArrayList<Object> detailedTypeList, Set<String> responseURLList) {
        super(versionsList, detailedTypeList, responseURLList);
    }

    @Override
    public void addVote(int version, Object response) {
        SMSPhoneReceivedListType _SMSPhoneReceivedListTypeype = (SMSPhoneReceivedListType) response;
        // Search vote
        for (Vote vote : voteList) {
            SMSPhoneReceivedListType castedResponse = (SMSPhoneReceivedListType) vote.getResponse();
            if (castedResponse.getSmsList().containsAll(_SMSPhoneReceivedListTypeype.getSmsList())) {
                // If found, incremente counter
                vote.setCounter(vote.getCounter() + 1);
                return;
            }
        }

        // If not found add a new one Vote
        voteList.add(new Vote(version, 1, response));
    }

}
