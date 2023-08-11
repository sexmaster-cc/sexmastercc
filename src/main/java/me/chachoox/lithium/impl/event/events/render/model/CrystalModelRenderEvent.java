package me.chachoox.lithium.impl.event.events.render.model;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class CrystalModelRenderEvent extends Event {
    private final ModelBase model;
    private final Entity entity;
    private final float limbSwing;
    private final float limbSwingAmount;
    private final float ageInTicks;
    private final float netHeadYaw;
    private final float headPitch;
    private final float scale;

    private CrystalModelRenderEvent(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.model = model;
        this.entity = entity;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
    }

    public Entity getEntity() {
        return entity;
    }

    public ModelBase getModel() {
        return model;
    }

    public float getLimbSwing() {
        return limbSwing;
    }

    public float getLimbSwingAmount() {
        return limbSwingAmount;
    }

    public float getAgeInTicks() {
        return ageInTicks;
    }

    public float getNetHeadYaw() {
        return netHeadYaw;
    }

    public float getHeadPitch() {
        return headPitch;
    }

    public float getScale() {
        return scale;
    }

    public static class Pre extends CrystalModelRenderEvent {
        public Pre(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public static class Post extends CrystalModelRenderEvent {
        public Post(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
}
