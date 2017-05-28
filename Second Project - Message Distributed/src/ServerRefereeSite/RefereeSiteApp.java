package ServerRefereeSite;

import ClientStubs.RepositoryStub;
import ComInf.Parameters;
import ComInf.ServerCom;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General description:
 *      Referee site server starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RefereeSiteApp 
{    
    /**
     * Principal program
     * @param args arguments
     */
    public static void main (String [] args)
    {
        RepositoryStub logger = new RepositoryStub(Parameters.serverRepositoryName, Parameters.serverRepositoryPort);
        RefereeSite refereeSite = new RefereeSite(logger); 
        RefereeSiteInterface refereeSiteInter = new RefereeSiteInterface(refereeSite);
        ServerCom scon, sconi; 
        RefereeSiteProxy cliProxy;

        /* service establishment */
        scon = new ServerCom (Parameters.serverRefereeSitePort);     
        scon.start ();                                      
        
        /* output */
        System.out.println("Referee Site");
        System.out.println("The service was established!");
        System.out.println("The server is listening.");

        /* processing orders */

        while (true)
        { 
            try 
            {
                sconi = scon.accept ();
                cliProxy = new RefereeSiteProxy (sconi, refereeSiteInter);
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
