package ComInf;

/**
 *  General description:
 *      All states for all entities
 * 
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class States
{
    /**
     *  General description:
     *      Referee states
     */
    public enum RefereeState
    {
        START_OF_THE_MATCH(101) 
        {
            @Override
            public String toString() 
            {
                return "STM";
            }
        },
        START_OF_THE_GAME(102) 
        {
            @Override
            public String toString() 
            {
                return "STG";
            }
        },
        TEAMS_READY(103) 
        {
            @Override
            public String toString() 
            {
                return "TSR";
            }
        },
        WAIT_FOR_TRIAL_CONCLUSION(104) 
        {
            @Override
            public String toString() 
            {
                return "WTC";
            }
        },
        END_OF_THE_MATCH(105) 
        {
            @Override
            public String toString() 
            {
                return "ETM";
            }
        },
        END_OF_THE_GAME(106) 
        {
            @Override
            public String toString() 
            {
                return "ETG";
            }
        };

        private final int code;

        /**
         *  Constructor
         *  
         *  @param c code of the state
         */
        private RefereeState(int c) 
        {
            code = c;
        }
        
        /**
         *  Get the state code
         * 
         *  @return state code
         */
        public int getRefereeStateCode() 
        {
            return code;
        }
        
        /**
         *  Get state by code
         *  @param code state code
         *  @return state
         */
        public static RefereeState getRefereeStateByCode(int code) 
        {
            for (RefereeState state : RefereeState.values()) 
                if (state.code == code) 
                    return state;
            return null;
        }
    }
    
    /**
     *  General description:
     *      Coach states
     */
    public enum CoachState
    {
        WAIT_FOR_REFEREE_COMMAND(201) 
        {
            @Override
            public String toString() 
            {
                return "WFRC";
            }
        },
        ASSEMBLE_TEAM(202) 
        {
            @Override
            public String toString() 
            {
                return "AETM";
            }
        },
        WATCH_TRIAL(203) 
        {
            @Override
            public String toString() 
            {
                return "WHTL";
            }
        };

        private final int code;

        /**
         *  Constructor
         *  
         *  @param c code of the state
         */
        private CoachState(int c) 
        {
            code = c;
        }

        /**
         *  Get the state code
         * 
         *  @return state code
         */
        public int getCoachStateCode() 
        {
            return code;
        }
        
        /**
         *  Get state by code
         * 
         *  @param code state code
         *  @return state
         */
        public static CoachState getCoachStateByCode(int code) 
        {
            for (CoachState state : CoachState.values()) 
                if (state.code == code) 
                    return state;
            return null;
        }
    }
    
    /**
     *  General description:
     *      Contestant states
     */
    public enum ContestantState
    {
        SEAT_AT_THE_BENCH(301) 
        {
            @Override
            public String toString() 
            {
                return "STB";
            }
        },
        STAND_IN_POSITION(302) 
        {
            @Override
            public String toString() 
            {
                return "SIP";
            }
        },
        DO_YOUR_BEST(303) 
        {
            @Override
            public String toString() 
            {
                return "DYB";
            }
        };

        private final int code;

        /**
         *  Constructor
         *  
         *  @param c code of the state
         */
        private ContestantState(int c) 
        {
            code = c;
        }

        /**
         *  Get the state code
         * 
         *  @return state code
         */
        public int getContestantStateCode() 
        {
            return code;
        }
        
        /**
         *  Get state by code
         * 
         *  @param code state code
         *  @return state
         */
        public static ContestantState getContestantStateByCode(int code) 
        {
            for (ContestantState state : ContestantState.values()) 
                if (state.code == code) 
                    return state;
            return null;
        }
    }
}
