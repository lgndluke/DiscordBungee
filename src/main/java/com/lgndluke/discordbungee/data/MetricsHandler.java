package com.lgndluke.discordbungee.data;

import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

/**
 * This Class is used to create a connection with bStats Metrics.
 * @author lgndluke
 **/
public class MetricsHandler {

    private static final int pluginID = 17907;
    private static Metrics metrics;

    /**
     * Establishes a connection to bStats metrics.
     **/
    public static void connect(Plugin plugin) {
        metrics = new Metrics(plugin, pluginID);
    }

    /**
     * Terminates an existing bStats Metrics connection.
     **/
    public static void disconnect() {
        if(metrics != null) {
            metrics.shutdown();
        }
    }

}
