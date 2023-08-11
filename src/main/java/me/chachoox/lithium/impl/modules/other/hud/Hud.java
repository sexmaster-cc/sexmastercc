package me.chachoox.lithium.impl.modules.other.hud;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.api.util.render.animation.Animation;
import me.chachoox.lithium.api.util.render.animation.Direction;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.pingspoof.PingSpoof;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.other.hud.mode.*;
import me.chachoox.lithium.impl.modules.other.hud.util.ItemHolder;
import me.chachoox.lithium.impl.modules.other.hud.util.TextRadarUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Hud extends Module {

    protected final EnumProperty<Rendering> rendering =
            new EnumProperty<>(
                    Rendering.UP,
                    new String[]{"Rendering", "rendr"},
                    "Direction to render stuff."
            );

    protected final EnumProperty<Organize> organize =
            new EnumProperty<>(
                    Organize.LENGTH,
                    new String[]{"Organize", "org"},
                    "The ordering of the arraylist."
            );

    protected final EnumProperty<ArrayListMode> arrayList =
            new EnumProperty<>(
                    ArrayListMode.GRAY,
                    new String[]{"Arraylist", "modlist", "ArraylistColor"},
                    "Renders the active modules."
            );

    protected final EnumProperty<HudColour> hudColour =
            new EnumProperty<>(
                    HudColour.GLOBAL,
                    new String[]{"HudColour", "hudcolor"},
                    "The color of the hud."
            );

    protected final EnumProperty<HudRainbow> rainbow =
            new EnumProperty<>(
                    HudRainbow.HORIZONTAL,
                    new String[]{"Rainbow", "hudrainbow"},
                    "The type of rainbow."
            );

    protected final EnumProperty<InfoColour> infoColour =
            new EnumProperty<>(
                    InfoColour.GLOBAL,
                    new String[]{"InfoColour", "infocolor"},
                    "Color of information."
            );

    protected final EnumProperty<PotionMode> potionColour =
            new EnumProperty<>(
                    PotionMode.VANILLA,
                    new String[]{"Potions", "potioncol", "potioncolor"},
                    "Color of the potions."
            );

    protected final EnumProperty<DurationColour> potionNumberColor =
            new EnumProperty<>(DurationColour.GRAY,
                    new String[]{"DurationColour", "potionnumbercolor"},
                    "Changes the duration color of potion effects."
            );

    protected final Property<Boolean> capes =
            new Property<>(
                    false,
                    new String[]{"Capes", "cap", "capejamin"},
                    "Capes for client users."
            );

    protected final Property<Boolean> rainbowWatermark =
            new Property<>(
                    false,
                    new String[]{"RainbowWatermark", "rainbowmark"},
                    "Changes the messages prefix to a rainbow"
            );

    protected final Property<Boolean> announceModules =
            new Property<>(false,
                    new String[]{"AnnounceModules", "modulenotify"},
                    "Announces in chat when you toggle a module."
            );

    public final Property<Boolean> shadow =
            new Property<>(
                    true,
                    new String[]{"Shadow", "shadw"},
                    "Tweaks the shadows in the font."
            );

    protected final EnumProperty<Watermark> watermark =
            new EnumProperty<>(
                    Watermark.LITHIUM,
                    new String[]{"Watermark", "mark"},
                    "Client watermark."
            );

    protected final Property<Boolean> offsetWatermark =
            new Property<>(
                    false,
                    new String[]{"OffsetWatermark", "offsetmark"},
                    "Vertical offset of the watermark"
            );

    protected final StringProperty customWatermark =
            new StringProperty(
                    "SexMaster.CC",
                    new String[]{"CustomMark", "customwatermark"}
            );

    protected final EnumProperty<Welcomer> welcomer =
            new EnumProperty<>(
                    Welcomer.NONE,
                    new String[]{"Welcomer", "welcome"},
                    "Hello >:3."
            );

    protected final StringProperty customWelcome =
            new StringProperty("Selamat datang <Player>",
                    new String[]{"CustomWelcome", "customwelcomer"}
            );

    protected final Property<Boolean> leftSideWelcomer =
            new Property<>(
                    false,
                    new String[]{"LeftSideWelcomer", "leftsidewelcome"},
                    "Draws the welcomer under the watermark if you have it enabled."
            );

    protected final Property<Boolean> textRadar =
            new Property<>(
                    false,
                    new String[]{"TextRadar", "texradar"},
                    "Draws a list of the people in our render."
            );

    protected final EnumProperty<ArmorMode> armor =
            new EnumProperty<>(
                    ArmorMode.DAMAGE,
                    new String[]{"Armor", "aa"},
                    "Damage: - Colors damage text by damage percent / Global: - Colors damage text as the global color."
            );

    protected final EnumProperty<ArmorText> armorText =
            new EnumProperty<>(
                    ArmorText.NEW,
                    new String[]{"ArmorText", "armortex"},
                    "Draws the durability of our armor."
            );


    protected final Property<Boolean> totems =
            new Property<>(
                    false,
                    new String[]{"Totems", "tots"},
                    "Draws how many totems we carry."
            );

    protected final Property<Boolean> items =
            new Property<>(
                    false,
                    new String[]{"Items", "item"},
                    "Draws how many gear we carry."
            );

    protected final Property<Boolean> rotations =
            new Property<>(
                    false,
                    new String[]{"Rotations", "rots"},
                    "Draws our current rotations."
            );

    protected final Property<Boolean> coords =
            new Property<>(
                    true,
                    new String[]{"Coordinates", "coords", "coord"},
                    "Draws our current coordinates."
            );

    protected final Property<Boolean> kmh =
            new Property<>(
                    true,
                    new String[]{"Speed", "sped", "kmh", "bps"},
                    "Draws our current movement speed."
            );

    protected final Property<Boolean> brand =
            new Property<>(
                    false,
                    new String[]{"ServerBrand", "brand"},
                    "Draws the server brand."
            );

    protected final EnumProperty<TpsMode> tps =
            new EnumProperty<>(
                    TpsMode.BOTH,
                    new String[]{"Tps", "Ticks", "TicksPerSecond"},
                    "Renders the servers ticks per second."
            );

    protected final Property<Boolean> packets =
            new Property<>(
                    false,
                    new String[]{"Packets", "packet", "packetpersecond"},
                    "Draws how many packets we sent per second."
            );

    protected final Property<Boolean> ping =
            new Property<>(
                    true,
                    new String[]{"Ping", "p", "latency"},
                    "Draws our ping."
            );

    protected final Property<Boolean> fps =
            new Property<>(
                    false,
                    new String[]{"Fps", "frames", "fp"},
                    "Draws our fps."
            );

    protected final Property<Boolean> lag =
            new Property<>(false,
                    new String[]{"Lag", "ddos", "impcatnotify", "4/9/2023"},
                    "Draws how long the server has stopped responding."
            );

    protected int outgoingPackets;
    protected int incomingPackets; //find a usage for dis

    protected StopWatch timer = new StopWatch();
    protected StopWatch lagTimer = new StopWatch();

    protected Collection<Module> modules;

    protected final LinkedList<Long> frames = new LinkedList<>();
    protected int fpsCount;

    private final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);
    private final ItemStack EXPERIENCE = new ItemStack(Items.EXPERIENCE_BOTTLE);
    private final ItemStack GAP = new ItemStack(Items.GOLDEN_APPLE);
    private final ItemStack CRYSTAL = new ItemStack(Items.END_CRYSTAL);

    public Hud() {
        super("HUD", new String[]{"HUD", "huud", "huuud"}, "Hud elements.", Category.OTHER);
        this.offerProperties(
                rendering, organize, arrayList, hudColour, rainbow, infoColour, potionColour, potionNumberColor, capes,
                rainbowWatermark, announceModules, shadow, watermark, offsetWatermark, customWatermark, welcomer, customWelcome, leftSideWelcomer, textRadar,
                armor, armorText, totems, items, rotations, coords, kmh, brand, tps, packets, ping, fps, lag
        );
        this.offerListeners(new ListenerUpdate(this), new ListenerGameLoop(this), new ListenerReceive(this), new ListenerSend(this), new ListenerRender(this));
        this.arrayList.addObserver(event -> resetAnimation());
    }

    @Override
    public void onLoad() {
        modules = Managers.MODULE.getModules();
    }

    @Override
    public void onWorldLoad() {
        resetAnimation();
    }

    private void resetAnimation() {
        for (Module module : modules) {
            if (module.isHidden()) {
                module.getAnimation().finished(Direction.BACKWARDS);
            } else {
                module.getAnimation().finished(Direction.FORWARDS);
            }
        }
    }

    protected void onRender(ScaledResolution resolution) {
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();

        if (watermark.getValue() != Watermark.NONE) {
            renderText(watermark.getValue() == Watermark.CUSTOM ? customWatermark.getValue() : watermark.getValue().getWatermark(), 2, offsetWatermark.getValue() ? 12 : 2);
        }

        if (welcomer.getValue() != Welcomer.NONE) {
            String welcome = welcomer.getValue().getWelcomer();

            if (welcomer.getValue() == Welcomer.CUSTOM) {
                welcome = customWelcome.getValue();
            }

            welcome = welcome.replace("<Player>", mc.player.getName());
            welcome = welcome.replace("<Time>", getTimeOfDay());

            if (!leftSideWelcomer.getValue()) {
                renderText(welcome, (resolution.getScaledWidth() / 2F) - (Managers.FONT.getStringWidth(welcome) / 2F) + 2, 2);
            } else {
                renderText(welcome, 2, offsetWatermark.getValue() ? 24 : 12);
            }
        }

        if (lag.getValue() && lagTimer.passed(2500L) && !mc.isSingleplayer()) {
            final String lagString = "Server hasn't responded in " + String.format("%.2f", (lagTimer.getTime() / 1000f)) + "s";
            renderText(lagString, (resolution.getScaledWidth() / 2F) - (Managers.FONT.getStringWidth(lagString) / 2F) + 2, welcomer.getValue() != Welcomer.NONE ? 12 : 2);
        }

        if (textRadar.getValue()) {
            renderTextRadar(((offsetWatermark.getValue() && watermark.getValue() != Watermark.NONE) ? 28 : 18) + ((welcomer.getValue() != Welcomer.NONE && leftSideWelcomer.getValue()) ? 10 : 0));
        }

        boolean renderingUp = rendering.getValue() == Rendering.UP;
        boolean chatOpened = mc.ingameGUI.getChatGUI().getChatOpen();

        if (arrayList.getValue() != ArrayListMode.NONE) {
            modules = Managers.MODULE.getModules();
            List<Module> modulesList = new ArrayList<>(modules);
            int offset = renderingUp ? 2 : height - (chatOpened ? 24 : 10);

            switch (organize.getValue()) {
                case ABC: {
                    modulesList.sort(Comparator.comparing(mod -> mod.displayLabel.getValue()));
                    break;
                }
                case LENGTH: {
                    modulesList.sort((mod1, mod2) -> Managers.FONT.getStringWidth(mod2.getFullLabel()) - Managers.FONT.getStringWidth(mod1.getFullLabel()));
                    break;
                }
            }

            for (Module module : modulesList) {
                if (module.isHidden()) {
                    continue;
                }

                final Animation moduleAnimation = module.getAnimation();
                moduleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);

                if (!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS)) {
                    continue;
                }

                final String fullLabel = module.getFullLabel();
                int x = width - Managers.FONT.getStringWidth(fullLabel);
                x += Math.abs((moduleAnimation.getOutput() - 1) * Managers.FONT.getStringWidth(fullLabel));
                renderArrayList(module, fullLabel, x - 2, offset);
                offset += (renderingUp ? 10 : -10);
            }
        }

        int offset = renderingUp ? (chatOpened ? 24 : 10) : 2;
        int y = 10;

        if (potionColour.getValue() != PotionMode.NONE) {
            for (PotionEffect effect : mc.player.getActivePotionEffects()) {
                int amplifier = effect.getAmplifier();
                String potionString = I18n.format(effect.getEffectName())
                        + (amplifier > 0 ? (" " + (amplifier + 1)
                        + "") : "") + ": "
                        + getPotionNumberColor(potionNumberColor.getValue())
                        + Potion.getPotionDurationString(effect, 1);
                int potionColor = effect.getPotion().getLiquidColor();
                if (potionColour.getValue() != PotionMode.GLOBAL) {
                    Managers.FONT.drawString(potionString, width - Managers.FONT.getStringWidth(potionString) - 2, renderingUp ? height - offset : offset, potionColor);
                } else {
                    renderText(potionString, width - Managers.FONT.getStringWidth(potionString) - 2, renderingUp ? height - offset : offset);
                }
                offset += y;
            }
        }

        if (brand.getValue()) {
            final String brandString = getInfoColour(false) + (mc.getCurrentServerData() == null ? "Vanilla" : mc.player.getServerBrand());
            renderText(brandString, width - Managers.FONT.getStringWidth(brandString) - 2, renderingUp ? height - offset : offset);
            offset += y;
        }

        if (kmh.getValue()) {
            final String kmhString = getInfoColour(false)
                    + "Speed: "
                    + getInfoColour(true)
                    + MathUtil.round(Managers.SPEED.getSpeed(), 2)
                    + "km/h";
            renderText(kmhString, width - Managers.FONT.getStringWidth(kmhString) - 2, renderingUp ? height - offset : offset);
            offset += y;
        }

        if (tps.getValue() != TpsMode.NONE) {
            String tpsString = null;

            switch (tps.getValue()) {
                case NORMAL: {
                    tpsString =
                            getInfoColour(false) +
                                    "TPS: "
                                    + getInfoColour(true)
                                    + MathUtil.round(Managers.TPS.getTps(), 2);
                    break;
                }
                case BOTH: {
                    tpsString =
                            getInfoColour(false) + "TPS: "
                            + getInfoColour(true)
                            + MathUtil.round(Managers.TPS.getTps(), 2)
                            + TextColor.GRAY
                            + " ("
                            + getInfoColour(true)
                            + MathUtil.round(Managers.TPS.getCurrentTps(), 2)
                            + TextColor.GRAY
                            + ")";
                    break;
                }
            }

            renderText(tpsString, width - Managers.FONT.getStringWidth(tpsString) - 2, renderingUp ? height - offset : offset);
            offset += y;
        }

        if (packets.getValue()) {
            final String packetString = getInfoColour(false)
                    + "Packets: "
                    + getInfoColour(true)
                    + outgoingPackets
                    + "/s";
            renderText(packetString,
                    width - Managers.FONT.getStringWidth(packetString) - 2,
                    renderingUp ? height - offset : offset);
            offset += y;
        }

        //noinspection ConstantConditions
        if (ping.getValue() && !mc.isSingleplayer() && mc.getConnection().getPlayerInfo(mc.player.getUniqueID()) != null) {
            final String pingString = getInfoColour(false)
                    + "Ping: "
                    + getInfoColour(true)
                    + NetworkUtil.getLatency() + "ms"
                    + (Managers.MODULE.get(PingSpoof.class).isEnabled() ? TextColor.GRAY
                    + " ("
                    + getInfoColour(true)
                    + NetworkUtil.getLatencyNoSpoof()
                    + "ms" + TextColor.GRAY + ")" : "")
                    .replace("-", "");
            renderText(pingString,
                    width - Managers.FONT.getStringWidth(pingString) - 2, renderingUp ? height - offset : offset);
            offset += y;
        }

        if (fps.getValue()) {
            final String fpsString = getInfoColour(false)
                    + "FPS: "
                    + getInfoColour(true) + fpsCount;
            renderText(fpsString, width - Managers.FONT.getStringWidth(fpsString) - 2, renderingUp ? height - offset : offset);
        }

        if (rotations.getValue()) {
            final String colorFix = hudColour.getValue() == HudColour.RAINBOW ? rainbow.getValue().getColor() : getInfoColour(false);
            renderText(getInfoColour(false)
                    + "Pitch: "
                    + getInfoColour(true)
                    + String.format("%.2f", MathHelper.wrapDegrees(mc.player.rotationPitch))
                    + colorFix
                    + " Yaw: "
                    + getInfoColour(true)
                    + String.format("%.2f", MathHelper.wrapDegrees(mc.player.rotationYaw)), 2, height - (coords.getValue() ? (chatOpened ? 34 : 20) : (chatOpened ? 24 : 10)));
        }

        if (coords.getValue()) {
            String directionString = getDirectionForDisplay();
            String coordsString = getInfoColour(false) + "XYZ: "
                    + getInfoColour(true)
                    + getRoundedDouble(mc.player.posX)
                    + TextColor.GRAY
                    + ", "
                    + getInfoColour(true)
                    + getRoundedDouble(mc.player.posY)
                    + TextColor.GRAY
                    + ", "
                    + getInfoColour(true)
                    + getRoundedDouble(mc.player.posZ);
            if (mc.player.dimension != 1) {
                coordsString += TextColor.GRAY
                        + " (" + getInfoColour(true)
                        + getRoundedDouble(getDimensionCoord(mc.player.posX))
                        + TextColor.GRAY
                        + ", " + getInfoColour(true)
                        + getRoundedDouble(getDimensionCoord(mc.player.posZ))
                        + TextColor.GRAY
                        + ")";
            }
            renderText(coordsString + directionString, 2, height - (chatOpened ? 24 : 10));
        }

        if (!mc.player.isSpectator()) {

            renderArmorHUD(resolution);

            if (totems.getValue()) {
                renderTotemHUD(resolution);
            }

            if (items.getValue()) {
                renderItemHUD(resolution);
            }
        }
    }

    public int getArmorY() {
        int y;
        if (mc.player.isInsideOfMaterial(Material.WATER) && mc.player.getAir() > 0 && !mc.player.capabilities.isCreativeMode) {
            y = 65;
        } else if (mc.player.getRidingEntity() != null && !mc.player.capabilities.isCreativeMode) {
            if (mc.player.getRidingEntity() instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) mc.player.getRidingEntity();
                y = (int) (45 + Math.ceil((entity.getMaxHealth() - 1.0F) / 20.0F) * 10);
            } else {
                y = 45;
            }
        } else if (mc.player.capabilities.isCreativeMode) {
            y = mc.player.isRidingHorse() ? 45 : 38;
        } else {
            y = 55;
        }
        return y;
    }

    public void renderArmorHUD(ScaledResolution resolution) {
        GlStateManager.enableTexture2D();
        int width = resolution.getScaledWidth() >> 1;
        int height = resolution.getScaledHeight();
        int i1;
        int i2 = 15;
        int i3 = i1 = 3;
        while (i3 >= 0) {
            ItemStack stack = mc.player.inventory.armorInventory.get(i1);
            if (!(stack.getItem() instanceof ItemAir)) {
                boolean renderArmor = armor.getValue() != ArmorMode.NONE;
                int y = height - getArmorY();
                int x = width + i2;
                int color = !(armor.getValue() == ArmorMode.GLOBAL) ? stack.getItem().getRGBDurabilityForDisplay(stack) : Colours.get().getColour().getRGB();

                if (renderArmor) {
                    GlStateManager.enableDepth();
                    mc.getRenderItem().zLevel = 200.0f;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, "");
                    mc.getRenderItem().zLevel = 0.0f;
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    String count = (stack.getCount() > 1) ? (stack.getCount() + "") : "";
                    Managers.FONT.drawString(count, (float) (x + 19 - 2 - Managers.FONT.getStringWidth(count)), (float) (y + 9), 0xFFFFFFFF);
                }

                i2 += 18;

                final int dmg = (int) ItemUtil.getDamageInPercent(stack);
                switch (armorText.getValue()) {
                    case OLD: {
                        Managers.FONT.drawString(dmg + "", x + 8 - (mc.fontRenderer.getStringWidth(dmg + "") >> 1), y + (renderArmor ? -8 : 6), color);
                        break;
                    }
                    case NEW: {
                        GlStateManager.pushMatrix();
                        GlStateManager.scale(0.625F, 0.625F, 0.625F);
                        GlStateManager.disableDepth();
                        mc.fontRenderer.drawStringWithShadow(dmg + "%", (x + getFixedArmorOffset(dmg)) * 1.6F, y * 1.6F + (renderArmor ? -8 : 16), color); //this looks weird with custom font
                        GlStateManager.enableDepth();
                        GlStateManager.scale(1.0f, 1.0f, 1.0f);
                        GlStateManager.popMatrix();
                        break;
                    }
                }
            }
            i3 = --i1;
        }

        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    public int getFixedArmorOffset(int percent) {
        if (percent == 100) {
            return 1;
        } else if (percent < 10) {
            return 5;
        } else {
            return 3;
        }
    }

    private void renderTotemHUD(ScaledResolution resolution) {
        int totems = ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING);
        if (totems > 0) {
            GlStateManager.enableTexture2D();

            int width = resolution.getScaledWidth();
            int height = resolution.getScaledHeight();
            int i = width / 2;
            int y = height - getArmorY();
            int x = i - 189 + 9 * 20 + 2;
            drawItem(TOTEM, totems, x, y);
        }
    }

    private void renderItemHUD(ScaledResolution resolution) {
        final ItemHolder experience = new ItemHolder(EXPERIENCE, ItemUtil.getItemCount(Items.EXPERIENCE_BOTTLE));
        final ItemHolder gaps = new ItemHolder(GAP, ItemUtil.getItemCount(Items.GOLDEN_APPLE));
        final ItemHolder crystal = new ItemHolder(CRYSTAL, ItemUtil.getItemCount(Items.END_CRYSTAL));

        final List<ItemHolder> holders = new ArrayList<>();
        holders.add(experience);
        holders.add(gaps);
        holders.add(crystal);

        int offsetY = 0;
        int i = resolution.getScaledWidth() / 2;
        int y = resolution.getScaledHeight() - 18;
        int x = i - 90 + 9 * 20 + 2;

        for (ItemHolder holder : holders) {
            drawItem(holder.getStack(), holder.getCount(), x , y - offsetY);
            offsetY += 16;
        }
    }

    private double getDimensionCoord(double coord) {
        if (mc.player.dimension == -1) {
            return coord * 8;
        } else if (mc.player.dimension == 0) {
            return coord / 8;
        }
        return coord;
    }

    public void renderText(String text, float x, float y) {
        String colorCode = hudColour.getValue() == HudColour.RAINBOW ? rainbow.getValue().getColor() : "";
        Managers.FONT.drawString(colorCode
                + text, x, y, (hudColour.getValue() == HudColour.GLOBAL || hudColour.getValue() == HudColour.ELITE)
                ? Colours.get().getColour().getRGB() : (hudColour.getValue() == HudColour.STATIC
                ? (ColorUtil.staticRainbow((y + 1) * 0.89f, Colours.get().getColour())) : 0xffffffff));
    }

    public void renderArrayList(Module module, String text, float x, float y) {
        if (hudColour.getValue() == HudColour.ELITE) {
            int color = getColorByCategory(module.getCategory());
            Managers.FONT.drawString(text, x, y, color);
        } else {
            renderText(text, x, y);
        }
    }

    public int getColorByCategory(Category category) {
        switch (category) {
            case COMBAT:
                return new Color(6, 59, 138).getRGB();
            case MISC:
                return new Color(178, 229, 208).getRGB();
            case MOVEMENT:
                return new Color(136, 221, 235).getRGB();
            case PLAYER:
                return new Color(225, 174, 195).getRGB();
            case RENDER:
                return new Color(60, 130, 0).getRGB();
            case OTHER:
                return new Color(255, 107, 107).getRGB();
        }
        return -1;
    }

    public void renderTextRadar(int start) {
        int offset = 0;

        List<EntityPlayer> playersList = mc.world.playerEntities;
        Map<String, Integer> players = new HashMap<>();

        for (EntityPlayer player : playersList) {
            if (player == null || player == mc.player || player.isDead) {
                continue;
            }

            final String playerName = player.getName();

            final int health = (int) EntityUtil.getHealth(player);
            String hp = String.valueOf(health);
            String hpStr = TextRadarUtil.getHealthColor(health) + hp;

            final float distance = mc.player.getDistance(player);
            String dist = String.valueOf((int) distance);
            String distStr = TextColor.WHITE + "[" + TextRadarUtil.getDistanceColor(distance) + dist + "m" + TextColor.WHITE + "] ";

            String pops = "";
            final Map<String, Integer> registry = Managers.TOTEM.getPopMap();
            pops += registry.containsKey(player.getName()) ? " -" + registry.get(player.getName()) : "";

            final String radarStr = distStr + TextColor.RESET + (Managers.FRIEND.isFriend(playerName) ? TextColor.AQUA : "") + playerName + " " + hpStr + TextColor.WHITE + pops;
            players.put(radarStr, (int) mc.player.getDistance(player));
        }

        if (players.isEmpty()) {
            return;
        }

        players = TextRadarUtil.sortByValue(players);

        for (Map.Entry<String, Integer> player : players.entrySet()) {
            Managers.FONT.drawString(player.getKey(), 2, start + offset, Colours.get().getColour().getRGB());
            offset += 10;
        }

    }

    private void drawItem(ItemStack item, int count, int x, int y) {
        GlStateManager.enableDepth();
        mc.getRenderItem().zLevel = 200F;
        mc.getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, item, x, y, "");
        mc.getRenderItem().zLevel = 0F;
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        Managers.FONT.drawString(TextColor.RESET + count + "", x + 19 - 2 - Managers.FONT.getStringWidth(count + ""), y + 9, Colours.get().getColour().getRGB());
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    private String getRoundedDouble(double pos) {
        return String.format("%.2f", pos);
    }

    private String getTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay < 12) {
            return "Good Morning";
        } else if (timeOfDay < 16) {
            return "Good Afternoon";
        } else if (timeOfDay < 21) {
            return "Good Evening";
        } else {
            return "Good Night";
        }
    }

    public static int getDirection4D() {
        return MathHelper.floor((mc.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    }

    private String getDirectionForDisplay() {
        switch (getDirection4D()) {
            case 0:
                return TextColor.GRAY + " [" + getInfoColour(true) + "+Z" + TextColor.GRAY + "]";
            case 1:
                return TextColor.GRAY + " [" + getInfoColour(true) + "-X" + TextColor.GRAY + "]";
            case 2:
                return TextColor.GRAY + " [" + getInfoColour(true) + "-Z" + TextColor.GRAY + "]";
            case 3:
            default:
                return TextColor.GRAY + " [" + getInfoColour(true) + "+X" + TextColor.GRAY + "]";
        }
    }

    public String getInfoColour(boolean info) {
        if (info) {
            return infoColour.getValue() == InfoColour.GLOBAL ? TextColor.WHITE : (hudColour.getValue() == HudColour.RAINBOW ? rainbow.getValue().getColor() : TextColor.RESET);
        }

        return infoColour.getValue() == InfoColour.GLOBAL ? (hudColour.getValue() == HudColour.RAINBOW ? "" : TextColor.RESET) : TextColor.GRAY;
    }

    public String getPotionNumberColor(DurationColour colour) {
        switch (colour) {
            case GRAY: return TextColor.GRAY;
            case WHITE: return TextColor.WHITE;
        }
        return "";
    }

    public String getWatermark() {
        final String WATERMARK = TextColor.DARK_PURPLE + "[" + TextColor.LIGHT_PURPLE + "SexMaster.CC" + TextColor.DARK_PURPLE + "] ";
        final String RAINBOW_MARK = rainbow.getValue().getColor() + "[SexMaster.CC] ";
        return rainbowWatermark.getValue() ? RAINBOW_MARK : WATERMARK;
    }

    public boolean isAnnouncingModules() {
        return announceModules.getValue();
    }

    public boolean isCapeEnabled() {
        return capes.getValue();
    }

    public boolean whiteBrackets() {
        return arrayList.getValue() == ArrayListMode.WHITE;
    }
}
