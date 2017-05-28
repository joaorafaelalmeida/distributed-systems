package ClientEntities;

import ClientStubs.*;
import ComInf.States;
import ComInf.States.RefereeState;

/**
 * General description:
 *      Definition of the Referee thread
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Referee extends Thread
{
    /** 
     * Actual state of the Referee
     * @serialField atualState
     */
    private RefereeState atualState;
    
    /** 
     * Playground stub
     * @serialField playground
     */
    private final PlaygroundStub playground;
    
    /** 
     * Contestant bench stub
     * @serialField bench
     */
    private final ContestantsBenchStub bench;
    
    /** 
     * Referee site stub
     * @serialField refereeSite
     */
    private final RefereeSiteStub refereeSite;
    
    /** 
     * Application logger
     * @serialField logger
     */
    private final RepositoryStub logger;
    
    /**
    *   Constructor
    *
    *   @param logger shared region to record all states
    *   @param playground shared region where the game occurs
    *   @param bench shared region where referee calls coaches
    *   @param refereeSite shared region where referee prepares the games
    */
    public Referee (RepositoryStub logger, PlaygroundStub playground, ContestantsBenchStub bench, RefereeSiteStub refereeSite)
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
        randomTime();
        refereeSite.announceNewGame();
        while(logger.endOfReferee())
        {
            playground.callTrial();
            playground.startTrial();
            if(!playground.assertTrialDecision())
                if(refereeSite.declareGameWinner())
                    refereeSite.declareMatchWinner();
                else
                    refereeSite.announceNewGame();
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
}
