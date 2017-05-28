package SharedRegions;

import App.Parameters;
import App.Semaphore;
import App.States;
import Entities.Coach;
import Entities.Referee;

/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeSite 
{
    /** Logger */
    private final Repository logger;
    
    /** Synchronization point for each coach */
    private final Semaphore[] coachesSem;
    
    public RefereeSite(Repository logger)
    {
        this.logger = logger;
        
        coachesSem = new Semaphore[Parameters.numCoaches];
        for (int i = 0; i < Parameters.numCoaches; i++) 
            coachesSem[i] = new Semaphore();
    }

    public void announceNewGame() 
    {
        ((Referee) Thread.currentThread()).setState(States.RefereeState.START_OF_THE_GAME);
        logger.updateRefereeState(((Referee) Thread.currentThread()).getActualState());
        
        for (int i = 0; i < Parameters.numCoaches; i++) 
                coachesSem[i].up();
    }

    public boolean declareGameWinner() 
    {
        ((Referee) Thread.currentThread()).setState(States.RefereeState.END_OF_THE_GAME);
        logger.updateRefereeState(((Referee) Thread.currentThread()).getActualState());
        return !logger.endOfGame();
    }

    public void declareMatchWinner() 
    {
        ((Referee) Thread.currentThread()).setState(States.RefereeState.END_OF_THE_MATCH);
        logger.endOfMatch(((Referee) Thread.currentThread()).getActualState());
    }

    public void informArrival() 
    {
        ((Coach) Thread.currentThread()).setState(States.CoachState.WAIT_FOR_REFEREE_COMMAND);
        logger.updateCoachState(((Coach) Thread.currentThread()).getActualState(), 
                ((Coach) Thread.currentThread()).getID());
        coachesSem[((Coach) Thread.currentThread()).getID()].down();
    }
    
}
