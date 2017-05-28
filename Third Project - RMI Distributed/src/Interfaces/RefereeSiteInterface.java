package Interfaces;

import ClientCoach.Coach;
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
public interface RefereeSiteInterface extends Remote
{
    /**
     * Referee announce a new game
     * @param referee referee
     * @throws java.rmi.RemoteException RemoteException
     */
    public void announceNewGame(Referee referee)  throws RemoteException;


    /**
     * Referee declare game winner
     * @return true if the match ended
     * @param referee referee
     * @throws java.rmi.RemoteException RemoteException
     */
    public boolean declareGameWinner(Referee referee)  throws RemoteException;


    /**
     * Referee declare match winner
     * @param referee referee
     * @throws java.rmi.RemoteException RemoteException
     */
    public void declareMatchWinner(Referee referee)  throws RemoteException;

    
    /**
     * Coach inform referee that he is ready
     * @param coach coach
     * @throws java.rmi.RemoteException RemoteException
     */
    public void informArrival(Coach coach)  throws RemoteException;

    /**
     * Shut down server
     * @throws java.rmi.RemoteException RemoteException
     */
    public void shutDown()throws RemoteException;
}
