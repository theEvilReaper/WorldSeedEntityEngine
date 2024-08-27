package net.worldseed.multipart.events;

import net.worldseed.multipart.GenericModel;
import net.worldseed.multipart.animations.AnimationHandlerImpl;
import org.jetbrains.annotations.NotNull;

/**
 * The event is fired from the engine when an animation is completed
 * @param model the model that the animation is attached to
 * @param animation the name of the animation that was completed
 * @param direction the direction of the animation
 */
public record AnimationCompleteEvent(
        @NotNull GenericModel model,
        @NotNull String animation,
        @NotNull AnimationHandlerImpl.AnimationDirection direction
) implements ModelEvent { }
