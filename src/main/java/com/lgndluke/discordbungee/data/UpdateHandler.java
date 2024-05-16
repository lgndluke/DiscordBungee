package com.lgndluke.discordbungee.data;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * This Class is used to check the SpigotMC.org API for Plugin updates.
 * @author lgndluke
 **/
public class UpdateHandler {

    private static final Plugin discordPlugin = ProxyServer.getInstance().getPluginManager().getPlugin("DiscordBungee");
    private static final int resourceID = 108421;
    private static final String configName = "config.yml";

    /**
     * Checks for updates of DiscordBungee.
     **/
    public static void check() {

        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(discordPlugin.getDataFolder(), configName));
            String versionString = config.getString("Version");
            checkForUpdates(version -> {
                if(!versionString.equals(version)) {
                    discordPlugin.getLogger().log(Level.WARNING, "A new Version of DiscordBungee is available. Please update!");
                }
            });
        } catch(IOException io) {
            discordPlugin.getLogger().log(Level.SEVERE, "ERROR: Loading 'Version' value from '" + configName + "' failed!", io);
        }

    }

    /**
     * Requests the current version string from the SpigotMC.org API.
     **/
    private static void checkForUpdates(Consumer<String> consumer) {
        discordPlugin.getProxy().getScheduler().runAsync(discordPlugin, () -> {
            try (InputStream inp = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID + "/~").openStream();
                 Scanner scan = new Scanner(inp)) {
                if(scan.hasNext()) {
                    consumer.accept(scan.next());
                }
            } catch (IOException io) {
                discordPlugin.getLogger().log(Level.WARNING, "ERROR: Querying version string from SpigotMC.org API failed!", io);
            }
        });
    }

}
