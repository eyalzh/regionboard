package org.samson.bukkit.plugins.regionboard.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.samson.bukkit.plugins.regionboard.region.Region;

public class PlayerLeavesRegion extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Region region;
	private Player player;	
	
	public PlayerLeavesRegion(Region region, Player player) {
		this.region = region;
		this.player = player;
	}	
	
	@Override
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }

	public Region getRegion() {
		return region;
	}

	public Player getPlayer() {
		return player;
	}    
    
}
