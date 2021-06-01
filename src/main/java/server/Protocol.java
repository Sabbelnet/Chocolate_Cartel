package server;

/**
 * enum containing network protocol commands
 */

public enum Protocol {
    /**
     *
     */
    PING,
    /**
     *
     */
    PONG,
    /**
     * Client sends username from player who wants to quit the game
     */
    LOGOT,
    /**
     * Client sends username to server, server checks if its unique and puts it on players list
     */
    LOGIN,
    /**
     * Client sends current score
     */
    UPDTE,
    /**
     * Client sends RuleCheck
     */
    RULCK,
    /**
     * Client sends message with its address
     */
    CHATO,
    /**
     * Client sends offer
     */
    SELTO,
    /**
     * Client sends request with username to spy
     */
    SPYTO,
    /**
     * Client sends requested details (text format) - Client just got spied on
     */
    SPYST,
    /**
     * Client sends request to join a group
     */
    GRPAD,


    /**
     *
     */
    SCORE,  //Highscore verteilen
    /**
     * Server sends Message/Event to a specific player or every player
     */
    CHATI,
    /**
     * Server sends confirmation to the clienets offer
     */
    SELIN,
    /**
     *
     */
    RULIN, //info
    /**
     * Server checks if tipped in username is already taken, if so changes it
     */
    NAMCK,
    /**
     * Server sends current list of total players
     */
    LSTSD,
    /**
     * Server sends group invitation
     */
    GRPIN,
    /**
     * Server starts game with presets
     */
    GAMGO,
    /**
     * Server sends error, if no valid command
     */
    DEFLT
    }
