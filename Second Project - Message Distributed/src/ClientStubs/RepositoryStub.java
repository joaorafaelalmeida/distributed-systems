package ClientStubs;

import ComInf.ClientCom;
import ComInf.Message;
import ComInf.States.*;
import genclass.GenericIO;

/**
 * General description:
 *      This type of data define the contestant beach stub
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RepositoryStub 
{
    /**
     * Computer system name where the server is located 
     * @serialField serverHostName
     */
    private String serverHostName;
    
    /**
     * Port number of the server listens
     * @serialField serverHostName
     */
    private int serverPortNumb;
    
    /**
     * Constructor of the repository stub
     * 
     * @param hostName computer system name
     * @param port port number
     */
    public RepositoryStub(String hostName, int port)
    {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
    }
    
    /**
     * Write in the log an update of referee state
     * 
     * @param state referee state
     */
    public void updateRefereeState(RefereeState state)
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_UPDATE_REFEREE_STATE, 
                state.getRefereeStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_UPDATE_REFEREE_STATE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    
    /**
     * Write in the log an update of coach state
     * 
     * @param state coach state
     * @param id coach id
     */
    public void updateCoachState(CoachState state, int id)
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_UPDATE_COACH_STATE, 
                id,
                state.getCoachStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_UPDATE_COACH_STATE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    
    /**
     * Write in the log an update of contestant state
     * 
     * @param state contestant state
     * @param id contestant id
     * @param idCoach coach id
     * @param strength contestant strength
     */
    public void updateContestantState(ContestantState state, int id, int idCoach, int strength)
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_UPDATE_CONTESTANT_STATE, 
                id,
                idCoach,
                strength,
                state.getContestantStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_UPDATE_CONTESTANT_STATE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    
    /**
     * Indicate the end of the referee
     * 
     * @return true if is the end
     */
    public boolean endOfReferee() 
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_END_OF_REFEREE);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_END_OF_REFEREE_TRUE && inMessage.getType () != Message.ACK_END_OF_REFEREE_FALSE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        if(inMessage.getType() == Message.ACK_END_OF_REFEREE_TRUE)
            return true;
        return false;
    }

    /**
     * Indicate the end of the coach
     * 
     * @return true if is the end
     */
    public boolean endOfCoach()  
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_END_OF_COACH);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_END_OF_COACH_TRUE && inMessage.getType () != Message.ACK_END_OF_COACH_FALSE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        if(inMessage.getType() == Message.ACK_END_OF_COACH_TRUE)
            return true;
        return false;
    }

    /**
     * Indicate the end of the contestant
     * 
     * @return true if is the end
     */
    public boolean endOfContestant()  
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_END_OF_CONTESTANT);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_END_OF_CONTESTANT_TRUE && inMessage.getType () != Message.ACK_END_OF_CONTESTANT_FALSE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        if(inMessage.getType() == Message.ACK_END_OF_CONTESTANT_TRUE)
            return true;
        return false;
    }

    /**
     * Is decided if the game ends or not
     * 
     * @return true if is the end
     */
    public boolean assertTrialDecision() 
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_ASSERT_TRIAL_DECISION_REP);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_ASSERT_TRIAL_DECISION_REP_TRUE && inMessage.getType () != Message.ACK_ASSERT_TRIAL_DECISION_REP_FALSE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        if(inMessage.getType() == Message.ACK_ASSERT_TRIAL_DECISION_REP_TRUE)
            return true;
        return false;
    }

    /**
     * Match is over
     * 
     * @param state referee state
     */
    public void endOfMatch(RefereeState state) 
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_END_OF_MATCH, 
                state.getRefereeStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_END_OF_MATCH)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     * Coach get his team deciding who have more strength
     * 
     * @param id id of the coach
     * @return array with trial elements id's
     */
    public int[] getTeam(int id) 
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_GET_TEAM, id, 0);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_GET_TEAM)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        return inMessage.getTeam();
    }

    /**
     * Indicate the end of the game
     * 
     * @return false if is the end
     */
    public boolean endOfGame() 
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_END_OF_GAME);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_END_OF_GAME_TRUE && inMessage.getType () != Message.ACK_END_OF_GAME_FALSE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        if(inMessage.getType() == Message.ACK_END_OF_GAME_TRUE)
            return true;
        return false;
    }
}
