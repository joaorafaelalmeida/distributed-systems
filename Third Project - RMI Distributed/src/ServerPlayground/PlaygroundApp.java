package ServerPlayground;


import ComInf.Parameters;
import static ComInf.Parameters.*;
import Interfaces.PlaygroundInterface;
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
 *      Playground server starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class PlaygroundApp 
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

        Playground playground = new Playground (logger);
        PlaygroundInterface playgroundInter = null;

        try
        { 
           playgroundInter = (PlaygroundInterface) UnicastRemoteObject.exportObject (playground, Parameters.serverPlaygroundPort+4);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote exception in the stub: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The stub for the  playground was generated!");

        /* register RMI service */

        Registry registry = null;

        try
        { 
            registry = LocateRegistry.getRegistry (Parameters.serverPlaygroundName, Parameters.serverPlaygroundPort);
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
            registry.bind (nameEntryPlayground, playgroundInter);
        }
        catch (RemoteException e)
        { 
            System.out.println ("Remote exception in the register: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (AlreadyBoundException e)
        { 
            System.out.println("Playground was already registed: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("Playground was registed!");
        
        /* wait for the end */
        try
        { 
            while (!endServer)
            synchronized (Class.forName ("ServerPlayground.PlaygroundApp"))
            { 
                try
                { 
                    (Class.forName ("ServerPlayground.PlaygroundApp")).wait ();
                }
                catch (InterruptedException e)
                { 
                    System.out.println("The program was interrupted!");
                }
            }
        }
        catch (ClassNotFoundException e)
        { 
            System.out.println("The data type playground was not found!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        /* shut down */

        try
        { 
            registry.unbind (nameEntryPlayground);
        }
        catch (RemoteException e)
        { 
            System.out.println("Remote exception in the register: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("The playground was not registed: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.println("The playground was unregisted!");
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
            synchronized (Class.forName("ServerPlayground.PlaygroundApp")) 
            {
                (Class.forName("ServerPlayground.PlaygroundApp")).notify();
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
