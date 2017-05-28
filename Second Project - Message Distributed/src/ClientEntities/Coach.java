package ClientEntities;

import ClientStubs.*;
import ComInf.States;
import ComInf.States.CoachState;

/**
 * General description:
 *      Definition of the Coach thread
 * 
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Coach extends Thread
{
    /** 
     * Actual state of the Coach 
     * @serialField atualState
     */
    private CoachState atualState;
    
    /** 
     * Id of the Coach
     * @serialField id
     */
    private final int id;
    
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
    *   @param id id of the coach
    *   @param logger shared region to record all states
    *   @param playground shared region where the game occurs
    *   @param bench shared region where coach select the players
    *   @param refereeSite shared region where coach inform referee
    */
    public Coach (int id, RepositoryStub logger, PlaygroundStub playground, ContestantsBenchStub bench, RefereeSiteStub refereeSite)
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
        randomTime();
        refereeSite.informArrival();
        while(logger.endOfCoach())
        {
            bench.callContestants();
            playground.informReferee();
            bench.reviewNotes();
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
    
}
