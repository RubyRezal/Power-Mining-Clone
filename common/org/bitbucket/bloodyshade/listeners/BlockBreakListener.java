/*
 * This piece of software is part of the PowerMining Bukkit Plugin
 * Author: BloodyShade (dev.bukkit.org/profiles/bloodyshade)
 *
 * Licensed under the LGPL v3
 * Further information please refer to the included lgpl-3.0.txt or the gnu website (http://www.gnu.org/licenses/lgpl)
 */

/*
 * This class is responsible for handling the actual mining when using a Hammer/Excavator
 */

package org.bitbucket.bloodyshade.listeners;

import java.util.Map;
import org.bitbucket.bloodyshade.PowerMining;
import org.bitbucket.bloodyshade.lib.PowerUtils;
import org.bitbucket.bloodyshade.lib.Reference;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {
	public PowerMining plugin;
	public boolean useDurabilityPerBlock;

	public BlockBreakListener(PowerMining plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		useDurabilityPerBlock = plugin.getConfig().getBoolean("useDurabilityPerBlock");
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void checkToolAndBreakBlocks(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack handItem = player.getItemInHand();
		Material handItemType = handItem.getType();

		if (player != null && (player instanceof Player)) {
			// If the player is sneaking, we want the tool to act like a normal pickaxe/shovel
			if (player.isSneaking())
				return;

			// If the player does not have permission to use the tool, acts like a normal pickaxe/shovel
			if (!PowerUtils.checkUsePermission(player, handItemType))
				return;

			// If this is not a power tool, acts like a normal pickaxe
			if (!PowerUtils.isPowerTool(handItem))
				return;

			Block block = event.getBlock();
			String playerName = player.getName();

			Material blockType = block.getType();

			PlayerInteractListener pil = plugin.getPlayerInteractHandler().getListener();
			BlockFace blockFace = pil.getBlockFaceByPlayerName(playerName);

			boolean useHammer = PowerUtils.isMineable(blockType);
			boolean useExcavator = PowerUtils.isDigable(blockType);

			// If the block is not allowed to be mined or dug, acts like a normal pickaxe/shovel
			if (!useHammer && !useExcavator)
				return;

			Map<Enchantment, Integer> enchants = handItem.getEnchantments();
			Enchantment enchant = null;
			int enchantLevel = 0;
			if (enchants.get(Enchantment.SILK_TOUCH) != null) {
				enchant = Enchantment.SILK_TOUCH;
				enchantLevel = enchants.get(Enchantment.SILK_TOUCH);
			}
			else if (enchants.get(Enchantment.LOOT_BONUS_BLOCKS) != null) {
				enchant = Enchantment.LOOT_BONUS_BLOCKS;
				enchantLevel = enchants.get(Enchantment.LOOT_BONUS_BLOCKS);
			}

			short curDur = handItem.getDurability();
			short maxDur = handItem.getType().getMaxDurability();

			// Breaks surrounding blocks as long as they match the corresponding tool
			for (Block e: PowerUtils.getSurroundingBlocks(blockFace, block)) {
				Material blockMat = e.getType();
				Location blockLoc = e.getLocation();

				if ((PowerUtils.isMineable(blockMat) && useHammer &&
						(Reference.MINABLE.get(blockMat) == null || Reference.MINABLE.get(blockMat).contains(handItem.getType()))) ||
						(PowerUtils.isDigable(blockMat) && useExcavator)) {

					// Check if player have permission to break the block
					if (!PowerUtils.canBreak(plugin, player, e))
						continue;

					// Snowballs do not drop if you just breakNaturally(), so this needs to be special parsed
					if (blockMat == Material.SNOW && useExcavator) {
						ItemStack snow = new ItemStack(Material.SNOW_BALL, 1 + e.getData());
						e.getWorld().dropItemNaturally(blockLoc, snow);
					}
					// If there is no enchant on the item or the block is not on the effect lists, just break
					else if (enchant == null || (!PowerUtils.canSilkTouch(blockMat) && !PowerUtils.canFortune(blockMat)))
						e.breakNaturally(handItem);
					else {
						ItemStack drop = PowerUtils.processEnchantsAndReturnItemStack(enchant, enchantLevel, e);

						if (drop != null) {
							e.getWorld().dropItemNaturally(blockLoc, drop);
							e.setType(Material.AIR);
						}
					}
					
					// If this is set, durability will be reduced from the tool for each broken block
					if (useDurabilityPerBlock) {
						if (curDur++ < maxDur)
							handItem.setDurability(curDur);
						else
							break;
					}
				}
			}
		}
	}
}
