
package ServerContestantsBench;

import ComInf.Parameters;
import static ComInf.Parameters.*;
import Interfaces.ContestantsBenchInterface;
import Interfaces.RepositoryInterface;
import java.net.SocketTimeoutException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General description:
 *      Contestant bench server starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class ContestantsBenchApp 
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

        ContestantsBench bench = new ContestantsBench (logger);
        ContestantsBenchInterface benchInter = null;

        try
        { 
           benchInter = (ContestantsBenchInterface) UnicastRemoteObject.exportObject (bench, Parameters.serverContestantBenchPort+4);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote exception in the stub: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The stub for the contestant bench was generated!");

        /* register RMI service */

        Registry registry = null;

        try
        { 
            registry = LocateRegistry.getRegistry (Parameters.serverContestantBenchName, Parameters.serverContestantBenchPort);
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
            registry.bind (nameEntryContestantBench, benchInter);
        }
        catch (RemoteException e)
        { 
            System.out.println ("Remote exception in the register: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (AlreadyBoundException e)
        { 
            System.out.println("Contestant bench was already registed: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("Contestant bench was registed!");
        
        /* wait for the end */
        try
        { 
            while (!endServer)
            synchronized (Class.forName ("ServerContestantsBench.ContestantsBenchApp"))
            { 
                try
                { 
                    (Class.forName ("ServerContestantsBench.ContestantsBenchApp")).wait ();
                }
                catch (InterruptedException e)
                { 
                    System.out.println("The program was interrupted!");
                }
            }
        }
        catch (ClassNotFoundException e)
        { 
            System.out.println("The data type contestant bench was not found!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        /* shut down */

        try
        { 
            registry.unbind (nameEntryContestantBench);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote exception in the register: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("The contestant bench was not registed: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The contestant bench was unregisted!");
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
            synchronized (Class.forName("ServerContestantsBench.ContestantsBenchApp")) 
            {
                (Class.forName("ServerContestantsBench.ContestantsBenchApp")).notify();
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
