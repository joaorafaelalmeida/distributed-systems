package ClientCoach;


import ComInf.Parameters;
import ComInf.States;
import ComInf.States.CoachState;
import ComInf.VectorClock;
import Interfaces.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General description:
 *      Definition of the Coach thread
 * 
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Coach extends Thread implements Serializable
{
    /** 
     * Actual state of the Coach 
     */
    private CoachState atualState;
    
    /** 
     * Id of the Coach
     */
    private final int id;
    
    /** 
     * Playground stub
     */
    private final PlaygroundInterface playground;
    
    /** 
     * Contestant bench stub
     */
    private final ContestantsBenchInterface bench;
    
    /** 
     * Referee site stub
     */
    private final RefereeSiteInterface refereeSite;
    
    /** 
     * Application logger
     */
    private final RepositoryInterface logger;
    
    /**
    *   Constructor
    *
    *   @param id id of the coach
    *   @param logger shared region to record all states
    *   @param playground shared region where the game occurs
    *   @param bench shared region where coach select the players
    *   @param refereeSite shared region where coach inform referee
    */
    public Coach (int id, RepositoryInterface logger, PlaygroundInterface playground, ContestantsBenchInterface bench, RefereeSiteInterface refereeSite)
    {
        this.id = id;
        this.logger = logger;
        this.playground = playground;
        this.bench = bench;
        this.refereeSite = refereeSite; 
        atualState = States.CoachState.WAIT_FOR_REFEREE_COMMAND;
    }
    
    /**
     *   Life cycle
     */
    @Override
    public void run() 
    {
        try 
        {
            randomTime();
            refereeSite.informArrival(this);
            while(logger.endOfCoach())
            {
                bench.callContestants(this);
                playground.informReferee(this);
                bench.reviewNotes(this);
            }
            endServers();
        } 
        catch (RemoteException e) 
        {
            System.out.println("Remote exception by the client " +
                                  getName () + ": " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
    }
    
    /**
     *  Function to return the coach id
     * 
     *  @return coach id
     */
    public int getID()
    {
        return id;
    }
    
    /**
     *  Function to return the coach state
     * 
     *  @return coach state
     */
    public CoachState getActualState()
    {
        return atualState;
    }
    
    /**
     *  Function to set the coach state
     *  @param state coach state
     */
    public void setState(CoachState state)
    {
        this.atualState = state;
    }
    
    /** 
     *  Function to stop Coach during a random time 
     */
    protected void randomTime ()
    {
        int time = (int)(Math.random () * 100); 
        try 
        {
            Thread.sleep(time);
        }
        catch (InterruptedException ex) 
        {
        }
    }
    
    /** 
     * Function to stop Referee during atime 
     * @throws java.rmi.RemoteException RemoteException
     */
    protected void endServers() throws RemoteException
    {
        playground.shutDown();
        refereeSite.shutDown();
        bench.shutDown();
        logger.shutDown();
    }
    
}
