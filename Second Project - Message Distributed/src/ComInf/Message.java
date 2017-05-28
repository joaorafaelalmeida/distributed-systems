package ComInf;

import java.io.Serializable;

/**
 *
 * @author joao
 */
public class Message implements Serializable
{
                /* Message types */
    
    /**
     * Request message: Coach select contestants to play in the trial
     * @serialField REQ_CALL_CONTESTANTS int message type
     */
    public static final int REQ_CALL_CONTESTANTS = 1;
    
    /**
     * Acknowledge message: Coach select contestants to play in the trial
     * @serialField ACK_CALL_CONTESTANTS int message type
     */
    public static final int ACK_CALL_CONTESTANTS = 2;
    
    /**
     * Request message: Contestant prepares to go to the playground to pull the rope
     * @serialField REQ_FOLLOW_COACH_ADVICE int message type
     */
    public static final int REQ_FOLLOW_COACH_ADVICE = 3;
    
    /**
     * Acknowledge message: Contestant prepares to go to the playground to pull the rope
     * @serialField ACK_FOLLOW_COACH_ADVICE int message type
     */
    public static final int ACK_FOLLOW_COACH_ADVICE = 4;         
            
    /**
     * Request message: Contestant seat down and rest
     * @serialField REQ_SEAT_DOWN int message type
     */
    public static final int REQ_SEAT_DOWN = 5;
            
    /**
     * Acknowledge message: Contestant seat down and rest
     * @serialField ACK_SEAT_DOWN int message type
     */
    public static final int ACK_SEAT_DOWN = 6;

    /**
     * Request message: Coach review notes
     * @serialField REQ_REVIEW_NOTES int message type
     */
    public static final int REQ_REVIEW_NOTES = 7;
    
    /**
     * Acknowledge message: Coach review notes
     * @serialField ACK_REVIEW_NOTES int message type
     */
    public static final int ACK_REVIEW_NOTES = 8;
            
    /**
     * Request message: Referee prepares the trial to start
     * @serialField REQ_CALL_TRIAL int message type
     */ 
    public static final int REQ_CALL_TRIAL = 9;
    
    /**
     * Acknowledge message: Referee prepares the trial to start
     * @serialField ACK_CALL_TRIAL int message type
     */ 
    public static final int ACK_CALL_TRIAL = 10;
            
    /**
     * Request message: Referee starts the trial
     * @serialField REQ_START_TRIAL int message type
     */
    public static final int REQ_START_TRIAL = 11;
            
    /**
     * Acknowledge message: Referee starts the trial
     * @serialField ACK_START_TRIAL int message type
     */
    public static final int ACK_START_TRIAL = 12;

    /**
     * Request message: Referee assert trial decision
     * @serialField REQ_ASSERT_TRIAL_DECISION int message type
     */
    public static final int REQ_ASSERT_TRIAL_DECISION = 13;
    
    /**
     * Acknowledge message: Referee assert trial decision
     * @serialField ACK_ASSERT_TRIAL_DECISION_TRUE int message type
     */
    public static final int ACK_ASSERT_TRIAL_DECISION_TRUE = 14;
    
    /**
     * Acknowledge message: Referee assert trial decision
     * @serialField ACK_ASSERT_TRIAL_DECISION_FALSE int message type
     */
    public static final int ACK_ASSERT_TRIAL_DECISION_FALSE = 15;

    /**
     * Request message: Coach inform referee that his team is ready
     * @serialField REQ_INFORM_REFEREE int message type
     */
    public static final int REQ_INFORM_REFEREE = 16;
    
    /**
     * Acknowledge message: Coach inform referee that his team is ready
     * @serialField ACK_INFORM_REFEREE int message type
     */
    public static final int ACK_INFORM_REFEREE = 17;

    /**
     * Request message: Contestant is ready to pull the rope
     * @serialField REQ_GET_READY int message type
     */
    public static final int REQ_GET_READY = 18;
    
