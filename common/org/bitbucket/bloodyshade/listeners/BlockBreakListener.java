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

	public BlockBreakListener(PowerMining plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void checkToolAndBreakBlocks(BlockBreakEvent event) {
		if (event.getPlayer() != null) {
			Block block = event.getBlock();
			ItemStack handItem = event.getPlayer().getItemInHand();
			String playerName = event.getPlayer().getName();

			Material blockMaterial = block.getType();
			Material playerHandItem = handItem.getType();

			PlayerInteractListener pil = plugin.getPlayerInteractHandler().getListener();
			BlockFace blockFace = pil.getBlockFacebyPlayerName(playerName);

			// If player is sneaking, we want the tool to act like a normal pickaxe/shovel
			if (event.getPlayer().isSneaking())
				return;

			if (Reference.MINABLE.contains(blockMaterial) || Reference.DIGABLE.contains(blockMaterial)) {
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

				ArrayList<Block> blocks = new ArrayList<Block>();
				World world = event.getPlayer().getWorld();

				int x, y, z;
				x = block.getX();
				y = block.getY();
				z = block.getZ();

				// These check the block face from which the block is being broken in order to get the correct surrounding blocks
				if (blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) {
					blocks.add(world.getBlockAt(x+1, y, z));
					blocks.add(world.getBlockAt(x-1, y, z));
					blocks.add(world.getBlockAt(x, y, z+1));
					blocks.add(world.getBlockAt(x, y, z-1));
					blocks.add(world.getBlockAt(x+1, y, z+1));
					blocks.add(world.getBlockAt(x-1, y, z-1));
					blocks.add(world.getBlockAt(x+1, y, z-1));
					blocks.add(world.getBlockAt(x-1, y, z+1));
				}
				else if (blockFace == BlockFace.EAST || blockFace == BlockFace.WEST) {
					blocks.add(world.getBlockAt(x, y, z+1));
					blocks.add(world.getBlockAt(x, y, z-1));
					blocks.add(world.getBlockAt(x, y+1, z));
					blocks.add(world.getBlockAt(x, y-1, z));
					blocks.add(world.getBlockAt(x, y+1, z+1));
					blocks.add(world.getBlockAt(x, y-1, z-1));
					blocks.add(world.getBlockAt(x, y-1, z+1));
					blocks.add(world.getBlockAt(x, y+1, z-1));					
				}
				else if (blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH) {
					blocks.add(world.getBlockAt(x+1, y, z));
					blocks.add(world.getBlockAt(x-1, y, z));
					blocks.add(world.getBlockAt(x, y+1, z));
					blocks.add(world.getBlockAt(x, y-1, z));
					blocks.add(world.getBlockAt(x+1, y+1, z));
					blocks.add(world.getBlockAt(x-1, y-1, z));
					blocks.add(world.getBlockAt(x+1, y-1, z));
					blocks.add(world.getBlockAt(x-1, y+1, z));
				}

				// Breaks surrounding blocks as long as they match the corresponding tool
				for (Block e: blocks) {
					if (e != null) {
						if ((Reference.MINABLE.contains(e.getType()) && useHammer) ||
								(Reference.DIGABLE.contains(e.getType())) && useExcavator) {
							e.breakNaturally(handItem);
						}
					}
				}

				// This deals durability damage for each broken block
				// Disabled since it feels a bit pointless
/*
				short curDur = handItem.getDurability();
				short maxDur = handItem.getType().getMaxDurability();

				for (Block e: blocks) {
					if (e != null) {
						if ((Reference.MINABLE.contains(e.getType()) && useHammer) ||
								(Reference.DIGABLE.contains(e.getType())) && useExcavator) {
							if (curDur++ < maxDur) {
								e.breakNaturally(handItem);
								handItem.setDurability(curDur);
							}
							else
								break;
						}
					}
				}
*/
			}
		}
	}
}
