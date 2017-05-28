package ServerPlayground;

import ComInf.Message;
import ComInf.MessageException;
import ComInf.Parameters;
import ComInf.ServerCom;
import ComInf.States.*;

/**
 * General description:
 *      This type of data define the playground interface
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class PlaygroundInterface 
{
    /**
     * Playground (represent the service)
     * @serialField playground
     */
    private Playground playground;

    /**
     * Constructor of playground interface
     * @param playground playground
     */
    public PlaygroundInterface(Playground playground)
    {
        this.playground = playground;
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
            case Message.REQ_CALL_TRIAL:
                if(inMessage.getState() != RefereeState.START_OF_THE_GAME.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getRefereeStateCode())
                    throw new MessageException ("Invalid referee state!", inMessage);
                break;
                
            case Message.REQ_START_TRIAL:
                if(inMessage.getState() != RefereeState.TEAMS_READY.getRefereeStateCode())
                    throw new MessageException ("Invalid referee state!", inMessage);
                break;
                
            case Message.REQ_ASSERT_TRIAL_DECISION:
                if(inMessage.getState() != RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getRefereeStateCode())
                    throw new MessageException ("Invalid referee state!", inMessage);
                break;
                
            case Message.REQ_INFORM_REFEREE:
                if(inMessage.getState() != CoachState.ASSEMBLE_TEAM.getCoachStateCode())
                    throw new MessageException ("Invalid coach state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                break;

            case Message.REQ_GET_READY:
                if(inMessage.getState() != ContestantState.STAND_IN_POSITION.getContestantStateCode())
                    throw new MessageException ("Invalid contestant state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numContestants))
                    throw new MessageException ("Invalid contestant id!", inMessage);
                
                if((inMessage.getCoachId() < 0 || inMessage.getCoachId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                
                if(inMessage.getStrength() < 0)
                    throw new MessageException ("Invalid strength!", inMessage);
                break;
                
            case Message.REQ_PULL_THE_ROPE:
                if(inMessage.getState() != ContestantState.DO_YOUR_BEST.getContestantStateCode())
                    throw new MessageException ("Invalid contestant state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numContestants))
                    throw new MessageException ("Invalid contestant id!", inMessage);
                
                if((inMessage.getCoachId() < 0 || inMessage.getCoachId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                
                if(inMessage.getStrength() < 0)
                    throw new MessageException ("Invalid strength!", inMessage);
                break;
                
            case Message.REQ_AM_DONE:
                if(inMessage.getState() != ContestantState.DO_YOUR_BEST.getContestantStateCode())
                    throw new MessageException ("Invalid contestant state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numContestants))
                    throw new MessageException ("Invalid contestant id!", inMessage);
                
                if((inMessage.getCoachId() < 0 || inMessage.getCoachId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                
                if(inMessage.getStrength() < 0)
                    throw new MessageException ("Invalid strength!", inMessage);
                break;
                
            case Message.REQ_SHUTDOWN_SERVER:
                break;
        
            default:               
                throw new MessageException ("Invalid type!", inMessage);
        }

        /* processing */

        switch (inMessage.getType ())
        {          
            case Message.REQ_CALL_TRIAL:
                //Set proxy definitons
                ((PlaygroundProxy) Thread.currentThread()).setState(inMessage.getState());
                
                //Do action
                playground.callTrial();
                
                //Create out message
                outMessage = new Message (Message.ACK_CALL_TRIAL, 
                        ((PlaygroundProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_START_TRIAL:
                //Set proxy definitons
                ((PlaygroundProxy) Thread.currentThread()).setState(inMessage.getState());
                
                //Do action
                playground.startTrial();
                
                //Create out message
                outMessage = new Message (Message.ACK_START_TRIAL, 
                        ((PlaygroundProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_ASSERT_TRIAL_DECISION:
                //Set proxy definitons
                ((PlaygroundProxy) Thread.currentThread()).setState(inMessage.getState());
                
                //Do action and create out message
                if(playground.assertTrialDecision())
                    outMessage = new Message (Message.ACK_ASSERT_TRIAL_DECISION_TRUE, 
                        ((PlaygroundProxy) Thread.currentThread()).getAtualState());
                else
                    outMessage = new Message (Message.ACK_ASSERT_TRIAL_DECISION_FALSE, 
                        ((PlaygroundProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_INFORM_REFEREE:
                //Set proxy definitons
                ((PlaygroundProxy) Thread.currentThread()).setState(inMessage.getState());
                ((PlaygroundProxy) Thread.currentThread()).setID(inMessage.getId());
                
                
                //Do action
                playground.informReferee();
                
                //Create out message
                outMessage = new Message (Message.ACK_INFORM_REFEREE,                        
                        ((PlaygroundProxy) Thread.currentThread()).getID(),
                        ((PlaygroundProxy) Thread.currentThread()).getAtualState());
                break;

            case Message.REQ_GET_READY:
                //Set proxy definitons
                ((PlaygroundProxy) Thread.currentThread()).setState(inMessage.getState());
                ((PlaygroundProxy) Thread.currentThread()).setID(inMessage.getId());
                ((PlaygroundProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((PlaygroundProxy) Thread.currentThread()).setStrength(inMessage.getStrength());
                
                //Do action
                playground.getReady();
                
                //Create out message
                outMessage = new Message (Message.ACK_GET_READY,
                        ((PlaygroundProxy) Thread.currentThread()).getID(),
                        ((PlaygroundProxy) Thread.currentThread()).getCoachId(),
                        ((PlaygroundProxy) Thread.currentThread()).getStrength(),
                        ((PlaygroundProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_PULL_THE_ROPE:
                //Set proxy definitons
                ((PlaygroundProxy) Thread.currentThread()).setState(inMessage.getState());
                ((PlaygroundProxy) Thread.currentThread()).setID(inMessage.getId());
                ((PlaygroundProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((PlaygroundProxy) Thread.currentThread()).setStrength(inMessage.getStrength());
                
                //Do action
                playground.pullTheRope();
                
                //Create out message
                outMessage = new Message (Message.ACK_PULL_THE_ROPE,
                        ((PlaygroundProxy) Thread.currentThread()).getID(),
                        ((PlaygroundProxy) Thread.currentThread()).getCoachId(),
                        ((PlaygroundProxy) Thread.currentThread()).getStrength(),
                        ((PlaygroundProxy) Thread.currentThread()).getAtualState());
                break;
                
            case Message.REQ_AM_DONE:
                //Set proxy definitons
                ((PlaygroundProxy) Thread.currentThread()).setState(inMessage.getState());
                ((PlaygroundProxy) Thread.currentThread()).setID(inMessage.getId());
                ((PlaygroundProxy) Thread.currentThread()).setCoachId(inMessage.getCoachId());
                ((PlaygroundProxy) Thread.currentThread()).setStrength(inMessage.getStrength());
                
                //Do action
                playground.amDone();
                
                //Create out message
                outMessage = new Message (Message.ACK_AM_DONE,
                        ((PlaygroundProxy) Thread.currentThread()).getID(),
                        ((PlaygroundProxy) Thread.currentThread()).getCoachId(),
                        ((PlaygroundProxy) Thread.currentThread()).getStrength(),
                        ((PlaygroundProxy) Thread.currentThread()).getAtualState());
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
