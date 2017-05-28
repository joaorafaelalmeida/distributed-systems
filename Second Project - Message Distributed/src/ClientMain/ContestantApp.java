package ClientMain;

import ClientEntities.Contestant;
import ClientStubs.ContestantsBenchStub;
import ClientStubs.PlaygroundStub;
import ClientStubs.RepositoryStub;
import static ComInf.Parameters.*;
import java.io.FileNotFoundException;

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
        RepositoryStub logger = new RepositoryStub(serverRepositoryName, serverRepositoryPort);
        ContestantsBenchStub bench = new ContestantsBenchStub(serverContestantBenchName, serverContestantBenchPort);
        PlaygroundStub playground = new PlaygroundStub(serverPlaygroundName, serverPlaygroundPort);

        /** Threads */
        Thread[][] thContestants = new Thread[numCoaches][numContestants];
        
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
