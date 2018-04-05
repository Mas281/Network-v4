package io.samdev.network.proxy;

import lombok.Value;

@Value
public class ProxySettings
{
    private final String motd;
    private final String protocolName;
}
