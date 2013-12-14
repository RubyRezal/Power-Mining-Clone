package org.bitbucket.bloodyshade.listeners;

import org.bitbucket.bloodyshade.PowerMining;
import org.bitbucket.bloodyshade.crafting.CraftItemExcavator;
import org.bitbucket.bloodyshade.crafting.CraftItemHammer;
import org.bitbucket.bloodyshade.lib.Reference;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftItemListener implements Listener {
	PowerMining plugin;

	public CraftItemListener(PowerMining plugin) {
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void canCraft(CraftItemEvent event) {
		boolean isCustom = false;

		ItemStack resultItem = event.getRecipe().getResult();

		if (Reference.PICKAXES.contains(resultItem.getType())) {
			if (resultItem.getItemMeta().getLore().contains(CraftItemHammer.loreString)) {
				isCustom = true;
			}
		}
		else if (Reference.SPADES.contains(resultItem.getType())) {
			if (resultItem.getItemMeta().getLore().contains(CraftItemExcavator.loreString)) {
				isCustom = true;
			}
		}

		if (isCustom && !event.getWhoClicked().hasPermission("powermining.craft"))
			event.setCancelled(true);
	}
}
