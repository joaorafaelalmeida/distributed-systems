package ClientStubs;

import ClientEntities.Coach;
import ClientEntities.Contestant;
import ComInf.ClientCom;
import ComInf.Message;
import ComInf.Parameters;
import ComInf.States;
import genclass.GenericIO;

/**
 * General description:
 *      This type of data define the contestant beach stub
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class ContestantsBenchStub 
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
     * Constructor of the constant bench stub
     * 
     * @param hostName computer system name
     * @param port port number
     */
    public ContestantsBenchStub(String hostName, int port)
    {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
    }
    
    /**
     * Coach select contestants to play in the trial
     */
    public void callContestants() 
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
        
        outMessage = new Message (Message.REQ_CALL_CONTESTANTS, 
                ((Coach) Thread.currentThread()).getID(), 
                ((Coach) Thread.currentThread()).getActualState().getCoachStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_CALL_CONTESTANTS)
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

    /**
     * Contestant prepares to go to the playground to pull the rope
     */
    public void followCoachAdvice() 
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
        
        outMessage = new Message (Message.REQ_FOLLOW_COACH_ADVICE, 
                ((Contestant) Thread.currentThread()).getID(), 
                ((Contestant) Thread.currentThread()).getCoachId(), 
                ((Contestant) Thread.currentThread()).getStrength(), 
                ((Contestant) Thread.currentThread()).getActualState().getContestantStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_FOLLOW_COACH_ADVICE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(States.ContestantState.getContestantStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Contestant)Thread.currentThread()).setState(States.ContestantState.getContestantStateByCode(inMessage.getState()));
    }

    /**
     * Contestant seat down and rest
     */
    public void seatDown() 
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
        
        outMessage = new Message (Message.REQ_SEAT_DOWN, 
                ((Contestant) Thread.currentThread()).getID(), 
                ((Contestant) Thread.currentThread()).getCoachId(), 
                ((Contestant) Thread.currentThread()).getStrength(), 
                ((Contestant) Thread.currentThread()).getActualState().getContestantStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_SEAT_DOWN)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(States.ContestantState.getContestantStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Contestant)Thread.currentThread()).setState(States.ContestantState.getContestantStateByCode(inMessage.getState()));
    }

    /**
     * Coach review notes
     */
    public void reviewNotes() 
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
        
        outMessage = new Message (Message.REQ_REVIEW_NOTES, 
                ((Coach) Thread.currentThread()).getID(), 
                ((Coach) Thread.currentThread()).getActualState().getCoachStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_REVIEW_NOTES)
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
