package net.worldseed.multipart.animations;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface AnimationHandler {

    /**
     * Register a new animation to the handler.
     *
     * @param name      name of the animation
     * @param animation json element of the animation
     * @param priority  priority of the animation
     */
    void registerAnimation(@NotNull String name, @NotNull JsonElement animation, int priority);

    /**
     * Register a new animation to the handler.
     *
     * @param animator animation to register
     */
    void registerAnimation(@NotNull ModelAnimation animator);

    /**
     * Play an animation on repeat
     *
     * @param animation name of animation to play
     * @throws IllegalArgumentException if the animation does not exist
     */
    void playRepeat(@NotNull String animation) throws IllegalArgumentException;

    /**
     * Play an animation on repeat
     *
     * @param animation name of animation to play
     * @param direction direction of the animation
     * @throws IllegalArgumentException if the animation does not exist
     */
    void playRepeat(@NotNull String animation, @NotNull AnimationDirection direction) throws IllegalArgumentException;

    /**
     * Stop a repeating animation
     *
     * @param animation name of animation to stop
     */
    void stopRepeat(String animation) throws IllegalArgumentException;

    /**
     * Play an animation once
     *
     * @param animation name of animation to play
     * @param cb        callback to call when animation is finished
     */
    void playOnce(String animation, Runnable cb) throws IllegalArgumentException;

    void playOnce(String animation, AnimationHandlerImpl.AnimationDirection direction, Runnable cb) throws IllegalArgumentException;

    /**
     * Destroy the animation handler
     */
    void destroy();

    /**
     * Get the current animation
     *
     * @return current animation
     */
    String getPlaying();

    Map<String, Integer> animationPriorities();

    enum AnimationDirection {
        FORWARD,
        BACKWARD,
        PAUSE
    }
}