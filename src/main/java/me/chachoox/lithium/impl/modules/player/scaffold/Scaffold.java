package me.chachoox.lithium.impl.modules.player.scaffold;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

//TODO: Rewrite
public class Scaffold extends Module {

    protected final EnumProperty<ListEnum> whitelist =
            new EnumProperty<>(
                    ListEnum.ANY,
                    ListEnum.SELECTION_ALIAS,
                    ListEnum.SELECTION_DESCRIPTION
            );

    protected final EnumProperty<SwingEnum> swing =
            new EnumProperty<>(
                    SwingEnum.NORMAL,
                    SwingEnum.ALIASES,
                    SwingEnum.DESCRIPTION
            );


    protected final EnumProperty<RotationsEnum> rotation =
            new EnumProperty<>(
                    RotationsEnum.NONE,
                    RotationsEnum.ALIASES,
                    RotationsEnum.DESCRIPTION
            );

    protected final Property<Boolean> tower =
            new Property<>(
                    true,
                    new String[]{"Tower", "tow", "t"},
                    "Gives you higher y motion so we go upwards faster."
            );

    protected final Property<Boolean> down =
            new Property<>(
                    false,
                    new String[]{"Downwards", "down", "d"},
                    "Creates a downwards staircase whenever we are sneaking."
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(
                    80, 0, 500,
                    new String[]{"PlaceDelay", "Delay", "del"},
                    "Delay between placing another block."
            );

    protected final BlockList blockList = new BlockList(ListEnum.BLOCKS_LIST_ALIAS);

    protected final StopWatch rotationTimer = new StopWatch();
    protected final StopWatch towerTimer = new StopWatch();
    protected final StopWatch clickTimer = new StopWatch();
    protected final StopWatch placeTimer = new StopWatch();

    protected float[] rotations;

    protected EnumFacing facing;
    protected BlockPos pos;
    protected BlockPos rot;

    protected boolean sneak;

    public Scaffold() {
        super("Scaffold", new String[]{"Scaffold", "Tower"}, "Automatically places blocks under you.", Category.PLAYER);
        this.offerListeners(new ListenerMotion(this), new ListenerMove(this), new ListenerRender(this));
        this.offerProperties(whitelist, swing, rotation, tower, down, delay, blockList);
    }

    @Override
    public void onEnable() {
        towerTimer.reset();
        pos = null;
        facing = null;
        rot = null;
    }

    protected BlockPos findNextPos() {
        BlockPos underPos = new BlockPos(mc.player).down();
        boolean under = false;
        if (down.getValue() && !mc.gameSettings.keyBindJump.isKeyDown() && mc.gameSettings.keyBindSneak.isKeyDown()) {
            under = true;
            underPos = underPos.down();
        }

        if (mc.world.getBlockState(underPos).getMaterial().isReplaceable()) {
            if (!under || mc.world.getBlockState(underPos.up()).getMaterial().isReplaceable()) {
                return underPos;
            }
        }

        if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown()) {
            BlockPos forwardPos = underPos.offset(mc.player.getHorizontalFacing());

            if (mc.world.getBlockState(forwardPos).getMaterial().isReplaceable()) {
                return forwardPos;
            }
        } else if (mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown()) {
            BlockPos backPos = underPos.offset(mc.player.getHorizontalFacing().getOpposite());

            if (mc.world.getBlockState(backPos).getMaterial().isReplaceable()) {
                return backPos;
            }
        }

        if (mc.gameSettings.keyBindRight.isKeyDown() && !mc.gameSettings.keyBindLeft.isKeyDown()) {
            BlockPos rightPos = underPos.offset(mc.player.getHorizontalFacing().rotateY());

            if (mc.world.getBlockState(rightPos).getMaterial().isReplaceable()) {
                return rightPos;
            }
        } else if (mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown()) {
            
            BlockPos leftPos = underPos.offset(mc.player.getHorizontalFacing().rotateYCCW());

            if (mc.world.getBlockState(leftPos).getMaterial().isReplaceable()) {
                return leftPos;
            }
        }
        return null;
    }

    protected boolean isValid(Block block) {
        if (block == null) {
            return false;
        }

        if (whitelist.getValue() == ListEnum.ANY) {
            return true;
        }
        if (whitelist.getValue() == ListEnum.WHITELIST) {
            return getList().contains(block);
        }
        return !getList().contains(block);
    }

    public Collection<Block> getList() {
        return blockList.getValue();
    }

}
