package me.chachoox.lithium.impl.modules.player.selfblocker;

import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: fix the head part when ur on 2x2 its weird sometimes
public class SelfBlocker extends BlockPlaceModule {

    protected final Property<Boolean> coverHead =
            new Property<>(
                    true,
                    new String[]{"CoverHead", "HeadCover", "HeadTrap"},
                    "Covers our head with obsidian."
            );

    protected final Property<Boolean> jumpDisable =
            new Property<>(
                    true,
                    new String[]{"JumpDisable", "AutoDisable", "NoJump"},
                    "Disables whenever we jumped so we dont spam obsidian."
            );

    public SelfBlocker() {
        super("SelfBlocker", new String[]{"SelfBlocker", "SelfTrap", "SelfBox"}, "Traps you in obsidian.", Category.PLAYER);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
        this.offerProperties(coverHead, jumpDisable);
    }

    protected List<BlockPos> getPlacements() {
        return getPlacements(getPlayerPos());
    }

    private List<BlockPos> getPlacements(BlockPos pos) {
        List<BlockPos> placements = new ArrayList<>();
        Set<EnumFacing> directions = new HashSet<>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos offset = pos.offset(facing);
            if (isEntityIntersecting(offset)) {
                for (EnumFacing direction : EnumFacing.VALUES) {
                    BlockPos extend = offset.offset(direction);
                    if (extend.equals(pos)) {
                        continue;
                    }

                    if (BlockUtil.isReplaceable(extend)) {
                        if (!isEntityIntersecting(extend)) {
                            placements.add(extend);
                        } else {
                            directions.add(direction);
                        }
                    }
                }

                if (directions.size() > 1) {
                    BlockPos supportPos = pos;
                    for (EnumFacing direction : directions) {
                        supportPos = supportPos.offset(direction);
                    }
                    for (EnumFacing direction : EnumFacing.VALUES) {
                        if (!isEntityIntersecting(supportPos.offset(direction))) {
                            placements.add(supportPos.offset(direction));
                        }
                    }
                }
            } else {
                placements.add(offset);
            }

            if (coverHead.getValue()) {
                EnumFacing direction = mc.player.getHorizontalFacing();
                placements.add(new BlockPos(mc.player.posX + direction.getXOffset(), mc.player.posY + 2, mc.player.posZ + direction.getZOffset()));
            }
        }

        for (BlockPos position : new ArrayList<>(placements)) {
            boolean support = true;
            for (EnumFacing direction : EnumFacing.VALUES) {
                BlockPos offsetPosition = position.offset(direction);
                if (!mc.world.isAirBlock(offsetPosition)) {
                    support = false;
                    break;
                }
            }
            if (support) {
                placements.add(position.down());
            }
        }

        return placements;
    }

    private boolean isEntityIntersecting(BlockPos pos) {
        for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
            if (entity == null || entity instanceof EntityXPOrb || entity instanceof EntityItem || entity instanceof EntityEnderCrystal) {
                continue;
            }
            if (entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos))) {
                return true;
            }
        }
        return false;
    }

    private BlockPos getPlayerPos() {
        double decimalPoint = mc.player.posY - Math.floor(mc.player.posY);
        return new BlockPos(mc.player.posX, decimalPoint > 0.8 ? Math.floor(mc.player.posY) + 1.0 : Math.floor(mc.player.posY), mc.player.posZ);
    }
}
