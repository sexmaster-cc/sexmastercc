package me.chachoox.lithium.impl.modules.render.holeesp;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.api.util.render.TessellatorUtil;
import me.chachoox.lithium.impl.modules.render.holeesp.mode.HolesMode;
import me.chachoox.lithium.impl.modules.render.holeesp.mode.Outline;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoleESP extends Module {

    protected final EnumProperty<HolesMode> holes =
            new EnumProperty<>(
                    HolesMode.HOLE,
                    new String[]{"Holes", "holemode"},
                    "Hole: - Only draws 1x1 / 2x1 holes " +
                            "/ Void: - Only draws void holes."
            );

    protected final NumberProperty<Integer> range =
            new NumberProperty<>(
                    6, 1, 50,
                    new String[]{"Range", "Distance", "r"},
                    "The range of how far we want to see holes."
            );

    protected final NumberProperty<Integer> voidRange =
            new NumberProperty<>(
                    16, 1, 50,
                    new String[]{"VoidRange", "voidrang", "voidr"},
                    "The range of how far we want to see holes."
            );

    protected final Property<Boolean> twoByOne =
            new Property<>(
                    false,
                    new String[]{"2x1", "doubles"},
                    "Draws 2x1 holes."
            );

    protected final NumberProperty<Float> height =
            new NumberProperty<>(
                    0f, -1f, 1f, 0.05f,
                    new String[]{"Height", "1x1H", "1x1height"},
                    "The height of 1x1 holes."
            );

    protected final NumberProperty<Float> doubleHeight =
            new NumberProperty<>(
                    0f, -1f, 1f, 0.05f,
                    new String[]{"2x1Height", "2x1H", "DoublesHeight", "Dh"},
                    "The height of the 2x1 holes."
            );

    protected final NumberProperty<Float> fadeHeight =
            new NumberProperty<>(
                    0f, 0f, 1f, 0.05f,
                    new String[]{"FadeHeight", "fh"}, "The height of the fade."
            );

    protected final EnumProperty<Outline> outlineMode =
            new EnumProperty<>(
                    Outline.NORMAL,
                    new String[]{"Outline", "Outlined", "out"},
                    "Normal: - Draws a normal outline surrounding the box " +
                            "/ Cross: - Draws and X shape and an outline on the hole."
            );

    protected final NumberProperty<Integer> wireAlpha =
            new NumberProperty<>(
                    125, 0, 255,
                    new String[]{"WireAlpha", "LineAlpha", "Alpha"},
                    "The alpha of the outline."
            );

    protected final NumberProperty<Float> lineWidth =
            new NumberProperty<>(
                    1f, 1f, 4f, 0.1f,
                    new String[]{"WireWidth", "LineWidth", "Width", "w"},
                    "The thickness of the outline."
            );

    protected final Property<Boolean> fade =
            new Property<>(
                    false,
                    new String[]{"Fade", "Fadedthanahoe", "cpvpnnmode"},
                    "Draws an upwards fade on the hole."
            );

    protected final ColorProperty obbyColor =
            new ColorProperty(
                    new Color(0x38FF0000, true),
                    false,
                    new String[]{"ObbyColor", "ObbyColour"}
            );

    protected final ColorProperty bedrockColor =
            new ColorProperty(
                    new Color(0x3800FF00, true),
                    false,
                    new String[]{"BedrockColor", "BedrockColour"}
            );

    protected final ColorProperty voidColor =
            new ColorProperty(
                    new Color(0x380000FF, true),
                    false,
                    new String[]{"VoidColor", "VoidColour"}
            );

    protected final NumberProperty<Integer> updates =
            new NumberProperty<>(
                    100, 0, 1000,
                    new String[]{"Updates", "update"},
                    "Delay for updating holes to check if theyre safe."
            );

    protected List<BlockPos> obbyHoles = new ArrayList<>();
    protected List<BlockPos> bedrockHoles = new ArrayList<>();
    protected List<BlockPos> voidHoles = new ArrayList<>();

    protected List<TwoBlockPos> obbyHolesTwoBlock = new ArrayList<>();
    protected List<TwoBlockPos> bedrockHolesTwoBlock = new ArrayList<>();

    protected StopWatch holeTimer = new StopWatch();
    protected StopWatch voidTimer = new StopWatch();

    public HoleESP() {
        super("HoleESP", new String[]{"HoleESP", "holesp", "safeesp", "safeholes"}, "Highlights safe holes.", Category.RENDER);
        this.offerProperties(holes, range, voidRange, twoByOne, height, doubleHeight, fadeHeight, outlineMode, wireAlpha, lineWidth,
                fade, obbyColor, bedrockColor, voidColor, updates);
        this.listeners.add(new ListenerRender(this));
    }

    @Override
    public void onEnable() {
        voidTimer.reset();
        holeTimer.reset();
    }

    @Override
    public String getSuffix() {
        return "" + getHoles();
    }

    protected Color getBedrockColor() {
        return bedrockColor.getColor();
    }

    protected Color getObbyColor() {
        return obbyColor.getColor();
    }

    protected Color getVoidColor() {
        return voidColor.getColor();
    }

    protected void calcHoles() {
        obbyHoles.clear();
        bedrockHoles.clear();
        obbyHolesTwoBlock.clear();
        bedrockHolesTwoBlock.clear();
        List<BlockPos> positions = BlockUtil.getSphere(RenderUtil.getEntity(), range.getValue(), false);
        for (BlockPos pos : positions) {
            if (HoleUtil.isObbyHole(pos)) {
                obbyHoles.add(pos);
            } else {
                final BlockPos validTwoBlock = HoleUtil.isDoubleObby(pos);
                if (validTwoBlock != null && twoByOne.getValue()) {
                    obbyHolesTwoBlock.add(new TwoBlockPos(pos, pos.add(validTwoBlock.getX(), validTwoBlock.getY(), validTwoBlock.getZ())));
                }
            }
            if (HoleUtil.isBedrockHole(pos)) {
                this.bedrockHoles.add(pos);
            } else {
                final BlockPos validTwoBlock = HoleUtil.isDoubleBedrock(pos);
                if (validTwoBlock != null && twoByOne.getValue()) {
                    bedrockHolesTwoBlock.add(new TwoBlockPos(pos, pos.add(validTwoBlock.getX(), validTwoBlock.getY(), validTwoBlock.getZ())));
                }
            }
        }
    }

    protected List<BlockPos> findVoidHoles() {
        final BlockPos playerPos = PositionUtil.getPosition(RenderUtil.getEntity());
        return getDisc(playerPos.add(0, -playerPos.getY(), 0), voidRange.getValue()).stream().filter(this::isVoid).collect(Collectors.toList());
    }

    private boolean isVoid(BlockPos pos) {
        return (mc.world.getBlockState(pos).getBlock() == Blocks.AIR || mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) && pos.getY() < 1 && pos.getY() >= 0;
    }

    private List<BlockPos> getDisc(BlockPos pos, float r) {
        ArrayList<BlockPos> circleblocks = new ArrayList<>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        int x = cx - (int) r;
        while ((float) x <= (float) cx + r) {
            int z = cz - (int) r;
            while ((float) z <= (float) cz + r) {
                double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
                if (dist < (double) (r * r)) {
                    BlockPos position = new BlockPos(x, cy, z);
                    circleblocks.add(position);
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    protected void drawOutline(AxisAlignedBB bb, float with, Color color) {
        switch (outlineMode.getValue()) {
            case NORMAL: {
                RenderUtil.drawOutline(bb, with, color);
                break;
            }
            case CROSS: {
                RenderUtil.drawCross(bb, with, color);
                break;
            }
        }
    }

    protected void drawFade(AxisAlignedBB bb, Color color) {
        TessellatorUtil.startRender();
        TessellatorUtil.drawGradientBox(bb, color);
        TessellatorUtil.drawGradientOutline(bb, color, lineWidth.getValue(), wireAlpha.getValue());
        TessellatorUtil.stopRender();
    }

    private int getHoles() {
        if (holes.getValue() != HolesMode.VOID) {
            return obbyHoles.size() + bedrockHoles.size() + obbyHolesTwoBlock.size() + bedrockHolesTwoBlock.size();
        }

        if (holes.getValue() != HolesMode.HOLE) {
            return voidHoles.size();
        } else {
            return obbyHoles.size() + bedrockHoles.size() + obbyHolesTwoBlock.size() + bedrockHolesTwoBlock.size() + voidHoles.size();
        }
    }
}



