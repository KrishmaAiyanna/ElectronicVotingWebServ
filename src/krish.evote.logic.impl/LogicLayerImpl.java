package krish.evote.logic.impl;

import krish.evote.EVException;
import krish.evote.entity.*;
import krish.evote.logic.LogicLayer;
import krish.evote.object.ObjectLayer;
import krish.evote.object.impl.ObjectLayerImpl;
import krish.evote.persistence.PersistenceLayer;
import krish.evote.persistence.impl.PersistenceLayerImpl;
import krish.evote.presentation.FindAllDistricts;
import krish.evote.session.Session;
import krish.evote.session.SessionManager;

import java.sql.Connection;
import java.util.Date;
import java.util.List;


public class LogicLayerImpl implements LogicLayer
{
    private ObjectLayer objectLayer = null;

    public LogicLayerImpl( Connection conn )
    {
        objectLayer = new ObjectLayerImpl();
        PersistenceLayer persistenceLayer = new PersistenceLayerImpl( conn, objectLayer );
        objectLayer.setPersistence( persistenceLayer );
        System.out.println( "LogicLayerImpl.LogicLayerImpl(conn): initialized" );
    }

    public LogicLayerImpl( ObjectLayer objectLayer )
    {
        this.objectLayer = objectLayer;
        System.out.println( "LogicLayerImpl.LogicLayerImpl(objectLayer): initialized" );
    }

    public String login(Session session, String userName, String password )
            throws EVException
    {
        LoginCtrl ctrlVerifyPerson = new LoginCtrl( objectLayer );
        return ctrlVerifyPerson.login( session, userName, password );
    }

    public long createDistrict(String name) throws EVException
    {
        DistrictCtrl createDistrictCtrl = new DistrictCtrl(objectLayer);
        return createDistrictCtrl.createDistrict(name);
    }

    @Override
    public List<ElectoralDistrict> findAllElectoralDistricts() throws EVException
    {
        DistrictCtrl findAllDistrictsCtrl = new DistrictCtrl(objectLayer);
        return findAllDistrictsCtrl.findAllDistricts();
    }

    @Override
    public List<ElectoralDistrict> findElectoralDistrict(String name) throws EVException
    {
        DistrictCtrl findAllDistrictsCtrl = new DistrictCtrl(objectLayer);
        return findAllDistrictsCtrl.findDistrict(name);
    }

    @Override
    public List<ElectoralDistrict> findElectoralDistrict(long id) throws EVException
    {
        DistrictCtrl findAllDistrictsCtrl = new DistrictCtrl(objectLayer);
        return findAllDistrictsCtrl.findDistrict(id);
    }

    @Override
    public void storeDistrict(ElectoralDistrict district) throws EVException
    {
        DistrictCtrl createDistrictCtrl = new DistrictCtrl(objectLayer);
        createDistrictCtrl.storeDistrict(district);
    }

    @Override
    public void logout( String ssid ) throws EVException
    {
        SessionManager.logout( ssid );
    }

    @Override
    public String loginVoter(Session session, String userName, String password) throws EVException
    {
        LoginCtrl ctrlVerifyPerson = new LoginCtrl( objectLayer );
        return ctrlVerifyPerson.loginVoter( session, userName, password );
    }

    @Override
    public long createVoter(String firstName, String lastName, String username, String password, String email, String address, int age, String voterId) throws EVException
    {
        VoterCtrl voterCtrl = new VoterCtrl(objectLayer);
        return voterCtrl.createVoter(firstName, lastName, username, password, email, address, age, voterId);
    }

    @Override
    public void setVoterDistrict(String username, long district_id) throws EVException
    {
        VoterCtrl voterCtrl = new VoterCtrl(objectLayer);
        voterCtrl.setVoterDistrict(username, district_id);
    }

    @Override
    public long createPoliticalParty(String name) throws EVException
    {
        PartyCtrl partyCtrl = new PartyCtrl(objectLayer);
        return partyCtrl.createParty(name);
    }

    @Override
    public List<PoliticalParty> findAllPoliticalParties() throws EVException
    {
        PartyCtrl partyCtrl = new PartyCtrl(objectLayer);
        return partyCtrl.findAllParties();
    }

    @Override
    public List<PoliticalParty> findPoliticalParty(String name) throws EVException
    {
        PartyCtrl partyCtrl = new PartyCtrl(objectLayer);
        return partyCtrl.findPoliticalParty(name);
    }

    @Override
    public List<PoliticalParty> findPoliticalParty(long id) throws EVException
    {
        PartyCtrl partyCtrl = new PartyCtrl(objectLayer);
        return partyCtrl.findPoliticalParty(id);
    }

    @Override
    public void storePoliticalParty(PoliticalParty party) throws EVException
    {
        PartyCtrl partyCtrl = new PartyCtrl(objectLayer);
        partyCtrl.storeParty(party);
    }

    @Override
    public long createBallot(Date openDate, Date closeDate, boolean approved, String districtName) throws EVException
    {
        BallotCtrl ballotCtrl = new BallotCtrl(objectLayer);
        DistrictCtrl districtCtrl = new DistrictCtrl(objectLayer);
        ElectoralDistrict district = null;
        if (districtName != null)
        {
            district = districtCtrl.findDistrict(districtName).get(0);
        }
        return ballotCtrl.createBallot(openDate, closeDate, approved, district);
    }

    @Override
    public List<Ballot> findAllBallots() throws EVException
    {
        BallotCtrl ballotCtrl = new BallotCtrl(objectLayer);
        return ballotCtrl.findAllBallots();
    }

    @Override
    public List<Ballot> findBallot(long id) throws EVException
    {
        BallotCtrl ballotCtrl = new BallotCtrl(objectLayer);
        return ballotCtrl.findBallot(id);
    }

    @Override
    public long createIssue(String question, Ballot ballot) throws EVException
    {
        IssueCtrl issueCtrl = new IssueCtrl(objectLayer);
        return issueCtrl.createIssue(question, ballot);
    }

    @Override
    public long createElection(String office, Boolean isPartisan, Ballot ballot) throws EVException
    {
        ElectionCtrl electionCtrl = new ElectionCtrl(objectLayer);
        return electionCtrl.createElection(office, isPartisan, ballot);
    }

    @Override
    public List<Election> findAllElections() throws EVException
    {
        ElectionCtrl electionCtrl = new ElectionCtrl(objectLayer);
        return electionCtrl.findAllElections();
    }

	@Override
	public String getUserEmail(Session session, String userName) throws EVException {
		 LoginCtrl ctrlVerifyPerson = new LoginCtrl( objectLayer );
	     return ctrlVerifyPerson.getUserEmail(session, userName);
	}

	@Override
	public void updateUserPassword(Session session, String userName, String newPassword) throws EVException {
		 LoginCtrl ctrlVerifyPerson = new LoginCtrl( objectLayer );
	      ctrlVerifyPerson.updateUserPassword(session, userName, newPassword);
	}

    /*@Override
    public List<Issue> findAllIssues() throws EVException
    {
        IssueCtrl issueCtrl = new IssueCtrl(objectLayer);
        return issueCtrl.findAllIssues();
    }*/
}
