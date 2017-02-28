package krish.evote.logic.impl;

import java.util.List;

import krish.evote.EVException;
import krish.evote.entity.ElectionsOfficer;
import krish.evote.entity.Voter;
import krish.evote.object.ObjectLayer;
import krish.evote.presentation.EvoteError;
import krish.evote.session.Session;
import krish.evote.session.SessionManager;

public class LoginCtrl
{
    private ObjectLayer objectLayer = null;

    public LoginCtrl( ObjectLayer objectLayer )
    {
        this.objectLayer = objectLayer;
    }

    public String login( Session session, String userName, String password )
            throws EVException
    {
        String ssid = null;

        ElectionsOfficer modelElectionsOfficer = objectLayer.createElectionsOfficer();
        modelElectionsOfficer.setUserName( userName );
        modelElectionsOfficer.setPassword( password );
        List<ElectionsOfficer> persons = objectLayer.findElectionsOfficer( modelElectionsOfficer );
        if( persons.size() > 0 ) {
            ElectionsOfficer person = persons.get( 0 );
            session.setUser( person );
            ssid = SessionManager.storeSession( session );

        }
        else
            throw new EVException( "LoginCtrl.login: Invalid User Name or Password" );

        return ssid;
    }

    public String loginVoter(Session session, String userName, String password) throws EVException
    {
        String ssid = null;

        Voter modelVoter = objectLayer.createVoter();
        modelVoter.setUserName(userName);
        modelVoter.setPassword(password);
        List<Voter> persons = objectLayer.findVoter(modelVoter);
        if (persons.size() > 0) {
            Voter person = persons.get(0);
            session.setUser(person);
            ssid = SessionManager.storeSession(session);

        } else
            throw new EVException("LoginCtrl.loginVoter: Invalid User Name or Password");

        return ssid;
    }
    
    public String getUserEmail(Session session, String userName) throws EVException
    {
    	String userEmail = null;
        Voter modelVoter = objectLayer.createVoter();
        modelVoter.setUserName(userName);
        List<Voter> persons = objectLayer.findVoter(modelVoter);
        if (persons != null && persons.size() == 1) {
            Voter person = persons.get(0);
            userEmail = person.getEmailAddress();
        }
        else
        {
        	 ElectionsOfficer modelElectionsOfficer = objectLayer.createElectionsOfficer();
             modelElectionsOfficer.setUserName( userName );
             List<ElectionsOfficer> electionOfficerList = objectLayer.findElectionsOfficer( modelElectionsOfficer );
             if( electionOfficerList!= null && electionOfficerList.size() == 1 ) {
                 ElectionsOfficer electionsOfficer = electionOfficerList.get( 0 );
                 userEmail = electionsOfficer.getEmailAddress();
             }
        }
        if(userEmail == null || userEmail.isEmpty())
        {
        	throw new EVException("LoginCtrl.loginVoter: Invalid username.");
        }
        return userEmail;
    }
    
    public void updateUserPassword(Session session, String userName, String newPassword) throws EVException
    {
    	boolean success = false;
    	Voter modelVoter = objectLayer.createVoter();
        modelVoter.setUserName(userName);
        List<Voter> persons = objectLayer.findVoter(modelVoter);
        if (persons != null && persons.size() == 1) {
            
        	Voter person = persons.get(0);
            Voter modelVoterForPasswordUpdate = objectLayer.createVoter();
            modelVoterForPasswordUpdate.setId(person.getId());
            modelVoterForPasswordUpdate.setFirstName(person.getFirstName());
            modelVoterForPasswordUpdate.setLastName(person.getLastName());
            modelVoterForPasswordUpdate.setUserName(person.getUserName());
            modelVoterForPasswordUpdate.setPassword(newPassword); //setting the new password
            modelVoterForPasswordUpdate.setEmailAddress(person.getEmailAddress());
            modelVoterForPasswordUpdate.setAddress(person.getAddress());
            modelVoterForPasswordUpdate.setAge(person.getAge());
            modelVoterForPasswordUpdate.setVoterId(person.getVoterId());
            modelVoterForPasswordUpdate.setElectoralDistrict(person.getElectoralDistrict());
            objectLayer.storeVoter(modelVoterForPasswordUpdate);
            success = true;
            
        } 
        else
        {
        	ElectionsOfficer modelElectionsOfficer = objectLayer.createElectionsOfficer();
            modelElectionsOfficer.setUserName( userName );
            List<ElectionsOfficer> electionOfficerList = objectLayer.findElectionsOfficer( modelElectionsOfficer );
            if (electionOfficerList != null && electionOfficerList.size() == 1) {
                
            	ElectionsOfficer electionsOfficer= electionOfficerList.get(0);
                ElectionsOfficer modelElectionOfficerForPasswordUpdate = objectLayer.createElectionsOfficer();
                modelElectionOfficerForPasswordUpdate.setId(electionsOfficer.getId());
                modelElectionOfficerForPasswordUpdate.setFirstName(electionsOfficer.getFirstName());
                modelElectionOfficerForPasswordUpdate.setLastName(electionsOfficer.getLastName());
                modelElectionOfficerForPasswordUpdate.setUserName(electionsOfficer.getUserName());
                modelElectionOfficerForPasswordUpdate.setPassword(newPassword); //setting the new password
                modelElectionOfficerForPasswordUpdate.setEmailAddress(electionsOfficer.getEmailAddress());
                modelElectionOfficerForPasswordUpdate.setAddress(electionsOfficer.getAddress());
                objectLayer.storeElectionsOfficer(modelElectionOfficerForPasswordUpdate);
                success = true;
            } 
        }
        
        if(!success)
    	{
        	throw new EVException("LoginCtrl.loginVoter: Could not update user password.");
    	}
        return;
    }
}
