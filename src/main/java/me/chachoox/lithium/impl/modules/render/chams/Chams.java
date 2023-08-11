package me.chachoox.lithium.impl.modules.render.chams;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class Chams extends Module {

    public final Property<Boolean> self =
            new Property<>(false,
                    new String[]{"SelfChams", "Self"},
                    "Renders chams on ourself."
            );

    public final Property<Boolean> playerWires =
            new Property<>(false,
                    new String[]{"PlayerWireframe", "PlayerWire", "WirePlayer"},
                    "Renders wireframe on players."
            );

    public final Property<Boolean> playerChams =
            new Property<>(false,
                    new String[]{"PlayerChams", "PlayerCham"},
                    "Renders chams on players."
            );

    public final Property<Boolean> crystalWires =
            new Property<>(false,
                    new String[]{"CrystalWireframe", "CrystalWire", "WireCrystal"},
                    "Renders wireframe on crystals."
            );

    public final Property<Boolean> crystalChams =
            new Property<>(false,
                    new String[]{"CrystalChams", "CrystalCham"},
                    "Renders chams on crystals."
            );

    public final Property<Boolean> normal =
            new Property<>(
                    false,
                    new String[]{"Normal", "og"},
                    "Orignal chams."
            );

    public final Property<Boolean> xqz =
            new Property<>(false,
                    new String[]{"Xqz", "throughwall", "truwall"},
                    "Render entities through walls."
            );

    public final Property<Boolean> glint =
            new Property<>(false,
                    new String[]{"Glint", "shyne", "enchant"},
                    "Renders a enchant effect in entities."
            );

    public final NumberProperty<Float> glintSpeed =
            new NumberProperty<>(5.0F, 0.1F, 20.0F, 0.1F,
                    new String[]{"GlintSpeed", "shynespeed", "enchantspeed"},
                    "Current speed of the glint."
            );

    public final NumberProperty<Float> glintScale =
            new NumberProperty<>(1.0F, 0.1F, 10.0F,
                    0.1F, new String[]{"GlintScale", "shynescale", "enchantscale"},
                    "Scale of the glint."
            );

    public final Property<Boolean> customGlint =
            new Property<>(false,
                    new String[]{"CustomGlint", "cglint", "altglint"},
                    "Uses an alternative glint texture instead of minecraft's one"
            );

    public final Property<Boolean> texture =
            new Property<>(false, new String[]{"Texture", "tex"},
                    "Render the model of the entity."
            );

    protected final Property<Boolean> damage =
            new Property<>(false,
                    new String[]{"Damage", "dag"},
                    "Changes the damage colour."
            );

    public final NumberProperty<Float> scale =
            new NumberProperty<>(1.0F, 0.1F, 2.0F, 0.1F,
                    new String[]{"CrystalScale", "scale"},
                    "Scale of crystals."
            );

    public final NumberProperty<Float> smallScale =
            new NumberProperty<>(0.0F, -1.0F, 0.0F, 0.1F,
                    new String[]{"CrystalModelScale", "smallcrystalscale", "modelscale"},
                    "Scale of the inner model of crystals."
            );

    public final NumberProperty<Float> spinSpeed =
            new NumberProperty<>(1.0f, 0.0f, 5.0f, 0.1f,
                    new String[]{"SpinSpeed", "spinsped", "spinspeeed", "spin"},
                    "Spin speed of crystals."
            );

    public final NumberProperty<Float> bounceSpeed =
            new NumberProperty<>(1.0f, 0.0f, 5.0f, 0.1f,
                    new String[]{"CrystalBounce", "bounce"},
                    "Bounce speed of crystals."
            );

    public final NumberProperty<Float> lineWidth =
            new NumberProperty<>(1.0F, 1.0F, 4.0F, 0.1F,
                    new String[]{"LineWidth", "width"},
                    "Width of wireframes"
            );

    private final ColorProperty visibleColor = new ColorProperty(new Color(0x34FFFFFF, true), true, new String[]{"VisibleColor", "viscolor"});

    private final ColorProperty invisibleColor = new ColorProperty(new Color(0x4DFFFFFF, true), true, new String[]{"XqzColor", "WallColor"});

    protected final ColorProperty friendColor = new ColorProperty(new Color(Colours.get().getFriendColour().getRGB(), true), false, new String[]{"FriendColor", "frdcolor"});

    private final ColorProperty wireframeColor = new ColorProperty(new Color(-1), true, new String[]{"WireframeColor", "outlinecolor"});

    protected final ColorProperty damageColor = new ColorProperty(new Color(255, 0, 0, 30), true, new String[]{"DamageColor", "dagamecol"});

    private final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private final ResourceLocation CUSTOM_ENCHANT_GLINT_RES = new ResourceLocation("lithium/textures/misc/enchanted_item_glint.png");

    public Chams() {
        super("Chams", new String[]{"Chams", "cham", "chammies", "charms"}, "Renders entities through walls.", Category.RENDER);
        this.offerProperties(self, playerWires, playerChams, crystalChams, crystalWires, normal, xqz, glint, glintSpeed, glintScale, customGlint,
                texture, damage, scale, smallScale, spinSpeed, bounceSpeed, lineWidth, visibleColor, invisibleColor, friendColor, wireframeColor, damageColor);
        this.offerListeners(
                new ListenerPreModel(this), new ListenerPostModel(this),
                new ListenerPreCrystalModel(this), new ListenerDamageColour(this)
        );
    }

    public void onWireframeModel(ModelBase base, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        glPushMatrix();
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);
        GlStateManager.color(getWireColor(entity).getRed() / 255f, getWireColor(entity).getGreen() / 255f, getWireColor(entity).getBlue() / 255f, getWireColor(entity).getAlpha() / 255f);
        GlStateManager.glLineWidth(lineWidth.getValue());
        base.render(entity, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
        GlStateManager.resetColor();
        glPopAttrib();
        glPopMatrix();
    }

    public void onGlintModel(ModelBase base, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        glPushMatrix();
        glPushAttrib(GL_ALL_ATTRIB_BITS);

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        mc.getRenderManager().renderEngine.bindTexture(customGlint.getValue() ? CUSTOM_ENCHANT_GLINT_RES : ENCHANTED_ITEM_GLINT_RES);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        glColor4f(getVisibleColor(entity).getRed() / 255f,
                getVisibleColor(entity).getGreen() / 255f,
                getVisibleColor(entity).getBlue() / 255f,
                getVisibleColor(entity).getAlpha() / 255f);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);

        for (int i = 0; i < 2; ++i) {
            GlStateManager.matrixMode(GL_TEXTURE);
            GlStateManager.loadIdentity();
            float tScale = 0.33333334F * glintScale.getValue();
            GlStateManager.scale(tScale, tScale, tScale);
            GlStateManager.rotate(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, (entity.ticksExisted + mc.getRenderPartialTicks()) * (0.001F + (float)i * 0.003F) * glintSpeed.getValue(), 0.0F);
            GlStateManager.matrixMode(GL_MODELVIEW);

            GlStateManager.color(getVisibleColor(entity).getRed() / 255f,
                    getVisibleColor(entity).getGreen() / 255f,
                    getVisibleColor(entity).getBlue() / 255f,
                    getVisibleColor(entity).getAlpha() / 255f);

            if (!xqz.getValue()) {
                glDepthMask(true);
                glEnable(GL_DEPTH_TEST);
            }

            base.render(entity, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);

            if (!xqz.getValue()) {
                glDisable(GL_DEPTH_TEST);
                glDepthMask(false);
            }
        }

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);

        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.color(1F, 1F, 1F, 1F);
        glPopAttrib();
        glPopMatrix();
    }

    public Color getFriendColor() {
        if (friendColor.isGlobal()) {
            return ColorUtil.changeAlpha(Colours.get().getFriendColour(), friendColor.getColor().getAlpha());
        }
        return friendColor.getColor();
    }

    public Color getVisibleColor(Entity entity) {
        if (entity instanceof EntityPlayer && Managers.FRIEND.isFriend((EntityPlayer) entity)) {
            return getFriendColor();
        } else {
            return visibleColor.getColor();
        }
    }

    public Color getInvisibleColor(Entity entity) {
        if (entity instanceof EntityPlayer && Managers.FRIEND.isFriend((EntityPlayer) entity)) {
            return new Color(getFriendColor().getRed(), getFriendColor().getGreen(), getFriendColor().getBlue(), invisibleColor.getColor().getAlpha());
        } else {
            return invisibleColor.getColor();
        }
    }

    public Color getWireColor(Entity entity) {
        if (entity instanceof EntityPlayer && Managers.FRIEND.isFriend((EntityPlayer) entity)) {
            return getFriendColor();
        } else {
            return wireframeColor.getColor();
        }
    }

    public Color getDamageColor() {
        return damageColor.getColor();
    }

    public float getScale() {
        return scale.getValue();
    }
}
