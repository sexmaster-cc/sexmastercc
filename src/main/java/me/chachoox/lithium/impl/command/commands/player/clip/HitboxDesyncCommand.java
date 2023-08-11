package me.chachoox.lithium.impl.command.commands.player.clip;

import me.chachoox.lithium.impl.command.Command;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class HitboxDesyncCommand extends Command {
    public HitboxDesyncCommand() {
        super(new String[]{"HitboxDesync", "hd"});
    }

    @Override
    public String execute() {
        EnumFacing f = mc.player.getHorizontalFacing();
        AxisAlignedBB bb = mc.player.getEntityBoundingBox();
        Vec3d center = bb.getCenter();
        Vec3d offset = new Vec3d(f.getDirectionVec());

        double MAGIC_OFFSET = .200009968835369999878673424677777777777761;
        Vec3d fin = merge(new Vec3d(new BlockPos(center)).add(.5, 0, .5).add(offset.scale(MAGIC_OFFSET)), f);
        mc.player.setPositionAndUpdate(fin.x == 0 ? mc.player.posX : fin.x, mc.player.posY, fin.z == 0 ? mc.player.posZ : fin.z);
        return "Executed russian exploit";
    }

    private Vec3d merge(Vec3d a, EnumFacing facing) {
        return new Vec3d(a.x * Math.abs(facing.getDirectionVec().getX()), a.y * Math.abs(facing.getDirectionVec().getY()), a.z * Math.abs(facing.getDirectionVec().getZ()));
    }
}
