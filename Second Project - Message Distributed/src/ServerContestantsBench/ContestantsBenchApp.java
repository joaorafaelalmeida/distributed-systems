
package ServerContestantsBench;

import ClientStubs.RepositoryStub;
import ComInf.Parameters;
import ComInf.ServerCom;
import java.net.SocketTimeoutException;
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
     * Principal program
     * @param args arguments
     */
    public static void main (String [] args)
    {
        RepositoryStub logger = new RepositoryStub(Parameters.serverRepositoryName, Parameters.serverRepositoryPort);
        ContestantsBench bench = new ContestantsBench(logger); 
        ContestantsBenchInterface benchInter = new ContestantsBenchInterface(bench);
        ServerCom scon, sconi; 
        ContestantsBenchProxy cliProxy;

        /* service establishment */
        scon = new ServerCom (Parameters.serverContestantBenchPort);     
        scon.start ();                                      
        
        /* output */
        System.out.println("Contestants Bench");
        System.out.println("The service was established!");
        System.out.println("The server is listening.");

        /* processing orders */

        while (true)
        { 
            try 
            {
                sconi = scon.accept ();
                cliProxy = new ContestantsBenchProxy (sconi, benchInter);
                cliProxy.start ();
            } 
            catch (SocketTimeoutException ex) 
            {
                break;
            }
        }
        
        System.out.println("End of server!");
   }
}
