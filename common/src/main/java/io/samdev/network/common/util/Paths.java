package io.samdev.network.common.util;

import lombok.experimental.UtilityClass;

import java.io.File;

/**
 * File path constants for network directories
 */
@UtilityClass
public final class Paths
{
    /**
     * The root of the network directory structure
     */
    public static final File NETWORK_ROOT = new File(System.getenv("NETWORK_ROOT"));

    /**
     * The network configuration file
     */
    public static final File NETWORK_CONFIG = new File(NETWORK_ROOT, "network_config.json");

    /**
     * The name of the server config file for each Spigot server instance
     */
    public static final String SERVER_CONFIG_NAME = "server_config.json";
}
