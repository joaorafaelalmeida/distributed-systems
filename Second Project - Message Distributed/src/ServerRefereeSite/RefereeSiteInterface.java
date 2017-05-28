package ServerRefereeSite;

import ComInf.Message;
import ComInf.MessageException;
import ComInf.Parameters;
import ComInf.ServerCom;
import ComInf.States.*;

/**
 * General description:
 *      This type of data define the referee site interface
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeSiteInterface 
{
    /**
     * Referee site (represent the service)
     * @serialField refereeSite
     */
    private RefereeSite refereeSite;

    /**
     * Constructor of referee site interface
     * @param refereeSite referee site
     */
    public RefereeSiteInterface(RefereeSite refereeSite)
    {
        this.refereeSite = refereeSite;
    }

    /**
     * Method to process and reply the messages
     * @param inMessage message received
     * @return Message if everything is ok
     * @throws ComInf.MessageException message exception
     */
    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;

        /* validating the received message */

        switch (inMessage.getType ())
        { 
            case Message.REQ_ANNOUNCE_NEW_GAME:
                if(inMessage.getState() != RefereeState.START_OF_THE_MATCH.getRefereeStateCode() &&
                        inMessage.getState() != RefereeState.END_OF_THE_GAME.getRefereeStateCode())
                    throw new MessageException ("Invalid referee state!", inMessage);
                break;
                
            case Message.REQ_DECLARE_GAME_WINNER:
                if(inMessage.getState() != RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getRefereeStateCode())
                    throw new MessageException ("Invalid referee state!", inMessage);
                break;
                
            case Message.REQ_DECLARE_MATCH_WINNER:
                if(inMessage.getState() != RefereeState.END_OF_THE_GAME.getRefereeStateCode())
                    throw new MessageException ("Invalid referee state!", inMessage);
                break;
                
            case Message.REQ_INFORM_ARRIVAL:
                if(inMessage.getState() != CoachState.WAIT_FOR_REFEREE_COMMAND.getCoachStateCode())
                    throw new MessageException ("Invalid coach state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                break;
                
            case Message.REQ_SHUTDOWN_SERVER:
                break;
        
            default:               
                throw new MessageException ("Invalid type!", inMessage);
        }

        /* processing */

        switch (inMessage.getType ())
        { 
            case Message.REQ_ANNOUNCE_NEW_GAME:
                //Set proxy definitons
                ((RefereeSiteProxy) Thread.currentThread()).setState(inMessage.getState());
                
                //Do action
                refereeSite.announceNewGame();
                
                //Create out message
                outMessage = new Message (Message.ACK_ANNOUNCE_NEW_GAME, 
                        ((RefereeSiteProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_DECLARE_GAME_WINNER:
                //Set proxy definitons
                ((RefereeSiteProxy) Thread.currentThread()).setState(inMessage.getState());
                
                //Do action and create out message
                if(refereeSite.declareGameWinner())
                    outMessage = new Message (Message.ACK_DECLARE_GAME_WINNER_TRUE, 
                            ((RefereeSiteProxy) Thread.currentThread()).getAtualState());
                else
                    outMessage = new Message (Message.ACK_DECLARE_GAME_WINNER_FALSE, 
                            ((RefereeSiteProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_DECLARE_MATCH_WINNER:
                //Set proxy definitons
                ((RefereeSiteProxy) Thread.currentThread()).setState(inMessage.getState());
                
                //Do action
                refereeSite.declareMatchWinner();
                
                //Create out message
                outMessage = new Message (Message.ACK_DECLARE_MATCH_WINNER, 
                        ((RefereeSiteProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_INFORM_ARRIVAL:
                //Set proxy definitons
                ((RefereeSiteProxy) Thread.currentThread()).setState(inMessage.getState());
                ((RefereeSiteProxy) Thread.currentThread()).setID(inMessage.getId());
                
                //Do action
                refereeSite.informArrival();
                
                //Create out message
                outMessage = new Message (Message.ACK_INFORM_ARRIVAL, 
                        ((RefereeSiteProxy) Thread.currentThread()).getAtualState());
                break;
            
            case Message.REQ_SHUTDOWN_SERVER:
                //Do action
                ServerCom.timeActive = true;
                
                //Create out message
                outMessage = new Message (Message.ACK_SHUTDOWN_SERVER);
                break;
        }

        return (outMessage);
   }
}
