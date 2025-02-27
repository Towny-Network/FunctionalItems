package dev.onebiteaidan.functionalItems.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class WeekChangeEvent extends Event {
    static final HandlerList HANDLERS = new HandlerList();

    public WeekChangeEvent() {}

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
