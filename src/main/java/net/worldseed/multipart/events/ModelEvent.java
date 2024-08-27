package net.worldseed.multipart.events;

import net.minestom.server.event.Event;
import net.worldseed.multipart.GenericModel;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the basic event for all model events in the engine
 */
public interface ModelEvent extends Event {

    /**
     * Returns the model which is involved to the event instance.
     * @return the model that the event is attached to
     */
    @NotNull GenericModel model();
}
