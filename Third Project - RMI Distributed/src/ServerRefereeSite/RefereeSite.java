package ServerRefereeSite;

import ClientReferee.Referee;
import ClientCoach.Coach;
import ComInf.Parameters;
import ComInf.Semaphore;
import ComInf.States.*;
import ComInf.VectorClock;
import Interfaces.RefereeSiteInterface;
import Interfaces.RepositoryInterface;
import java.rmi.RemoteException;

/**
 * General description:
 *      This type of data define the referee site, that constitute one of the shared region of this solution
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeSite implements RefereeSiteInterface
{
    /** Logger */
    private final RepositoryInterface logger;
    
    /** Synchronization point for each coach */
    private final Semaphore[] coachesSem;
    
    /** 
     * Access in regime of mutual exclusion 
     * @serialField access
     */
    private final Semaphore access;
    
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
    public RefereeSite(RepositoryInterface logger)
    {
        this.logger = logger;
        clock = new VectorClock(13, 0);
        coachesSem = new Semaphore[Parameters.numCoaches];
        for (int i = 0; i < Parameters.numCoaches; i++) 
            coachesSem[i] = new Semaphore();
        shutDownCount=0;
        access = new Semaphore();
        access.up();
    }

    /**
     * Referee announce a new game
     */
    public void announceNewGame(Referee referee) throws RemoteException 
    {
        VectorClock tmp;
        clock.increment();
        referee.setState(RefereeState.START_OF_THE_GAME);
        tmp = logger.updateRefereeState(clock,  referee.getActualState());
        clock.update(tmp);
        
        for (int i = 0; i < Parameters.numCoaches; i++) 
                coachesSem[i].up();
    }

    /**
     * Referee declare game winner
     * @return true if the match ended
     */
    public boolean declareGameWinner(Referee referee) throws RemoteException 
    {
        VectorClock tmp;
        clock.increment();
        referee.setState(RefereeState.END_OF_THE_GAME);
        tmp = logger.updateRefereeState(clock,  referee.getActualState());
        clock.update(tmp);
        return !logger.endOfGame();
    }

    /**
     * Referee declare match winner
     */
    public void declareMatchWinner(Referee referee) throws RemoteException 
    {
        VectorClock tmp;
        clock.increment();
        referee.setState(RefereeState.END_OF_THE_MATCH);
        tmp = logger.endOfMatch(clock, referee.getActualState());
        clock.update(tmp);
    }

    /**
     * Coach inform referee that he is ready
     */
    public void informArrival(Coach coach) throws RemoteException 
    {
        VectorClock tmp;
        clock.increment();
        coach.setState(CoachState.WAIT_FOR_REFEREE_COMMAND);
        tmp = logger.updateCoachState(clock, coach.getActualState(), 
                coach.getID());
        clock.update(tmp);
        coachesSem[coach.getID()].down();
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
        end = (shutDownCount == 3);
        access.down();
        if(end)
            RefereeSiteApp.shutdown();
    }
    
}
