package ServerContestantsBench;

import ComInf.MemFIFO;
import ComInf.Parameters;
import ComInf.Semaphore;
import ClientStubs.RepositoryStub;
import ComInf.States.*;

/**
 * General description:
 *      This type of data define the contestant bench, that constitute one of the shared region of this solution
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class ContestantsBench 
{
    /** 
     * Logger 
     * @serialField logger
     */
    private final RepositoryStub logger;
    
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

    /**
     * Constructor
     * 
     * @param logger Shared region where the log file is written
     */
    public ContestantsBench(RepositoryStub logger)
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
        
        selectedContestant = new boolean[Parameters.numCoaches][Parameters.numContestants];
        for (int i = 0; i < Parameters.numCoaches; i++) 
            for (int j = 0; j < Parameters.numContestants; j++)
                selectedContestant[i][j] = false;
    }

    /**
     * Coach select contestants to play in the trial
     */
    public void callContestants() 
    {
        int[] team = logger.getTeam(((ContestantsBenchProxy) Thread.currentThread()).getID());
        access.down();
        for (int i = 0; i < Parameters.numContestantsByTrial; i++) 
        {
            teams[((ContestantsBenchProxy) Thread.currentThread()).getID()].write(team[i]);
            selectedContestant[((ContestantsBenchProxy) Thread.currentThread()).getID()][team[i]] = true;
        }
        access.up();
        for (int i = 0; i < Parameters.numContestants; i++) 
            contestantsSem[((ContestantsBenchProxy) Thread.currentThread()).getID()][i].up();
                
        ((ContestantsBenchProxy) Thread.currentThread()).setState(CoachState.ASSEMBLE_TEAM.getCoachStateCode());
        logger.updateCoachState(CoachState.getCoachStateByCode(((ContestantsBenchProxy) Thread.currentThread()).getAtualState()), 
                ((ContestantsBenchProxy) Thread.currentThread()).getID());
        
        coachesSem[((ContestantsBenchProxy) Thread.currentThread()).getID()].down();
    }

    /**
     * Contestant prepares to go to the playground to pull the rope
     */
    public void followCoachAdvice() 
    {
        ((ContestantsBenchProxy) Thread.currentThread()).setState(ContestantState.STAND_IN_POSITION.getContestantStateCode());
        logger.updateContestantState(ContestantState.getContestantStateByCode(((ContestantsBenchProxy) Thread.currentThread()).getAtualState()), 
                ((ContestantsBenchProxy) Thread.currentThread()).getID(), 
                ((ContestantsBenchProxy) Thread.currentThread()).getCoachId(),
                ((ContestantsBenchProxy) Thread.currentThread()).getStrength());
        
        access.down();
        contestantCounter[((ContestantsBenchProxy) Thread.currentThread()).getCoachId()]++;
        if(contestantCounter[((ContestantsBenchProxy) Thread.currentThread()).getCoachId()] == Parameters.numContestantsByTrial)
        {
            contestantCounter[((ContestantsBenchProxy) Thread.currentThread()).getCoachId()] = 0;
            coachesSem[((ContestantsBenchProxy) Thread.currentThread()).getCoachId()].up();
        }
        access.up();
    }

    /**
     * Contestant seat down and rest
     */
    public void seatDown() 
    {
        boolean selected;
        do
        {
            ((ContestantsBenchProxy) Thread.currentThread()).setState(ContestantState.SEAT_AT_THE_BENCH.getContestantStateCode());
            logger.updateContestantState(ContestantState.getContestantStateByCode(((ContestantsBenchProxy) Thread.currentThread()).getAtualState()), 
                    ((ContestantsBenchProxy) Thread.currentThread()).getID(),
                    ((ContestantsBenchProxy) Thread.currentThread()).getCoachId(),
                    ((ContestantsBenchProxy) Thread.currentThread()).getStrength());
            ((ContestantsBenchProxy) Thread.currentThread()).increaseStrength();
            contestantsSem[((ContestantsBenchProxy) Thread.currentThread()).getCoachId()][((ContestantsBenchProxy) Thread.currentThread()).getID()].down();        
            access.down();
            selected = selectedContestant[((ContestantsBenchProxy) Thread.currentThread()).getCoachId()][((ContestantsBenchProxy) Thread.currentThread()).getID()];
            access.up();
        }
        while(!selected);
        selectedContestant[((ContestantsBenchProxy) Thread.currentThread()).getCoachId()][((ContestantsBenchProxy) Thread.currentThread()).getID()] = false;
    }

    /**
     * Coach review notes
     */
    public void reviewNotes() 
    {
        ((ContestantsBenchProxy) Thread.currentThread()).setState(CoachState.WAIT_FOR_REFEREE_COMMAND.getCoachStateCode());
        logger.updateCoachState(CoachState.getCoachStateByCode(((ContestantsBenchProxy) Thread.currentThread()).getAtualState()), 
                ((ContestantsBenchProxy) Thread.currentThread()).getID());

        access.up();
        if(!logger.endOfCoach())
            for (int i = 0; i < Parameters.numContestants; i++) 
            {
                selectedContestant[((ContestantsBenchProxy) Thread.currentThread()).getID()][i] = true;
                contestantsSem[((ContestantsBenchProxy) Thread.currentThread()).getID()][i].up();
            }
        access.down();
            
    }
    
}
