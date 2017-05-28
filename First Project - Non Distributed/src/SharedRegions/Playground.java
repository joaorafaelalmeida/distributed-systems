package SharedRegions;

import App.MemFIFO;
import App.Parameters;
import App.Semaphore;
import App.States;
import Entities.*;


/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Playground 
{
    /** Logger */
    private final Repository logger;
    
    /** Counter to identify the last coach */
    private int countCoaches;
    
    /** Counter to identify the last contestant */
    private int countContestants;
    
    /** Memory to store all teams elements */
    private final MemFIFO<Integer>[] teams;
    
    private int[] contestantCounter;
    
            // Synchronization points
    
    /** Access in regime of mutual exclusion */
    private final Semaphore access;
    
    /** Synchronization point for the referee */
    private final Semaphore refereeSem;
    
    /** Synchronization point for each coach */
    private final Semaphore[] coachesSem;
    
    /** Synchronization point for each contestant in the trial */
    private final Semaphore[][] contestantsSem;
    
    public Playground(Repository logger)
    {
        this.logger = logger;
        
        refereeSem = new Semaphore();
        
        coachesSem = new Semaphore[Parameters.numCoaches];
        for (int i = 0; i < Parameters.numCoaches; i++) 
            coachesSem[i] = new Semaphore();
        
        contestantsSem = new Semaphore[Parameters.numCoaches][Parameters.numContestants];
        for (int i = 0; i < Parameters.numCoaches; i++) 
            for (int j = 0; j < Parameters.numContestants; j++)
                contestantsSem[i][j] = new Semaphore();
        
        access = new Semaphore();
        access.up();

        contestantCounter = new int[Parameters.numCoaches];
        teams = new MemFIFO[Parameters.numCoaches];
        for (int i = 0; i < Parameters.numCoaches; i++) 
        {
            contestantCounter[i] = 0;
            teams[i] = new MemFIFO(new Integer[Parameters.numContestantsByTrial]);
        }
        
        countCoaches = 0;
        countContestants = 0;
    }
    
    public void callTrial() 
    {
        ((Referee) Thread.currentThread()).setState(States.RefereeState.TEAMS_READY);
        logger.updateRefereeState(((Referee) Thread.currentThread()).getActualState());
        refereeSem.down();
    }

    public void startTrial() 
    {
        access.down();
        for (int i = 0; i < Parameters.numCoaches; i++) 
            for (int j = 0; j < Parameters.numContestantsByTrial; j++)
                contestantsSem[i][(int)teams[i].read()].up();
        access.up();
            
        ((Referee) Thread.currentThread()).setState(States.RefereeState.WAIT_FOR_TRIAL_CONCLUSION);
        logger.updateRefereeState(((Referee) Thread.currentThread()).getActualState());
        refereeSem.down();
    }

    public boolean assertTrialDecision() 
    {
        boolean ret = logger.assertTrialDecision();
        for (int i = 0; i < Parameters.numCoaches; i++) 
            coachesSem[i].up();
        return ret;
    }

    public void informReferee() 
    {        
        coachesSem[((Coach) Thread.currentThread()).getID()].down();
           
        access.down();
        countCoaches++;
        if(countCoaches == Parameters.numCoaches)
        {   
            countCoaches = 0;
            refereeSem.up();
        }
        access.up();

        ((Coach) Thread.currentThread()).setState(States.CoachState.WATCH_TRIAL);
        logger.updateCoachState(((Coach) Thread.currentThread()).getActualState(), 
                ((Coach) Thread.currentThread()).getID());

        coachesSem[((Coach) Thread.currentThread()).getID()].down(); 
    }

    public void getReady() 
    {
        access.down();
        teams[((Contestant) Thread.currentThread()).getCoachId()].write(((Contestant) Thread.currentThread()).getID());
        
        contestantCounter[((Contestant) Thread.currentThread()).getCoachId()]++;
        if(contestantCounter[((Contestant) Thread.currentThread()).getCoachId()] == Parameters.numContestantsByTrial)
        {
            contestantCounter[((Contestant) Thread.currentThread()).getCoachId()] = 0;
            coachesSem[((Contestant) Thread.currentThread()).getCoachId()].up();
        }
        access.up();
        
        contestantsSem[((Contestant) Thread.currentThread()).getCoachId()][((Contestant) Thread.currentThread()).getID()].down();
        
        ((Contestant) Thread.currentThread()).setState(States.ContestantState.DO_YOUR_BEST);
        logger.updateContestantState(((Contestant) Thread.currentThread()).getActualState(), 
                ((Contestant) Thread.currentThread()).getID(), 
                ((Contestant) Thread.currentThread()).getCoachId(),
                ((Contestant) Thread.currentThread()).getStreght());
    }

    public void pullTheRope() 
    {
        int time = (int)(Math.random () * 100); 
        try 
        {
            Thread.sleep(time);
        }
        catch (InterruptedException ex) 
        {}
    }

    public void amDone() 
    {
        access.up();
        ((Contestant) Thread.currentThread()).decreaseStreght();
        countContestants++;
        if(countContestants == Parameters.numContestantsByTrial*2)
        {
            countContestants = 0;
            refereeSem.up();
        }
        access.down();
    }
}
