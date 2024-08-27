package net.worldseed.multipart.events;

import net.worldseed.multipart.GenericModel;
import org.jetbrains.annotations.NotNull;

/**
 * This event will be fired when a player is controlling a model
 *
 * @param model    the model being controlled
 * @param forward  the forward movement
 * @param sideways the sideways movement
 * @param jump     if the player is jumping
 */
public record ModelControlEvent(
        @NotNull GenericModel model,
        float forward,
        float sideways,
        boolean jump
) implements ModelEvent {
}

