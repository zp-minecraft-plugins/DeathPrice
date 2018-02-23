package me.zackpollard.DeathPrice;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MyDeathListener implements Listener {
	public static DeathPrice plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public MyDeathListener(DeathPrice instance){
		plugin = instance;
		Bukkit.getServer().getPluginManager().registerEvents(this,instance);
	}
	@EventHandler(ignoreCancelled = true)
	public void onDeathEvent(EntityDeathEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if(p.hasPermission("deathprice.exempt")){
				
				return;
			}
			
			double balance = DeathPrice.economy.getBalance(p.getName());
			
			if(plugin.getConfig().getBoolean("DeathPrice.AlwaysFixedPrice")){
				
				double amount = plugin.getConfig().getLong("DeathPrice.FixedPrice");
				plugin.removeMoney(p, amount);
				
				if(p.getKiller() instanceof Player){
					Double percent = plugin.getConfig().getDouble("DeathPrice.PercentToKiller");
					if(percent != 0){
						Double payback = (amount / 100) * percent;
						Player killer = (Player) p.getKiller();
						plugin.sendMoney(p.getName(), killer, payback);
					}
				}
				return;
			}
			if(DeathPrice.economy.getBalance(p.getName()) > plugin.getConfig().getLong("DeathPrice.FixedPriceBelow")){
				double amount = balance / 100 * plugin.getConfig().getLong("DeathPrice.PercentageRemovedOnDeath");
				plugin.removeMoney(p, amount);
				if(p.getKiller() instanceof Player){
					Double percent = plugin.getConfig().getDouble("DeathPrice.PercentToKiller");
					if(percent != 0){
						Double payback = (amount / 100) * percent;
						Player killer = (Player) p.getKiller();
						plugin.sendMoney(p.getName(), killer, payback);
					}
				}
			} else {
				double amount = plugin.getConfig().getLong("DeathPrice.FixedPrice");
				plugin.removeMoney(p, amount);
				if(p.getKiller() instanceof Player){
					Double percent = plugin.getConfig().getDouble("DeathPrice.PercentToKiller");
					if(percent != 0){
						Double payback = (amount / 100) * percent;
						Player killer = (Player) p.getKiller();
						plugin.sendMoney(p.getName(), killer, payback);
					}
				}
			}
		}
	}
}