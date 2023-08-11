package me.chachoox.lithium.impl.modules.render.displaytweaks;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.*;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.modules.render.displaytweaks.util.*;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.Display;

import java.awt.*;

public class DisplayTweaks extends Module {

    protected final EnumProperty<TitleScreens> titleScreen =
            new EnumProperty<>(
                    TitleScreens.SHADER,
                    new String[]{"TitleScreen", "customtitlescreen"},
                    "Tweaks minecraft's title screen."
            );

    protected final EnumProperty<Images> image =
            new EnumProperty<>(
                    Images.PIG,
                    new String[]{"Images", "image"},
                    "The image that will be used."
            );

    protected final EnumProperty<GLSLShaders> shader =
            new EnumProperty<>(GLSLShaders.WAIFU,
                    new String[]{"Shader", "shad", "screen"},
                    "The shader that will be used."
            );

    protected final ColorProperty titleColor =
            new ColorProperty(
                    new Color(0XFF555555),
                    false,
                    new String[]{"TitleColor", "titlescreencolor"}
            );

    protected final ColorProperty titleColorTwo =
            new ColorProperty(new Color(0X0F0F0F),
                    false,
                    new String[]{"TitleColorTwo", "titlecolortwo"}
            );

    protected final NumberProperty<Integer> fps =
            new NumberProperty<>(60, 5, 60,
                    new String[]{"Fps", "maxfps", "frames", "f"},
                    "Max amount of frames in the title screen."
            );

    protected final Property<Boolean> screenshotFix =
            new Property<>(
                    false,
                    new String[]{"ScreenShotFix", "ssfix", "pollosfix"},
                    "Removes lag when screenshotting."
            );

    protected final Property<Boolean> uploadToImgur =
            new Property<>(
                    false,
                    new String[]{"UploadToImgur", "imgur"},
                    "Uploads your screenshot to imgur."
            );

    protected final Property<Boolean> aspectRatio =
            new Property<>(
                    false,
                    new String[]{"AspectRatio", "aspect"},
                    "Changes your aspect ratio."
            );

    protected final NumberProperty<Integer> aspectRatioWidth =
            new NumberProperty<>(
                    mc.displayWidth, 0, mc.displayWidth,
                    new String[]{"Width", "w"},
                    "Width of the screen aspect"
            );

    protected final NumberProperty<Integer> aspectRatioHeight =
            new NumberProperty<>(
                    mc.displayHeight, 0, mc.displayHeight,
                    new String[]{"Height", "h"},
                    "Height of the screen aspect"
            );

    protected final Property<Boolean> hotbarKeys =
            new Property<>(
                    false,
                    new String[]{"HotbarKeys", "keys"},
                    "Draws keybinds in hotbar"
            );

    protected final ColorProperty hotbarkeysColor =
            new ColorProperty(
                    Color.WHITE,
                    false,
                    new String[]{"KeyColor", "hotbarkeycolor"}
            );

    protected final NumberProperty<Integer> currentAccountPosY =
            new NumberProperty<>(
                    42, 0, 200,
                    new String[]{"AccountY", "accountposy"},
                    "Current height of the account message in the title screen."
            );

    protected final EnumProperty<ColorEnum> accountTextColor =
            new EnumProperty<>(
                    ColorEnum.RED,
                    new String[]{"AccountText", "CurrentAccountText", "acctext"},
                    "Color of the text displayed on the title screen."
            );

    protected final StringProperty window =
            new StringProperty(
                    "Minecraft 1.12.2",
                    new String[]{"Window", "screentitle", "title", "titlename"}
            );

    protected final EnumProperty<Icons> icon =
            new EnumProperty<>(
                    Icons.CLASSIC,
                    new String[]{"Icon", "icons", "ico"},
                    "The window icon."
            );

    public GLSLSandboxShader GLSL_SHADER = null;

    public DisplayTweaks() {
        super("DisplayTweaks", new String[]{"DisplayTweaks", "TitleScreen", "CustomTitleScreen", "BetterScreenshots", "AspectRatioChanger"},
                "Display tweaks that would look retarded as other modules.", Category.RENDER);
        this.offerProperties(
                titleScreen, image, shader, fps, titleColor, titleColorTwo,
                screenshotFix, uploadToImgur, aspectRatio, aspectRatioHeight,
                aspectRatioWidth, hotbarKeys, hotbarkeysColor, currentAccountPosY,
                accountTextColor, window, icon
        );
        this.offerListeners(new ListenerAspect(this), new ListenerRender(this));
        this.shader.addObserver(event -> setShader(event.getValue().get()));
        this.window.addObserver(event -> Display.setTitle(event.getValue()));
        this.icon.addObserver(event -> IconUtil.setWindowIcon(event.getValue().get()));
    }

    public Color getColor() {
        return hotbarkeysColor.getColor();
    }

    public Color getTitleColorOne() {
        return titleColor.getColor();
    }

    public Color getTitleColorTwo() {
        return titleColorTwo.getColor();
    }

    public boolean isCustomTitle() {
        return isShader() || isImage() || isColor();
    }

    public boolean isShader() {
        return isEnabled() && titleScreen.getValue() == TitleScreens.SHADER;
    }

    public boolean isImage() {
        return isEnabled() && titleScreen.getValue() == TitleScreens.IMAGE;
    }

    public boolean isColor() {
        return isEnabled() && titleScreen.getValue() == TitleScreens.COLOR;
    }

    public String getImage() {
        return image.getValue().get();
    }

    public int getAccountMessagePosY() {
        return currentAccountPosY.getValue();
    }

    public String getAccountTextColor() {
        return accountTextColor.getValue().getColor();
    }

    public boolean shouldUpload() {
        return isEnabled() && uploadToImgur.getValue();
    }

    public boolean betterScreenShots() {
        return isEnabled() && screenshotFix.getValue();
    }

    public void loadFromConfig(Module module) {
        if (module instanceof DisplayTweaks) {
            Display.setTitle(((DisplayTweaks) module).window.getValue());
            setShader(((DisplayTweaks) module).shader.getValue().get());
            IconUtil.setWindowIcon(((DisplayTweaks) module).icon.getValue().get());
        }
    }

    public void setShader(String shader) {
        try {
            Logger.getLogger().log(Level.INFO, "Trying to setup shader screen.");
            GLSL_SHADER = new GLSLSandboxShader(shader);
        } catch (Exception e) {
            Logger.getLogger().log(TextColor.RED + "Fatal errorjamin while trying to load shader (" + shader + ")");
            Logger.getLogger().log(Level.INFO, "Failed to setup shader screen.");
            GLSL_SHADER = null;
        }
    }

    public int getFps() {
        return fps.getValue();
    }
}
