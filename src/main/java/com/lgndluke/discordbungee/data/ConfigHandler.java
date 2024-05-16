package com.lgndluke.discordbungee.data;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * This Class provides config related utility functions.
 * @author lgndluke
 **/
public class ConfigHandler {

    private static final Plugin discordPlugin = ProxyServer.getInstance().getPluginManager().getPlugin("DiscordBungee");
    private static final String configName = "config.yml";

    /**
     * Initializes the 'config.yml' file on server startup.
     **/
    public static void initialize() {

        if(!discordPlugin.getDataFolder().exists()) {
            if(discordPlugin.getDataFolder().mkdir()) {
                discordPlugin.getLogger().log(Level.INFO, "Successfully created the Plugins folder.");
            }
        }

        File config = new File(discordPlugin.getDataFolder(), configName);

        if(!config.exists()) {
            try(InputStream inp = discordPlugin.getResourceAsStream(configName)) {
                Files.copy(inp, config.toPath());
                discordPlugin.getLogger().log(Level.INFO, "Successfully created '" + configName + "' file.");
                return;
            } catch(IOException io) {
                discordPlugin.getLogger().log(Level.SEVERE, "ERROR: Creating the '" + configName + "' file failed.", io);
            }
        }

        update();

    }

    /**
     * Returns all String value from inside the 'config.yml' file as part of a HashMap.
     * @return HashMap containing the String values of the 'config.yml' file.
     **/
    public static Map<String, String> getStringValues() {

        Map<String, String> stringMap = new HashMap<>();

        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(discordPlugin.getDataFolder(), configName));
            stringMap.put("Prefix", config.getString("Prefix"));
            stringMap.put("Message", config.getString("Message"));
            stringMap.put("Link", "https://discord.gg/" + config.getString("Link"));
            stringMap.put("LinkColor", config.getString("LinkColor"));
            stringMap.put("HexColor", config.getString("HexColor"));
            stringMap.put("ExecutedByConsole", config.getString("ExecutedByConsole"));
            stringMap.put("NoPermission", config.getString("NoPermission"));
        } catch(IOException io) {
            discordPlugin.getLogger().log(Level.SEVERE, "ERROR: Loading string values from '" + configName + "' failed.", io);
        }

        return stringMap;

    }

    /**
     * Returns all Boolean values from inside the 'config.yml' file as part of a HashMap.
     * @return HashMap containing the Boolean values of the 'config.yml' file.
     **/
    public static Map<String, Boolean> getBooleanValues() {

        Map<String, Boolean> boolMap = new HashMap<>();

        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(discordPlugin.getDataFolder(), configName));
            boolMap.put("UseHexColor", config.getBoolean("UseHexColor"));
            boolMap.put("LinkIsBold", config.getBoolean("LinkIsBold"));
            boolMap.put("LinkIsItalic", config.getBoolean("LinkIsItalic"));
            boolMap.put("LinkIsUnderlined", config.getBoolean("LinkIsUnderlined"));
        } catch(IOException io) {
            discordPlugin.getLogger().log(Level.SEVERE, "ERROR: Loading boolean values from '" + configName + "' failed.", io);
        }

        return boolMap;

    }

    /**
     * Updates an existing 'config.yml' file with the resource defaults on Plugin update.
     **/
    private static void update() {

        final int LINE_OF_VERSION = 3;

        try(InputStream inp = discordPlugin.getResourceAsStream(configName);
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(inp))) {

            String newVersion = "";

            for(int i=0; i<LINE_OF_VERSION; i++) {
                newVersion = buffReader.readLine();
            }

            newVersion = newVersion.substring(newVersion.length() - 6, newVersion.length() - 1);

            Configuration configFile = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(discordPlugin.getDataFolder(), configName));
            String currVersion = configFile.getString("Version");

            if(!newVersion.equals(currVersion)) {
                File config = new File(discordPlugin.getDataFolder(), configName);
                Files.copy(discordPlugin.getResourceAsStream(configName), config.toPath(), REPLACE_EXISTING);
                discordPlugin.getLogger().log(Level.INFO, "Successfully updated file '" + configName + "'!");
            }
        } catch(IOException io) {
            discordPlugin.getLogger().log(Level.SEVERE, "ERROR: Updating the '" + configName + "' file failed.", io);
        }

    }

}
