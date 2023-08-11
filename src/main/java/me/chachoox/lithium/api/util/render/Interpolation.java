package me.chachoox.lithium.api.util.render;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.asm.mixins.render.IRenderManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Interpolation implements Minecraftable {

    public static Vec3d interpolatedEyePos() {
        return mc.player.getPositionEyes(mc.getRenderPartialTicks());
    }

    public static Vec3d interpolatedEyeVec() {
        return mc.player.getLook(mc.getRenderPartialTicks());
    }

    public static Vec3d interpolatedEyeVec(EntityPlayer player) {
        return player.getLook(mc.getRenderPartialTicks());
    }

    public static Vec3d interpolateEntity(Entity entity) {
        double x;
        double y;
        double z;
        {
            x = interpolateLastTickPos(entity.posX, entity.lastTickPosX) - getRenderPosX();
            y = interpolateLastTickPos(entity.posY, entity.lastTickPosY) - getRenderPosY();
            z = interpolateLastTickPos(entity.posZ, entity.lastTickPosZ) - getRenderPosZ();
        }

        return new Vec3d(x, y, z);
    }

    public static double interpolateLastTickPos(double pos, double lastPos) {
        return lastPos + (pos - lastPos) * ((IMinecraft) mc).getTimer().renderPartialTicks;
    }

    public static AxisAlignedBB interpolatePos(BlockPos pos, float height) {
        return new AxisAlignedBB(
                pos.getX() - mc.getRenderManager().viewerPosX,
                pos.getY() - mc.getRenderManager().viewerPosY,
                pos.getZ() - mc.getRenderManager().viewerPosZ,
                pos.getX() - mc.getRenderManager().viewerPosX + 1,
                pos.getY() - mc.getRenderManager().viewerPosY + height,
                pos.getZ() - mc.getRenderManager().viewerPosZ + 1);
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(
                bb.minX - mc.getRenderManager().viewerPosX,
                bb.minY - mc.getRenderManager().viewerPosY,
                bb.minZ - mc.getRenderManager().viewerPosZ,
                bb.maxX - mc.getRenderManager().viewerPosX,
                bb.maxY - mc.getRenderManager().viewerPosY,
                bb.maxZ - mc.getRenderManager().viewerPosZ);
    }

    public static AxisAlignedBB offsetRenderPos(AxisAlignedBB bb) {
        return bb.offset(-getRenderPosX(), -getRenderPosY(), -getRenderPosZ());
    }

    public static double getRenderPosX() {
        return ((IRenderManager) mc.getRenderManager()).getRenderPosX();
    }

    public static double getRenderPosY() {
        return ((IRenderManager) mc.getRenderManager()).getRenderPosY();
    }

    public static double getRenderPosZ() {
        return ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
    }

    public static Frustum createFrustum(Entity entity) {
        Frustum frustum = new Frustum();
        setFrustum(frustum, entity);
        return frustum;
    }

    public static void setFrustum(Frustum frustum, Entity entity) {
        double x = interpolateLastTickPos(entity.posX, entity.lastTickPosX);
        double y = interpolateLastTickPos(entity.posY, entity.lastTickPosY);
        double z = interpolateLastTickPos(entity.posZ, entity.lastTickPosZ);

        frustum.setPosition(x, y, z);

    }

}
