package com.lgndluke.discordbungee;

import com.lgndluke.discordbungee.commands.Discord;
import com.lgndluke.discordbungee.data.ConfigHandler;
import com.lgndluke.discordbungee.data.MetricsHandler;
import com.lgndluke.discordbungee.data.UpdateHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class DiscordBungee extends Plugin {

    @Override
    public void onEnable() {

        new Thread(() -> {
            MetricsHandler.connect(this);
            ConfigHandler.initialize();
            UpdateHandler.check();
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Discord("discord", "discordbungee.use", "dc"));
        }).start();

    }

    @Override
    public void onDisable() {

        MetricsHandler.disconnect();

    }

}
