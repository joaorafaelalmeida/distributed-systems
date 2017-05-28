package ClientReferee;

import static ComInf.Parameters.*;
import Interfaces.ContestantsBenchInterface;
import Interfaces.PlaygroundInterface;
import Interfaces.RefereeSiteInterface;
import Interfaces.RepositoryInterface;
import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * General description:
 *      Client referee starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeApp 
{
    /**
     *  Main method.
     *
     *  @param args runtime arguments
     *  @throws java.io.FileNotFoundException logger file have problems
     */
    public static void main(String[] args) throws FileNotFoundException
    { 
        System.out.println("Referee client");
        /** Shared regions */
        RepositoryInterface logger = null;
        ContestantsBenchInterface bench = null;
        PlaygroundInterface playground = null;
        RefereeSiteInterface refereeSite = null;
        Registry registry;
        
        try
        { 
            registry = LocateRegistry.getRegistry (serverRepositoryName, serverRepositoryPort);
            logger = (RepositoryInterface) registry.lookup (nameEntryRepository);
            
            registry = LocateRegistry.getRegistry (serverContestantBenchName, serverContestantBenchPort);
            bench = (ContestantsBenchInterface) registry.lookup (nameEntryContestantBench);
            
            registry = LocateRegistry.getRegistry (serverPlaygroundName, serverPlaygroundPort);
            playground = (PlaygroundInterface) registry.lookup (nameEntryPlayground);
            
            registry = LocateRegistry.getRegistry (serverRefereeSiteName, serverRefereeSitePort);
            refereeSite = (RefereeSiteInterface) registry.lookup (nameEntryRefereeSite);
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
        
        /** Threads */
        Referee thReferee;
        
        /** Instantiate Referee thread */
        thReferee = new Referee(logger, playground, bench, refereeSite);
        
        /** Start Referee thread */
        thReferee.start();

        try
        {
            /** Join Referee thread */
            thReferee.join();
        } 
        catch (InterruptedException ex) 
        {
            System.out.println(ex.toString());
        }
        System.out.println("Game over!!!");
    }
}
