package ServerPlayground;

import ClientReferee.Referee;
import ClientContestant.Contestant;
import ClientCoach.Coach;
import ComInf.MemFIFO;
import Interfaces.RepositoryInterface;
import ComInf.Parameters;
import ComInf.Semaphore;
import ComInf.States.*;
import ComInf.VectorClock;
import Interfaces.PlaygroundInterface;
import java.rmi.RemoteException;


/**
 * General description:
 *      This type of data define the playground, that constitute one of the shared region of this solution
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Playground implements PlaygroundInterface
{
    /** Logger */
    private final RepositoryInterface logger;
    
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
    
    private final VectorClock clock;
    
    /**
     * Count to shut down server
     * @serialField shutDownCount
     */
    private int shutDownCount;
    
    /**
     * Constructor
     * 
     * @param logger Shared region where the log file is written
     */
    public Playground(RepositoryInterface logger)
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
        clock = new VectorClock(13, 0);
        countCoaches = 0;
        countContestants = 0;
        shutDownCount=0;
    }
    
    /**
     * Referee prepares the trial to start
     */
    public void callTrial(Referee referee) throws RemoteException 
    {
        VectorClock tmp;
        clock.increment();
        referee.setState(RefereeState.TEAMS_READY);
        tmp = logger.updateRefereeState(clock, referee.getActualState());
        clock.update(tmp);
        refereeSem.down();
    }

    /**
     * Referee starts the trial
     */
    public void startTrial(Referee referee) throws RemoteException  
    {
        access.down();
        VectorClock tmp;
        clock.increment();
        for (int i = 0; i < Parameters.numCoaches; i++) 
            for (int j = 0; j < Parameters.numContestantsByTrial; j++)
                contestantsSem[i][(int)teams[i].read()].up();
        access.up();
            
        referee.setState(RefereeState.WAIT_FOR_TRIAL_CONCLUSION);
        tmp = logger.updateRefereeState(clock, referee.getActualState());
        clock.update(tmp);
        refereeSem.down();
    }

    /**
     * Referee assert trial decision
     * 
     * @return true if don't have yet a game winner
     */
    public boolean assertTrialDecision(Referee referee) throws RemoteException  
    {
        VectorClock tmp;
        clock.increment();
        for (int i = 0; i < Parameters.numCoaches; i++) 
            coachesSem[i].up();
        return logger.assertTrialDecision();
    }

    /**
     * Coach inform referee that his team is ready
     */
    public void informReferee(Coach coach) throws RemoteException  
    {    
        coachesSem[coach.getID()].down();
           
        access.down();
        VectorClock tmp;
        clock.increment();
        countCoaches++;
        if(countCoaches == Parameters.numCoaches)
        {   
            countCoaches = 0;
            refereeSem.up();
        }
        access.up();

        coach.setState(CoachState.WATCH_TRIAL);
        tmp = logger.updateCoachState(clock, coach.getActualState(), 
                coach.getID());
        clock.update(tmp);
        coachesSem[coach.getID()].down(); 
    }

    /**
     *  Contestant is ready to pull the rope
     */
    public void getReady(Contestant contestant) throws RemoteException  
    {
        access.down();
        VectorClock tmp;
        clock.increment();
        teams[contestant.getCoachId()].write(contestant.getID());
        
        contestantCounter[contestant.getCoachId()]++;
        if(contestantCounter[contestant.getCoachId()] == Parameters.numContestantsByTrial)
        {
            contestantCounter[contestant.getCoachId()] = 0;
            coachesSem[contestant.getCoachId()].up();
        }
        access.up();
        
        contestantsSem[contestant.getCoachId()][contestant.getID()].down();
        
        contestant.setState(ContestantState.DO_YOUR_BEST);
        tmp = logger.updateContestantState(clock, contestant.getActualState(), 
                contestant.getID(), 
                contestant.getCoachId(),
                contestant.getStrength());
        clock.update(tmp);
    }

    /**
     * Contestant pull the rope
     */
    public void pullTheRope(Contestant contestant) throws RemoteException  
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
    public void amDone(Contestant contestant) throws RemoteException  
    {
        access.down();
        VectorClock tmp;
        clock.increment();
        contestant.decreaseStrength();
        countContestants++;
        if(countContestants == Parameters.numContestantsByTrial*2)
        {
            countContestants = 0;
            refereeSem.up();
        }
        access.up();
    }
    
    /**
     * Shut down server
     */
    @Override
    public void shutDown() throws RemoteException 
    {
        boolean end = false;
        access.up();
        shutDownCount++;
        end = (shutDownCount == 13);
        access.down();
        if(end)
            PlaygroundApp.shutdown();
    }
}
