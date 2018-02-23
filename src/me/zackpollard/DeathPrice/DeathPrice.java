package me.zackpollard.DeathPrice;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathPrice extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public static Economy economy = null;
	
	public void onDisable() {
		this.logger.info("DeathPrice is now disabled.");
	}
	public void onEnable() {
		if (!setupEconomy() ) {
			logger.info(ChatColor.RED + String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
	        getServer().getPluginManager().disablePlugin(this);
	        return;
	    }
		new MyDeathListener(this);
		
		this.getCommand("deathprice").setExecutor(new DeathExecutor(this));
		
		final FileConfiguration config = this.getConfig();
        config.options().header("Sets what is blocked ingame");
        
        config.addDefault("DeathPrice.AlwaysFixedPrice", true);
        config.addDefault("DeathPrice.PercentageRemovedOnDeath", Integer.valueOf(5));
        config.addDefault("DeathPrice.FixedPriceBelow", Integer.valueOf(1000));
        config.addDefault("DeathPrice.FixedPrice", Integer.valueOf(50));
        config.addDefault("DeathPrice.PercentToKiller", Integer.valueOf(50));

        config.options().copyDefaults(true);
        saveConfig();
		
		this.logger.info("DeathPrice is now enabled.");
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
	
	public boolean removeMoney(Player p, Double amount){
		
		EconomyResponse r = economy.withdrawPlayer(p.getName(), amount);
		if(r.transactionSuccess()) {
			p.sendMessage(ChatColor.GOLD + "[DeathPrice] - " + ChatColor.WHITE + "- " + ChatColor.RED + "You died and " + amount + " was removed from your account");
			return true;
        } else {
            p.sendMessage(String.format("An error occured: %s", r.errorMessage));
            return false;
        }
	}
	public boolean sendMoney(String player, Player killer, Double amount) {
		
		EconomyResponse r = economy.depositPlayer(killer.getName(), amount);
		if(r.transactionSuccess()) {
			killer.sendMessage(ChatColor.GOLD + "[DeathPrice]" + ChatColor.WHITE + " - " + ChatColor.GREEN + "You killed " + player + " and you have been sent " + amount + economy.currencyNamePlural());
			return true;
		} else {
			killer.sendMessage(String.format("An error occured: %s",  r.errorMessage));
			return false;
		}
	}
}