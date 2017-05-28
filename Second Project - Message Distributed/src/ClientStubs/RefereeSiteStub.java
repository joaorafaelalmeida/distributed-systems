package ClientStubs;

import ClientEntities.Coach;
import ClientEntities.Referee;
import ComInf.ClientCom;
import ComInf.Message;
import ComInf.States;
import genclass.GenericIO;

/**
 * General description:
 *      This type of data define the contestant beach stub
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeSiteStub 
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
     * Constructor of the referee site stub
     * 
     * @param hostName computer system name
     * @param port port number
     */
    public RefereeSiteStub(String hostName, int port)
    {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
    }
    
    /**
     * Referee announce a new game
     */
    public void announceNewGame() 
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
        
        outMessage = new Message (Message.REQ_ANNOUNCE_NEW_GAME, 
                ((Referee) Thread.currentThread()).getActualState().getRefereeStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_ANNOUNCE_NEW_GAME)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(States.RefereeState.getRefereeStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Referee)Thread.currentThread()).setState(States.RefereeState.getRefereeStateByCode(inMessage.getState()));
    }

    /**
     * Referee declare game winner
     * @return true if the match ended
     */
    public boolean declareGameWinner() 
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
        
        outMessage = new Message (Message.REQ_DECLARE_GAME_WINNER, 
                ((Referee) Thread.currentThread()).getActualState().getRefereeStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_DECLARE_GAME_WINNER_FALSE && inMessage.getType () != Message.ACK_DECLARE_GAME_WINNER_TRUE )
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(States.RefereeState.getRefereeStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Referee)Thread.currentThread()).setState(States.RefereeState.getRefereeStateByCode(inMessage.getState()));
        if(inMessage.getType() == Message.ACK_DECLARE_GAME_WINNER_TRUE)
            return true;
        return false;
    }

    /**
     * Referee declare match winner
     */
    public void declareMatchWinner() 
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
        
        outMessage = new Message (Message.REQ_DECLARE_MATCH_WINNER, 
                ((Referee) Thread.currentThread()).getActualState().getRefereeStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_DECLARE_MATCH_WINNER)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(States.RefereeState.getRefereeStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Referee)Thread.currentThread()).setState(States.RefereeState.getRefereeStateByCode(inMessage.getState()));
    }

    /**
     * Coach inform referee that he is ready
     */
    public void informArrival() 
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
        
        outMessage = new Message (Message.REQ_INFORM_ARRIVAL, 
                ((Coach) Thread.currentThread()).getID(), 
                ((Coach) Thread.currentThread()).getActualState().getCoachStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_INFORM_ARRIVAL)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(States.CoachState.getCoachStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Coach)Thread.currentThread()).setState(States.CoachState.getCoachStateByCode(inMessage.getState()));
    }
}
