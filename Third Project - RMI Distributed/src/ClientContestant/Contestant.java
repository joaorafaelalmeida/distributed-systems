package ClientContestant;


import ComInf.Parameters;
import ComInf.States;
import ComInf.States.ContestantState;
import ComInf.VectorClock;
import Interfaces.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General description:
 *      Definition of the Contestant thread
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Contestant extends Thread implements Serializable
{
    /** 
     * Actual state of the contestant
     */
    private ContestantState atualState;
    
    /** 
     * Id of the contestant 
     */
    private final int id;
    
    /** 
     * Id of the coach relative the contestant team
     */
    private final int idCoach;
    
    /** 
     * Strength of the contestant
     */
    private int strength;
    
    /** 
     * Playground stub
     */
    private final PlaygroundInterface playground;
    
    /** 
     * Contestant bench stub
     */
    private final ContestantsBenchInterface bench;
    
    /** 
     * Application logger
     */
    private final RepositoryInterface logger;
    
    /**
    *   Constructor
    * 
    *   @param id id of the contestant
    *   @param idCoach id of the coach
    *   @param logger shared region to record all states
    *   @param playground shared region where the game occurs
    *   @param bench shared region where players rest
    */
    public Contestant (int id, int idCoach, RepositoryInterface logger, PlaygroundInterface playground, ContestantsBenchInterface bench)
    {
        strength = (int)(10 + Math.random ()* 5);
        this.id = id;
        this.idCoach = idCoach;
        this.logger = logger;
        this.playground = playground;
        this.bench = bench;
        atualState = States.ContestantState.SEAT_AT_THE_BENCH;
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
            bench.seatDown(this);
            while(logger.endOfContestant())
            {
                bench.followCoachAdvice(this);
                playground.getReady(this);
                playground.pullTheRope(this);
                playground.amDone(this);
                bench.seatDown(this);
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
     *  Function to return the contestant id
     * 
     *  @return contestant id
     */
    public int getID()
    {
        return id;
    }
    
    /**
     *  Function to return the coach id
     * 
     *  @return coach id
     */
    public int getCoachId()
    {
        return idCoach;
    }
    
    /**
     *  Function to return the contestant state
     * 
     *  @return contestant state
     */
    public ContestantState getActualState()
    {
        return atualState;
    }
    
    /**
     *  Function to set the contestant state
     *  
     *  @param state contestant state
     */
    public void setState(ContestantState state)
    {
        this.atualState = state;
    }
    
    /** 
     *  Function to stop Contestant during a random time 
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
     *  Function to increase strength 
     */
    public void increaseStrength() 
    {
        strength++;
    }
    
    /** 
     *  Function to decrease strength 
     */
    public void decreaseStrength()
    {
        strength--;
    }
    
    /**
     *  Function to return the contestant strength
     * 
     *  @return strength
     */
    public int getStrength()
    {
        return strength;
    }
    
    /** 
     * Function to stop Referee during atime 
     * @throws java.rmi.RemoteException RemoteException
     */
    protected void endServers() throws RemoteException
    {
        playground.shutDown();
        bench.shutDown();
        logger.shutDown();
    }
}
