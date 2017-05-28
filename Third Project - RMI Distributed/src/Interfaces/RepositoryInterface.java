package Interfaces;

import ComInf.States.*;
import ComInf.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General description:
 *      This type of data define the interaction protocol, like a remote object 
 * that constitute one of the services
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public interface RepositoryInterface extends Remote
{
    /**
     * Write in the log an update of referee state
     * @param state referee state
     * @param vcl clock
     * @return vector clock
     * @throws java.rmi.RemoteException RemoteException
     */
    public VectorClock  updateRefereeState(VectorClock vcl, RefereeState state) throws RemoteException;

    
    /**
     * Write in the log an update of coach state
     * 
     * @param state coach state
     * @param id coach id
     * @param vcl clock
     * @return vector clock
     * @throws java.rmi.RemoteException RemoteException
     */
    public VectorClock updateCoachState(VectorClock vcl, CoachState state, int id)throws RemoteException;

    
    /**
     * Write in the log an update of contestant state
     * 
     * @param state contestant state
     * @param id contestant id
     * @param idCoach coach id
     * @param strength contestant strength
     * @param vcl clock
     * @return vector clock
     * @throws java.rmi.RemoteException RemoteException
     */
    public VectorClock updateContestantState(VectorClock vcl, ContestantState state, int id, int idCoach, int strength)throws RemoteException;

    
    /**
     * Indicate the end of the referee
     * 
     * @return true if is the end
     * @throws java.rmi.RemoteException RemoteException
     */
    public boolean endOfReferee() throws RemoteException;


    /**
     * Indicate the end of the coach
     * 
     * @return true if is the end
     * @throws java.rmi.RemoteException RemoteException
     */
    public boolean endOfCoach()  throws RemoteException;


    /**
     * Indicate the end of the contestant
     * 
     * @return true if is the end
     * @throws java.rmi.RemoteException RemoteException
     */
    public boolean endOfContestant() throws RemoteException;


    /**
     * Is decided if the game ends or not
     * 
     * @return true if is the end
     * @throws java.rmi.RemoteException RemoteException
     */
    public boolean assertTrialDecision() throws RemoteException;


    /**
     * Match is over
     * 
     * @param state referee state
     * @param vcl clock
     * @return vector clock
     * @throws java.rmi.RemoteException RemoteException
     */
    public VectorClock endOfMatch(VectorClock vcl, RefereeState state) throws RemoteException;


    /**
     * Coach get his team deciding who have more strength
     * 
     * @param id id of the coach
     * @return array with trial elements id's
     * @throws java.rmi.RemoteException RemoteException
     */
    public int[] getTeam(int id) throws RemoteException;


    /**
     * Indicate the end of the game
     * 
     * @return false if is the end
     * @throws java.rmi.RemoteException RemoteException
     */
    public boolean endOfGame() throws RemoteException;
    
    /**
     * Shut down server
     * @throws java.rmi.RemoteException RemoteException
     */
    public void shutDown()throws RemoteException;
}
