
package ClientCoach;

import static ComInf.Parameters.*;
import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Interfaces.*;

/**
 * General description:
 *      Client coach starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class CoachApp 
{
    /**
     *  Main method.
     *
     *  @param args runtime arguments
     *  @throws java.io.FileNotFoundException logger file have problems
     */
    public static void main(String[] args) throws FileNotFoundException
    {        
        System.out.println("Coach client");
        
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
        Coach[] thCoaches = new Coach[numCoaches];
        
        /** Instantiate Coaches threads */
        for(int i = 0; i < numCoaches; i++)
            thCoaches[i]= new Coach(i, logger, playground, bench, refereeSite);
        
        /** Start Coaches threads */
        for(int i = 0; i < numCoaches; i++)
            thCoaches[i].start();

        try
        {
            /** Join Coaches threads */
            for(int i = 0; i < numCoaches; i++)
                thCoaches[i].join();
        } 
        catch (InterruptedException ex) 
        {
            System.out.println(ex.toString());
        }
        System.out.println("Game over!!!");
    }
}
