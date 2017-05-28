package ServerContestantsBench;

import ClientContestant.Contestant;
import ClientCoach.Coach;
import ComInf.MemFIFO;
import ComInf.Parameters;
import ComInf.Semaphore;
import Interfaces.RepositoryInterface;
import ComInf.States.*;
import ComInf.VectorClock;
import Interfaces.ContestantsBenchInterface;
import java.rmi.RemoteException;

/**
 * General description:
 *      This type of data define the contestant bench, that constitute one of the shared region of this solution
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class ContestantsBench implements ContestantsBenchInterface
{
    /** 
     * Logger 
     * @serialField logger
     */
    private final RepositoryInterface logger;
    
    /** 
     * Memory to store all teams elements
     * @serialField teams
     */
    private final MemFIFO<Integer>[] teams;
    
    /**
     * Contestant counter
     * @serialField contestantCounter
     */
    private int[] contestantCounter;
    
            // Synchronization points
    
    /** 
     * Access in regime of mutual exclusion 
     * @serialField access
     */
    private final Semaphore access;
    
    /** 
     * Synchronization point for the referee 
     * @serialField refereeSem
     */
    private final Semaphore refereeSem;
    
    /** 
     * Synchronization point for each coach 
     * @serialField coachesSem
     */
    private final Semaphore[] coachesSem;
    
    /** 
     * Synchronization point for each contestant in the trial 
     * @serialField contestantsSem
     */
    private final Semaphore[][] contestantsSem;
    
    /**
     * Selected contestant to play
     * @serialField selectedContestant
     */
    private boolean selectedContestant[][];

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
    public ContestantsBench(RepositoryInterface logger)
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
        selectedContestant = new boolean[Parameters.numCoaches][Parameters.numContestants];
        for (int i = 0; i < Parameters.numCoaches; i++) 
            for (int j = 0; j < Parameters.numContestants; j++)
                selectedContestant[i][j] = false;
        shutDownCount=0;
    }

    /**
     * Coach select contestants to play in the trial
     */
    public void callContestants(Coach coach) throws RemoteException 
    {
        VectorClock tmp;
        clock.increment();
        int[] team = logger.getTeam(coach.getID());
        access.down();

        for (int i = 0; i < Parameters.numContestantsByTrial; i++) 
        {
            teams[coach.getID()].write(team[i]);
            selectedContestant[coach.getID()][team[i]] = true;
        }
        access.up();
        for (int i = 0; i < Parameters.numContestants; i++) 
            contestantsSem[coach.getID()][i].up();
                
        coach.setState(CoachState.ASSEMBLE_TEAM);
        tmp = logger.updateCoachState(clock, coach.getActualState(), 
                coach.getID());
        clock.update(tmp);
        
        coachesSem[coach.getID()].down();
    }

    /**
     * Contestant prepares to go to the playground to pull the rope
     */
    public void followCoachAdvice(Contestant contestant) throws RemoteException 
    {
        VectorClock tmp;
        clock.increment();
        contestant.setState(ContestantState.STAND_IN_POSITION);
        tmp = logger.updateContestantState(clock, contestant.getActualState(), 
                contestant.getID(), 
                contestant.getCoachId(),
                contestant.getStrength());
        clock.update(tmp);
        access.down();
        contestantCounter[contestant.getCoachId()]++;
        if(contestantCounter[contestant.getCoachId()] == Parameters.numContestantsByTrial)
        {
            contestantCounter[contestant.getCoachId()] = 0;
            coachesSem[contestant.getCoachId()].up();
        }
        access.up();
    }

    /**
     * Contestant seat down and rest
     */
    public void seatDown(Contestant contestant) throws RemoteException 
    {
        boolean selected;
        access.down();
        VectorClock tmp;
        clock.increment();
        access.up();
        do
        {
            contestant.setState(ContestantState.SEAT_AT_THE_BENCH);
            contestant.increaseStrength();
            tmp = logger.updateContestantState(clock, contestant.getActualState(), 
                    contestant.getID(),
                    contestant.getCoachId(),
                    contestant.getStrength());
            clock.update(tmp);
            contestantsSem[contestant.getCoachId()][contestant.getID()].down();        
            access.down();
            selected = selectedContestant[contestant.getCoachId()][contestant.getID()];
            access.up();
        }
        while(!selected);
        selectedContestant[contestant.getCoachId()][contestant.getID()] = false;
    }

    /**
     * Coach review notes
     */
    public void reviewNotes(Coach coach) throws RemoteException 
    {
        VectorClock tmp;
        clock.increment();
        coach.setState(CoachState.WAIT_FOR_REFEREE_COMMAND);
        tmp = logger.updateCoachState(clock, coach.getActualState(), 
                coach.getID());
        clock.update(tmp);
        access.down();
        
        if(!logger.endOfCoach())
            for (int i = 0; i < Parameters.numContestants; i++) 
            {
                selectedContestant[coach.getID()][i] = true;
                contestantsSem[coach.getID()][i].up();
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
            ContestantsBenchApp.shutdown();
    }
    
}
