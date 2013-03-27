package pt.ist.anacom.replication.vote;

import java.util.ArrayList;
import java.util.Set;

public class VoteList {

    private ArrayList<Vote> voteList = new ArrayList<Vote>();
    ArrayList<String> responseURLList = null;

    public VoteList() {
        voteList = new ArrayList<Vote>();
    }

    /**
     * @param detailedTypeList list of responses of the replicas
     * @param responseURLList set of url's of the replicas
     */
    public VoteList(ArrayList<Integer> versionsList, ArrayList<Object> detailedTypeList, Set<String> responseURLList) {
        for (int i = 0; i < versionsList.size(); i++)
            addVote(versionsList.get(i), detailedTypeList.get(i));
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
            System.out.println("[LOG]Counter Version " + vote.getVersion() + " counter " + vote.getCounter());
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
