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
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bitbucket.bloodyshade.PowerMining;
import org.bitbucket.bloodyshade.crafting.CraftItemExcavator;
import org.bitbucket.bloodyshade.crafting.CraftItemHammer;
import org.bitbucket.bloodyshade.lib.Reference;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

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
		if (event.getPlayer() != null) {
			// If the player is sneaking, we want the tool to act like a normal pickaxe/shovel
			if (event.getPlayer().isSneaking())
				return;

			Block block = event.getBlock();
			ItemStack handItem = event.getPlayer().getItemInHand();
			String playerName = event.getPlayer().getName();

			Material blockType = block.getType();
			Material handItemType = handItem.getType();
			
			PlayerInteractListener pil = plugin.getPlayerInteractHandler().getListener();
			BlockFace blockFace = pil.getBlockFacebyPlayerName(playerName);

			if (Reference.MINABLE.containsKey(blockType) || Reference.DIGABLE.contains(blockType)) {
				String loreString = "";
				boolean useHammer = false;
				boolean useExcavator = false;

				if (Reference.PICKAXES.contains(handItemType)) {
					loreString = CraftItemHammer.loreString;
					useHammer = true;
				}
				else if (Reference.SPADES.contains(handItemType)) {
					loreString = CraftItemExcavator.loreString;
					useExcavator = true;
				}
				else {
					return;
				}

				Map<Enchantment, Integer> enchants = handItem.getEnchantments();
				Enchantment enchant = null;
				int enchantLevel = 1;

				if (enchants.containsKey(Enchantment.SILK_TOUCH)) {
					enchant = Enchantment.SILK_TOUCH;
					enchantLevel = enchants.get(Enchantment.SILK_TOUCH);
				}
				else if (enchants.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
					enchant = Enchantment.LOOT_BONUS_BLOCKS;
					enchantLevel = enchants.get(Enchantment.LOOT_BONUS_BLOCKS);
				}

				List<String> lore = handItem.getItemMeta().getLore();
				if (lore == null || !lore.contains(loreString))
					return;

				short curDur = handItem.getDurability();
				short maxDur = handItem.getType().getMaxDurability();

				WorldGuardPlugin wg = plugin.getWorldGuard();
				GriefPrevention gp = plugin.getGriefPrevention();

				// Breaks surrounding blocks as long as they match the corresponding tool
				for (Block e: getSurroundingBlocks(blockFace, block)) {
					Material blockMat = e.getType();
					Location blockLoc = e.getLocation();

					if ((Reference.MINABLE.containsKey(blockMat) && useHammer &&
							(Reference.MINABLE.get(blockMat) == null ||
								Reference.MINABLE.get(blockMat).contains(handItem.getType()))) ||
							(Reference.DIGABLE.contains(blockMat) && useExcavator)) {

						if ((wg != null && (wg instanceof WorldGuardPlugin)) && !wg.canBuild(event.getPlayer(), blockLoc))
							continue;

						if (gp != null && (gp instanceof GriefPrevention)) {
							Claim claim = GriefPrevention.instance.dataStore.getClaimAt(blockLoc, true);

							if (claim != null && claim.allowBreak(event.getPlayer(), e) != null)
								continue;
						}

						if (blockMat == Material.SNOW) {
							ItemStack snow = new ItemStack(Material.SNOW_BALL, 1);
							e.getWorld().dropItemNaturally(blockLoc, snow);
						}

						if (enchant == null ||
								((!Reference.MINEABLE_SILKTOUCH.contains(blockMat) && Reference.MINEABLE_FORTUNE.get(blockMat) == null) &&
								(!Reference.DIGABLE_SILKTOUCH.contains(blockMat) && Reference.DIGABLE_FORTUNE.get(blockMat) == null))) {
							e.breakNaturally(handItem);
						}
						else if (enchant == Enchantment.SILK_TOUCH) {
							ItemStack drop = new ItemStack(blockMat, 1);
							e.getWorld().dropItemNaturally(blockLoc, drop);
							e.setType(Material.AIR);
						}
						else if (enchant == Enchantment.LOOT_BONUS_BLOCKS) {
							int amount = 1;
							Random rand = new Random();
							ItemStack drop = null;

							if (Reference.MINEABLE_FORTUNE.get(blockMat) != null) {
								switch (blockMat) {
									case GLOWSTONE: // Glowstone drops 2-4 dust, up to 4 max
										amount = Math.min((rand.nextInt(5) + 2) + enchantLevel, 4);

										break;
									case REDSTONE_ORE: // Redstone drops 4-5 dust, up to 8 max
									case GLOWING_REDSTONE_ORE:
										amount = Math.min((rand.nextInt(2) + 4) + enchantLevel, 8);

										break;
									case COAL_ORE: // All these drop only 1 item
									case DIAMOND_ORE:
									case EMERALD_ORE:
									case QUARTZ_ORE:
										amount = getAmountPerFortune(enchantLevel, 1);
										plugin.getLogger().info("Total drops: " + Integer.toString(amount));
										break;
									case LAPIS_ORE: // Lapis Ore drops 4-8 lapis, up to 32 max
										amount = Math.min(getAmountPerFortune(enchantLevel, (rand.nextInt(5) + 4)), 32);
										break;
									default:
										break;
								}

								if (blockMat == Material.LAPIS_ORE) {
									drop = new ItemStack(Reference.MINEABLE_FORTUNE.get(blockMat), amount, (short)4);
								}
								else
									drop = new ItemStack(Reference.MINEABLE_FORTUNE.get(blockMat), amount);
							}
							else if (Reference.DIGABLE_FORTUNE.get(blockMat) != null) {
								if (blockMat == Material.GLOWSTONE) {// Glowstone drops 2-4 dust, up to 4 max
									amount = Math.min((rand.nextInt(5) + 2) + enchantLevel, 4);

									drop = new ItemStack(Reference.DIGABLE_FORTUNE.get(blockMat), amount);
								}
								else if (blockMat == Material.GRAVEL) {
									double chance = 0.10;

									if (enchantLevel == 1)
										chance = 0.14;
									else if (enchantLevel == 2)
										chance = 0.25;
									else if (enchantLevel == 3)
										chance = 1.0;

									if (rand.nextFloat() <= chance)
										drop = new ItemStack(Reference.DIGABLE_FORTUNE.get(blockMat), 1);
									else // If no flint is going to be dropped, drop gravel instead
										drop = new ItemStack(blockMat, 1);
								}
							}

							if (drop != null) {
								e.getWorld().dropItemNaturally(blockLoc, drop);
								e.setType(Material.AIR);
							}
						}

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

	public int getAmountPerFortune(int level, int amount) {
		Random rand = new Random();

		if (level == 1 && rand.nextFloat() <= 0.33)
			return amount * 2;
		else if (level == 2) {
			if (rand.nextFloat() <= 0.25)
				return amount * 3;
			if (rand.nextFloat() <= 0.25)
				return amount * 2;
		}
		else if (level == 3) {
			if (rand.nextFloat() <= 0.20)
				return amount * 4;
			if (rand.nextFloat() <= 0.20)
				return amount * 3;
			if (rand.nextFloat() <= 0.20)
				return amount * 2;
		}

		return amount;
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
