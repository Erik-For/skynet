package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class ErrorMessages {

    public static final BaseComponent[] noPermission = new ComponentBuilder("You don't have permission to execute this command!").color(ChatColor.RED).create();


}