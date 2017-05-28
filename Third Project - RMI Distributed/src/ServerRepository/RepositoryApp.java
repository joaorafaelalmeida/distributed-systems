package ServerRepository;


import ComInf.Parameters;
import static ComInf.Parameters.nameEntryRepository;
import static ComInf.Parameters.nameEntryRepository;
import static ComInf.Parameters.serverRepositoryName;
import static ComInf.Parameters.serverRepositoryPort;
import Interfaces.RepositoryInterface;
import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General description:
 *      Repository server starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RepositoryApp 
{    
    /**
     * Variable if true server shutdown
     */
    public static boolean endServer = false;
    
    /**
     * Principal program
     * @param args arguments
     * @throws java.io.FileNotFoundException file not found exception
     */
    public static void main (String [] args) throws FileNotFoundException
    {        
        /* Instantiations */

        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());

        Repository logger = new Repository ();
        RepositoryInterface loggerInter = null;

        try
        { 
           loggerInter = (RepositoryInterface) UnicastRemoteObject.exportObject (logger, Parameters.serverRepositoryPort+4);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote exception in the stub: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The stub for the  logger was generated!");

        /* register RMI service */

        Registry registry = null;

        try
        { 
            registry = LocateRegistry.getRegistry (Parameters.serverRepositoryName, Parameters.serverRepositoryPort);
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
            registry.bind (nameEntryRepository, loggerInter);
        }
        catch (RemoteException e)
        { 
            System.out.println ("Remote exception in the register: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (AlreadyBoundException e)
        { 
            System.out.println("Repository was already registed: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("Repository was registed!");
        
        /* wait for the end */
        try
        { 
            while (!endServer)
            synchronized (Class.forName ("ServerRepository.RepositoryApp"))
            { 
                try
                { 
                    (Class.forName ("ServerRepository.RepositoryApp")).wait ();
                }
                catch (InterruptedException e)
                { 
                    System.out.println("The program was interrupted!");
                }
            }
        }
        catch (ClassNotFoundException e)
        { 
            System.out.println("The data type logger was not found!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        /* shut down */

        try
        { 
            registry.unbind (nameEntryRepository);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote exception in the register: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("The logger was not registed: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The logger was unregisted!");
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
            synchronized (Class.forName("ServerRepository.RepositoryApp")) 
            {
                (Class.forName("ServerRepository.RepositoryApp")).notify();
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
