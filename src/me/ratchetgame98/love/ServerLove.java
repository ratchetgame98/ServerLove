package me.ratchetgame98.love;

import java.util.List;
import java.util.logging.Logger;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerLove extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static ServerLove plugin;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
		this.saveConfig();
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
		this.getConfig().set("Updater", this.getConfig().get("Updater"));
		if(this.getConfig().getBoolean("Updater")== true){
			Updater updater = new Updater(this, 84782, getFile(), UpdateType.DEFAULT, true);
			updater.getResult();
		}else{
			Updater updater = new Updater(this, 84782, getFile(), UpdateType.NO_DOWNLOAD, true);
			updater.getResult();
		}
		this.saveConfig();
	}
	
public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
	if(commandLabel.equalsIgnoreCase("marry")){
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Usage: /marry <args>");
			sender.sendMessage(ChatColor.RED + "Available Args: Propose, Accept/Deny, Wedding, CheckPropose, Check, Divorce");
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("propose")){
				sender.sendMessage(ChatColor.RED + "Please specify your partner");
			}else if(args[0].equalsIgnoreCase("accept")){
				sender.sendMessage(ChatColor.RED + "Please specify your partner");
			}else if(args[0].equalsIgnoreCase("deny")){
				sender.sendMessage(ChatColor.RED + "Please specify your partner");
			}else if(args[0].equalsIgnoreCase("Divorce")){
				sender.sendMessage(ChatColor.RED + "Please specify your partner");
			}
		}else if(args.length == 2){
			final Player target = Bukkit.getServer().getPlayer(args[1]);
			if(args[0].equalsIgnoreCase("propose")){
				if(target == null){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(args[1].equals(sender.getName())){
					sender.sendMessage(ChatColor.YELLOW + "You can't marry yourself :P");
					sender.getServer().broadcastMessage(sender.getName() + ChatColor.YELLOW + " tried to marry themself :P." + ChatColor.GOLD + " #ForeverAlone");
				}else if(this.getConfig().getBoolean("Marry." + target.getName() + ".married") == true){
					sender.sendMessage(ChatColor.RED + "That player already has a partner");
				}else if(this.getConfig().getString("Blocked." + target.getName() + ".block").contains(sender.getName())){
					sender.sendMessage(ChatColor.RED + "This player has blocked you from sending them requests");
					sender.sendMessage(ChatColor.RED + "You may PM them to unblock you if you wish");
				}else{
					if(this.getConfig().getList("Blocked." + target.getName() + ".block") == null){
						this.getConfig().set("Marry." + target.getName() + ".propose", sender.getName());
						this.getConfig().set("Marry." + target.getName() + ".check", true);
						sender.sendMessage(ChatColor.GREEN + "You have proposed to " + ChatColor.RESET + target.getName() + ChatColor.GREEN + " and we shall see how they reply to your proposal");
						target.sendMessage(sender.getName() + ChatColor.BLUE + " has proposed to marry you. Use " + ChatColor.GOLD + "/marry accept/deny " + ChatColor.RESET + sender.getName() + ChatColor.BLUE + " to reply to their proposal");
						sender.getServer().broadcastMessage(ChatColor.GOLD + "[MARRIAGE] " + ChatColor.RESET + sender.getName() + ChatColor.GREEN + " has proposed to marry " + ChatColor.RESET + target.getName());
					}
					if(!this.getConfig().getString("Blocked." + target.getName() + ".block").contains(sender.getName())){
						this.getConfig().set("Marry." + target.getName() + ".propose", sender.getName());
						this.getConfig().set("Marry." + target.getName() + ".check", true);
						sender.sendMessage(ChatColor.GREEN + "You have proposed to " + ChatColor.RESET + target.getName() + ChatColor.GREEN + " and we shall see how they reply to your proposal");
						target.sendMessage(sender.getName() + ChatColor.BLUE + " has proposed to marry you. Use " + ChatColor.GOLD + "/marry accept/deny " + ChatColor.RESET + sender.getName() + ChatColor.BLUE + " to reply to their proposal");
						sender.getServer().broadcastMessage(ChatColor.GOLD + "[MARRIAGE] " + ChatColor.RESET + sender.getName() + ChatColor.GREEN + " has proposed to marry " + ChatColor.RESET + target.getName());
					}
					
				}
			}if(args[0].equalsIgnoreCase("accept")){
				if(target == null){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".propose") != target.getName()){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".propose", target.getName()) != null){
					sender.sendMessage(ChatColor.GREEN + "You are now successfully engaged to " + ChatColor.RESET + target.getName());
					target.sendMessage(sender.getName() + ChatColor.GREEN + " has accepted your proposal, and you are now successfully engaged");
					sender.getServer().broadcastMessage(ChatColor.GOLD + "[MARRIAGE] " + ChatColor.RESET + target.getName() + ChatColor.GREEN + " and " + ChatColor.RESET + sender.getName() + ChatColor.GREEN + " are now happily engaged and will be married shortly");
					this.getConfig().set("Marry." + target.getName() + ".check", false);
					this.getConfig().set("Marry." + sender.getName() + ".propose", null);
					this.getConfig().set("Marry." + target.getName() + ".propose", null);
					this.getConfig().set("Marry." + sender.getName() + ".engaged", target.getName());
					this.getConfig().set("Marry." + target.getName() + ".engaged", sender.getName());
					this.getConfig().set("Marry." + sender.getName() + ".marriage", true);
					this.getConfig().set("Marry." + target.getName() + ".marriage", true);
				}
			}else if(args[0].equalsIgnoreCase("deny")){
				if(target == null){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".propose") != target.getName()){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".propose", target.getName()) != null){
					sender.sendMessage(ChatColor.DARK_RED + "You have successfully denied the proposal by " + ChatColor.RESET + target.getName());
					target.sendMessage(sender.getName() + ChatColor.RED + " has denied your proposal");
					sender.getServer().broadcastMessage(ChatColor.GOLD + "[MARRIAGE] " + ChatColor.RESET + sender.getName() + ChatColor.RED + " has denied the marriage proposal by " + ChatColor.RESET + target.getName());
					this.getConfig().set("Marry." + target.getName() + ".check", false);
					this.getConfig().set("Marry." + sender.getName() + ".propose", null);
					this.getConfig().set("Marry." + target.getName() + ".propose", null);
				}
			}else if(args[0].equalsIgnoreCase("wedding")){
				if(target == null){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".engaged") != target.getName()){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".engaged", target.getName()) != null){
					sender.getServer().broadcastMessage(ChatColor.GOLD + "[MARRIAGE] " + ChatColor.RESET + sender.getName() + ChatColor.GREEN + " and " + ChatColor.RESET + target.getName() + ChatColor.GREEN + "'s " + ChatColor.GREEN + "wedding is about to take place now");
					target.sendMessage(ChatColor.GREEN + "Use " + ChatColor.GOLD + "/marry wedding " + ChatColor.RESET + sender.getName() + ChatColor.GOLD + " accept/deny " + ChatColor.GREEN + "to accept or deny the marriage");
				}
			}else if(args[0].equalsIgnoreCase("CheckPropose")){
				if(this.getConfig().getBoolean("Marry." + sender.getName() + ".check") == true){
					sender.sendMessage(ChatColor.GREEN + "You have a pending proposal by " + ChatColor.RESET + this.getConfig().getString("Marry." + sender.getName() + ".propose").toString());
				}else{
					sender.sendMessage(ChatColor.RED + "You have no current pending proposals");
				}
			}else if(args[0].equalsIgnoreCase("divorce")){
				if(target == null){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".married") != target.getName()){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".married", target.getName()) != null){
					if(this.getConfig().getBoolean("DivorceConfirmation") ==  true){
						sender.sendMessage(ChatColor.RED + "We have sent a confirmation to your partner about the divorce");
						target.sendMessage(ChatColor.RED + "Your partner wants a divorce and you need to confirm the divorce as well. Use " + ChatColor.GOLD + "/marry divorce " + ChatColor.RESET + sender.getName() + ChatColor.GOLD + " confirm " + ChatColor.RED + "to confirm the divorce");
					}else{
						sender.sendMessage(ChatColor.RED + "You have successfully confirmed your divorce with " + ChatColor.RESET + target.getName());
						target.sendMessage(ChatColor.RED + "Your marriage with " + ChatColor.RESET + sender.getName() + ChatColor.RED + " has ended");
						sender.getServer().broadcastMessage(ChatColor.GOLD + "[MARRIAGE] " + ChatColor.RED + "The marriage between " + ChatColor.RESET + sender.getName() + ChatColor.RED + " and " + ChatColor.RESET + target.getName() + ChatColor.RED + " has ended");
						this.getConfig().set("Marry." + sender.getName() + ".married", null);
						this.getConfig().set("Marry." + target.getName() + ".married", null);
						this.getConfig().set("Marry." + sender.getName() + ".marriage", false);
						this.getConfig().set("Marry." + target.getName() + ".marriage", false);
					}
				}
			}
		}if(args.length == 3){
			final Player target = Bukkit.getServer().getPlayer(args[1]);
			if(args[0].equalsIgnoreCase("wedding")){
				if(target == null){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".engaged") != target.getName()){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(this.getConfig().get("Marry." + sender.getName() + ".engaged", target.getName()) != null){
					}if(args[2].equalsIgnoreCase("accept")){
						sender.sendMessage(ChatColor.GREEN + "You are now successfully married to " + ChatColor.RESET + target.getName());
						target.sendMessage(ChatColor.GREEN + "You are now successfully married to " + ChatColor.RESET + sender.getName());
						sender.getServer().broadcastMessage(ChatColor.GOLD + "[MARRIAGE]" + ChatColor.RESET + sender.getName() + ChatColor.GREEN + " and " + ChatColor.RESET + target.getName() + ChatColor.GREEN + " have successfully married to eachother");
						this.getConfig().set("Marry." + sender.getName() + ".engaged", null);
						this.getConfig().set("Marry." + target.getName() + ".engaged", null);
						this.getConfig().set("Marry." + sender.getName() + ".married", target.getName());
						this.getConfig().set("Marry." + target.getName() + ".married", sender.getName());
					}else if(args[2].equalsIgnoreCase("deny")){
						sender.sendMessage(ChatColor.RED + "Your marriage has been denied");
						target.sendMessage(ChatColor.RED + "Your marriage had been denied");
						this.getConfig().set("Marry." + sender.getName() + ".engaged", null);
						this.getConfig().set("Marry." + target.getName() + ".engaged", null);
					}
				}if(args[0].equalsIgnoreCase("divorce")){
					if(target == null){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Marry." + sender.getName() + ".married") != target.getName()){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Marry." + sender.getName() + ".married", target.getName()) != null){
						if(args[2].equalsIgnoreCase("confirm")){
							sender.sendMessage(ChatColor.RED + "You have successfully confirmed your divorce with " + ChatColor.RESET + target.getName());
							target.sendMessage(ChatColor.RED + "Your marriage with " + ChatColor.RESET + sender.getName() + ChatColor.RED + " has ended");
							sender.getServer().broadcastMessage(ChatColor.GOLD + "[MARRIAGE] " + ChatColor.RED + "The marriage between " + ChatColor.RESET + sender.getName() + ChatColor.RED + " and " + ChatColor.RESET + target.getName() + ChatColor.RED + " has ended");
							this.getConfig().set("Marry." + sender.getName() + ".married", null);
							this.getConfig().set("Marry." + target.getName() + ".married", null);
							this.getConfig().set("Marry." + sender.getName() + ".marriage", false);
							this.getConfig().set("Marry." + target.getName() + ".marriage", false);
						}
					}
				}
			}this.saveConfig();
		}if(commandLabel.equalsIgnoreCase("date")){
			if(args.length == 0){
				sender.sendMessage(ChatColor.RED + "Usage: /date <args>");
				sender.sendMessage(ChatColor.RED + "Available Args: Propose, Accept/Deny, CheckPropose, BreakUp");
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("propose")){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(args[0].equalsIgnoreCase("accept")){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(args[0].equalsIgnoreCase("deny")){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(args[0].equalsIgnoreCase("BreakUp")){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else if(args[0].equalsIgnoreCase("Block")){
					sender.sendMessage(ChatColor.RED + "Please specify who you want to block");
				}
			}else if(args.length == 2){
				final Player target = Bukkit.getServer().getPlayer(args[1]);
				if(args[0].equalsIgnoreCase("propose")){
					if(target == null){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(args[1].equals(sender.getName())){
						sender.sendMessage(ChatColor.YELLOW + "You can't date yourself :P");
						sender.getServer().broadcastMessage(sender.getName() + ChatColor.YELLOW + " tried to date themself :P." + ChatColor.GOLD + " #ForeverAlone");
					}else if(this.getConfig().getBoolean("Date." + target.getName() + ".dated") == true){
						sender.sendMessage(ChatColor.RED + "That player already has a partner");
					}else if(this.getConfig().getString("Blocked." + target.getName() + ".block").contains(sender.getName())){
						sender.sendMessage(ChatColor.RED + "This player has blocked you from sending them requests");
						sender.sendMessage(ChatColor.RED + "You may PM them to unblock you if you wish");
					}else{
						if(this.getConfig().getString("Blocked." + target.getName() + ".block")== null){
							this.getConfig().set("Date." + target.getName() + ".propose", sender.getName());
							sender.sendMessage(ChatColor.GREEN + "You have proposed to " + ChatColor.RESET + target.getName() + ChatColor.GREEN + " and we shall see how they reply to your proposal");
							target.sendMessage(sender.getName() + ChatColor.BLUE + " has asked you out on a date. Use " + ChatColor.GOLD + "/date accept/deny " + ChatColor.RESET + sender.getName() + ChatColor.BLUE + " to reply to their proposal");
							sender.getServer().broadcastMessage(ChatColor.GOLD + "[DATE] " + ChatColor.RESET + sender.getName() + ChatColor.GREEN + " has proposed to date " + ChatColor.RESET + target.getName());
						}
						if(!this.getConfig().getString("Blocked." + target.getName() + ".block").contains(sender.getName())){
							this.getConfig().set("Date." + target.getName() + ".propose", sender.getName());
							sender.sendMessage(ChatColor.GREEN + "You have proposed to " + ChatColor.RESET + target.getName() + ChatColor.GREEN + "and we shall see how they reply to your proposal");
							target.sendMessage(sender.getName() + ChatColor.BLUE + " has asked you out on a date. Use " + ChatColor.GOLD + "/date accept/deny " + ChatColor.RESET + sender.getName() + ChatColor.BLUE + " to reply to their proposal");
							sender.getServer().broadcastMessage(ChatColor.GOLD + "[DATE] " + ChatColor.RESET + sender.getName() + ChatColor.GREEN + " has proposed to date " + ChatColor.RESET + target.getName());
						}
					}
				}else if(args[0].equalsIgnoreCase("accept")){
					if(target == null){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Date." + sender.getName() + ".propose") != target.getName()){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Date." + sender.getName() + ".propose", target.getName()) != null){
						sender.sendMessage(ChatColor.GREEN + "You are now successfully dating " + ChatColor.RESET + target.getName());
						target.sendMessage(sender.getName() + ChatColor.GREEN + " has accepted your proposal, and you are now successfully dating");
						sender.getServer().broadcastMessage(ChatColor.GOLD + "[DATE] " + ChatColor.RESET + target.getName() + ChatColor.GREEN + " and " + ChatColor.RESET + sender.getName() + ChatColor.GREEN + " are now happily dating eachother");
						this.getConfig().set("Date." + sender.getName() + ".propose", null);
						this.getConfig().set("Date." + target.getName() + ".propose", null);
						this.getConfig().set("Date." + sender.getName() + ".dating", target.getName());
						this.getConfig().set("Date." + target.getName() + ".dating", sender.getName());
						this.getConfig().set("Date." + sender.getName() + ".dated", true);
						this.getConfig().set("Date." + target.getName() + ".dated", true);
					}
				}else if(args[0].equalsIgnoreCase("deny")){
					if(target == null){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Date." + sender.getName() + ".propose") != target.getName()){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Date." + sender.getName() + ".propose", target.getName()) != null){
						sender.sendMessage(ChatColor.DARK_RED + "You have successfully denied the proposal by " + ChatColor.RESET + target.getName());
						target.sendMessage(sender.getName() + ChatColor.RED + " has denied your proposal");
						sender.getServer().broadcastMessage(ChatColor.GOLD + "[DATE] " + ChatColor.RESET + sender.getName() + ChatColor.RED + " has denied the dating proposal by " + ChatColor.RESET + target.getName());
						this.getConfig().set("Date." + sender.getName() + ".propose", null);
						this.getConfig().set("Date." + target.getName() + ".propose", null);
					}
				}else if(args[0].equalsIgnoreCase("BreakUp")){
					if(target == null){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Date." + sender.getName() + ".dating") != target.getName()){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Date." + sender.getName() + ".dating", target.getName()) != null){
						sender.sendMessage(ChatColor.RED + "We have sent a confirmation to your partner about the Break Up");
						target.sendMessage(ChatColor.RED + "Your partner wants to Break Up and you need to confirm the Break Up as well. Use " + ChatColor.GOLD + "/date breakup " + ChatColor.RESET + sender.getName() + ChatColor.GOLD + " confirm " + ChatColor.RED + "to confirm the Break Up");
					}
				}else if(args[0].equalsIgnoreCase("CheckPropose")){
					this.getConfig().getString("Date." + sender.getName() + ".propose");
					if(this.getConfig().get("Date." + sender.getName() + ".propose") == null){
						sender.sendMessage(ChatColor.RED + "No pending proposals");
					}else{
						sender.sendMessage(ChatColor.RED + "You have a pending proposal from " + ChatColor.RESET + this.getConfig().getString("Marry." + sender.getName() + ".propose"));
					}
				}
			}else if(args.length == 3){
				final Player target = Bukkit.getServer().getPlayer(args[1]);
				if(args[0].equalsIgnoreCase("breakup")){
					if(target == null){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Date." + sender.getName() + ".dating") != target.getName()){
						sender.sendMessage(ChatColor.RED + "Please specify your partner");
					}else if(this.getConfig().get("Date." + sender.getName() + ".dating", target.getName()) != null){
						if(args[2].equalsIgnoreCase("confirm")){
							sender.sendMessage(ChatColor.RED + "You have successfully confirmed your Break Up with " + ChatColor.RESET + target.getName());
							target.sendMessage(ChatColor.RED + "Your date with " + ChatColor.RESET + sender.getName() + ChatColor.RED + " has ended");
							sender.getServer().broadcastMessage(ChatColor.GOLD + "[DATE] " + ChatColor.RED + "The date between " + ChatColor.RESET + sender.getName() + ChatColor.RED + " and " + ChatColor.RESET + target.getName() + ChatColor.RED + " has ended");
							this.getConfig().set("Date." + sender.getName() + ".dating", null);
							this.getConfig().set("Date." + target.getName() + ".dating", null);
							this.getConfig().set("Date." + sender.getName() + ".dated", false);
							this.getConfig().set("Date." + target.getName() + ".dated", false);
						}
					}
				}
			}this.saveConfig();
		}if(commandLabel.equalsIgnoreCase("serverlove")){
			if(args.length == 0){
				sender.sendMessage(ChatColor.RED + "Usage: /serverlove <args>");
				sender.sendMessage(ChatColor.RED + "Available Args: Block, Update, Lawyer, DivConf");
			}else if(args.length == 1){
				final Player target = Bukkit.getServer().getPlayer(args[1]);
				if(args[0].equalsIgnoreCase("block")){
					sender.sendMessage(ChatColor.RED + "Please specify who you want to block");
				}if(args[0].equalsIgnoreCase("update")){
					if(sender.hasPermission(new Permissions().update)){
						sender.sendMessage(ChatColor.RED + "Please choose what to change the updater (True/False)");
					}else{
						sender.sendMessage(ChatColor.RED + "You do not have permission to perform this command");
					}
				}if(args[0].equalsIgnoreCase("Lawyer")){
					if(target == null){
						sender.sendMessage(ChatColor.RED + "Please specify a lawyer");
					}else{
						if(this.getConfig().getString("Lawyer." + sender.getName()) != null){
							sender.sendMessage(ChatColor.RED + "You already have a lawyer");
						}else if(this.getConfig().getString("Lawyer." + sender.getName()) != null){
							sender.sendMessage(ChatColor.RED + "That player is already a lawyer");
						}else{
							this.getConfig().set("Lawyer." + sender.getName(), target.getName());
							this.getConfig().set("Lawyer." + target.getName(), sender.getName());
						}
					}
				}if(args[0].equalsIgnoreCase("DivConf")){
					if(sender.hasPermission(new Permissions().divconf)){
						sender.sendMessage(ChatColor.RED + "Usage: /serverlove divconf <true/false>");
					}else{
						sender.sendMessage(ChatColor.RED + "You do not have permission to perform this command");
					}
				}
			}else if(args.length == 2){
				final Player target = Bukkit.getServer().getPlayer(args[1]);
				if(args[0].equalsIgnoreCase("block")){
					if(target == null){
						sender.sendMessage(ChatColor.RED + "Please specify who you want to block");
					}else{
						if(this.getConfig().getString("Blocked." + sender.getName()) != null){
							List<String> block = this.getConfig().getStringList("Blocked." + sender.getName() + ".block");
							block.remove(target.getName());
							this.getConfig().set("Blocked." + sender.getName() + ".block", block);
							sender.sendMessage(ChatColor.RED + "You have successfully unblocked requests from " + ChatColor.RESET + target.getName());
						}else{
							List<String> block = this.getConfig().getStringList("Blocked." + sender.getName() + ".block");
							block.add(target.getName());
							this.getConfig().set("Blocked." + sender.getName() + ".block", block);
							sender.sendMessage(ChatColor.RED + "You have successfully blocked requests from " + ChatColor.RESET + target.getName());
						}
					}
				}else if(args[0].equalsIgnoreCase("update")){
					if(sender.hasPermission(new Permissions().update)){
						if(args[1].equalsIgnoreCase("true")){
							this.getConfig().set("Updater", true);
							sender.sendMessage("Updater changed to true");
						}else if(args[1].equalsIgnoreCase("false")){
							this.getConfig().set("Updater", true);
							sender.sendMessage("Updater changed to false");
						}else if(args[1] == null){
							sender.sendMessage("Please choose true or false");
						}
					}else{
						sender.sendMessage(ChatColor.RED + "You do not have permission to perform this command");
					}
				}else if(args[0].equalsIgnoreCase("divconf")){
					if(sender.hasPermission(new Permissions().divconf)){
						if(args[1].equalsIgnoreCase("true")){
							this.getConfig().set("DivorceConfirmation", true);
							sender.sendMessage("Divorce confirmations are required");
						}else if(args[1].equalsIgnoreCase("false")){
							this.getConfig().set("DivorceConfirmation", false);
							sender.sendMessage("Divorce confirmations are not required");
						}
					}
				}
			}this.saveConfig();
		}else if(commandLabel.equalsIgnoreCase("inlove")){
			if(args.length == 0){
				sender.sendMessage(ChatColor.RED + "Usage: /inlove <player>");
			}else if(args.length == 1){
				final Player target = Bukkit.getServer().getPlayer(args[0]);
				if(target == null){
					sender.sendMessage(ChatColor.RED + "Please specify who you are in love with");
				}else{
					if(this.getConfig().get("Inlove." + sender.getName() + ".inlove") == null){
						sender.sendMessage(ChatColor.GREEN + "You have successfully specified that you are in love with " + ChatColor.RESET + target.getName());
						this.getConfig().set("Inlove." + sender.getName() + ".inlove", target.getName());
						this.saveConfig();
					}else if(this.getConfig().get("Inlove." + sender.getName() + ".inlove") != target.getName()){
						sender.sendMessage(ChatColor.RED + "You must first stop loving who you already love. You currently love " + ChatColor.RESET + this.getConfig().get("Inlove." + sender.getName() + ".inlove").toString());
					}else if(this.getConfig().get("Inlove." + sender.getName() + ".inlove") == target.getName()){
						sender.sendMessage(ChatColor.RED + "You have now stopped loving " + ChatColor.RESET + target.getName());
					}
				}
			}
		}else if(commandLabel.equalsIgnoreCase("kiss")){
			if(args.length == 0){
				sender.sendMessage(ChatColor.RED + "/kiss <partner>");
			}else if(args.length == 1){
				final Player target = Bukkit.getServer().getPlayer(args[0]);
				if(target == null){
					sender.sendMessage(ChatColor.RED + "Please specify your partner");
				}else{
					if(this.getConfig().get("Marry." + sender.getName() + ".married") != target.getName() && this.getConfig().get("Date." + sender.getName() + ".dating") != target.getName()){
						sender.sendMessage(ChatColor.RED + "You are not in a relationship with this player");
					}else if(this.getConfig().getBoolean("Marry." + target.getName() + ".married") == true){
						sender.sendMessage(ChatColor.RED + "This player is already married");
					}else if(this.getConfig().getBoolean("Date." + target.getName() + ".dated") == true){
						sender.sendMessage(ChatColor.RED + "This player is already dating another player");
					}else{
						sender.sendMessage(ChatColor.GREEN + "<3 You have now kissed " + target.getName());
						target.sendMessage(sender.getName() + ChatColor.GREEN + " has kissed you <3. You may kiss them back using " + ChatColor.GOLD + "/kiss " + ChatColor.RESET + sender.getName());
					}
				}
			}
		}
	return false;
	}
}