package App;

/**
 *
 * @author 65767 - João Rafael Duarte de Almeida
 */
public class States
{
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

        // Construtor
        private RefereeState(int c) 
        {
            code = c;
        }

        public int getRefereeStateCode() 
        {
            return code;
        }
    }
    
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

        // Construtor
        private CoachState(int c) 
        {
            code = c;
        }

        public int getCoachStateCode() 
        {
            return code;
        }
    }
    
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

        // Construtor
        private ContestantState(int c) 
        {
            code = c;
        }

        public int getContestantStateCode() 
        {
            return code;
        }
    }
}
