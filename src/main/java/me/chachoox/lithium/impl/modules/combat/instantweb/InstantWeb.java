package me.chachoox.lithium.impl.modules.combat.instantweb;

import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.MathUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InstantWeb extends BlockPlaceModule {

    protected final Property<Boolean> face =
            new Property<>(
                    true,
                    new String[]{"Face", "head", "upper", "top"},
                    "Places webs on players face instead of just feet."
            );

    protected final NumberProperty<Float> range =
            new NumberProperty<>(
                    4.0f, 1.0f, 6.0f, 0.1f,
                    new String[]{"PlaceRange", "PlaceDistance", "Range", "Distance"},
                    "How far we want to place webs."
            );

    protected final NumberProperty<Float> targetRange =
            new NumberProperty<>(
                    9.0f, 1.0f, 12.0f, 0.1f,
                    new String[]{"TargetRange", "targetrang", "TargetDistance"},
                    "How far we the target can be."
            );

    protected final Property<Boolean> jumpDisable =
            new Property<>(
                    true,
                    new String[]{"JumpDisable", "AutoDisable", "NoJump"},
                    "Disables whenever we jumped."
            );

    protected EntityPlayer target;

    public InstantWeb() {
        super("InstantWeb", new String[]{"InstantWeb", "AutoWeb", "webber", "webaura", "instantweb"}, "Spams webs on players position to get them stuck.", Category.COMBAT);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
        this.offerProperties(face, range, targetRange, jumpDisable);
    }

    protected List<BlockPos> getPlacements() {
        BlockPos pos = new BlockPos(target.getPositionVector());
        List<BlockPos> placements = new ArrayList<>();
        if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR || mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR
                && mc.player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < MathUtil.square(range.getValue()))
        {
            placements.add(pos);
            if (face.getValue()) {
                placements.add(pos.up());
            }
        }

        return placements;
    }
}
