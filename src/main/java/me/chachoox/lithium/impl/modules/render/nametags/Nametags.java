package me.chachoox.lithium.impl.modules.render.nametags;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.render.nametags.mode.PingEnum;
import me.chachoox.lithium.impl.modules.render.nametags.mode.SneakEnum;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class Nametags extends Module {

    protected final Property<Boolean> armor =
            new Property<>(
                    true,
                    new String[]{"Armor", "aa"},
                    "Draws the armor of the player."
            );

    protected final Property<Boolean> enchants =
            new Property<>(
                    true,
                    new String[]{"Enchants", "ench"},
                    "Draws the enchants of the armor."
            );

    protected final Property<Boolean> simple =
            new Property<>(
                    false,
                    new String[]{"SimpleEnchants", "simpleench"},
                    "Draws less enchantments."
            );

    protected final Property<Boolean> lowercase =
            new Property<>(
                    false,
                    new String[]{"LowerCase", "lowercaseench"},
                    "Draws enchantments in lowercase."
            );

    protected final Property<Boolean> durability =
            new Property<>(
                    true,
                    new String[]{"Durability", "dura"},
                    "Draws the durability of the armor and items."
            );

    protected final Property<Boolean> itemStack =
            new Property<>(
                    true,
                    new String[]{"StackName", "stack"},
                    "Draws the name of the item being held."
            );

    protected final NumberProperty<Float> scaling =
            new NumberProperty<>(
                    0.3f, 0.1f, 1.0f, 0.1f,
                    new String[]{"Scaling", "scale", "size"},
                    "The scale of the nametags."
            );

    protected final Property<Boolean> rectBorder =
            new Property<>(
                    true,
                    new String[]{"Rect", "rectborder"},
                    "Draws a rect to fill the nametag."
            );

    protected final NumberProperty<Float> opacity =
            new NumberProperty<>(
                    0.5f, 0.0f, 1.0f, 0.01f,
                    new String[]{"Opacity", "rectalpha"},
                    "The opacity of the rect."
            );

    protected final NumberProperty<Float> outlineOpacity =
            new NumberProperty<>(
                    0.75f, 0.0f, 1.0f, 0.01f,
                    new String[]{"OutlineOpacity", "outlinealpha"},
                    "The opacity of the outline."
            );

    protected final Property<Boolean> invisibles =
            new Property<>(
                    true,
                    new String[]{"Invisibles", "invis"},
                    "Draws nametags on invisibles players."
            );

    protected final EnumProperty<PingEnum> ping =
            new EnumProperty<>(
                    PingEnum.NORMAL,
                    new String[]{"Ping", "p"},
                    "Displays the ping of the player."
            );

    protected final Property<Boolean> entityId =
            new Property<>(
                    false,
                    new String[]{"EntityId", "id"},
                    "Displays the id of the player."
            );

    protected final Property<Boolean> gameMode =
            new Property<>(
                    false,
                    new String[]{"Gamemode", "gamemod"},
                    "Displays the game mode of the player."
            );

    protected final Property<Boolean> totemPops =
            new Property<>(
                    true,
                    new String[]{"Pops", "pop"},
                    "Displays how many totems the player has popped."
            );

    protected final EnumProperty<SneakEnum> sneak =
            new EnumProperty<>(
                    SneakEnum.NONE,
                    new String[]{"Sneak", "crouch"},
                    "Changes color if the player is sneaking."
            );

    protected final Property<Boolean> burrow =
            new Property<>(
                    false,
                    new String[]{"Burrow", "burro"},
                    "Changes color if the player is inside a block."
            );

    protected final Property<Boolean> syncBorder =
            new Property<>(
                    false,
                    new String[]{"GlobalOutline", "syncoutline"},
                    "Syncs the outline color."
            );

    protected final Property<Boolean> holeColor =
            new Property<>(
                    false,
                    new String[]{"HoleOutlineColor", "safecolor"},
                    "Changes color if the player is in a safe spot."
            );

    protected final List<Block> burrowList = Arrays.asList(
            Blocks.BEDROCK,
            Blocks.OBSIDIAN,
            Blocks.ENDER_CHEST,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.BEACON,
            Blocks.PISTON,
            Blocks.REDSTONE_BLOCK,
            Blocks.ENCHANTING_TABLE,
            Blocks.ANVIL
    );

    public Nametags() {
        super("Nametags", new String[]{"Nametags", "nametag", "betternametags"}, "Better player name tags.", Category.RENDER);
        this.offerProperties(armor, enchants, simple, lowercase, durability, itemStack, scaling, rectBorder, opacity, outlineOpacity,
                invisibles, ping, entityId, gameMode, totemPops, sneak, burrow, syncBorder, holeColor);
        this.listeners.add(new ListenerRender(this));
    }

    protected void renderStack(ItemStack stack, int x, int y, int enchHeight) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        int height = enchHeight > 4 ? (enchHeight - 4) * 8 / 2 : 0;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y + height);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, y + height);
        mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        if (enchants.getValue()) {
            this.renderEnchants(stack, x, y - 24);
        }
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchants(ItemStack stack, int xOffset, int yOffset) {
        final List<String> enchantTexts;
        Set<Enchantment> e = EnchantmentHelper.getEnchantments(stack).keySet();
        enchantTexts = new ArrayList<>(e.size());
        for (Enchantment enchantment : e) {

            if (simple.getValue()) {
                if (!(enchantment.getName().contains("all")
                        || enchantment.getName().contains("knockback")
                        || enchantment.getName().contains("fire") //fire aspect
                        || enchantment.getName().contains("arrowDamage") //power
                        || enchantment.getName().contains("explosion") //blast prot
                        || enchantment.getName().contains("fall") //feather falling
                        || enchantment.getName().contains("durability") //unbreaking
                        || enchantment.getName().contains("mending")))
                    continue;
            }

            enchantTexts.add(getEnchantText(enchantment, EnchantmentHelper.getEnchantmentLevel(enchantment, stack)));
        }

        for (String enchantment : enchantTexts) {
            if (enchantment != null) {
                Managers.FONT.drawString(lowercase.getValue() ? enchantment.toLowerCase() : TextUtil.capitalize(enchantment), xOffset * 2.0f, yOffset, 0xFFFFFFFF);
                yOffset += 8;
            }
        }

        if (stack.getItem().equals(Items.GOLDEN_APPLE) && stack.hasEffect()) {
            Managers.FONT.drawString("God", xOffset * 2.0f, yOffset, -3977919);
        }
    }

    private String getEnchantText(Enchantment ench, int lvl) {
        ResourceLocation resource = Enchantment.REGISTRY.getNameForObject(ench);

        String name = resource == null ? ench.getName() : resource.toString();
        int lvlOffset = lvl > 1 ? 12 : 13;

        if (name.length() > lvlOffset) {
            name = name.substring(10, lvlOffset);
        }

        if (lvl > 1) {
            name += lvl;
        }

        return name.length() < 2 ? name : TextUtil.getFixedName(name);
    }

    protected void renderText(ItemStack stack, float y) {
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        String name = stack.getDisplayName();

        Managers.FONT.drawString(name, (float) (-Managers.FONT.getStringWidth(name) >> 1), y, 0xFFFFFFFF);

        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
    }

    protected void renderDurability(ItemStack stack, float x, float y) {
        int percent = (int) ItemUtil.getDamageInPercent(stack);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.disableDepth();
        Managers.FONT.drawString(percent + "%", x * 2, y, stack.getItem().getRGBDurabilityForDisplay(stack));
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
    }

    protected String getDisplayTag(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();

        double health = Math.ceil(EntityUtil.getHealth(player));
        String color;

        if (health > 18) {
            color = TextColor.GREEN;
        } else if (health > 16) {
            color = TextColor.DARK_GREEN;
        } else if (health > 12) {
            color = TextColor.YELLOW;
        } else if (health > 8) {
            color = TextColor.GOLD;
        } else if (health > 5) {
            color = TextColor.RED;
        } else {
            color = TextColor.DARK_RED;
        }

        String idString = "";
        if (entityId.getValue()) {
            idString = idString + " ID: " + player.getEntityId();
        }

        String gameModeStr = "";
        if (gameMode.getValue()) {
            gameModeStr = player.isCreative() ? gameModeStr + " [C]" : (player.isSpectator() || player.isInvisible() ? gameModeStr + " [I]" : gameModeStr + " [S]");
        }

        String pingStr = "";
        if (ping.getValue() != PingEnum.NONE) {
            try {
                int responseTime = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                switch (ping.getValue()) {
                    case COLORED: {
                        pingStr += " " + (getPingColor(responseTime) + responseTime) + "ms";
                        break;
                    }
                    case NORMAL: {
                        pingStr += " " + (responseTime + "ms");
                        break;
                    }
                }
            } catch (Exception ignored) { }
        }

        String popStr = "";
        if (totemPops.getValue()) {
            final Map<String, Integer> registry = Managers.TOTEM.getPopMap();
            popStr += registry.containsKey(player.getName()) ? getPopColor(registry.get(player.getName())) + " -" + registry.get(player.getName()) : "";
        }

        name = name + idString + gameModeStr + pingStr + color + " " + ((int) health) + popStr;

        return name;
    }

    protected int getNameColor(EntityPlayer player) {
        if (Managers.FRIEND.isFriend(player)) {
            return Colours.get().getFriendColour().getRGB();
        }

        if (burrow.getValue()) {
            BlockPos pos = PositionUtil.getPosition(player);
            IBlockState state = mc.world.getBlockState(pos);
            if (burrowList.contains(state.getBlock()) && state.getBoundingBox(mc.world, pos).offset(pos).maxY > player.posY) {
                return 0xFF5E15D8;
            }
        }

        if (player.isInvisible()) {
            return 0xFFFF2500;
        }

        //noinspection ConstantConditions
        if (mc.getConnection() != null && mc.getConnection().getPlayerInfo(player.getUniqueID()) == null) {
            return 0xFFEF0147;
        }

        if (player.isSneaking() && sneak.getValue() != SneakEnum.NONE) {
            return sneak.getValue() == SneakEnum.LIGHT ? 0xFF9900 : 0xFF9A1FF5;
        }

        return 0xFFFFFFFF;
    }

    protected int getBorderColor(EntityPlayer player) {
        if (syncBorder.getValue()) {

            if (Managers.FRIEND.isFriend(player)) {
                return Colours.get().getFriendColour().getRGB();
            }

            if (burrow.getValue()) {
                BlockPos pos = PositionUtil.getPosition(player);
                IBlockState state = mc.world.getBlockState(pos);
                if (burrowList.contains(state.getBlock()) && state.getBoundingBox(mc.world, pos).offset(pos).maxY > player.posY) {
                    return 0xFF5E15D8;
                }
            }

            if (player.isInvisible()) {
                return 0xFFFF2500;
            }

            //noinspection ConstantConditions
            if (mc.getConnection() != null && mc.getConnection().getPlayerInfo(player.getUniqueID()) == null) {
                return 0xFFEF0147;
            }

            if (player.isSneaking() && sneak.getValue() != SneakEnum.NONE) {
                return sneak.getValue() == SneakEnum.LIGHT ? 0xFF9900 : 0xFF9A1FF5;
            }
        }

        return syncBorder.getValue() ? Colours.get().getColour().getRGB() : (int) (127.0f * outlineOpacity.getValue()) << 24;
    }

    private String getPopColor(int pops) {
        if (pops == 1) {
            return TextColor.GREEN;
        }
        if (pops == 2) {
            return TextColor.DARK_GREEN;
        }
        if (pops == 3) {
            return TextColor.YELLOW;
        }
        if (pops == 4) {
            return TextColor.GOLD;
        }
        if (pops == 5) {
            return TextColor.RED;
        }
        return TextColor.DARK_RED;
    }

    private String getPingColor(int ping) {
        if (ping > 200) {
            return TextColor.RED;
        } else if (ping > 100) {
            return TextColor.YELLOW;
        } else {
            return TextColor.GREEN;
        }
    }
}