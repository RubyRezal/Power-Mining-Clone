/*
 * This piece of software is part of the PowerMining Bukkit Plugin
 * Author: BloodyShade (dev.bukkit.org/profiles/bloodyshade)
 * 
 * Licensed under the LGPL v3
 * Further information please refer to the included lgpl-3.0.txt or the gnu website (http://www.gnu.org/licenses/lgpl)
 */

/*
 * Keeps a reference to several CONSTs used throughout the code
 */

package org.bitbucket.bloodyshade.lib;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public class Reference {
	public static List<Material> MINABLE = Arrays.asList(
		Material.STONE,
		Material.COBBLESTONE,
		Material.OBSIDIAN,
		Material.MOSSY_COBBLESTONE,
		Material.COAL_ORE,
		Material.REDSTONE_ORE,
		Material.GLOWING_REDSTONE_ORE,
		Material.LAPIS_ORE,
		Material.IRON_ORE,
		Material.GOLD_ORE,
		Material.DIAMOND_ORE,
		Material.EMERALD_ORE,
		Material.SANDSTONE,
		Material.ENDER_STONE,
		Material.NETHERRACK,
		Material.NETHER_BRICK,
		Material.GLOWSTONE,
		Material.QUARTZ_ORE,
		Material.STAINED_CLAY,
		Material.HARD_CLAY
		//Material.PACKED_ICE
	);

	public static List<Material> DIGABLE = Arrays.asList(
		Material.GRASS,
		Material.DIRT,
		Material.SAND,
		Material.GRAVEL,
		Material.CLAY,
		Material.SOUL_SAND,
		Material.SNOW_BLOCK,
		Material.SNOW,
		Material.MYCEL
	);

	public static List<Material> PICKAXES = Arrays.asList(
		Material.WOOD_PICKAXE,
		Material.STONE_PICKAXE,
		Material.IRON_PICKAXE,
		Material.GOLD_PICKAXE,
		Material.DIAMOND_PICKAXE
	);

	public static List<Material> SPADES = Arrays.asList(
		Material.WOOD_SPADE,
		Material.STONE_SPADE,
		Material.IRON_SPADE,
		Material.GOLD_SPADE,
		Material.DIAMOND_SPADE
	);
}
