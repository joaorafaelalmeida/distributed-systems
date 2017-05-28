package ClientContestant;

import static ComInf.Parameters.*;
import Interfaces.ContestantsBenchInterface;
import Interfaces.PlaygroundInterface;
import Interfaces.RepositoryInterface;
import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * General description:
 *      Client contestant starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class ContestantApp 
{
    /**
     *  Main method.
     *
     *  @param args runtime arguments
     *  @throws java.io.FileNotFoundException logger file have problems
     */
    public static void main(String[] args) throws FileNotFoundException
    {        
        System.out.println("Contestants client");
        /** Shared regions */
        RepositoryInterface logger = null;
        ContestantsBenchInterface bench = null;
        PlaygroundInterface playground = null;
        Registry registry;
        
        try
        { 
            registry = LocateRegistry.getRegistry (serverRepositoryName, serverRepositoryPort);
            logger = (RepositoryInterface) registry.lookup (nameEntryRepository);
            
            registry = LocateRegistry.getRegistry (serverContestantBenchName, serverContestantBenchPort);
            bench = (ContestantsBenchInterface) registry.lookup (nameEntryContestantBench);
            
            registry = LocateRegistry.getRegistry (serverPlaygroundName, serverPlaygroundPort);
            playground = (PlaygroundInterface) registry.lookup (nameEntryPlayground);
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
        Contestant[][] thContestants = new Contestant[numCoaches][numContestants];

        /** Instantiate Contestants threads */
        for(int c = 0; c < numCoaches; c++)
            for(int i = 0; i < numContestants; i++)
                thContestants[c][i]= new Contestant(i, c, logger, playground, bench);

        /** Start Contestants threads */
        for(int c = 0; c < numCoaches; c++)
            for(int i = 0; i < numContestants; i++)
                thContestants[c][i].start();

        try
        {
            /** Join Contestants threads */
            for(int c = 0; c < numCoaches; c++)
                for(int i = 0; i < numContestants; i++)
                    thContestants[c][i].join();
        } 
        catch (InterruptedException ex) 
        {
            System.out.println(ex.toString());
        }
        System.out.println("Game over!!!");
    }
}
