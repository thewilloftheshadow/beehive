package com.shadowdev.beehive.listeners;

import org.bukkit.event.Listener;

import com.nisovin.shopkeepers.api.events.ShopkeeperOpenUIEvent;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.shadowdev.beehive.BeeHiveSMP;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public final class ShopkeeperOpenUIEventListener implements Listener {
	BeeHiveSMP plugin;

	public ShopkeeperOpenUIEventListener(BeeHiveSMP plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onOpen(ShopkeeperOpenUIEvent event) {
		Shopkeeper shopkeeper = event.getShopkeeper();
		Player player = event.getPlayer();

		if (shopkeeper.getName().length() > 0) {

			String[] validPermissions = { "beehive.shop." + shopkeeper.getName(), "beehive.shop.*", "beehive.admin" };

			this.plugin.debug("Checking permission for shop: " + validPermissions.toString());

			boolean canUse = false;

			for (String permission : validPermissions) {
				if (BeeHiveSMP.perms.playerHas(player, permission)) {
					canUse = true;
					break;
				}
			}

			if (!canUse) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.YELLOW + "You haven't unlocked this shop yet!");
			}
		}
	}

}
