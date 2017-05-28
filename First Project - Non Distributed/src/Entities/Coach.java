package Entities;

import App.States;
import App.States.CoachState;
import SharedRegions.*;

/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Coach extends Thread
{
    /** Actual state of the Coach */
    private CoachState atualState;
    
    /** Id of the Coach */
    private final int id;
    
    /** Shared region that Coach needs */
    private final Playground playground;
    private final ContestantsBench bench;
    private final RefereeSite refereeSite;
    
    /** Aplication logger*/
    private final Repository logger;
    
    
    public Coach (int id, Repository logger, Playground playground, ContestantsBench bench, RefereeSite refereeSite)
    {
        this.id = id;
        this.logger = logger;
        this.playground = playground;
        this.bench = bench;
        this.refereeSite = refereeSite; 
        atualState = States.CoachState.WAIT_FOR_REFEREE_COMMAND;
    }
    
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
    
    public int getID()
    {
        return id;
    }
    
    public CoachState getActualState()
    {
        return atualState;
    }
    
    public void setState(CoachState state)
    {
        this.atualState = state;
    }
    
    /** 
     * Function to stop Coach during a random time 
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
