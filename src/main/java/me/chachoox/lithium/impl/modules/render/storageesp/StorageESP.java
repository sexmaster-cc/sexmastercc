package me.chachoox.lithium.impl.modules.render.storageesp;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class StorageESP extends Module {

    public StorageESP() {
        super("StorageESP", new String[]{"StorageESP", "chestesp", "echestesp"}, "Highlights storages to help you find bases.", Category.RENDER);
        this.offerListeners(new ListenerRender(this));
    }

    protected void drawTileEntities() {
        Frustum frustum = new Frustum();
        Entity renderEntity = mc.getRenderViewEntity() == null ? mc.player : mc.getRenderViewEntity();

        try {
            double x = renderEntity.posX;
            double y = renderEntity.posY;
            double z = renderEntity.posZ;
            frustum.setPosition(x, y, z);
            for (TileEntity tileEntity : mc.world.loadedTileEntityList) {
                if ((tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) || tileEntity instanceof TileEntityShulkerBox) {
                    if (mc.player.getDistance(tileEntity.getPos().getX(),tileEntity.getPos().getY(),tileEntity.getPos().getZ()) > 1000.0F) {
                        continue;
                    }
                    double posX = tileEntity.getPos().getX() - Interpolation.getRenderPosX();
                    double posY = tileEntity.getPos().getY() - Interpolation.getRenderPosY();
                    double posZ = tileEntity.getPos().getZ() - Interpolation.getRenderPosZ();
                    AxisAlignedBB bb = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX, posY, posZ);
                    if (tileEntity instanceof TileEntityChest) {
                        TileEntityChest adjacent = null;
                        if (((TileEntityChest) tileEntity).adjacentChestXNeg != null)
                            adjacent = ((TileEntityChest) tileEntity).adjacentChestXNeg;
                        if (((TileEntityChest) tileEntity).adjacentChestXPos != null)
                            adjacent = ((TileEntityChest) tileEntity).adjacentChestXPos;
                        if (((TileEntityChest) tileEntity).adjacentChestZNeg != null)
                            adjacent = ((TileEntityChest) tileEntity).adjacentChestZNeg;
                        if (((TileEntityChest) tileEntity).adjacentChestZPos != null)
                            adjacent = ((TileEntityChest) tileEntity).adjacentChestZPos;
                        if (adjacent != null)
                            bb = bb.union(new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(
                                    adjacent.getPos().getX() - Interpolation.getRenderPosX(),
                                    adjacent.getPos().getY() - Interpolation.getRenderPosY(),
                                    adjacent.getPos().getZ() - Interpolation.getRenderPosZ()));
                    }
                    GL11.glPushMatrix();
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glDisable(3553);
                    GL11.glEnable(2848);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    colorTileEntityInside(tileEntity);
                    RenderUtil.drawBox(bb);
                    colorTileEntity(tileEntity);
                    RenderUtil.drawOutline(bb, 1.0F);
                    GL11.glDisable(2848);
                    GL11.glEnable(3553);
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                    GL11.glDisable(3042);
                    RenderUtil.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glPopMatrix();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void colorTileEntityInside(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            if (((TileEntityChest) tileEntity).getChestType() == BlockChest.Type.TRAP) {
                RenderUtil.color(new Color(250, 54, 0, 28));
            } else {
                RenderUtil.color(new Color(234, 183, 88, 80));
            }
        } else if (tileEntity instanceof TileEntityEnderChest) {
            RenderUtil.color(new Color(174, 0, 255, 28));
        } else if (tileEntity instanceof TileEntityShulkerBox) {
            RenderUtil.color(new Color(81, 140, 255, 28));
        }
    }

    protected void colorTileEntity(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            if (((TileEntityChest) tileEntity).getChestType() == BlockChest.Type.TRAP) {
                RenderUtil.color(new Color(250, 54, 0, 189));
            } else {
                RenderUtil.color(new Color(234, 183, 88, 189));
            }
        } else if (tileEntity instanceof TileEntityEnderChest) {
            RenderUtil.color(new Color(174, 0, 255, 189));
        }
    }
}
