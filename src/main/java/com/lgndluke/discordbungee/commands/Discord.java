package com.lgndluke.discordbungee.commands;

import com.lgndluke.discordbungee.data.ConfigHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.awt.*;
import java.util.Map;
import java.util.logging.Level;

/**
 * This Class represents the '/discord' command.
 * @author lgndluke
 **/
public class Discord extends Command {

    private final Plugin discordPlugin = ProxyServer.getInstance().getPluginManager().getPlugin("DiscordBungee");
    private Map<String, String> stringMap = ConfigHandler.getStringValues();
    private Map<String, Boolean> boolMap = ConfigHandler.getBooleanValues();

    public Discord(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(player.hasPermission("discordbungee.use")) {
                updateValues();
                sendDiscordMessage(player);
            } else {
                sendNoPermissionMessage(player);
            }
        } else {
            discordPlugin.getLogger().log(Level.SEVERE, stringMap.get("ExecutedByConsole"));
        }

    }

    private void sendDiscordMessage(ProxiedPlayer player) {

        TextComponent dcLink = new TextComponent(stringMap.get("Link"));

        if (boolMap.get("UseHexColor")) {
            dcLink.setColor(ChatColor.of(Color.decode(stringMap.get("HexColor"))));
        } else {
            dcLink.setColor(ChatColor.of(stringMap.get("LinkColor")));
        }

        dcLink.setBold(boolMap.get("LinkIsBold"));
        dcLink.setItalic(boolMap.get("LinkIsItalic"));
        dcLink.setUnderlined(boolMap.get("LinkIsUnderlined"));

        dcLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, stringMap.get("Link")));

        ComponentBuilder dcMsgBuilder = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', stringMap.get("Prefix") + stringMap.get("Message"))).append(dcLink);
        player.sendMessage(dcMsgBuilder.create());

    }

    private void sendNoPermissionMessage(ProxiedPlayer player) {
        ComponentBuilder noPermBuilder = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', stringMap.get("Prefix") + stringMap.get("NoPermission")));
        player.sendMessage(noPermBuilder.create());
    }

    private void updateValues() {
        stringMap = ConfigHandler.getStringValues();
        boolMap = ConfigHandler.getBooleanValues();
    }

}
