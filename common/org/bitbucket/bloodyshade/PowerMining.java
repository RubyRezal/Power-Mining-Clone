/*
 * This piece of software is part of the PowerMining Bukkit Plugin
 * Author: BloodyShade (dev.bukkit.org/profiles/bloodyshade)
 *
 * Licensed under the LGPL v3
 * Further information please refer to the included lgpl-3.0.txt or the gnu website (http://www.gnu.org/licenses/lgpl)
 */

/*
 * Main Plugin class, responsible for initializing the plugin and it's respective systems, also keeps a reference to the handlers
 */

package org.bitbucket.bloodyshade;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bitbucket.bloodyshade.handlers.BlockBreakHandler;
import org.bitbucket.bloodyshade.handlers.CraftItemHandler;
import org.bitbucket.bloodyshade.handlers.PlayerInteractHandler;

import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public final class PowerMining extends JavaPlugin {
	PlayerInteractHandler handlerPlayerInteract;
	BlockBreakHandler handlerBlockBreak;
	CraftItemHandler handlerCraftItem;
	Plugin worldguard;
	Plugin griefprevention;
	Plugin towny;

	@Override
	public void onEnable(){
		handlerPlayerInteract = new PlayerInteractHandler();
		handlerBlockBreak = new BlockBreakHandler();
		handlerCraftItem = new CraftItemHandler();

		handlerPlayerInteract.Init(this);
		handlerBlockBreak.Init(this);
		handlerCraftItem.Init(this);

		worldguard = getServer().getPluginManager().getPlugin("WorldGuard");
		griefprevention = getServer().getPluginManager().getPlugin("GriefPrevention");
		towny = getServer().getPluginManager().getPlugin("Towny");

		this.saveDefaultConfig();

		getLogger().info("PowerMining plugin was enabled.");
    }

	@Override
	public void onDisable() {
		getLogger().info("PowerMining plugin was disabled.");
	}

	public PlayerInteractHandler getPlayerInteractHandler() {
		return handlerPlayerInteract;
	}

	public BlockBreakHandler getBlockBreakHandler() {
		return handlerBlockBreak;
	}

	public CraftItemHandler getCraftItemHandler() {
		return handlerCraftItem;
	}

	public WorldGuardPlugin getWorldGuard() {
		return (WorldGuardPlugin) worldguard;
	}

	public GriefPrevention getGriefPrevention() {
		return (GriefPrevention) griefprevention;
	}

	public Towny getTowny() {
		return (Towny) towny;
	}
}
