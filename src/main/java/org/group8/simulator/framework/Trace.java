package org.group8.simulator.framework;

/**
 * The {@code Trace} class provides simple tracing functionality to output messages to the console
 * based on the specified trace level. It is used to control the verbosity of logs during the simulation.
 */
public class Trace {

    /**
     * Enum representing different levels of tracing output:
     * <ul>
     *   <li>{@code INFO} - Informational messages.</li>
     *   <li>{@code WAR} - Warning messages.</li>
     *   <li>{@code ERR} - Error messages.</li>
     * </ul>
     */
    public enum Level {INFO, WAR, ERR}

    /** The current trace level, which determines the minimum severity of messages that will be printed. */
    private static Level traceLevel;

    /**
     * Sets the global trace level. Messages with a level below this will not be printed.
     *
     * @param lvl the trace level to set (INFO, WAR, ERR)
     */
    public static void setTraceLevel(Level lvl) {
        traceLevel = lvl;
    }

    /**
     * Outputs a message to the console if the specified level is greater than or equal to the current trace level.
     *
     * @param lvl the severity level of the message
     * @param txt the message to print
     */
    public static void out(Level lvl, String txt) {
        if (lvl.ordinal() >= traceLevel.ordinal()) {
            System.out.println(txt);
        }
    }
}