    /**
     * Acknowledge message: Contestant is ready to pull the rope
     * @serialField ACK_GET_READY int message type
     */
    public static final int ACK_GET_READY = 19;

    /**
     * Request message: Contestant pull the rope
     * @serialField REQ_PULL_THE_ROPE int message type
     */
    public static final int REQ_PULL_THE_ROPE = 20;
    
    /**
     * Acknowledge message: Contestant pull the rope
     * @serialField ACK_PULL_THE_ROPE int message type
     */
    public static final int ACK_PULL_THE_ROPE = 21;

    /**
     * Request message: Contestant finish to pull the rope
     * @serialField REQ_AM_DONE int message type
     */
    public static final int REQ_AM_DONE = 22;
    
    /**
     * Acknowledge message: Contestant finish to pull the rope
     * @serialField ACK_AM_DONE  int message type
     */
    public static final int ACK_AM_DONE = 23;

    /**
     * Request message: Referee announce a new game
     * @serialField REQ_ANNOUNCE_NEW_GAME int message type
     */
    public static final int REQ_ANNOUNCE_NEW_GAME = 24;
    
    /**
     * Acknowledge message: Referee announce a new game
     * @serialField ACK_ANNOUNCE_NEW_GAME int message type
     */
    public static final int ACK_ANNOUNCE_NEW_GAME = 25;

    /**
     * Request message: Referee declare game winner
     * @serialField REQ_DECLARE_GAME_WINNER int message type
     */
    public static final int REQ_DECLARE_GAME_WINNER = 26;
    
    /**
     * Acknowledge message: Referee declare game winner
     * @serialField ACK_DECLARE_GAME_WINNER_TRUE int message type
     */
    public static final int ACK_DECLARE_GAME_WINNER_TRUE = 27;
    
    /**
     * Acknowledge message: Referee declare game winner
     * @serialField ACK_DECLARE_GAME_WINNER_FALSE int message type
     */
    public static final int ACK_DECLARE_GAME_WINNER_FALSE = 28;

    /**
     * Request message: Referee declare match winner
     * @serialField REQ_DECLARE_MATCH_WINNER int message type
     */
    public static final int REQ_DECLARE_MATCH_WINNER = 29;
    
    /**
     * Acknowledge message: Referee declare match winner
     * @serialField ACK_DECLARE_MATCH_WINNER int message type
     */
    public static final int ACK_DECLARE_MATCH_WINNER = 30;

    /**
     * Request message: Coach inform referee that he is ready
     * @serialField REQ_INFORM_ARRIVAL int message type
     */ 
    public static final int REQ_INFORM_ARRIVAL = 31;
    
    /**
     * Acknowledge message: Coach inform referee that he is ready
     * @serialField ACK_INFORM_ARRIVAL int message type
     */ 
    public static final int ACK_INFORM_ARRIVAL = 32;
    
    /**
     * Request message: Write in the log an update of referee state
     * @serialField REQ_UPDATE_REFEREE_STATE int message type
     */
    public static final int REQ_UPDATE_REFEREE_STATE = 33;
    
    /**
     * Acknowledge message: Write in the log an update of referee state
     * @serialField ACK_UPDATE_REFEREE_STATE int message type
     */
    public static final int ACK_UPDATE_REFEREE_STATE = 34;
    
    /**
     * Request message: Write in the log an update of coach state
     * @serialField REQ_UPDATE_COACH_STATE int message type 
     */
    public static final int REQ_UPDATE_COACH_STATE = 35;
    
    /**
     * Acknowledge message: Write in the log an update of coach state
     * @serialField ACK_UPDATE_COACH_STATE int message type
     */
    public static final int ACK_UPDATE_COACH_STATE = 36;
    
    /**
     * Request message: Write in the log an update of contestant state
     * @serialField REQ_UPDATE_CONTESTANT_STATE int message type
     */
    public static final int REQ_UPDATE_CONTESTANT_STATE = 37;
    
