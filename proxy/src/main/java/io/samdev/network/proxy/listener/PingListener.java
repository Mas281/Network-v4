package io.samdev.network.proxy.listener;

import com.google.inject.Inject;
import io.samdev.network.proxy.ProxySettings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener
{
    @Inject
    private ProxySettings proxySettings;

    @EventHandler
    public void onProxyPing(ProxyPingEvent event)
    {
        ServerPing ping = event.getResponse();

        ping.setDescriptionComponent(
            new TextComponent(
                ChatColor.translateAlternateColorCodes('&', proxySettings.getMotd())
            )
        );

        ping.getVersion().setName(proxySettings.getProtocolName());
        ping.getPlayers().setMax(ping.getPlayers().getOnline() + 1);
    }
}