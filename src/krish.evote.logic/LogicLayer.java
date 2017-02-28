package krish.evote.logic;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.*;
import edu.uga.cs.evote.session.Session;

import java.util.Date;
import java.util.List;

public interface LogicLayer
{
    public String                   login(Session session, String userName, String password ) throws EVException;
    public String                   loginVoter(Session session, String userName, String password ) throws EVException;
    public void                     logout( String ssid ) throws EVException;
    public long                     createDistrict(String name) throws EVException;
    public long                     createVoter(String firstName, String lastName, String username,
                                                String password, String email, String address, int age, String voterId) throws EVException;
    public List<ElectoralDistrict>  findAllElectoralDistricts() throws EVException;
    public List<ElectoralDistrict>  findElectoralDistrict(String name) throws EVException;
    public List<ElectoralDistrict>  findElectoralDistrict(long id) throws EVException;
    public void                     storeDistrict(ElectoralDistrict district) throws EVException;
    public void                     setVoterDistrict(String username, long district_id) throws EVException;

    public long                     createPoliticalParty(String name) throws EVException;
    public List<PoliticalParty>     findAllPoliticalParties() throws EVException;
    public List<PoliticalParty>     findPoliticalParty(String name) throws EVException;
    public List<PoliticalParty>     findPoliticalParty(long id) throws EVException;
    public void                     storePoliticalParty(PoliticalParty party) throws EVException;

    public long                     createBallot(Date openDate, Date closeDate, boolean approved, String districtName) throws EVException;

    public List<Ballot>             findAllBallots() throws EVException;
    public List<Ballot>             findBallot(long id) throws EVException;

    public long                     createIssue(String question, Ballot ballot) throws EVException;

    public List<Election>           findAllElections() throws EVException;
    public long                     createElection(String office, Boolean isPartisan, Ballot ballot) throws EVException;
    public String                   getUserEmail(Session session, String userName) throws EVException;
    public void                   updateUserPassword(Session session, String userName, String newPassword) throws EVException;
}
