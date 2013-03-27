package pt.ist.anacom.replication.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import pt.ist.anacom.shared.stubs.client.BalanceType;

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
        
        int sumOfResponses = sumOfResponses();
        Vote bestVote = null;
        long counterLength = Math.round((double)quorumLength/2);
        System.out.println("counterLength = " + counterLength);
        // Search the most voted vote
        for (Vote vote : voteList) {
            BalanceType balType = (BalanceType) vote.getResponse();
            System.out.println("Balance = " + balType.getBalance() + " counter = " +  vote.getCounter());
            if (vote.getCounter() >= counterLength) {
                if(bestVote == null){
                    bestVote = vote;
                    System.out.println("Took -> " + balType.getBalance());
                }
                else if(bestVote.getVersion() < vote.getVersion()){
                    bestVote =  vote;
                }
                else if (bestVote.getVersion() == vote.getVersion()){
                    System.out.println("CATASTROFE!!!! 2 falhas!!!!!!");
                    System.exit(-1);
                }
                    
            }
            // Para o caso limite de 3 diferentes no quorum
            if(sumOfResponses != 4 && bestVote != null && bestVote.getVersion() < vote.getVersion()){
                System.out.println("!!!!!!! LIMIT ->" + quorumLength);
                bestVote = null;
            }
        }

        return (bestVote == null) ? null : bestVote.getResponse();

    }

    @Override
    public String toString() {
        return voteList + "\n" + responseURLList;
    }

    public ArrayList<String> getResponseURLList() {
        return responseURLList;
    }

    /** Counts the number of received responses in voteList
     * 
     * @return number of responses
     */
    private int sumOfResponses(){
        int sum = 0;
        for(Vote vote : voteList)
            sum += vote.getCounter();
        return sum;
    }
}
