package ServerContestantsBench;

import ComInf.Message;
import ComInf.MessageException;
import ComInf.Parameters;
import ComInf.ServerCom;
import ComInf.States.*;

/**
 * General description:
 *      This type of data define the contestant beach interface
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class ContestantsBenchInterface 
{
    /**
     * Contestant bench (represent the service)
     * @serialField bench
     */
    private ContestantsBench bench;

    /**
     * Constructor of contestants bench interface
     * @param bench constants bench
     */
    public ContestantsBenchInterface(ContestantsBench bench)
    {
        this.bench = bench;
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
            case Message.REQ_CALL_CONTESTANTS:
                if(inMessage.getState() != CoachState.WAIT_FOR_REFEREE_COMMAND.getCoachStateCode())
                    throw new MessageException ("Invalid coach state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                break;
        
            case Message.REQ_FOLLOW_COACH_ADVICE:
                if(inMessage.getState() != ContestantState.SEAT_AT_THE_BENCH.getContestantStateCode())
                    throw new MessageException ("Invalid contestant state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numContestants))
                    throw new MessageException ("Invalid contestant id!", inMessage);
                
                if((inMessage.getCoachId() < 0 || inMessage.getCoachId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                
                if(inMessage.getStrength() < 0)
                    throw new MessageException ("Invalid strength!", inMessage);
                break;
        
            case Message.REQ_SEAT_DOWN:
                if(inMessage.getState() != ContestantState.DO_YOUR_BEST.getContestantStateCode() &&
                        inMessage.getState() != ContestantState.SEAT_AT_THE_BENCH.getContestantStateCode())
                    throw new MessageException ("Invalid contestant state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numContestants))
                    throw new MessageException ("Invalid contestant id!", inMessage);
                
                if((inMessage.getCoachId() < 0 || inMessage.getCoachId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                
                if(inMessage.getStrength() < 0)
                    throw new MessageException ("Invalid strength!", inMessage);
                break;
        
            case Message.REQ_REVIEW_NOTES:  
                if(inMessage.getState() != CoachState.WATCH_TRIAL.getCoachStateCode())
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
            case Message.REQ_CALL_CONTESTANTS:
                //Set proxy definitons
                ((ContestantsBenchProxy) Thread.currentThread()).setState(inMessage.getState());
                ((ContestantsBenchProxy) Thread.currentThread()).setID(inMessage.getId());

                //Do action
                bench.callContestants();
                
                //Create out message
                outMessage = new Message (Message.ACK_CALL_CONTESTANTS, 
                        ((ContestantsBenchProxy) Thread.currentThread()).getID(),
                        ((ContestantsBenchProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_FOLLOW_COACH_ADVICE:   
                //Set proxy definitons    
                ((ContestantsBenchProxy) Thread.currentThread()).setState(inMessage.getState());
                ((ContestantsBenchProxy) Thread.currentThread()).setID(inMessage.getId());
                ((ContestantsBenchProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((ContestantsBenchProxy) Thread.currentThread()).setStrength(inMessage.getStrength());
                
                //Do action
                bench.followCoachAdvice();
                
                //Create out message
                outMessage = new Message (Message.ACK_FOLLOW_COACH_ADVICE, 
                        ((ContestantsBenchProxy) Thread.currentThread()).getID(),
                        ((ContestantsBenchProxy) Thread.currentThread()).getCoachId(),
                        ((ContestantsBenchProxy) Thread.currentThread()).getStrength(),
                        ((ContestantsBenchProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_SEAT_DOWN:       
                //Set proxy definitons
                ((ContestantsBenchProxy) Thread.currentThread()).setState(inMessage.getState());
                ((ContestantsBenchProxy) Thread.currentThread()).setID(inMessage.getId());
                ((ContestantsBenchProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((ContestantsBenchProxy) Thread.currentThread()).setStrength(inMessage.getStrength());
                
                //Do action
                bench.seatDown();
                
                //Create out message
                outMessage = new Message (Message.ACK_SEAT_DOWN, 
                        ((ContestantsBenchProxy) Thread.currentThread()).getID(),
                        ((ContestantsBenchProxy) Thread.currentThread()).getCoachId(),
                        ((ContestantsBenchProxy) Thread.currentThread()).getStrength(),
                        ((ContestantsBenchProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_REVIEW_NOTES:     
                //Set proxy definitons                                                
                ((ContestantsBenchProxy) Thread.currentThread()).setState(inMessage.getState());
                ((ContestantsBenchProxy) Thread.currentThread()).setID(inMessage.getId());
                
                //Do action
                bench.reviewNotes();
                
                //Create out message
                outMessage = new Message (Message.ACK_REVIEW_NOTES, 
                        ((ContestantsBenchProxy) Thread.currentThread()).getID(),
                        ((ContestantsBenchProxy) Thread.currentThread()).getAtualState());
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