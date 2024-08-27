package net.worldseed.multipart.events;

import net.minestom.server.entity.Entity;
import net.worldseed.multipart.GenericModel;
import org.jetbrains.annotations.NotNull;

/**
 * The event is fired from the engine when a rider dismounts from a model
 * @param model the model that the rider dismounted from
 * @param rider the entity that dismounted from the model
 */
public record ModelDismountEvent(@NotNull GenericModel model, @NotNull Entity rider) implements ModelEvent { }

