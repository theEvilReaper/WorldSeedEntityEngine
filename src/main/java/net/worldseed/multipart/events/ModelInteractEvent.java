package net.worldseed.multipart.events;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.worldseed.gestures.EmoteModel;
import net.worldseed.multipart.GenericModel;
import org.jetbrains.annotations.NotNull;

/**
 * The event is fired from the engine when an entity interacts with a model
 * @param model the model that the entity interacted with
 * @param interactor the entity that interacted with the model
 */
public record ModelInteractEvent(@NotNull GenericModel model, @NotNull Entity interactor) implements ModelEvent {

    /**
     * Creates a new instance from the event but the model is a {@link EmoteModel}.
     * @param model the model that the entity interacted with
     * @param event the event that was fired
     */
    public ModelInteractEvent(@NotNull EmoteModel model, @NotNull PlayerEntityInteractEvent event) {
        this(model, event.getPlayer());
    }
}
