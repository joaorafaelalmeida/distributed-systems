
package ClientMain;

import ClientEntities.Coach;
import ClientStubs.ContestantsBenchStub;
import ClientStubs.PlaygroundStub;
import ClientStubs.RefereeSiteStub;
import ClientStubs.RepositoryStub;
import static ComInf.Parameters.*;
import java.io.FileNotFoundException;

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
        RepositoryStub logger = new RepositoryStub(serverRepositoryName, serverRepositoryPort);
        ContestantsBenchStub bench = new ContestantsBenchStub(serverContestantBenchName, serverContestantBenchPort);
        PlaygroundStub playground = new PlaygroundStub(serverPlaygroundName, serverPlaygroundPort);
        RefereeSiteStub refereeSite = new RefereeSiteStub(serverRefereeSiteName, serverRefereeSitePort);
        
        /** Threads */
        Thread[] thCoaches = new Thread[numCoaches];
        
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
