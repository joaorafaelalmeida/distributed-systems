package Interfaces;

import ClientCoach.Coach;
import ClientContestant.Contestant;
import ClientReferee.Referee;
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
public interface PlaygroundInterface extends Remote
{
    /**
     * Referee prepares the trial to start
     * @param referee referee
     * @throws java.rmi.RemoteException RemoteException
     */
    public void callTrial(Referee referee) throws RemoteException;


    /**
     * Referee starts the trial
     * @param referee referee
     * @throws java.rmi.RemoteException RemoteException
     */
    public void startTrial(Referee referee)  throws RemoteException;


    /**
     * Referee assert trial decision
     * @param referee referee
     * @throws java.rmi.RemoteException RemoteException
     * 
     * @return true if don't have yet a game winner
     */
    public boolean assertTrialDecision(Referee referee)  throws RemoteException;


    /**
     * Coach inform referee that his team is ready
     * @param  coach coach
     * @throws java.rmi.RemoteException RemoteException
     */
    public void informReferee(Coach coach)  throws RemoteException;


    /**
     *  Contestant is ready to pull the rope
     * @param  contestant contestant
     * @throws java.rmi.RemoteException RemoteException
     */
    public void getReady(Contestant contestant)  throws RemoteException;


    /**
     * Contestant pull the rope
     * @param  contestant contestant
     * @throws java.rmi.RemoteException RemoteException
     */
    public void pullTheRope(Contestant contestant)  throws RemoteException;

    /**
     * Contestant finish to pull the rope
     * @param  contestant contestant
     * @throws java.rmi.RemoteException RemoteException
     */
    public void amDone(Contestant contestant)  throws RemoteException;

    /**
     * Shut down server
     * @throws java.rmi.RemoteException RemoteException
     */
    public void shutDown()throws RemoteException;
}
