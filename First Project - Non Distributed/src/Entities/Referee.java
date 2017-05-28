package Entities;

import App.States;
import App.States.RefereeState;
import SharedRegions.*;

/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Referee extends Thread
{
    /** Actual state of the Referee */
    private RefereeState atualState;
    
    /** Shared region that Referee needs */
    private final Playground playground;
    private final RefereeSite refereeSite;
    private final ContestantsBench bench;
    
    /** Aplication logger*/
    private final Repository logger;
    
    
    public Referee (Repository logger, Playground playground, ContestantsBench bench, RefereeSite refereeSite)
    {
        this.logger = logger;
        this.playground = playground;
        this.refereeSite = refereeSite;  
        this.bench = bench;
        atualState = States.RefereeState.START_OF_THE_MATCH;
    }
    
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

    public RefereeState getActualState()
    {
        return atualState;
    }
    
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
