package ServerPlayground;

import ClientStubs.RepositoryStub;
import ComInf.MemFIFO;
import ComInf.Parameters;
import ComInf.Semaphore;
import ComInf.States.*;


/**
 * General description:
 *      This type of data define the playground, that constitute one of the shared region of this solution
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Playground 
{
    /** Logger */
    private final RepositoryStub logger;
    
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
    
    /**
     * Constructor
     * 
     * @param logger Shared region where the log file is written
     */
    public Playground(RepositoryStub logger)
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
    
    /**
     * Referee prepares the trial to start
     */
    public void callTrial() 
    {
        ((PlaygroundProxy) Thread.currentThread()).setState(RefereeState.TEAMS_READY.getRefereeStateCode());
        logger.updateRefereeState(RefereeState.getRefereeStateByCode(((PlaygroundProxy) Thread.currentThread()).getAtualState()));
        refereeSem.down();
    }

    /**
     * Referee starts the trial
     */
    public void startTrial() 
    {
        access.down();
        for (int i = 0; i < Parameters.numCoaches; i++) 
            for (int j = 0; j < Parameters.numContestantsByTrial; j++)
                contestantsSem[i][(int)teams[i].read()].up();
        access.up();
            
        ((PlaygroundProxy) Thread.currentThread()).setState(RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getRefereeStateCode());
        logger.updateRefereeState(RefereeState.getRefereeStateByCode(((PlaygroundProxy) Thread.currentThread()).getAtualState()));
        refereeSem.down();
    }

    /**
     * Referee assert trial decision
     * 
     * @return true if don't have yet a game winner
     */
    public boolean assertTrialDecision() 
    {
        boolean ret = logger.assertTrialDecision();
        for (int i = 0; i < Parameters.numCoaches; i++) 
            coachesSem[i].up();
        return ret;
    }

    /**
     * Coach inform referee that his team is ready
     */
    public void informReferee() 
    {        
        coachesSem[((PlaygroundProxy) Thread.currentThread()).getID()].down();
           
        access.down();
        countCoaches++;
        if(countCoaches == Parameters.numCoaches)
        {   
            countCoaches = 0;
            refereeSem.up();
        }
        access.up();

        ((PlaygroundProxy) Thread.currentThread()).setState(CoachState.WATCH_TRIAL.getCoachStateCode());
        logger.updateCoachState(CoachState.getCoachStateByCode(((PlaygroundProxy) Thread.currentThread()).getAtualState()), 
                ((PlaygroundProxy) Thread.currentThread()).getID());

        coachesSem[((PlaygroundProxy) Thread.currentThread()).getID()].down(); 
    }

    /**
     *  Contestant is ready to pull the rope
     */
    public void getReady() 
    {
        access.down();
        teams[((PlaygroundProxy) Thread.currentThread()).getCoachId()].write(((PlaygroundProxy) Thread.currentThread()).getID());
        
        contestantCounter[((PlaygroundProxy) Thread.currentThread()).getCoachId()]++;
        if(contestantCounter[((PlaygroundProxy) Thread.currentThread()).getCoachId()] == Parameters.numContestantsByTrial)
        {
            contestantCounter[((PlaygroundProxy) Thread.currentThread()).getCoachId()] = 0;
            coachesSem[((PlaygroundProxy) Thread.currentThread()).getCoachId()].up();
        }
        access.up();
        
        contestantsSem[((PlaygroundProxy) Thread.currentThread()).getCoachId()][((PlaygroundProxy) Thread.currentThread()).getID()].down();
        
        ((PlaygroundProxy) Thread.currentThread()).setState(ContestantState.DO_YOUR_BEST.getContestantStateCode());
        logger.updateContestantState(ContestantState.getContestantStateByCode(((PlaygroundProxy) Thread.currentThread()).getAtualState()), 
                ((PlaygroundProxy) Thread.currentThread()).getID(), 
                ((PlaygroundProxy) Thread.currentThread()).getCoachId(),
                ((PlaygroundProxy) Thread.currentThread()).getStrength());
    }

    /**
     * Contestant pull the rope
     */
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

    /**
     * Contestant finish to pull the rope
     */
    public void amDone() 
    {
        access.up();
        ((PlaygroundProxy) Thread.currentThread()).decreaseStrength();
        countContestants++;
        if(countContestants == Parameters.numContestantsByTrial*2)
        {
            countContestants = 0;
            refereeSem.up();
        }
        access.down();
    }
}
