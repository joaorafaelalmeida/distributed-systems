package Interfaces;


import ClientCoach.Coach;
import ClientContestant.Contestant;
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
public interface ContestantsBenchInterface extends Remote
{
    /**
     * Coach select contestants to play in the trial
     * @param coach coach
     * @throws java.rmi.RemoteException  RemoteException
     */
    public void callContestants(Coach coach) throws RemoteException;

    /**
     * Contestant prepares to go to the playground to pull the rope
     * @param contestant contestant
     * @throws java.rmi.RemoteException  RemoteException
     */
    public void followCoachAdvice(Contestant contestant) throws RemoteException;


    /**
     * Contestant seat down and rest
     * @param contestant contestant
     * @throws java.rmi.RemoteException  RemoteException
     */
    public void seatDown(Contestant contestant) throws RemoteException;


    /**
     * Coach review notes
     * @param coach coach
     * @throws java.rmi.RemoteException  RemoteException
     */
    public void reviewNotes(Coach coach) throws RemoteException;
    
    /**
     * Shut down server
     * @throws java.rmi.RemoteException  RemoteException
     */
    public void shutDown()throws RemoteException;
}
