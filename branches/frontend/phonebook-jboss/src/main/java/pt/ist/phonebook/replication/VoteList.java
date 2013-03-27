package pt.ist.phonebook.replication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import pt.ist.phonebook.shared.stubs.client.ContactDetailedReqType;

public class VoteList {

    private ArrayList<Vote> voteList = new ArrayList<Vote>();
    ArrayList<String> responseURLList = null;

    public VoteList() {
        voteList = new ArrayList<Vote>();
    }

    public VoteList(Collection<ContactDetailedReqType> contactReqType, Set<String> responseURLList) {
        for (ContactDetailedReqType contReqType : contactReqType)
            this.addVote(contReqType.getVersion().intValue(), contReqType);
        this.responseURLList = new ArrayList<String>(responseURLList);
    }

    public ArrayList<Vote> getVoteList() {
        return voteList;
    }

    public void setVoteList(ArrayList<Vote> votesList) {
        this.voteList = votesList;
    }

    public void addVote(int version, Object response) {

        // Search vote
        for (Vote vote : voteList) {
            if (vote.getVersion() == version) {
                // If found, incremente counter
                vote.setCounter(vote.getCounter() + 1);
                return;
            }
        }

        // If not found add a new one Vote
        voteList.add(new Vote(version, 1, response));
    }

    public Object getBestResponse(int quorumLength) {

        // Search the most voted vote
        for (Vote vote : voteList) {
            System.out.println("[jUDDI] Counter Version " + vote.getVersion() + " counter " + vote.getCounter());
            Double length = (double) (quorumLength / 2);
            if (vote.getCounter() >= 2) {
                return vote.getResponse();
            }
        }

        return null;

    }

    @Override
    public String toString() {
        return voteList + "\n" + responseURLList;
    }

    public ArrayList<String> getResponseURLList() {
        return responseURLList;
    }

}
