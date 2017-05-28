package App;

import Entities.*;
import SharedRegions.*;
import java.io.FileNotFoundException;

/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class GameRope 
{
    public static void main(String[] args) throws FileNotFoundException
    {        
        /** Shared regions */
        Repository logger = new Repository();
        ContestantsBench bench = new ContestantsBench(logger);
        Playground playground = new Playground(logger);
        RefereeSite refereeSite = new RefereeSite(logger);
        
        /** Threads */
        Thread[][] thContestants = new Thread[Parameters.numCoaches][Parameters.numContestants];
        Thread[] thCoaches = new Thread[Parameters.numCoaches];
        Thread thReferee;
        
        /** Instantiate Referee thread */
        thReferee = new Referee(logger, playground, bench, refereeSite);
        
        /** Instantiate Contestants threads */
        for(int c = 0; c < Parameters.numCoaches; c++)
            for(int i = 0; i < Parameters.numContestants; i++)
                thContestants[c][i]= new Contestant(i, c, logger, playground, bench);
        
        /** Instantiate Coaches threads */
        for(int i = 0; i < Parameters.numCoaches; i++)
            thCoaches[i]= new Coach(i, logger, playground, bench, refereeSite);
        
        /** Start Referee thread */
        thReferee.start();
        
        /** Start Contestants threads */
        for(int c = 0; c < Parameters.numCoaches; c++)
            for(int i = 0; i < Parameters.numContestants; i++)
                thContestants[c][i].start();
        
        /** Start Coaches threads */
        for(int i = 0; i < Parameters.numCoaches; i++)
            thCoaches[i].start();

        try
        {
            /** Join Referee thread */
            thReferee.join();
            
            /** Join Contestants threads */
            for(int c = 0; c < Parameters.numCoaches; c++)
                for(int i = 0; i < Parameters.numContestants; i++)
                    thContestants[c][i].join();

            /** Join Coaches threads */
            for(int i = 0; i < Parameters.numCoaches; i++)
                thCoaches[i].join();
        } 
        catch (InterruptedException ex) 
        {
            System.out.println(ex.toString());
        }
        System.out.println("Game over!!!");
    }
    
}
