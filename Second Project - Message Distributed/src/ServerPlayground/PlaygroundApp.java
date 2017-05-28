package ServerPlayground;

import ClientStubs.RepositoryStub;
import ComInf.Parameters;
import ComInf.ServerCom;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General description:
 *      Playground server starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class PlaygroundApp 
{    
    /**
     * Principal program
     * @param args arguments
     */
    public static void main (String [] args)
    {
        RepositoryStub logger = new RepositoryStub(Parameters.serverRepositoryName, Parameters.serverRepositoryPort);
        Playground playground = new Playground(logger); 
        PlaygroundInterface playgroundInter = new PlaygroundInterface(playground);
        ServerCom scon, sconi; 
        PlaygroundProxy cliProxy;

        /* service establishment */
        scon = new ServerCom (Parameters.serverPlaygroundPort);     
        scon.start ();                                      
        
        /* output */
        System.out.println("Playground");
        System.out.println("The service was established!");
        System.out.println("The server is listening.");

        /* processing orders */

        while (true)
        { 
            try 
            {
                sconi = scon.accept ();
                cliProxy = new PlaygroundProxy (sconi, playgroundInter);
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
