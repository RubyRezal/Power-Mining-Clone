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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

public class Reference {
	public static HashMap<Material, ArrayList<Material>> MINABLE;
	static {
		// Creates whitelist of Pickaxes per Material
		ArrayList<Material> tempPick = new ArrayList<Material>();

		// Minable by all pickaxes
		MINABLE = new HashMap<Material, ArrayList<Material>>();
		MINABLE.put(Material.COBBLESTONE, null);
		MINABLE.put(Material.STONE, null);
		MINABLE.put(Material.BRICK, null);
		MINABLE.put(Material.MOSSY_COBBLESTONE, null);
		MINABLE.put(Material.COAL_ORE, null);
		MINABLE.put(Material.SANDSTONE, null);
		MINABLE.put(Material.ENDER_STONE, null);
		MINABLE.put(Material.NETHERRACK, null);
		MINABLE.put(Material.NETHER_BRICK, null);
		MINABLE.put(Material.QUARTZ_ORE, null);
		MINABLE.put(Material.STAINED_CLAY, null);
		MINABLE.put(Material.HARD_CLAY, null);

		// Minable by diamond pickaxes
		tempPick.add(Material.DIAMOND_PICKAXE);
		MINABLE.put(Material.OBSIDIAN, tempPick);

		// Minable by iron and diamond pickaxes
		tempPick.add(Material.IRON_PICKAXE);
		MINABLE.put(Material.REDSTONE_ORE, tempPick);
		MINABLE.put(Material.GLOWING_REDSTONE_ORE, tempPick);
		MINABLE.put(Material.GOLD_ORE, tempPick);
		MINABLE.put(Material.DIAMOND_ORE, tempPick);
		MINABLE.put(Material.EMERALD_ORE, tempPick);

		// Minable by stone, iron and diamond pickaxes
		tempPick.add(Material.STONE_PICKAXE);
		MINABLE.put(Material.LAPIS_ORE, tempPick);
		MINABLE.put(Material.IRON_ORE, tempPick);
	};

	public static List<Material> DIGABLE = Arrays.asList(
		Material.GRASS,
		Material.DIRT,
		Material.SAND,
		Material.GRAVEL,
		Material.CLAY,
		Material.SOUL_SAND,
		Material.SNOW_BLOCK,
		Material.SNOW,
		Material.MYCEL,
		Material.SOIL
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
