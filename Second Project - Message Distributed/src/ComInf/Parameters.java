package ComInf;

/**
 *  General description:
 *      Parameters used in all application
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class Parameters 
{
    /** 
     * Number of contestants by team 
     * @serialField numContestants int number of contestants
     */
    public static final int numContestants = 5;
    
    /** 
     * Number of contestants by trial 
     * @serialField numContestantsByTrial int number of contestants by trial
     */
    public static final int numContestantsByTrial = 3;
    
    /** 
     * Number of coaches 
     * @serialField numCoaches int number of coaches
     */
    public static final int numCoaches = 2;
    
    /** 
     * Number of games 
     * @serialField numGames int number of games
     */
    public static final int numGames = 3;
    
    /** 
     * Number max of trials
     * @serialField numTrials int number max of trials by game
     */
    public static final int numTrials = 6;
    
    /** 
     * Number of points to a team win by KO 
     * @serialField numPointsToWin int number of points to win by ko
     */
    public static final int numPointsToWin = 4;
    
    /** 
     * Name of the file that contain the log 
     * @serialField fileName String file name of the log
     */
    public static final String fileName = "log.txt";
    
    /**
     * @serialField serverContestantBenchName String server contestant bench name
     */
    public static final String serverContestantBenchName = "l040101-ws01.ua.pt";
    
    /**
     * @serialField serverRefereeSiteName String server referee site name
     */
    public static final String serverRefereeSiteName = "l040101-ws02.ua.pt";
    
    /**
     * @serialField serverPlaygroundName String server playground name
     */ 
    public static final String serverPlaygroundName = "l040101-ws03.ua.pt";
    
    /**
     * @serialField serverRepositoryName String server repository name
     */
    public static final String serverRepositoryName = "l040101-ws04.ua.pt";
    
    /**
     * @serialField serverContestantBenchPort int server contestant bench port
     */
    public static final int serverContestantBenchPort = 22370;
    
    /**
     * @serialField serverRefereeSitePort int server referee site port
     */
    public static final int serverRefereeSitePort = 22371;  
    
    /**
     * @serialField serverPlaygroundPort int server playground port
     */
    public static final int serverPlaygroundPort = 22372;
    
    /**
     * @serialField serverRepositoryPort int server repository port
     */
    public static final int serverRepositoryPort = 22373;
}