    /**
     * Acknowledge message: Write in the log an update of contestant state
     * @serialField ACK_UPDATE_CONTESTANT_STATE int message type
     */
    public static final int ACK_UPDATE_CONTESTANT_STATE = 38;
    
    /**
     * Request message: Indicate the end of the referee
     * @serialField REQ_END_OF_REFEREE int message type 
     */
    public static final int REQ_END_OF_REFEREE = 39;
    
    /**
     * Acknowledge message: Indicate the end of the referee
     * @serialField ACK_END_OF_REFEREE_TRUE int message type
     */
    public static final int ACK_END_OF_REFEREE_TRUE = 40;
    
    /**
     * Acknowledge message: Indicate the end of the referee
     * @serialField ACK_END_OF_REFEREE_FALSE int message type 
     */
    public static final int ACK_END_OF_REFEREE_FALSE = 41;

    /**
     * Request message: Indicate the end of the coach
     * @serialField REQ_END_OF_COACH int message type 
     */
    public static final int REQ_END_OF_COACH = 42;
    
    /**
     * Acknowledge message: Indicate the end of the coach
     * @serialField ACK_END_OF_COACH_TRUE int message type
     */
    public static final int ACK_END_OF_COACH_TRUE = 43;
    
    /**
     * Acknowledge message: Indicate the end of the coach
     * @serialField ACK_END_OF_COACH_FALSE int message type
     */
    public static final int ACK_END_OF_COACH_FALSE = 44;

    /**
     * Request message: Indicate the end of the contestant
     * @serialField REQ_END_OF_CONTESTANT int message type 
     */ 
    public static final int REQ_END_OF_CONTESTANT = 45;
    
    /**
     * Acknowledge message: Indicate the end of the contestant
     * @serialField ACK_END_OF_CONTESTANT_TRUE int message type 
     */ 
    public static final int ACK_END_OF_CONTESTANT_TRUE = 46;
    
    /**
     * Acknowledge message: Indicate the end of the contestant
     * @serialField ACK_END_OF_CONTESTANT_FALSE int message type 
     */ 
    public static final int ACK_END_OF_CONTESTANT_FALSE = 47;

    /**
     * Request message: Is decided if the game ends or not
     * @serialField REQ_ASSERT_TRIAL_DECISION_REP int message type
     */
    public static final int REQ_ASSERT_TRIAL_DECISION_REP = 48;
    
    /**
     * Acknowledge message: Is decided if the game ends or not
     * @serialField ACK_ASSERT_TRIAL_DECISION_REP_TRUE int message type
     */
    public static final int ACK_ASSERT_TRIAL_DECISION_REP_TRUE = 49;
    
    /**
     * Acknowledge message: Is decided if the game ends or not
     * @serialField ACK_ASSERT_TRIAL_DECISION_REP_FALSE int message type
     */
    public static final int ACK_ASSERT_TRIAL_DECISION_REP_FALSE = 50;

    /**
     * Request message: Match is over
     * @serialField REQ_END_OF_MATCH int message type
     */ 
    public static final int REQ_END_OF_MATCH = 51;
    
    /**
     * Acknowledge message: Match is over
     * @serialField ACK_END_OF_MATCH int message type
     */ 
    public static final int ACK_END_OF_MATCH = 52;

    /**
     * Request message: Coach get his team deciding who have more strength
     * @serialField REQ_GET_TEAM int message type
     */
    public static final int REQ_GET_TEAM = 53;
    
    /**
     * Acknowledge message: Coach get his team deciding who have more strength
     * @serialField ACK_GET_TEAM int message type
     */
    public static final int ACK_GET_TEAM = 54;

    /**
     * Request message: Indicate the end of the game
     * @serialField REQ_END_OF_GAME int message type
     */
    public static final int REQ_END_OF_GAME = 55;
    
