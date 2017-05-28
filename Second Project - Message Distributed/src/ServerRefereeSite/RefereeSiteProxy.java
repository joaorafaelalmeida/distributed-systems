package ServerRefereeSite;

import ComInf.Message;
import ComInf.MessageException;
import ComInf.ServerCom;

/**
 * General description:
 *      This type of data define the referee site proxy
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeSiteProxy extends Thread 
{
    /**
     * Proxu number
     * @serialField nProxy
     */
    private static int nProxy = 0;

    /**
     * Server communication
     * @serialField sconi
     */
    private ServerCom sconi;

    /**
     * Referee site interface
     * @serialField refereeSiteInter
     */
    private RefereeSiteInterface refereeSiteInter;

    /**
     * State of the entity
     * @serialField state
     */
    private int state;
    
    /**
     * Id of the coach
     * @serialField id
     */
    private int id;
    
    /**
     * Constructor of the referee site proxy
     * @param sconi server communication
     * @param refereeSiteInter referee site interface
     */
    public RefereeSiteProxy(ServerCom sconi, RefereeSiteInterface refereeSiteInter) 
    {
        super("Proxy_" + RefereeSiteProxy.getProxyId());
        this.sconi = sconi;
        this.refereeSiteInter = refereeSiteInter;
    }

    @Override
    public void run() 
    {
        Object inMessage = null,                                      
               outMessage = null;                           

        inMessage = sconi.readObject ();                     
        try 
        {
            outMessage = refereeSiteInter.processAndReply((Message)inMessage);        
        } 
        catch (MessageException e) 
        {
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                
        sconi.close();                                                
    }
    
    /**
     * Private static method to get the proxy id
     */
    private static int getProxyId ()
    {
        Class<ServerRefereeSite.RefereeSiteProxy> cl = null;             
                                                           
        int proxyId;                                         

        try
        { 
            cl = (Class<ServerRefereeSite.RefereeSiteProxy>) Class.forName ("ServerRefereeSite.RefereeSiteProxy");
        }
        catch (ClassNotFoundException e)
        { 
            System.out.println("The type of RefereeSiteProxy data can not be found!");
            e.printStackTrace ();
            System.exit (1);
        }

        synchronized (cl)
        { 
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }
        
    /**
     * Get the actual state of the proxy
     * @return state in integer
     */
    public int getAtualState()
    {
        return state;
    }
    
    /**
     * Set the actual state of the proxy
     * @param state state in integer
     */
    public void setState(int state)
    {
        this.state = state;
    }
    
    /**
     * Get the id
     * @return id
     */
    public int getID()
    {
        return id;
    }
    
    /**
     * Set the id
     * @param id id of the contestant or coach
     */
    public void setID(int id)
    {
        this.id = id;
    }
}
