package me.zackpollard.DeathPrice;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeathExecutor implements CommandExecutor{
	
	private DeathPrice plugin;
	
	public DeathExecutor(DeathPrice plugin){
		
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("deathprice.reload") || sender.hasPermission("exemptpvp.admin")){
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
				plugin.reloadConfig();
				sender.sendMessage(ChatColor.GREEN + "ExemptPvP config reloaded");
				
				return true;
			}
		}
		
		sender.sendMessage(ChatColor.RED + "Proper usage is /pvp (exempt/remove/list/reload)");
		return true;
	}
}