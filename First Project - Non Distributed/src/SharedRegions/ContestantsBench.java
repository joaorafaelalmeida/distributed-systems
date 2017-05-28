package SharedRegions;

import App.MemFIFO;
import App.Parameters;
import App.Semaphore;
import App.States;
import Entities.Coach;
import Entities.Contestant;
import Entities.Referee;

/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class ContestantsBench 
{
    /** Logger */
    private final Repository logger;
    
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
    
    private boolean selectedContestant[][];

    
    public ContestantsBench(Repository logger)
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

    public void callContestants() 
    {
        int[] team = logger.getTeam(((Coach) Thread.currentThread()).getID());
        access.down();
        for (int i = 0; i < Parameters.numContestantsByTrial; i++) 
        {
            teams[((Coach) Thread.currentThread()).getID()].write(team[i]);
            selectedContestant[((Coach) Thread.currentThread()).getID()][team[i]] = true;
        }
        access.up();
        for (int i = 0; i < Parameters.numContestants; i++) 
            contestantsSem[((Coach) Thread.currentThread()).getID()][i].up();
                
        ((Coach) Thread.currentThread()).setState(States.CoachState.ASSEMBLE_TEAM);
        logger.updateCoachState(((Coach) Thread.currentThread()).getActualState(), 
                ((Coach) Thread.currentThread()).getID());
        
        coachesSem[((Coach) Thread.currentThread()).getID()].down();
    }

    public void followCoachAdvice() 
    {
        ((Contestant) Thread.currentThread()).setState(States.ContestantState.STAND_IN_POSITION);
        logger.updateContestantState(((Contestant) Thread.currentThread()).getActualState(), 
                ((Contestant) Thread.currentThread()).getID(), 
                ((Contestant) Thread.currentThread()).getCoachId(),
                ((Contestant) Thread.currentThread()).getStreght());
        
        access.down();
        contestantCounter[((Contestant) Thread.currentThread()).getCoachId()]++;
        if(contestantCounter[((Contestant) Thread.currentThread()).getCoachId()] == Parameters.numContestantsByTrial)
        {
            contestantCounter[((Contestant) Thread.currentThread()).getCoachId()] = 0;
            coachesSem[((Contestant) Thread.currentThread()).getCoachId()].up();
        }
        access.up();
    }

    public void seatDown() 
    {
        boolean selected;
        do
        {
            ((Contestant) Thread.currentThread()).setState(States.ContestantState.SEAT_AT_THE_BENCH);
            logger.updateContestantState(((Contestant) Thread.currentThread()).getActualState(), 
                    ((Contestant) Thread.currentThread()).getID(), 
                    ((Contestant) Thread.currentThread()).getCoachId(),
                    ((Contestant) Thread.currentThread()).getStreght());
            ((Contestant) Thread.currentThread()).increaseStreght();
            contestantsSem[((Contestant) Thread.currentThread()).getCoachId()][((Contestant) Thread.currentThread()).getID()].down();        
            access.down();
            selected = selectedContestant[((Contestant) Thread.currentThread()).getCoachId()][((Contestant) Thread.currentThread()).getID()];
            access.up();
        }
        while(!selected);
        selectedContestant[((Contestant) Thread.currentThread()).getCoachId()][((Contestant) Thread.currentThread()).getID()] = false;
    }

    public void reviewNotes() 
    {
        ((Coach) Thread.currentThread()).setState(States.CoachState.WAIT_FOR_REFEREE_COMMAND);
        logger.updateCoachState(((Coach) Thread.currentThread()).getActualState(), 
                ((Coach) Thread.currentThread()).getID());

        access.up();
        if(!logger.endOfCoach())
            for (int i = 0; i < Parameters.numContestants; i++) 
            {
                selectedContestant[((Coach) Thread.currentThread()).getID()][i] = true;
                contestantsSem[((Coach) Thread.currentThread()).getID()][i].up();
            }
        access.down();
            
    }
    
}
