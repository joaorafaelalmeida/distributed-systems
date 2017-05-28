package ClientStubs;

import ClientEntities.*;
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
public class PlaygroundStub 
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
     * Constructor of the playground stub
     * 
     * @param hostName computer system name
     * @param port port number
     */
    public PlaygroundStub(String hostName, int port)
    {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
    }
    
    /**
     * Referee prepares the trial to start
     */
    public void callTrial() 
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
        
        outMessage = new Message (Message.REQ_CALL_TRIAL, 
                ((Referee) Thread.currentThread()).getActualState().getRefereeStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_CALL_TRIAL)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(RefereeState.getRefereeStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Referee)Thread.currentThread()).setState(RefereeState.getRefereeStateByCode(inMessage.getState()));
    }

    /**
     * Referee starts the trial
     */
    public void startTrial() 
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
        
        outMessage = new Message (Message.REQ_START_TRIAL, 
                ((Referee) Thread.currentThread()).getActualState().getRefereeStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_START_TRIAL)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(RefereeState.getRefereeStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Referee)Thread.currentThread()).setState(RefereeState.getRefereeStateByCode(inMessage.getState()));
    }

    /**
     * Referee assert trial decision
     * 
     * @return true if don't have yet a game winner
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
        
        outMessage = new Message (Message.REQ_ASSERT_TRIAL_DECISION, 
                ((Referee) Thread.currentThread()).getActualState().getRefereeStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_ASSERT_TRIAL_DECISION_FALSE && inMessage.getType () != Message.ACK_ASSERT_TRIAL_DECISION_TRUE )
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(RefereeState.getRefereeStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Referee)Thread.currentThread()).setState(RefereeState.getRefereeStateByCode(inMessage.getState()));
        if(inMessage.getType() == Message.ACK_ASSERT_TRIAL_DECISION_TRUE)
            return true;
        return false;
    }

    /**
     * Coach inform referee that his team is ready
     */
    public void informReferee() 
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
        
        outMessage = new Message (Message.REQ_INFORM_REFEREE, 
                ((Coach) Thread.currentThread()).getID(), 
                ((Coach) Thread.currentThread()).getActualState().getCoachStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_INFORM_REFEREE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(CoachState.getCoachStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Coach)Thread.currentThread()).setState(CoachState.getCoachStateByCode(inMessage.getState()));
    }

    /**
     *  Contestant is ready to pull the rope
     */
    public void getReady() 
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
        
        outMessage = new Message (Message.REQ_GET_READY, 
                ((Contestant) Thread.currentThread()).getID(), 
                ((Contestant) Thread.currentThread()).getCoachId(), 
                ((Contestant) Thread.currentThread()).getStrength(), 
                ((Contestant) Thread.currentThread()).getActualState().getContestantStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_GET_READY)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(ContestantState.getContestantStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Contestant)Thread.currentThread()).setState(ContestantState.getContestantStateByCode(inMessage.getState()));
    }

    /**
     * Contestant pull the rope
     */
    public void pullTheRope() 
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
        
        outMessage = new Message (Message.REQ_PULL_THE_ROPE, 
                ((Contestant) Thread.currentThread()).getID(), 
                ((Contestant) Thread.currentThread()).getCoachId(), 
                ((Contestant) Thread.currentThread()).getStrength(), 
                ((Contestant) Thread.currentThread()).getActualState().getContestantStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_PULL_THE_ROPE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(ContestantState.getContestantStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Contestant)Thread.currentThread()).setState(ContestantState.getContestantStateByCode(inMessage.getState()));
    }

    /**
     * Contestant finish to pull the rope
     */
    public void amDone() 
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
        
        outMessage = new Message (Message.REQ_AM_DONE, 
                ((Contestant) Thread.currentThread()).getID(), 
                ((Contestant) Thread.currentThread()).getCoachId(), 
                ((Contestant) Thread.currentThread()).getStrength(), 
                ((Contestant) Thread.currentThread()).getActualState().getContestantStateCode());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_AM_DONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        
        if(ContestantState.getContestantStateByCode(inMessage.getState()) == null)
        {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        ((Contestant)Thread.currentThread()).setState(ContestantState.getContestantStateByCode(inMessage.getState()));
    }
    
}
