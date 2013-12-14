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

import java.util.ArrayList;
import java.util.Collections;

import org.bitbucket.bloodyshade.PowerMining;
import org.bitbucket.bloodyshade.crafting.CraftItemExcavator;
import org.bitbucket.bloodyshade.crafting.CraftItemHammer;
import org.bitbucket.bloodyshade.lib.Reference;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

	@EventHandler(priority = EventPriority.LOW)
	public void checkToolAndBreakBlocks(BlockBreakEvent event) {
		if (event.getPlayer() != null) {
			// If player is sneaking, we want the tool to act like a normal pickaxe/shovel
			if (event.getPlayer().isSneaking())
				return;

			Block block = event.getBlock();
			ItemStack handItem = event.getPlayer().getItemInHand();
			String playerName = event.getPlayer().getName();

			Material blockMaterial = block.getType();
			Material playerHandItem = handItem.getType();

			PlayerInteractListener pil = plugin.getPlayerInteractHandler().getListener();
			BlockFace blockFace = pil.getBlockFacebyPlayerName(playerName);

			if (Reference.MINABLE.containsKey(blockMaterial) || Reference.DIGABLE.contains(blockMaterial)) {
				String loreString = "";
				boolean useHammer = false;
				boolean useExcavator = false;

				if (Reference.PICKAXES.contains(playerHandItem)) {
					loreString = CraftItemHammer.loreString;
					useHammer = true;
				}
				else if (Reference.SPADES.contains(playerHandItem)) {
					loreString = CraftItemExcavator.loreString;
					useExcavator = true;
				}
				else {
					return;
				}

				if (!handItem.getItemMeta().getLore().contains(loreString))
					return;

				short curDur = handItem.getDurability();
				short maxDur = handItem.getType().getMaxDurability();

				// Breaks surrounding blocks as long as they match the corresponding tool
				for (Block e: getSurroundingBlocks(blockFace, block)) {
					if ((Reference.MINABLE.containsKey(e.getType()) && useHammer &&
							(Reference.MINABLE.get(e.getType()) == null ||
								Reference.MINABLE.get(e.getType()).contains(handItem.getType()))) ||
							(Reference.DIGABLE.contains(e.getType()) && useExcavator)) {
						if (useDurabilityPerBlock) {
							if (curDur++ < maxDur)
								handItem.setDurability(curDur);
							else
								break;
						}

						e.breakNaturally(handItem);
					}
				}
			}
		}
	}

	public ArrayList<Block> getSurroundingBlocks(BlockFace blockFace, Block targetBlock) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		World world = targetBlock.getWorld();

		int x, y, z;
		x = targetBlock.getX();
		y = targetBlock.getY();
		z = targetBlock.getZ();

		// Check the block face from which the block is being broken in order to get the correct surrounding blocks
		switch(blockFace) {
			case UP:
			case DOWN:
				blocks.add(world.getBlockAt(x+1, y, z));
				blocks.add(world.getBlockAt(x-1, y, z));
				blocks.add(world.getBlockAt(x, y, z+1));
				blocks.add(world.getBlockAt(x, y, z-1));
				blocks.add(world.getBlockAt(x+1, y, z+1));
				blocks.add(world.getBlockAt(x-1, y, z-1));
				blocks.add(world.getBlockAt(x+1, y, z-1));
				blocks.add(world.getBlockAt(x-1, y, z+1));
				break;
			case EAST:
			case WEST:
				blocks.add(world.getBlockAt(x, y, z+1));
				blocks.add(world.getBlockAt(x, y, z-1));
				blocks.add(world.getBlockAt(x, y+1, z));
				blocks.add(world.getBlockAt(x, y-1, z));
				blocks.add(world.getBlockAt(x, y+1, z+1));
				blocks.add(world.getBlockAt(x, y-1, z-1));
				blocks.add(world.getBlockAt(x, y-1, z+1));
				blocks.add(world.getBlockAt(x, y+1, z-1));
				break;
			case NORTH:
			case SOUTH:
				blocks.add(world.getBlockAt(x+1, y, z));
				blocks.add(world.getBlockAt(x-1, y, z));
				blocks.add(world.getBlockAt(x, y+1, z));
				blocks.add(world.getBlockAt(x, y-1, z));
				blocks.add(world.getBlockAt(x+1, y+1, z));
				blocks.add(world.getBlockAt(x-1, y-1, z));
				blocks.add(world.getBlockAt(x+1, y-1, z));
				blocks.add(world.getBlockAt(x-1, y+1, z));
				break;
			default:
				break;
		}

		// Trim the nulls from the list
		blocks.removeAll(Collections.singleton(null));
		return blocks;
	}
}
