/*
 * This piece of software is part of the PowerMining Bukkit Plugin
 * Author: BloodyShade (dev.bukkit.org/profiles/bloodyshade)
 *
 * Licensed under the LGPL v3
 * Further information please refer to the included lgpl-3.0.txt or the gnu website (http://www.gnu.org/licenses/lgpl)
 */

/*
 * This class is responsible for cancelling the enchanting in case the user does not have permission
 */

package org.bitbucket.bloodyshade.listeners;

import java.util.List;

import org.bitbucket.bloodyshade.PowerMining;
import org.bitbucket.bloodyshade.crafting.CraftItemExcavator;
import org.bitbucket.bloodyshade.crafting.CraftItemHammer;
import org.bitbucket.bloodyshade.lib.Reference;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantItemListener implements Listener {
	PowerMining plugin;

	public EnchantItemListener(PowerMining plugin) {
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void canEnchant(EnchantItemEvent event) {
		boolean canEnchant = false;

		Player player = event.getEnchanter();
		ItemStack item = event.getItem();
		List<String> lore = item.getItemMeta().getLore();

		// If the item has no lore, it can't be one of the power tools
		if (lore == null)
			return;

		if (Reference.PICKAXES.contains(item.getType()) || Reference.SPADES.contains(item.getType())) {
			if (lore.contains(CraftItemHammer.loreString) || lore.contains(CraftItemExcavator.loreString)) {
				switch (item.getType()) {
					case WOOD_PICKAXE:
						if (player.hasPermission("powermining.enchant.hammer.wood"))
							canEnchant = true;

						break;
					case STONE_PICKAXE:
						if (player.hasPermission("powermining.enchant.hammer.stone"))
							canEnchant = true;

						break;
					case IRON_PICKAXE:
						if (player.hasPermission("powermining.enchant.hammer.iron"))
							canEnchant = true;

						break;
					case GOLD_PICKAXE:
						if (player.hasPermission("powermining.enchant.hammer.gold"))
							canEnchant = true;

						break;
					case DIAMOND_PICKAXE:
						if (player.hasPermission("powermining.enchant.hammer.diamond"))
							canEnchant = true;

						break;
					case WOOD_SPADE:
						if (player.hasPermission("powermining.enchant.excavator.wood"))
							canEnchant = true;

						break;
					case STONE_SPADE:
						if (player.hasPermission("powermining.enchant.excavator.stone"))
							canEnchant = true;

						break;
					case IRON_SPADE:
						if (player.hasPermission("powermining.enchant.excavator.iron"))
							canEnchant = true;

						break;
					case GOLD_SPADE:
						if (player.hasPermission("powermining.enchant.excavator.gold"))
							canEnchant = true;

						break;
					case DIAMOND_SPADE:
						if (player.hasPermission("powermining.enchant.excavator.diamond"))
							canEnchant = true;

						break;
					default:
						break;
				}
			}
		}

		if (!canEnchant)
			event.setCancelled(true);
	}
}
