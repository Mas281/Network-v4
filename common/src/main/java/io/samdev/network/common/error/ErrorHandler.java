package io.samdev.network.common.error;

import io.samdev.network.common.util.UtilJson;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import lombok.experimental.UtilityClass;

/**
 * Class containing static
 * utilities for error handling
 */
@UtilityClass
public final class ErrorHandler
{
    static
    {
        SentryCredentials credentials = UtilJson.parseCredentials(SentryCredentials.class, "sentry");
        sentryClient = SentryClientFactory.sentryClient(credentials.getDsn());
    }

    /**
     * The {@link SentryClient} instance for error reporting
     */
    private final SentryClient sentryClient;

    /**
     * Reports an exception to Sentry
     * and automatically throws it
     *
     * @see #report(Exception, boolean)
     *
     * @param exception The exception
     */
    public static void report(Exception exception)
    {
        report(exception, true);
    }

    /**
     * Reports an exception to Sentry
     *
     * @param exception The exception
     * @param printStackTrace Whether the exception's stacktrace should be printed
     */
    public static void report(Exception exception, boolean printStackTrace)
    {
        sentryClient.sendException(exception);

        if (printStackTrace)
        {
            exception.printStackTrace();
        }
    }
}
