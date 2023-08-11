package me.chachoox.lithium.impl.modules.combat.selffill;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class SelfFill extends Module {

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

    protected final Property<Boolean> rotate =
            new Property<>(
                    false,
                    RotationsEnum.ALIASES,
                    "Looks at the floor when self filling."
            );

    protected final Property<Boolean> altSwap =
            new Property<>(false,
                    new String[]{"AltSwap", "AlternativeSwap", "CooldownBypass"},
                    "Uses an alternative type of swap that bypass some anticheats (2b2tpvp main)"
            );

    protected final Property<Boolean> strict =
            new Property<>(
                    false,
                    new String[]{"Strict", "betterplace", "strictplacement"},
                    "Stricter placements."
            );

    protected final Property<Boolean> wait =
            new Property<>(
                    false,
                    new String[]{"Wait", "NoDisable"},
                    "Waits to self fill again instead of disabling the module."
            );

    protected final Property<Boolean> noVoid =
            new Property<>(
                    false,
                    new String[]{"NoVoid", "AntiVoid"},
                    "Wont self fill if the pos is below 0."
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(
                    100, 0, 500,
                    new String[]{"Delay", "placeDelay", "Delay/place"},
                    "Delay between placing blocks."
            );

    protected final Property<Boolean> attack =
            new Property<>(
                    false,
                    new String[]{"Attack", "Destroy", "DestroyLonely"},
                    "Attacks crystals that are in the way."
            );

    protected final NumberProperty<Integer> attackDelay =
            new NumberProperty<>(
                    100, 0, 500,
                    new String[]{"AttackDelay", "crystalAttackDelay", "ad"},
                    "Delay between attacking crystals."
            );

    protected final BlockList blockList = new BlockList(ListEnum.BLOCKS_LIST_ALIAS);

    protected final StopWatch breakTimer = new StopWatch();
    protected final StopWatch confirmTimer = new StopWatch();
    protected final StopWatch timer = new StopWatch();

    protected BlockPos startPos;

    public SelfFill() {
        super("SelfFill", new String[]{"Selffill", "burrow", "blocklag", "nonghostblockscaffold", "feetscaffold"}, "Places blocks inside you", Category.COMBAT);
        this.offerProperties(whitelist, swing, rotate, altSwap, strict, wait, noVoid, delay, attack, attackDelay, blockList);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
        this.initializeBlocks();
    }

    @Override
    public void onEnable() {
        timer.setTime(0);
        breakTimer.setTime(0);
        if (isNull()) {
            return;
        }
        startPos = PositionUtil.getPosition();
    }

    @Override
    public void onDisable() {
        startPos = null;
    }

    protected double getY(Entity entity) {
        double d = getY(entity, 2.1, 10.0, true);
        if (Double.isNaN(d)) {
            d = getY(entity, -3.0, -10.0, false);
            if (Double.isNaN(d)) {
                return mc.player.posY + 1.242610501394747;
            }
        }

        return d;
    }

    protected double getY(Entity entity, double min, double max, boolean add) {
        if (min > max && add || max > min && !add) {
            return Double.NaN;
        }

        double x = entity.posX;
        double y = entity.posY;
        double z = entity.posZ;

        boolean air = false;
        BlockPos last = null;
        for (double off = min;
             add ? off < max : off > max;
            //noinspection ConstantConditions
             off = (add ? ++off : --off))
        {
            BlockPos pos = new BlockPos(x, y - off, z);
            if (noVoid.getValue() && pos.getY() < 0) {
                continue;
            }

            IBlockState state = mc.world.getBlockState(pos);
            if (!state.getMaterial().blocksMovement() || state.getBlock() == Blocks.AIR) {
                if (air) {
                    if (add) {
                        return pos.getY();
                    } else {
                        return last.getY();
                    }
                }

                air = true;
            } else {
                air = false;
            }

            last = pos;
        }

        return Double.NaN;
    }

    protected boolean isInsideBlock() {
        double x = mc.player.posX;
        double y = mc.player.posY + 0.20;
        double z = mc.player.posZ;

        return mc.world.getBlockState(new BlockPos(x, y, z)).getMaterial().blocksMovement() || !mc.player.collidedVertically;
    }

    private void initializeBlocks() {
        if (!getList().contains(Blocks.OBSIDIAN)) {
            getList().add(Blocks.OBSIDIAN);
        }

        if (!getList().contains(Blocks.ENDER_CHEST)) {
            getList().add(Blocks.ENDER_CHEST);
        }
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
