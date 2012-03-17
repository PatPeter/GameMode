package me.scranner.GameMode;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GameModeWrapper extends JavaPlugin
{
	Logger log = Logger.getLogger("Minecraft");

	public void onDisable() {
		log.info("[GameMode] by scranner disabled successfully.");
	}

	public void onEnable() {
		log.info("[GameMode] by scranner enabled successfully.");
	}


	public boolean toggleGameMode(Player player) {    			 	    
		if (player.getGameMode() == GameMode.CREATIVE) {
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage(ChatColor.YELLOW + "You are now in survival mode.");
		} else if (player.getGameMode() == GameMode.SURVIVAL) {
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage(ChatColor.YELLOW + "You are now in creative mode.");
		}
		return true;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		Server server = player.getServer();

		if (label.equalsIgnoreCase("gm")) {
			if (args.length == 0) { // If only /gm was typed
				GameMode gm = player.getGameMode();
				
				if (player.hasPermission("gm.admin")) {  	
					return toggleGameMode(player);
				} else if (gm == GameMode.SURVIVAL && player.hasPermission("gm.creative")) {
					return toggleGameMode(player);
				} else if (gm == GameMode.CREATIVE && player.hasPermission("gm.survival")) {
					return toggleGameMode(player);
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient permissions.");
				}
				
			// If more than /gm was typed
			} else if (args.length > 0) { 
				// If a player asks for help, ignore further parameters
				if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
					usage(player);
					return true;
				} else {
					// Check for admin permission before checking parameters - more efficient
					if (player.hasPermission("gm.admin")) { 
						Player target;
						String name;
						Player[] players = server.getOnlinePlayers();
						for (int i = 0; i < players.length; i++) {
							target = players[i];
							name = target.getDisplayName();
							
							// If a match between an online player and the first parameter is found
							if (args[0].equalsIgnoreCase(name)) {
								if (args.length == 1) {
									return toggleGameMode(target);
								} else if (args[1].equalsIgnoreCase("0") || args[1].equalsIgnoreCase("s")) {
									if (target.getGameMode() == GameMode.SURVIVAL) {
										player.sendMessage(ChatColor.YELLOW + name + " is already in survival mode.");
										return true;
									} else {
										target.setGameMode(GameMode.SURVIVAL);
										target.sendMessage(ChatColor.YELLOW + "You are now in survival mode.");
										return true;
									}
								} else if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("c")) {
									if (player.getGameMode() == GameMode.CREATIVE) {
										player.sendMessage(ChatColor.YELLOW + name + " is already in creative mode.");
										return true;
									} else {
										player.setGameMode(GameMode.CREATIVE);
										target.sendMessage(ChatColor.YELLOW + "You are now in creative mode.");
										return true;
									}
								} else if (args[1].equalsIgnoreCase("has")) {
									GameMode pgm = target.getGameMode();
									
									if (pgm == GameMode.SURVIVAL) {
										player.sendMessage(ChatColor.YELLOW + name + " is in survival mode.");
										return true;
									} else if (pgm == GameMode.CREATIVE) {
										player.sendMessage(ChatColor.YELLOW + name + " is in creative mode.");
										return true;
									}
								}
							} else {
								player.sendMessage(ChatColor.YELLOW + args[0] + " is not online.");
								return true;
							}	
						}
					} else {
						player.sendMessage(ChatColor.RED + "Insufficient permissions.");
						return true;
					}
				}
			}
		}

		return false;
	}


	public void usage(Player player) {
		player.sendMessage("/gm - Toggle your own game mode.");
		player.sendMessage("/gm <player> - Toggle another player's game mode.");
		player.sendMessage("/gm <player> <mode> - Set a player's game mode (0/s or 1/c).");
		player.sendMessage("/gm <player> has - Get the game mode of another player.");
	}
}