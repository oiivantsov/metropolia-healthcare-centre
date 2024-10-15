package org.group8.simulator.framework;

/**
 * The Trace class is used to log and output messages in the simulation at different levels.
 * It allows the user to set a trace level, so that only messages of a certain priority or higher are logged.
 */
public class Trace {

    /**
     * Enum representing the different trace levels: INFO, WAR (Warning), and ERR (Error).
     */
    public enum Level {INFO, WAR, ERR}

    private static Level traceLevel;

    /**
     * Sets the current trace level. Only messages with a level equal to or higher than
     * this level will be output.
     *
     * @param lvl the trace level to set
     */
    public static void setTraceLevel(Level lvl) {
        traceLevel = lvl;
    }

    /**
     * Outputs a message if the specified trace level is equal to or higher than the current trace level.
     *
     * @param lvl the level of the message (INFO, WAR, ERR)
     * @param txt the text of the message to output
     */
    public static void out(Level lvl, String txt) {
        if (lvl.ordinal() >= traceLevel.ordinal()) {
            System.out.println(txt);
        }
    }
}
