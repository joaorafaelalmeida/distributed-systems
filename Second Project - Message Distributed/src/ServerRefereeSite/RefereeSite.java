package ServerRefereeSite;

import ComInf.Parameters;
import ComInf.Semaphore;
import ComInf.States.*;
import ClientStubs.RepositoryStub;

/**
 * General description:
 *      This type of data define the referee site, that constitute one of the shared region of this solution
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeSite 
{
    /** Logger */
    private final RepositoryStub logger;
    
    /** Synchronization point for each coach */
    private final Semaphore[] coachesSem;
    
    /**
     * Constructor
     * 
     * @param logger Shared region where the log file is written
     */
    public RefereeSite(RepositoryStub logger)
    {
        this.logger = logger;
        
        coachesSem = new Semaphore[Parameters.numCoaches];
        for (int i = 0; i < Parameters.numCoaches; i++) 
            coachesSem[i] = new Semaphore();
    }

    /**
     * Referee announce a new game
     */
    public void announceNewGame() 
    {
        ((RefereeSiteProxy) Thread.currentThread()).setState(RefereeState.START_OF_THE_GAME.getRefereeStateCode());
        logger.updateRefereeState(RefereeState.getRefereeStateByCode(((RefereeSiteProxy) Thread.currentThread()).getAtualState()));
        
        for (int i = 0; i < Parameters.numCoaches; i++) 
                coachesSem[i].up();
    }

    /**
     * Referee declare game winner
     * @return true if the match ended
     */
    public boolean declareGameWinner() 
    {
        ((RefereeSiteProxy) Thread.currentThread()).setState(RefereeState.END_OF_THE_GAME.getRefereeStateCode());
        logger.updateRefereeState(RefereeState.getRefereeStateByCode(((RefereeSiteProxy) Thread.currentThread()).getAtualState()));
        return !logger.endOfGame();
    }

    /**
     * Referee declare match winner
     */
    public void declareMatchWinner() 
    {
        ((RefereeSiteProxy) Thread.currentThread()).setState(RefereeState.END_OF_THE_MATCH.getRefereeStateCode());
        logger.endOfMatch(RefereeState.getRefereeStateByCode(((RefereeSiteProxy) Thread.currentThread()).getAtualState()));
    }

    /**
     * Coach inform referee that he is ready
     */
    public void informArrival() 
    {
        ((RefereeSiteProxy) Thread.currentThread()).setState(CoachState.WAIT_FOR_REFEREE_COMMAND.getCoachStateCode());
        logger.updateCoachState(CoachState.getCoachStateByCode(((RefereeSiteProxy) Thread.currentThread()).getAtualState()), 
                ((RefereeSiteProxy) Thread.currentThread()).getID());
        coachesSem[((RefereeSiteProxy) Thread.currentThread()).getID()].down();
    }
    
}
