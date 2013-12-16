/*
 * This piece of software is part of the PowerMining Bukkit Plugin
 * Author: BloodyShade (dev.bukkit.org/profiles/bloodyshade)
 *
 * Licensed under the LGPL v3
 * Further information please refer to the included lgpl-3.0.txt or the gnu website (http://www.gnu.org/licenses/lgpl)
 */

/*
 * This class is responsible for cancelling the crafting in case the user does not have permission
 */

package org.bitbucket.bloodyshade.listeners;

import java.util.List;

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

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void canCraft(CraftItemEvent event) {
		boolean isCustom = false;

		ItemStack resultItem = event.getRecipe().getResult();
		List<String> lore = resultItem.getItemMeta().getLore();

		if (Reference.PICKAXES.contains(resultItem.getType())) {
			if (lore != null && lore.contains(CraftItemHammer.loreString)) {
				isCustom = true;
			}
		}
		else if (Reference.SPADES.contains(resultItem.getType())) {
			if (lore != null && lore.contains(CraftItemExcavator.loreString)) {
				isCustom = true;
			}
		}

		if (isCustom && !event.getWhoClicked().hasPermission("powermining.craft"))
			event.setCancelled(true);
	}
}