    /**
     * Acknowledge message: Indicate the end of the game
     * @serialField ACK_END_OF_GAME_TRUE int message type
     */
    public static final int ACK_END_OF_GAME_TRUE = 56;
    
    /**
     * Acknowledge message: Indicate the end of the game
     * @serialField ACK_END_OF_GAME_FALSE int message type 
     */
    public static final int ACK_END_OF_GAME_FALSE = 57;
    
    /**
     * Request message: Indicate the end of the servers
     * @serialField ACK_SHUTDOWN_SERVER int message type
     */
    public static final int REQ_SHUTDOWN_SERVER = 58;
    
    /**
     * Acknowledge message: Indicate the end of the servers
     * @serialField ACK_SHUTDOWN_SERVER int message type
     */
    public static final int ACK_SHUTDOWN_SERVER = 59;
    
    /**
     * Message type
     * @serialField msgType int message type
     */
    private int msgType;
    
    /**
     * Id
     * @serialField id int id
     */
    private int id = -1;
    
    /**
     * Coach id to identify the contestant
     * @serialField coachId int coach id
     */
    private int coachId = -1;
    
    /**
     * State
     * @serialField state int state
     */
    private int state;
    
    /**
     * Strength
     * @serialField strength int strength
     */
    private int strength;
    
    /**
     * Array with id of the contestant that will play
     * @serialField team int[] team
     */
    private int[] team;
    
    /**
     * Instantiate the message
     * @param type message type
     */
    public Message (int type)
    {
        this.msgType = type;
    }
    
    /**
     * Instantiate the message
     * @param type message type
     * @param state state passed in the message
     */
    public Message(int type, int state)
    {
        this.msgType = type;
        this.state = state;
    }
    
    /**
     * Instantiate the message
     * @param type message type
     * @param id id of the coach
     * @param state state passed in the message
     */
    public Message(int type, int id, int state)
    {
        this.msgType = type;
        this.id = id;
        this.state = state;
    }
    
    /**
     * Instantiate the message
     * @param type message type
     * @param id id of the coach
     * @param state state passed in the message
     * @param team team that will play
     */
    public Message(int type, int id, int state, int[] team)
    {
        this.msgType = type;
        this.id = id;
        this.state = state;
        this.team = team;
    }
    
    /**
     * Instantiate the message
     * @param type message type
     * @param id id of the coach
     * @param team team that will play
     */
    public Message(int type, int id, int[] team)
    {
        this.msgType = type;
        this.id = id;
        this.team = team;
    }
        
    /**
     * Instantiate the message
     * @param type message type
     * @param id id of the contestant
     * @param coachId id of the coach
     * @param strength strength of the contestant
     * @param state state passed in the message
     */
    public Message(int type, int id, int coachId, int strength, int state)
    {
        this.msgType = type;
        this.id = id;
        this.coachId = coachId;
        this.state = state;
        this.strength = strength;
    }
    
    /**
     * Get the message type 
     * @return message type
     */
    public int getType()
    {
       return msgType;
    }

    /**
     * Get the id
     * @return id the coach or contestant
     */
    public int getId() 
    {
        return id;
    }
    
    /**
     * Get the coach id 
     * @return id of the coach
     */
    public int getCoachId()
    {
        return coachId;
    }

    /**
     * Get the state  
     * @return state in integer
     */
    public int getState() 
    {
        return state;
    }
    
    /**
     * Get the strength 
     * @return strength
     */
    public int getStrength() 
    {
        return strength;
    }
    
    /**
     * Get the team
     * @return team in a integer array
     */
    public int[] getTeam() 
    {
        return team;
    }
    
    @Override
    public String toString ()
    {
        return "Type = " + msgType +
               "\nId = " + id +
               "\nId Coach = " + coachId +
               "\nState = " + state +
               "\nStrength = " + strength +
               "\nTeam = " + team;
    }
}
