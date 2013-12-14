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

import org.bukkit.plugin.java.JavaPlugin;
import org.bitbucket.bloodyshade.handlers.BlockBreakHandler;
import org.bitbucket.bloodyshade.handlers.CraftItemHandler;
import org.bitbucket.bloodyshade.handlers.PlayerInteractHandler;

public final class PowerMining extends JavaPlugin {
	PlayerInteractHandler handlerPlayerInteract;
	BlockBreakHandler handlerBlockBreak;
	CraftItemHandler handlerCraftItem;

	@Override
	public void onEnable(){
		handlerPlayerInteract = new PlayerInteractHandler();
		handlerBlockBreak = new BlockBreakHandler();
		handlerCraftItem = new CraftItemHandler();

		handlerPlayerInteract.Init(this);
		handlerBlockBreak.Init(this);
		handlerCraftItem.Init(this);

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
}
