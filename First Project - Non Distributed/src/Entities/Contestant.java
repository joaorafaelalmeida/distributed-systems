package Entities;

import App.Parameters;
import App.States;
import App.States.ContestantState;
import SharedRegions.ContestantsBench;
import SharedRegions.Playground;
import SharedRegions.Repository;

/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Contestant extends Thread
{
    /** Actual state of the Constestant */
    private ContestantState atualState;
    
    /** Id of the Constestant */
    private final int id;
    private final int idCoach;
    
    /** Strength of the Constestant */
    private int strength;
    
    /** Shared region that Coach needs */
    private final Playground playground;
    private final ContestantsBench bench;
    
    /** Aplication logger*/
    private final Repository logger;
    
    public Contestant (int id, int idCoach, Repository logger, Playground playground, ContestantsBench bench)
    {
        strength = Parameters.startStrength;
        this.id = id;
        this.idCoach = idCoach;
        this.logger = logger;
        this.playground = playground;
        this.bench = bench;
        atualState = States.ContestantState.SEAT_AT_THE_BENCH;
    }
    
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
    
    public int getID()
    {
        return id;
    }
    
    public int getCoachId()
    {
        return idCoach;
    }
    
    public ContestantState getActualState()
    {
        return atualState;
    }
    
    public void setState(ContestantState state)
    {
        this.atualState = state;
    }
    
    /** 
     * Function to stop Contestant during a random time 
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

    public void increaseStreght() 
    {
        strength++;
    }
    
    public void decreaseStreght()
    {
        strength--;
    }
    
    public int getStreght()
    {
        return strength;
    }
}
