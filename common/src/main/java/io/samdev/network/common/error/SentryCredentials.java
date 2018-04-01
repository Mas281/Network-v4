package io.samdev.network.common.error;

import io.samdev.network.common.database.Credentials;
import lombok.Value;

/**
 * Class holding Sentry credentials
 */
@Value
class SentryCredentials implements Credentials
{
    private final String dsn;
}
