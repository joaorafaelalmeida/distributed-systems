package ClientEntities;

import ClientStubs.*;
import ComInf.States;
import ComInf.States.ContestantState;


/**
 * General description:
 *      Definition of the Contestant thread
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Contestant extends Thread
{
    /** 
     * Actual state of the contestant
     * @serialField atualState
     */
    private ContestantState atualState;
    
    /** 
     * Id of the contestant 
     * @serialField id
     */
    private final int id;
    
    /** 
     * Id of the coach relative the contestant team
     * @serialField idCoach
     */
    private final int idCoach;
    
    /** 
     * Strength of the contestant
     * @serialField strength
     */
    private int strength;
    
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
     * Application logger
     * @serialField logger
     */
    private final RepositoryStub logger;
    
    /**
    *   Constructor
    * 
    *   @param id id of the contestant
    *   @param idCoach id of the coach
    *   @param logger shared region to record all states
    *   @param playground shared region where the game occurs
    *   @param bench shared region where players rest
    */
    public Contestant (int id, int idCoach, RepositoryStub logger, PlaygroundStub playground, ContestantsBenchStub bench)
    {
        strength = (int)(5 + Math.random ()* 5);
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
        randomTime();
        bench.seatDown();
        while(logger.endOfContestant())
        {
            bench.followCoachAdvice();
            playground.getReady();
            playground.pullTheRope();
            playground.amDone();
            bench.seatDown();
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
}
