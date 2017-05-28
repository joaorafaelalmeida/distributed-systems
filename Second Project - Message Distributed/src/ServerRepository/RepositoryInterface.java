package ServerRepository;

import ComInf.Message;
import ComInf.MessageException;
import ComInf.Parameters;
import ComInf.States.*;

/**
 * General description:
 *      This type of data define the repository interface
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RepositoryInterface 
{
    /**
     * Repository (represent the service)
     * @serialField logger
     */
    private Repository logger;

    /**
     * Constructor of repository interface
     * @param logger repository
     */
    public RepositoryInterface(Repository logger)
    {
        this.logger = logger;
    }

    /**
     * Method to process and reply the messages
     * @param inMessage message received
     * @return Message if everything is ok
     * @throws ComInf.MessageException Message exception
     */
    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;

        /* validating the received message */

        switch (inMessage.getType ())
        {     
            case Message.REQ_UPDATE_REFEREE_STATE:
                if(inMessage.getState() != RefereeState.END_OF_THE_GAME.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.END_OF_THE_MATCH.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.START_OF_THE_GAME.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.START_OF_THE_MATCH.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.TEAMS_READY.getRefereeStateCode() &&
                        inMessage.getState() != RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getRefereeStateCode() )
                    throw new MessageException ("Invalid referee state!", inMessage);
                break;
                
            case Message.REQ_UPDATE_COACH_STATE:
                if(inMessage.getState() != CoachState.ASSEMBLE_TEAM.getCoachStateCode() &&
                        inMessage.getState() != CoachState.WAIT_FOR_REFEREE_COMMAND.getCoachStateCode() &&
                        inMessage.getState() != CoachState.WATCH_TRIAL.getCoachStateCode())
                    throw new MessageException ("Invalid coach state!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                break;
                
            case Message.REQ_UPDATE_CONTESTANT_STATE:
                if(inMessage.getState() != ContestantState.DO_YOUR_BEST.getContestantStateCode() &&
                        inMessage.getState() != ContestantState.SEAT_AT_THE_BENCH.getContestantStateCode() &&
                        inMessage.getState() != ContestantState.STAND_IN_POSITION.getContestantStateCode())
                    throw new MessageException ("Invalid contestant state!", inMessage);
                
                if((inMessage.getCoachId() < 0 || inMessage.getCoachId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numContestants))
                    throw new MessageException ("Invalid contestant id!", inMessage);
                
                if(inMessage.getStrength() < 0)
                    throw new MessageException ("Invalid strength!", inMessage);
                break;
                
            case Message.REQ_END_OF_MATCH:
                if(inMessage.getState() != RefereeState.END_OF_THE_GAME.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.END_OF_THE_MATCH.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.START_OF_THE_GAME.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.START_OF_THE_MATCH.getRefereeStateCode() && 
                        inMessage.getState() != RefereeState.TEAMS_READY.getRefereeStateCode() &&
                        inMessage.getState() != RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getRefereeStateCode() )
                    throw new MessageException ("Invalid referee state!", inMessage);
                break;
                
            case Message.REQ_GET_TEAM:
                if((inMessage.getId() < 0 || inMessage.getId() > Parameters.numCoaches))
                    throw new MessageException ("Invalid coach id!", inMessage);
                break;
        
            case Message.REQ_END_OF_REFEREE:
            case Message.REQ_END_OF_COACH:
            case Message.REQ_END_OF_CONTESTANT:
            case Message.REQ_ASSERT_TRIAL_DECISION_REP:
            case Message.REQ_END_OF_GAME:
            case Message.REQ_SHUTDOWN_SERVER:
                break;
                
            default:               
                throw new MessageException ("Invalid type!", inMessage);
        }

        /* processing */

        switch (inMessage.getType ())
        { 
            case Message.REQ_UPDATE_REFEREE_STATE:
                //Do action
                logger.updateRefereeState(RefereeState.getRefereeStateByCode(inMessage.getState()));
                
                //Create out message
                outMessage = new Message(Message.ACK_UPDATE_REFEREE_STATE);
                break;
                
            case Message.REQ_UPDATE_COACH_STATE:
                //Do action
                logger.updateCoachState(CoachState.getCoachStateByCode(inMessage.getState()), 
                        inMessage.getId());
                
                //Create out message
                outMessage = new Message(Message.ACK_UPDATE_COACH_STATE);
                break;
                
            case Message.REQ_UPDATE_CONTESTANT_STATE:
                //Do action
                logger.updateContestantState(ContestantState.getContestantStateByCode(inMessage.getState()), 
                        inMessage.getId(),
                        inMessage.getCoachId(),
                        inMessage.getStrength());
                
                //Create out message
                outMessage = new Message(Message.ACK_UPDATE_CONTESTANT_STATE);
                break;
                
            case Message.REQ_END_OF_REFEREE:
                //Do action and create out message
                if(logger.endOfReferee())
                    outMessage = new Message(Message.ACK_END_OF_REFEREE_TRUE);
                else
                    outMessage = new Message(Message.ACK_END_OF_REFEREE_FALSE);
                break;
                
            case Message.REQ_END_OF_COACH:
                //Do action and create out message
                if(logger.endOfCoach())
                    outMessage = new Message(Message.ACK_END_OF_COACH_TRUE);
                else
                    outMessage = new Message(Message.ACK_END_OF_COACH_FALSE);
                break;
                
            case Message.REQ_END_OF_CONTESTANT:
                //Do action and create out message
                if(logger.endOfContestant())
                    outMessage = new Message(Message.ACK_END_OF_CONTESTANT_TRUE);
                else
                    outMessage = new Message(Message.ACK_END_OF_CONTESTANT_FALSE);
                break;
                
            case Message.REQ_ASSERT_TRIAL_DECISION_REP:
                //Do action and create out message
                if(logger.assertTrialDecision())
                    outMessage = new Message(Message.ACK_ASSERT_TRIAL_DECISION_REP_TRUE);
                else
                    outMessage = new Message(Message.ACK_ASSERT_TRIAL_DECISION_REP_FALSE);
                break;
                
            case Message.REQ_END_OF_MATCH:
                //Do action
                logger.endOfMatch(RefereeState.getRefereeStateByCode(inMessage.getState()));
                
                //Create out message
                outMessage = new Message(Message.ACK_END_OF_MATCH);
                break;
                
            case Message.REQ_GET_TEAM:
                //Do action
                int [] team = logger.getTeam(inMessage.getId());
                
                //Create out message
                outMessage = new Message(Message.ACK_GET_TEAM, inMessage.getId(), team);
                break;
                
            case Message.REQ_END_OF_GAME:
                //Do action and create out message
                if(logger.endOfGame())
                    outMessage = new Message(Message.ACK_END_OF_GAME_TRUE);
                else
                    outMessage = new Message(Message.ACK_END_OF_GAME_FALSE);
                break;
        }

        return (outMessage);
   }
}
