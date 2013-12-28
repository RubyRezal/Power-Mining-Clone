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
import org.bukkit.Material;

public class Reference {
	public static HashMap<Material, ArrayList<Material>> MINABLE = new HashMap<Material, ArrayList<Material>>();

	public static ArrayList<Material> DIGGABLE = new ArrayList<Material>();

	public static ArrayList<Material> MINABLE_SILKTOUCH =  new ArrayList<Material>(Arrays.asList(
		Material.STONE,
		Material.COAL_ORE,
		Material.REDSTONE_ORE,
		Material.GLOWING_REDSTONE_ORE,
		Material.LAPIS_ORE,
		Material.DIAMOND_ORE,
		Material.EMERALD_ORE,
		Material.ICE,
		Material.QUARTZ_ORE,
		Material.GLOWSTONE
	));

	public static ArrayList<Material> DIGGABLE_SILKTOUCH =  new ArrayList<Material>(Arrays.asList(
		Material.GRASS,
		Material.CLAY,
		Material.SNOW_BLOCK,
		Material.MYCEL,
		Material.GLOWSTONE
	));

	public static HashMap<Material, Material> MINABLE_FORTUNE;
	static {
		MINABLE_FORTUNE = new HashMap<Material, Material>();

		MINABLE_FORTUNE.put(Material.COAL_ORE, Material.COAL);
		MINABLE_FORTUNE.put(Material.REDSTONE_ORE, Material.REDSTONE);
		MINABLE_FORTUNE.put(Material.GLOWING_REDSTONE_ORE, Material.REDSTONE);
		MINABLE_FORTUNE.put(Material.LAPIS_ORE, Material.INK_SACK);
		MINABLE_FORTUNE.put(Material.DIAMOND_ORE, Material.DIAMOND);
		MINABLE_FORTUNE.put(Material.EMERALD_ORE, Material.EMERALD);
		MINABLE_FORTUNE.put(Material.QUARTZ_ORE, Material.QUARTZ);
		MINABLE_FORTUNE.put(Material.GLOWSTONE, Material.GLOWSTONE_DUST);
	};

	public static HashMap<Material, Material> DIGGABLE_FORTUNE;
	static {
		DIGGABLE_FORTUNE = new HashMap<Material, Material>();

		DIGGABLE_FORTUNE.put(Material.GRAVEL, Material.FLINT);
		DIGGABLE_FORTUNE.put(Material.GLOWSTONE, Material.GLOWSTONE_DUST);
	};

	public static ArrayList<Material> PICKAXES =  new ArrayList<Material>(Arrays.asList(
		Material.WOOD_PICKAXE,
		Material.STONE_PICKAXE,
		Material.IRON_PICKAXE,
		Material.GOLD_PICKAXE,
		Material.DIAMOND_PICKAXE
	));

	public static ArrayList<Material> SPADES = new ArrayList<Material>(Arrays.asList(
		Material.WOOD_SPADE,
		Material.STONE_SPADE,
		Material.IRON_SPADE,
		Material.GOLD_SPADE,
		Material.DIAMOND_SPADE
	));
}
