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
		/*log.info("|=================================|");
		log.info("|       Created By scranner       |");
		log.info("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		log.info("|       Simple Game Modes         |");
		log.info("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		log.info("|      --==  Disabled  ==--       |");
		log.info("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		log.info("|              v1.0               |");
		log.info("|=================================|");*/
	}

	public void onEnable() {
		log.info("[GameMode] by scranner enabled successfully.");
		/*log.info("|=================================|");
		log.info("|       Created By scranner       |");
		log.info("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		log.info("|        Simple Game Modes        |");
		log.info("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		log.info("|       --==  Enabled  ==--       |");
		log.info("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		log.info("|               v1.0              |");
		log.info("|=================================|");*/
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
			} else if (args.length > 0) { // If more than /gm was typed	
				if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) { // If a player asks for help, ignore further parameters
					usage(player);
					return true;
				} else {
					if (player.hasPermission("gm.admin")) { // Check for admin permission before checking parameters - more efficient
						Player target;
						String name;
						Player[] players = server.getOnlinePlayers();
						for (int i = 0; i < players.length; i++) {
							target = players[i];
							name = target.getDisplayName();
							if (args[0].equalsIgnoreCase(name)) { // If a match between an online player and the first parameter is found
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
				}/* else if (args[0].equals("s")) {	  	    		  
					if (player.hasPermission("gm.survival") || (player.hasPermission("gm.admin"))) {
						if (player.getGameMode() == GameMode.SURVIVAL) {
							player.sendMessage(ChatColor.YELLOW + "You are already in survival mode!");
							return true;
						} else {
							player.sendMessage(ChatColor.YELLOW + "You are now in survival mode!");
							player.setGameMode(GameMode.SURVIVAL);
							return true;
						}
					} else {
						player.sendMessage(ChatColor.RED + "Insufficent Permissions!");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("c")) {
					if (player.hasPermission("gm.creative") || (player.hasPermission("gm.admin"))) { 	    			    	    		
						if (player.getGameMode() == GameMode.CREATIVE) {
							player.sendMessage(ChatColor.YELLOW + "You are already in creative mode!");
							return true;
						} else {
							player.sendMessage(ChatColor.YELLOW + "You are now in creative mode!");
							player.setGameMode(GameMode.CREATIVE);
							return true;
						}
					} else {
						player.sendMessage(ChatColor.RED + "Insufficent Permissions!");
						return true;
					}
				}*/
			}
		}

		return false;
	}


	public void usage(Player player) {
		player.sendMessage("/gm - Toggle your own game mode.");
		player.sendMessage("/gm <player> - Toggle another player's game mode.");
		player.sendMessage("/gm <player> <mode> - Set a player's game mode (0/s or 1/c).");
		player.sendMessage("/gm <player> has - Get the game mode of another player.");
		/*if (player.hasPermission("gm.admin")) {
			player.sendMessage(ChatColor.RED + "Commands");
			player.sendMessage(ChatColor.YELLOW + "	/gm help");
			player.sendMessage(ChatColor.YELLOW + "	/gm c - sets your game mode to creative");
			player.sendMessage(ChatColor.YELLOW + "	/gm s - sets your game mode to survival");
			player.sendMessage(ChatColor.YELLOW + "	/gm toggle - toggles between survival and creative");
			player.sendMessage(ChatColor.YELLOW + "	/gm s (username) - set anothers game mode to survival");
			player.sendMessage(ChatColor.YELLOW + "	/gm c (username) - set anothers game mode to creative");
		} else if (player.hasPermission("gm.creative") && (player.hasPermission("gm.survival"))) {
			player.sendMessage(ChatColor.RED + "Commands");
			player.sendMessage(ChatColor.YELLOW + "	/gm help");
			player.sendMessage(ChatColor.YELLOW + "	/gm c - sets your game mode to creative");
			player.sendMessage(ChatColor.YELLOW + "	/gm s - sets your game mode to survival");
			player.sendMessage(ChatColor.YELLOW + "	/gm toggle - toggles between survival and creative");
		} else if (player.hasPermission("gm.creative")) {
			player.sendMessage(ChatColor.RED + "Commands");
			player.sendMessage(ChatColor.YELLOW + "	/gm help");
			player.sendMessage(ChatColor.YELLOW + "	/gm c - sets your game mode to creative");			
		} else if (player.hasPermission("gm.survival")) {
			player.sendMessage(ChatColor.RED + "Commands");
			player.sendMessage(ChatColor.YELLOW + "	/gm help");
			player.sendMessage(ChatColor.YELLOW + "	/gm s - sets your game mode to survival");
		} else {
			player.sendMessage(ChatColor.YELLOW + "You have no commands avalable to you!");
		}*/
	}
}
/**
 * CHANGELOG
 * v 0.1
 * ? - Finished coding.
 * 
 * v 0.2
 * ? - Fixed bugs.
 * 
 * v 1.0
 * ? - Public release.
 * 
 * v 1.1
 * ? - Added /gm toggle.
 * ? - Fixed more bugs.
 * 
 * v 2.0 by PatPeter
 * 2011-12-24 19:30 - Commented out bloated onEnable(), onDisable(), and usage() body.
 * 2011-12-24 19:30 - Replaced onEnable() and onDisable() bodies with single-line messages.
 * 2011-12-24 19:30 - Replaced usage() body with a single-line message.
 * 2011-12-24 19:35 - Changed "/gm toggle" to simply /gm.
 * 2011-12-24 19:35 - Added ? to help check.
 * 2011-12-31 18:30 - Added more lines to usage without repetition.
 * 2011-12-31 18:40 - Moved gm.admin test outside the for loop - no need to have it loop and
 * then test twice if the player cannot change the game mode of another.
 * 2011-12-31 19:15 - Re-added ability to use 0 and 1 for survival and creative, respectively.
 * 2011-12-31 19:35 - Fixed several typos, i.e. "Insufficent" and "isent".
 * 2011-12-31 19:45 - Changed toggle logic so that a player can only switch to the game mode he
 * or she has permissions for, instead of requiring that that player have both gm.survival and
 * gm.creative, which made both of these redundant.
 * 2011-12-31 20:00 - Offloaded algorithm inefficiency to memory in for loop.
 * 
 * v 2.1 by PatPeter
 * 2012-01-01 00:30 - Removed redundant information on alternate player toggle.
 * 
 */