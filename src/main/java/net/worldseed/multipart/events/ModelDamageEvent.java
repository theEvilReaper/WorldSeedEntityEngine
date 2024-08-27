package net.worldseed.multipart.events;

import net.minestom.server.entity.damage.Damage;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.sound.SoundEvent;
import net.worldseed.multipart.GenericModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This event should be used when a model receives damage.
 */
public class ModelDamageEvent implements ModelEvent, CancellableEvent {

    private final GenericModel model;
    private final Damage damage;
    private SoundEvent sound;
    private boolean animation = true;
    private boolean cancelled;

    /**
     * Creates a new instance of the event.
     * @param model the model that is receiving the damage
     * @param damage the damage that is being received
     * @param sound the sound that should be played when the damage is received
     */
    public ModelDamageEvent(@NotNull GenericModel model, @NotNull Damage damage, @Nullable SoundEvent sound) {
        this.model = model;
        this.damage = damage;
        this.sound = sound;
    }

    /**
     * Creates a new instance of the event from an {@link EntityDamageEvent}.
     * It takes some properties from the provided event.
     * @param model the model that is receiving the damage
     * @param event the event that was fired
     */
    public ModelDamageEvent(@NotNull GenericModel model, @NotNull EntityDamageEvent event) {
        this(model, event.getDamage(), event.getSound());
        this.animation = event.shouldAnimate();
    }

    /**
     * Gets the damage.
     *
     * @return the damage
     */
    @NotNull
    public Damage getDamage() {
        return damage;
    }

    /**
     * Gets the damage sound.
     *
     * @return the damage sound
     */
    @Nullable
    public SoundEvent getSound() {
        return sound;
    }

    /**
     * Changes the damage sound.
     *
     * @param sound the new damage sound
     */
    public void setSound(@Nullable SoundEvent sound) {
        this.sound = sound;
    }

    /**
     * Gets whether the damage animation should be played.
     *
     * @return true if the animation should be played
     */
    public boolean shouldAnimate() {
        return animation;
    }

    /**
     * Sets whether the damage animation should be played.
     *
     * @param animation whether the animation should be played or not
     */
    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    /**
     * Returns the current cancellation state from the event.
     * @return the given state
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Updates the cancelled state of the event.
     * @param cancel the new cancelled state
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Returns the model which is involved to the event instance.
     * @return the model that the event is attached to
     */
    @Override
    public @NotNull GenericModel model() {
        return model;
    }
}
