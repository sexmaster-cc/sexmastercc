package me.chachoox.lithium.impl.modules.render.skeleton;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Skeleton extends Module {

    private final ColorProperty color = new ColorProperty(new Color(-1), true, new String[]{"Color", "colour"});

    private final NumberProperty<Float> lineWidth =
            new NumberProperty<>(
                    1.2f, 1.0f, 4.0f, 0.1f,
                    new String[]{"LineWidth", "width", "wirewidth"},
                    "Thickness of the line."
            );

    protected final Map<EntityPlayer, float[][]> rotationList = new HashMap<>();

    public Skeleton() {
        super("Skeleton", new String[]{"Skeleton", "spooky", "skelet"}, "Draws a skeleton on entities.", Category.RENDER);
        this.offerProperties(color, lineWidth);
        this.offerListeners(new ListenerRender(this));
    }

    protected Color getColor() {
        return color.getColor();
    }

    public void onRenderModel(ModelBase modelBase, Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (modelBase instanceof ModelBiped) {
                rotationList.put((EntityPlayer)entity, getBipedRotations((ModelBiped)modelBase));
            }
        }
    }

    private float[][] getBipedRotations(ModelBiped biped) {
        float[][] rotations = new float[5][];

        float[] headRotation = new float[3];
        headRotation[0] = biped.bipedHead.rotateAngleX;
        headRotation[1] = biped.bipedHead.rotateAngleY;
        headRotation[2] = biped.bipedHead.rotateAngleZ;
        rotations[0] = headRotation;

        float[] rightArmRotation = new float[3];
        rightArmRotation[0] = biped.bipedRightArm.rotateAngleX;
        rightArmRotation[1] = biped.bipedRightArm.rotateAngleY;
        rightArmRotation[2] = biped.bipedRightArm.rotateAngleZ;
        rotations [1] = rightArmRotation;

        float[] leftArmRotation = new float[3];
        leftArmRotation[0] = biped.bipedLeftArm.rotateAngleX;
        leftArmRotation[1] = biped.bipedLeftArm.rotateAngleY;
        leftArmRotation[2] = biped.bipedLeftArm.rotateAngleZ;
        rotations[2] = leftArmRotation;

        float[] rightLegRotation = new float[3];
        rightLegRotation[0] = biped.bipedRightLeg.rotateAngleX;
        rightLegRotation[1] = biped.bipedRightLeg.rotateAngleY;
        rightLegRotation[2] = biped.bipedRightLeg.rotateAngleZ;
        rotations[3] = rightLegRotation;

        float[] leftLegRotation = new float[3];
        leftLegRotation[0] = biped.bipedLeftLeg.rotateAngleX;
        leftLegRotation[1] = biped.bipedLeftLeg.rotateAngleY;
        leftLegRotation[2] = biped.bipedLeftLeg.rotateAngleZ;
        rotations[4] = leftLegRotation;

        return rotations;
    }

    protected float getLineWidth() {
        return lineWidth.getValue();
    }
}
