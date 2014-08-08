package org.samson.bukkit.plugins.regionboard.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.samson.bukkit.plugins.regionboard.region.Region;

public class RegionAutoResetEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private Region region;
	
	public RegionAutoResetEvent(Region region) {
		this.region = region;
	}

	@Override
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public Region getRegion() {
		return region;
	}
	
}
