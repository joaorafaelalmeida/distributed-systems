package ServerRepository;

import ClientStubs.RepositoryStub;
import ComInf.ClientCom;
import ComInf.Message;
import ComInf.Parameters;
import ComInf.ServerCom;
import genclass.GenericIO;
import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General description:
 *      Repository server starter - semaphore-based solution.
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class RepositoryApp 
{    
    /**
     * Principal program
     * @param args arguments
     * @throws java.io.FileNotFoundException file not found exception
     */
    public static void main (String [] args) throws FileNotFoundException
    {
        Repository logger = new Repository(); 
        RepositoryInterface loggerInter = new RepositoryInterface(logger);
        ServerCom scon, sconi; 
        RepositoryProxy cliProxy;

        /* service establishment */
        scon = new ServerCom (Parameters.serverRepositoryPort);     
        scon.start ();                                      
        
        /* output */
        System.out.println("Repository");
        System.out.println("The service was established!");
        System.out.println("The server is listening.");

        /* processing orders */

        while (true)
        { 
            try 
            {
                sconi = scon.accept ();
                cliProxy = new RepositoryProxy (sconi, loggerInter);
                cliProxy.start ();
            } 
            catch (SocketTimeoutException ex) 
            {
                break;
            }
            
        }
        
        for (int i = 0; i < 2; i++) 
        {
            shutDownServers(Parameters.serverContestantBenchName, Parameters.serverContestantBenchPort);
            shutDownServers(Parameters.serverRefereeSiteName, Parameters.serverRefereeSitePort);
            shutDownServers(Parameters.serverPlaygroundName, Parameters.serverPlaygroundPort);
            try
            { 
                Thread.sleep ((long) (500));
            }
            catch (InterruptedException e) {}
        }
        System.out.println("End of server!");
    }
    
    /**
     * Static method to send a shutdown message to other servers
     */
    private static void shutDownServers(String serverHostName, int serverPortNumb)
    {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  
        { 
            try
            { 
                Thread.sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (Message.REQ_SHUTDOWN_SERVER);
        
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK_SHUTDOWN_SERVER)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread().getName () + ": Invalid type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
}
