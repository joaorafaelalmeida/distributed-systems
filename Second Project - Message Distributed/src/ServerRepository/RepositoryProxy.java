package ServerRepository;

import ComInf.Message;
import ComInf.MessageException;
import ComInf.ServerCom;

/**
 * General description:
 *      This type of data define the repository proxy
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RepositoryProxy extends Thread 
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
     * Repository interface
     * @serialField loggerInter
     */
    private RepositoryInterface loggerInter;

    /**
     * State of the entity
     * @serialField state
     */
    private int state;
    
    /**
     * Id of the contestant or coach
     * @serialField id
     */
    private int id;
    
    /**
     * Coach id used to identify the contestant team
     * @serialField coachId
     */
    private int coachId;
    
    /**
     * Strength of the contestant
     * @serialField strength
     */
    private int strength;
    
    /**
     * Constructor of the repository proxy
     * @param sconi server communication
     * @param loggerInter repository interface
     */
    public RepositoryProxy(ServerCom sconi, RepositoryInterface loggerInter) 
    {
        super("Proxy_" + RepositoryProxy.getProxyId());
        this.sconi = sconi;
        this.loggerInter = loggerInter;
    }

    @Override
    public void run() 
    {
        Object inMessage = null,                                      
               outMessage = null;                           

        inMessage = sconi.readObject ();                     
        try 
        {
            outMessage = loggerInter.processAndReply((Message)inMessage);        
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
        Class<ServerRepository.RepositoryProxy> cl = null;             
                                                           
        int proxyId;                                         

        try
        { 
            cl = (Class<ServerRepository.RepositoryProxy>) Class.forName ("ServerRepository.RepositoryProxy");
        }
        catch (ClassNotFoundException e)
        { 
            System.out.println("The type of RepositoryProxy data can not be found!");
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
    
    /**
     * Get the coach id to identify the contestant team
     * @return id of the coach
     */
    public int getCoachId()
    {
        return coachId;
    }
    
    /**
     * Set the coach id to identify the contestant team
     * @param coachId coach id
     */
    public void setCoachId(int coachId)
    {
        this.coachId = coachId;
    }
    
    /**
     * Get the contestant strength
     * @return strength
     */
    public int getStrength()
    {
        return strength;
    }
    
    /**
     * Set the contestant strength
     * @param strength strength
     */
    public void setStrength(int strength)
    {
        this.strength = strength;
    }
    
    /**
     * Increase the contestant strength
     */
    public void increaseStrength()
    {
        strength++;
    }
    
    /**
     * Decrease the contestant strength
     */
    public void decreaseStrength()
    {
        strength--;
    }
}
