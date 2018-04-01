package io.samdev.network.common.util;

import lombok.experimental.UtilityClass;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logging utilities
 */
@UtilityClass
public final class Logging
{
    /**
     * The logging implementation
     *
     * Programs depending on the common library
     * should set their own {@link LogProvider}
     * implementation to allow all components
     * of the program to share the same logging system
     */
    private static LogProvider implementation = new DefaultLogProvider();

    /**
     * Logs a message with the "INFO" level
     *
     * @see #implementation
     *
     * @param msg The message
     * @param params Formatting arguments
     */
    public static void info(String msg, Object... params)
    {
        implementation.log(msg, Level.INFO, params);
    }

    /**
     * Logs a message with the "WARNING" level
     *
     * @see #implementation
     *
     * @param msg The message
     * @param params Formatting arguments
     */
    public static void warn(String msg, Object... params)
    {
        implementation.log(msg, Level.WARNING);
    }

    /**
     * Logs a message with the "SEVERE" level
     *
     * @see #implementation
     *
     * @param msg The message
     * @param params Formatting arguments
     */
    public static void severe(String msg, Object... params)
    {
        implementation.log(msg, Level.SEVERE);
    }

    /**
     * Sets the {@link LogProvider} instance to use for logging
     *
     * @param provider The log provider
     */
    public static void setLogProvider(LogProvider provider)
    {
        implementation = provider;
    }

    /**
     * Abstraction of a logging provider
     */
    public interface LogProvider
    {
        /**
         * Logs a message
         *
         * @param msg The message
         * @param level The log level
         * @param args Formatting arguments
         */
        void log(String msg, Level level, Object... args);
    }

    /**
     * The default {@link LogProvider} implementation to
     * resort to when no other implementation is available
     *
     * Wraps {@link DefaultLogProvider} using Java's global logger
     *
     * In a production scenario, a custom {@link LogProvider}
     * implementation should always be provided
     */
    private static class DefaultLogProvider extends WrappingLogProvider
    {
        public DefaultLogProvider()
        {
            super(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME));
        }
    }

    /**
     * A {@link LogProvider} implementation
     * for simply wrapping an already available
     * {@link Logger} object, to allow the common
     * libraries to share that same logger
     */
    public static class WrappingLogProvider implements LogProvider
    {
        private final Logger logger;

        public WrappingLogProvider(Logger logger)
        {
            this.logger = logger;
        }

        @Override
        public void log(String msg, Level level, Object... params)
        {
            logger.log(level, msg, params);
        }
    }
}
