package SharedRegions;

import App.Parameters;
import App.Semaphore;
import App.States;
import App.States.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Repository 
{
    /** Referee state */
    private String refereeState;
    
    /** Coaches states */
    private String [] coachState;
    
    /** Contestants states */
    private String [][]contestantState;
    
    /** Contestants strength */
    private int [][] contestantStrength;
    
    /** Contestants in trial */
    private int [][] queueTrial;
    
    /** Size of trial queue */
    private int [] queueSize;
    
    /** Trial number */
    private int trialNumber;
    
    /** Position of the centre the rope at the beginning of the trial */
    private int ropePosition;
    
    /** Access in regime of mutual exclusion */
    private final Semaphore access;
    
    /** The Logger should use printw to write all log information */
    private PrintWriter printw; 
    
    private int numGame;
    
    private boolean endOfMatch;
    
    private int pointsTeam[];
    
    private int teamWonTrial;
    
    private int teamWonGame[];
    
    public Repository() throws FileNotFoundException
    {
        //Log file
        File file = new File(Parameters.fileName);
        printw = new PrintWriter(file);
        
        //Instantiate variables for the log
        coachState = new String[Parameters.numCoaches];
        contestantState = new String[Parameters.numCoaches][Parameters.numContestants];
        contestantStrength = new int[Parameters.numCoaches][Parameters.numContestants];
        queueTrial = new int[Parameters.numCoaches][Parameters.numContestantsByTrial];
        queueSize = new int[Parameters.numCoaches];
        pointsTeam = new int[Parameters.numCoaches];
        teamWonGame = new int[Parameters.numCoaches];
        numGame = 0;
        
        //Set initial states
        refereeState = States.RefereeState.START_OF_THE_MATCH.toString();
        for(int c = 0; c < Parameters.numCoaches; c++) 
        {
            teamWonGame[c] = 0;
            pointsTeam[c] = 0;
            coachState[c] = States.CoachState.WAIT_FOR_REFEREE_COMMAND.toString();
            for(int p = 0; p < Parameters.numContestants; p++)
            {
                contestantState[c][p] = States.ContestantState.SEAT_AT_THE_BENCH.toString();
                contestantStrength[c][p] = Parameters.startStrength;
            }
        }
       
        //Instantiate semaphores
        this.access = new Semaphore();
        this.access.up();
        
        endOfMatch = false;

        //Write header log
        header();
        write();
        
    }
    
    /**
     * Writes the header of the log file to the printw PrintWriter
     */
    private void header() 
    {
        printw.format("%3s","REF");
        for(int c = 0; c < Parameters.numCoaches; c++) 
        {
            printw.format(" COA %d", c+1);
            for(int p = 0; p < Parameters.numContestants; p++)
            {
                printw.format(" CONT %d", p+1);
            }
        }
        printw.format("       TRIAL       ");
        printw.format("\n");
        
        printw.format("%3s","STA");
        for(int c = 0; c < Parameters.numCoaches; c++) 
        {
            printw.format(" STAT ");
            for(int p = 0; p < Parameters.numContestants; p++)
            {
                printw.format(" STA SG");
            }
        }
        printw.format(" 3 2 1 . 1 2 3 NB PS");
        printw.format("\n");
        
        printw.flush();
    }
    
    /**
     * Writes a new line with all the information to the printw PrintWriter
     */
    private void write() 
    {
        printw.format("%3s", refereeState);
        for(int c = 0; c < Parameters.numCoaches; c++) 
        {
            printw.format(" %4s ", coachState[c]);
            for(int p = 0; p < Parameters.numContestants; p++)
            {
                printw.format(" %3s", contestantState[c][p]);
                printw.format(" %2d", contestantStrength[c][p]);
            }
        }
        
        for(int q = queueSize[0]; q < Parameters.numContestantsByTrial; q++) 
        {
            printw.format("%2s","-");
        }
        
        for(int q = queueSize[0]-1; q >= 0; q--) 
        {
            printw.format("%2d",(queueTrial[0][q]+1));
        }        
        
        printw.format(" .");
        
        for(int q = 0; q < queueSize[1]; q++) 
        {
            printw.format("%2d",(queueTrial[1][q]+1));
        }
        for(int q = queueSize[1]; q < Parameters.numContestantsByTrial; q++) 
        {
            printw.format("%2s","-");
        }

        printw.format(" %2d", trialNumber);
        printw.format(" %2d", ropePosition);
        printw.format("\n");        
        printw.flush();
    }

    public void updateRefereeState(RefereeState state)
    {
        access.down();        
        refereeState = state.toString();
        
        if(state.equals(RefereeState.WAIT_FOR_TRIAL_CONCLUSION))
        {
            trialNumber++;
            ropePosition = Math.abs(pointsTeam[0] - pointsTeam[1]);
            if(pointsTeam[0] - pointsTeam[1] >= 1)
                teamWonTrial = 1;
            else
                if(pointsTeam[1] - pointsTeam[0] >= 1)
                    teamWonTrial = 2;
                else
                    teamWonTrial = 0;
            
            for(int c = 0; c < Parameters.numCoaches; c++) 
                pointsTeam[c] = 0;
        }
        
        if(state.equals(RefereeState.START_OF_THE_GAME))
            numGame++;
        
        write();
        
        if(state.equals(RefereeState.END_OF_THE_GAME))
        {
            //Write result in log
            printw.format("Game %1d", numGame);
            if(ropePosition >= 4)
                printw.format(" was won by team %1d by knock out in %1d trials", teamWonTrial, trialNumber);
            else
                if(trialNumber == 6 && ropePosition > 0)
                    printw.format(" was won by team %1d by points", teamWonTrial);
                else
                    printw.format(" was a draw");
            printw.format("\n");        
            printw.flush();
            if(teamWonTrial == 1)
                teamWonGame[0]++;
            else 
                if(teamWonTrial == 2)
                    teamWonGame[1]++;
            
            trialNumber = 0;
            ropePosition = 0;
        }
        access.up();
    }
    
    public void updateCoachState(CoachState state, int id)
    {
        access.down();
        coachState[id] = state.toString();
        write();
        access.up();
    }
    
    public void updateContestantState(ContestantState state, int id, int idCoach, int strength)
    {
        access.down();

        if(state.equals(ContestantState.STAND_IN_POSITION))
        {
            queueTrial[idCoach][queueSize[idCoach]] = id; 
            queueSize[idCoach]++;
            pointsTeam[idCoach] += strength;
        }
        if(state.equals(ContestantState.SEAT_AT_THE_BENCH) && contestantState[idCoach][id].equals(ContestantState.DO_YOUR_BEST.toString()))
            queueSize[idCoach]--;
        
        contestantStrength[idCoach][id] = strength;
        contestantState[idCoach][id] = state.toString();
        write();
        access.up();
    }
    
    public boolean endOfReferee() 
    {
        access.down();
        boolean ret = endOfMatch;
        access.up();
        return !ret;
    }

    public boolean endOfCoach()  
    {
        access.down();
        boolean ret = endOfMatch;
        access.up();
        return !ret;
    }

    public boolean endOfContestant()  
    {
        access.down();
        boolean ret = endOfMatch;
        access.up();
        return !ret;
    }

    public boolean assertTrialDecision() 
    {
        access.down();
        if(ropePosition >= 4 || trialNumber == 6)
        {
            if(numGame == Parameters.numGames)
                endOfMatch = true;
            access.up();
            return false;
        }
        access.up();
        return true;
    }

    public void endOfMatch(RefereeState state) 
    {
        access.down();
        refereeState = state.toString();
        write();
        access.up();
        
        try 
        {
            Thread.sleep(100);
        }
        catch (InterruptedException ex) 
        {
        }
        
        access.down();
        if(teamWonGame[0] != teamWonGame[1])
            printw.format("Match was won by team %1d (%1d-%1d)", (teamWonGame[0] > teamWonGame[1] ? 1 : 2), teamWonGame[0],teamWonGame[1]);
        else
            printw.format("Match was a draw");
        printw.format("\n");        
        printw.flush();
        access.up();
    }

    public int[] getTeam(int id) 
    {
        int[] team = new int[Parameters.numContestantsByTrial];
        
        //Select the strongest
        int idMax = 0;
        int strength = contestantStrength[id][idMax];
        for (int i = 0; i < Parameters.numContestants; i++) 
            if(strength < contestantStrength[id][i])
            {
                idMax = i;
                strength = contestantStrength[id][i];
            }
        
        //Select two contestants random to randomize the solution
        int rConst1, rConst2;
        do
        {
            rConst1 = (int) (Math.random() * Parameters.numContestants);
            rConst2 = (int) (Math.random() * Parameters.numContestants);
        }
        while(rConst1 == idMax || rConst2 == idMax || rConst1 == rConst2);
        team[0] = idMax;
        team[1] = rConst1;
        team[2] = rConst2;

        return team;
    }

    public boolean endOfGame() 
    {
        if(numGame == Parameters.numGames)
            return false;
        return true;
    }
    
}
