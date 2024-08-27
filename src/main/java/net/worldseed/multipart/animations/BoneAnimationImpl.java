package net.worldseed.multipart.animations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.worldseed.multipart.GenericModel;
import net.worldseed.multipart.ModelEngine;
import net.worldseed.multipart.ModelLoader;
import net.worldseed.multipart.model_bones.ModelBone;
import net.worldseed.multipart.mql.MQLPoint;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BoneAnimationImpl implements BoneAnimation {

    private final ModelLoader.AnimationType type;
    private final FrameProvider frameProvider;
    private final String name;
    private final int length;
    private boolean playing = false;
    private short tick = 0;
    private AnimationHandlerImpl.AnimationDirection direction = AnimationHandlerImpl.AnimationDirection.FORWARD;

    /**
     * Creates a new bone animation for the {@link ModelLoader.AnimationType#SCALE}
     *
     * @param model         the model to attach the animation to
     * @param animationName the name of the animation
     * @param bone          the bone to animate
     * @param keyframes     the keyframes of the animation
     * @param length        the length of the animation
     * @return the created bone animation
     */
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull BoneAnimationImpl ofScale(@NotNull GenericModel model, @NotNull String animationName, @NotNull ModelBone bone, @NotNull JsonElement keyframes, double length) {
        return new BoneAnimationImpl(model.getId(), animationName, bone, keyframes, ModelLoader.AnimationType.SCALE, length);
    }

    /**
     * Creates a new bone animation for the {@link ModelLoader.AnimationType#ROTATION}
     *
     * @param model         the model to attach the animation to
     * @param animationName the name of the animation
     * @param bone          the bone to animate
     * @param keyframes     the keyframes of the animation
     * @param length        the length of the animation
     * @return the created bone animation
     */
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull BoneAnimationImpl ofRotation(@NotNull GenericModel model, @NotNull String animationName, @NotNull ModelBone bone, @NotNull JsonElement keyframes, double length) {
        return new BoneAnimationImpl(model.getId(), animationName, bone, keyframes, ModelLoader.AnimationType.ROTATION, length);
    }

    /**
     * Creates a new bone animation for the {@link ModelLoader.AnimationType#TRANSLATION}
     *
     * @param model         the model to attach the animation to
     * @param animationName the name of the animation
     * @param bone          the bone to animate
     * @param keyframes     the keyframes of the animation
     * @param length        the length of the animation
     * @return the created bone animation
     */
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull BoneAnimationImpl ofTranslation(@NotNull GenericModel model, @NotNull String animationName, @NotNull ModelBone bone, @NotNull JsonElement keyframes, double length) {
        return new BoneAnimationImpl(model.getId(), animationName, bone, keyframes, ModelLoader.AnimationType.TRANSLATION, length);
    }

    /**
     * Create a new bone animation. This constructor is private and should not be used directly.
     * @param modelName the name of the model
     * @param animationName the name of the animation
     * @param bone the bone to animate
     * @param keyframes the keyframes of the animation
     * @param animationType the type of the animation
     * @param length the length of the animation
     */
    BoneAnimationImpl(@NotNull String modelName, @NotNull String animationName, @NotNull ModelBone bone, @NotNull JsonElement keyframes, @NotNull ModelLoader.AnimationType animationType, double length) {
        this.type = animationType;
        this.length = (int) (length * 20);
        this.name = animationName;

        String boneAnimationModel = bone.getName() + "/" + animationName;
        FrameProvider found;
        switch (this.type) {
            case ROTATION -> ModelLoader.getCacheRotation(modelName, boneAnimationModel);
            case TRANSLATION -> ModelLoader.getCacheTranslation(modelName, boneAnimationModel);
            case SCALE -> ModelLoader.getCacheScale(modelName, boneAnimationModel);
        }

        found = length != 0 ? computeCachedTransforms(keyframes) : computeMathTransforms(keyframes);

        switch (this.type) {
            case ROTATION -> ModelLoader.addToRotationCache(modelName, boneAnimationModel, found);
            case TRANSLATION -> ModelLoader.addToTranslationCache(modelName, boneAnimationModel, found);
            case SCALE -> ModelLoader.addToScaleCache(modelName, boneAnimationModel, found);
        }

        this.frameProvider = found;
        bone.addAnimation(this);
    }

    public ModelLoader.AnimationType getType() {
        return type;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void tick() {
        if (!isPlaying()) return;
        if (direction == AnimationHandlerImpl.AnimationDirection.FORWARD) {
            tick++;
            if (tick > length && length != 0) tick = 0;
        } else if (direction == AnimationHandlerImpl.AnimationDirection.BACKWARD) {
            tick--;
            if (tick < 0 && length != 0) tick = (short) length;
        }
    }

    public Point getTransform() {
        if (!this.playing) return switch (this.type) {
            case ROTATION, TRANSLATION -> Vec.ZERO;
            case SCALE -> Vec.ONE;
        };
        return this.frameProvider.getFrame(tick);
    }

    public Point getTransformAtTime(int time) {
        return this.frameProvider.getFrame(time);
    }

    public void setDirection(AnimationHandlerImpl.AnimationDirection direction) {
        this.direction = direction;
    }

    private FrameProvider computeMathTransforms(JsonElement keyframes) {
        LinkedHashMap<Double, PointInterpolation> transform = new LinkedHashMap<>();

        try {
            for (Map.Entry<String, JsonElement> entry : keyframes.getAsJsonObject().entrySet()) {
                double time = Double.parseDouble(entry.getKey());
                MQLPoint point = ModelEngine.getMQLPos(entry.getValue().getAsJsonObject().get("post").getAsJsonArray().get(0).getAsJsonObject()).orElse(MQLPoint.ZERO);
                String lerp = entry.getValue().getAsJsonObject().get("lerp_mode").getAsString();
                transform.put(time, new PointInterpolation(point, lerp));
            }
        } catch (IllegalStateException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException e) {
            try {
                e.printStackTrace();
                MQLPoint point = ModelEngine.getMQLPos(keyframes.getAsJsonObject()).orElse(MQLPoint.ZERO);
                transform.put(0.0, new PointInterpolation(point, "linear"));
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }

        return new ComputedFrameProvider(transform, type, length);
    }

    private FrameProvider computeCachedTransforms(JsonElement keyframes) {
        LinkedHashMap<Double, PointInterpolation> transform = new LinkedHashMap<>();

        try {
            for (Map.Entry<String, JsonElement> entry : keyframes.getAsJsonObject().entrySet()) {
                double time = Double.parseDouble(entry.getKey());

                if (entry.getValue() instanceof JsonObject obj) {
                    if (obj.get("post") instanceof JsonArray arr) {
                        if (arr.get(0) instanceof JsonObject) {
                            MQLPoint point = ModelEngine.getMQLPos(obj.get("post").getAsJsonArray().get(0)).orElse(MQLPoint.ZERO);
                            String lerp = entry.getValue().getAsJsonObject().get("lerp_mode").getAsString();
                            if (lerp == null) lerp = "linear";
                            transform.put(time, new PointInterpolation(point, lerp));
                        } else {
                            MQLPoint point = ModelEngine.getMQLPos(obj.get("post").getAsJsonArray()).orElse(MQLPoint.ZERO);
                            String lerp = entry.getValue().getAsJsonObject().get("lerp_mode").getAsString();
                            if (lerp == null) lerp = "linear";
                            transform.put(time, new PointInterpolation(point, lerp));
                        }
                    }
                } else if (entry.getValue() instanceof JsonArray arr) {
                    MQLPoint point = ModelEngine.getMQLPos(arr).orElse(MQLPoint.ZERO);
                    transform.put(time, new PointInterpolation(point, "linear"));
                }
            }
        } catch (IllegalStateException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException e) {
            try {
                e.printStackTrace();
                MQLPoint point = ModelEngine.getMQLPos(keyframes.getAsJsonObject()).orElse(MQLPoint.ZERO);
                transform.put(0.0, new PointInterpolation(point, "linear"));
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }

        return new CachedFrameProvider(length, transform, type);
    }

    public void stop() {
        this.tick = 0;
        this.playing = false;
        this.direction = AnimationHandler.AnimationDirection.FORWARD;
    }

    public void play() {
        switch (this.direction) {
            case FORWARD -> this.tick = 0;
            case BACKWARD -> this.tick = (short) (length - 1);
            default -> {
            } // Nothing todo when paused
        }
        this.playing = true;
    }

    public String name() {
        return name;
    }

    record PointInterpolation(MQLPoint p, String lerp) { }
}
