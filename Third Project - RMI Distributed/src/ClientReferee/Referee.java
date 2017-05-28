package ClientReferee;

import ComInf.States;
import ComInf.States.RefereeState;
import ComInf.VectorClock;
import Interfaces.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General description:
 *      Definition of the Referee thread
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Referee extends Thread implements Serializable
{
    /** 
     * Actual state of the Referee
     */
    private RefereeState atualState;
    
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
    *   @param logger shared region to record all states
    *   @param playground shared region where the game occurs
    *   @param bench shared region where referee calls coaches
    *   @param refereeSite shared region where referee prepares the games
    */
    public Referee (RepositoryInterface logger, PlaygroundInterface playground, ContestantsBenchInterface bench, RefereeSiteInterface refereeSite)
    {
        this.logger = logger;
        this.playground = playground;
        this.refereeSite = refereeSite;  
        this.bench = bench;
        this.atualState = States.RefereeState.START_OF_THE_MATCH;
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
            refereeSite.announceNewGame(this);
            while(logger.endOfReferee())
            {
                playground.callTrial(this);
                playground.startTrial(this);
                if(!playground.assertTrialDecision(this))
                    if(refereeSite.declareGameWinner(this))
                        refereeSite.declareMatchWinner(this);
                    else
                        refereeSite.announceNewGame(this);  
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
     *  Function to return the referee state
     * 
     *  @return referee state
     */
    public RefereeState getActualState()
    {
        return atualState;
    }
    
    /**
     *  Function to set the referee state
     *  
     *  @param state referee state
     */
    public void setState(RefereeState state)
    {
        this.atualState = state;
    }
    
    /** 
     * Function to stop Referee during a random time 
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
        try 
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex) 
        {
        }

        playground.shutDown();
        refereeSite.shutDown();
        bench.shutDown();
        logger.shutDown();
    }
    
}
