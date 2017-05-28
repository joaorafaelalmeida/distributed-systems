package ClientMain;

import ClientEntities.Referee;
import ClientStubs.ContestantsBenchStub;
import ClientStubs.PlaygroundStub;
import ClientStubs.RefereeSiteStub;
import ClientStubs.RepositoryStub;
import static ComInf.Parameters.*;
import java.io.FileNotFoundException;

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
        RepositoryStub logger = new RepositoryStub(serverRepositoryName, serverRepositoryPort);
        ContestantsBenchStub bench = new ContestantsBenchStub(serverContestantBenchName, serverContestantBenchPort);
        PlaygroundStub playground = new PlaygroundStub(serverPlaygroundName, serverPlaygroundPort);
        RefereeSiteStub refereeSite = new RefereeSiteStub(serverRefereeSiteName, serverRefereeSitePort);
        
        /** Threads */
        Thread thReferee;
        
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
