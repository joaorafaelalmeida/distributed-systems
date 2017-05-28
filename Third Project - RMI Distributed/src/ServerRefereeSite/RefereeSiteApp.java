package ServerRefereeSite;

import ComInf.Parameters;
import static ComInf.Parameters.nameEntryRefereeSite;
import static ComInf.Parameters.nameEntryRepository;
import static ComInf.Parameters.serverRepositoryName;
import static ComInf.Parameters.serverRepositoryPort;
import Interfaces.RefereeSiteInterface;
import Interfaces.RepositoryInterface;
import java.net.SocketTimeoutException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * General description:
 *      Referee site server starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeSiteApp 
{    
    /**
     * Variable if true server shutdown
     */
    public static boolean endServer = false;
    
    /**
     * Principal program
     * @param args arguments
     */
    public static void main (String [] args)
    {
        /** Shared regions */
        RepositoryInterface logger = null;
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (serverRepositoryName, serverRepositoryPort);
            logger = (RepositoryInterface) registry.lookup (nameEntryRepository);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception in server location: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Some servers aren't registed: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        
        /* Instantiations */

        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());

        RefereeSite refereeSite = new RefereeSite (logger);
        RefereeSiteInterface refereeSiteInter = null;

        try
        { 
           refereeSiteInter = (RefereeSiteInterface) UnicastRemoteObject.exportObject (refereeSite, Parameters.serverRefereeSitePort+4);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote exception in the stub: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The stub for the  refereeSite was generated!");

        /* register RMI service */

        Registry registry = null;

        try
        { 
            registry = LocateRegistry.getRegistry (Parameters.serverRefereeSiteName, Parameters.serverRefereeSitePort);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote Exception in the RMI creation: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The RMI register was created!");

        try
        { 
            registry.bind (nameEntryRefereeSite, refereeSiteInter);
        }
        catch (RemoteException e)
        { 
            System.out.println ("Remote exception in the register: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (AlreadyBoundException e)
        { 
            System.out.println("Referee site was already registed: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("Referee site was registed!");
        
        /* wait for the end */
        try
        { 
            while (!endServer)
            synchronized (Class.forName ("ServerRefereeSite.RefereeSiteApp"))
            { 
                try
                { 
                    (Class.forName ("ServerRefereeSite.RefereeSiteApp")).wait ();
                }
                catch (InterruptedException e)
                { 
                    System.out.println("The program was interrupted!");
                }
            }
        }
        catch (ClassNotFoundException e)
        { 
            System.out.println("The data type refereeSite was not found!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        /* shut down */

        try
        { 
            registry.unbind (nameEntryRefereeSite);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote exception in the register: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("The refereeSite was not registed: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The refereeSite was unregisted!");
        System.out.println("End of server!");
        System.exit (1);
    }
    
    /**
     * Shut down server
     */
    public static void shutdown() 
    {
        endServer = true;
        try 
        {
            synchronized (Class.forName("ServerRefereeSite.RefereeSiteApp")) 
            {
                (Class.forName("ServerRefereeSite.RefereeSiteApp")).notify();
            }
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println("O tipo de dados ServerSleepingBarbers não foi encontrado (acordar)!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
