package me.chachoox.lithium.impl.modules.render.holeesp;

import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.holeesp.mode.HolesMode;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class ListenerRender extends ModuleListener<HoleESP, Render3DEvent> {
    public ListenerRender(HoleESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (module.holes.getValue() != HolesMode.VOID) {
            if (module.holeTimer.passed(module.updates.getValue())) {
                module.calcHoles();
                module.holeTimer.reset();
            }

            RenderUtil.startRender();

            drawBedrockHole();
            drawObbyHole();
            drawTwoByOneBedrockHole();
            drawTwoByOneObbyHole();

            RenderUtil.endRender();
        }

        if (module.holes.getValue() != HolesMode.HOLE) {
            if (mc.player.dimension == 1) {
                return;
            }

            if (mc.player.getPosition().getY() > module.range.getValue() || mc.player.getPosition().getY() < -10) {
                return;
            }

            if (module.voidTimer.passed(module.updates.getValue())) {
                module.voidHoles.clear();
                module.voidHoles = module.findVoidHoles();
                module.voidTimer.reset();
            }

            drawVoidHoles();
        }
    }

    private void drawObbyHole() {
        for (BlockPos pos : module.obbyHoles) {
            if (!isInFrustum(pos)) {
                continue;
            }

            AxisAlignedBB bb = Interpolation.interpolatePos(pos, getProperHeight());

            if (module.fade.getValue()) {
                module.drawFade(bb, module.getObbyColor());
            } else {
                RenderUtil.drawBox(bb, module.getObbyColor());
                module.drawOutline(bb, module.lineWidth.getValue(), ColorUtil.changeAlpha(module.getObbyColor(), module.wireAlpha.getValue()));
            }
        }
    }

    private void drawBedrockHole() {
        for (BlockPos pos : module.bedrockHoles) {
            if (!isInFrustum(pos)) {
                continue;
            }

            AxisAlignedBB bb = Interpolation.interpolatePos(pos, getProperHeight());

            if (module.fade.getValue()) {
                module.drawFade(bb, module.getBedrockColor());
            } else {
                RenderUtil.drawBox(bb, module.getBedrockColor());
                module.drawOutline(bb, module.lineWidth.getValue(), ColorUtil.changeAlpha(module.getBedrockColor(), module.wireAlpha.getValue()));
            }
        }
    }

    private void drawTwoByOneObbyHole() {
        for (TwoBlockPos pos : module.obbyHolesTwoBlock) {
            if (!isInFrustum(pos.getOne()) && !isInFrustum(pos.getTwo())) {
                continue;
            }

            final AxisAlignedBB bb = new AxisAlignedBB(
                    pos.getOne().getX() - mc.getRenderManager().viewerPosX,
                    pos.getOne().getY() - mc.getRenderManager().viewerPosY,
                    pos.getOne().getZ() - mc.getRenderManager().viewerPosZ,
                    pos.getTwo().getX() + 1 - mc.getRenderManager().viewerPosX,
                    pos.getTwo().getY() + getProperHeightTwoByOne() - mc.getRenderManager().viewerPosY,
                    pos.getTwo().getZ() + 1 - mc.getRenderManager().viewerPosZ
            );

            if (module.fade.getValue()) {
                module.drawFade(bb, module.getObbyColor());
            } else {
                RenderUtil.drawBox(bb, module.getObbyColor());
                module.drawOutline(bb, module.lineWidth.getValue(), ColorUtil.changeAlpha(module.getObbyColor(), module.wireAlpha.getValue()));
            }
        }
    }

    private void drawTwoByOneBedrockHole() {
        for (TwoBlockPos pos : module.bedrockHolesTwoBlock) {
            if (!isInFrustum(pos.getOne()) && !isInFrustum(pos.getTwo())) {
                continue;
            }

            final AxisAlignedBB bb = new AxisAlignedBB(
                    pos.getOne().getX() - mc.getRenderManager().viewerPosX,
                    pos.getOne().getY() - mc.getRenderManager().viewerPosY,
                    pos.getOne().getZ() - mc.getRenderManager().viewerPosZ,
                    pos.getTwo().getX() + 1 - mc.getRenderManager().viewerPosX,
                    pos.getTwo().getY() + getProperHeightTwoByOne() - mc.getRenderManager().viewerPosY,
                    pos.getTwo().getZ() + 1 - mc.getRenderManager().viewerPosZ
            );

            if (module.fade.getValue()) {
                module.drawFade(bb, module.getBedrockColor());
            } else {
                RenderUtil.drawBox(bb, module.getBedrockColor());
                module.drawOutline(bb, module.lineWidth.getValue(), ColorUtil.changeAlpha(module.getBedrockColor(), module.wireAlpha.getValue()));
            }
        }
    }

    private void drawVoidHoles() {
        for (BlockPos hole : module.voidHoles) {
            if (!isInFrustum(hole)) {
                continue;
            }

            AxisAlignedBB bb = Interpolation.interpolatePos(hole, 0.0F);

            RenderUtil.startRender();
            RenderUtil.drawBox(bb, module.getVoidColor());
            RenderUtil.drawOutline(bb, module.lineWidth.getValue(), ColorUtil.changeAlpha(module.getVoidColor(), module.wireAlpha.getValue()));
            RenderUtil.endRender();
        }
    }

    private float getProperHeight() {
        return module.fade.getValue() ? module.fadeHeight.getValue() : module.height.getValue();
    }

    private float getProperHeightTwoByOne() {
        return module.fade.getValue() ? module.fadeHeight.getValue() : module.doubleHeight.getValue();
    }

    public boolean isInFrustum(BlockPos pos) {
        Entity renderEntity = RenderUtil.getEntity();
        if (renderEntity == null) {
            return false;
        }

        if (renderEntity.getDistanceSq(pos) < 1.5F) {
            return true;
        }

        return isInFrustum(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public boolean isInFrustum(double x, double y, double z) {
        Entity renderEntity = RenderUtil.getEntity();
        if (renderEntity == null) {
            return false;
        }

        Frustum frustum = Interpolation.createFrustum(renderEntity);
        return frustum.isBoundingBoxInFrustum(new AxisAlignedBB(x - 1,y - 1,x - 1,x + 1,y + 1,z + 1));
    }
}

