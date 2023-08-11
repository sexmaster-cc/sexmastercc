package me.chachoox.lithium.asm.mixins.client;

import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.impl.event.events.keyboard.ClickMiddleEvent;
import me.chachoox.lithium.impl.event.events.keyboard.MouseEvent;
import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.events.misc.KeyboardEvent;
import me.chachoox.lithium.impl.event.events.misc.ShutDownEvent;
import me.chachoox.lithium.impl.event.events.screen.GuiScreenEvent;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.events.world.WorldClientEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.fpslimit.FPSLimit;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import me.chachoox.lithium.impl.modules.render.displaytweaks.util.IconUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, priority = 1001)
public abstract class MixinMinecraft implements IMinecraft {

    @Shadow
    public EntityPlayerSP player;

    @Shadow
    public WorldClient world;

    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    public GameSettings gameSettings;

    @Shadow
    protected abstract void rightClickMouse();

    @Shadow
    protected abstract void clickMouse();

    @Shadow
    protected abstract void middleClickMouse();

    @Override
    @Accessor("rightClickDelayTimer")
    public abstract int getRightClickDelay();

    @Override
    @Accessor("rightClickDelayTimer")
    public abstract void setRightClickDelay(int delay);

    @Override
    @Accessor("leftClickCounter")
    public abstract void setLeftClickCounter(int count);

    @Override
    @Accessor("timer")
    public abstract Timer getTimer();

    @Override
    @Accessor("session")
    public abstract void setSession(Session session);

    @Override
    public void click(Click type) {
        switch (type) {
            case RIGHT:
                this.rightClickMouse();
                break;
            case LEFT:
                this.clickMouse();
                break;
            case MIDDLE:
                this.middleClickMouse();
                break;
            default:
        }
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 0, shift = At.Shift.BEFORE))
    private void onKeyboard(CallbackInfo callbackInfo) {
        if (Keyboard.getEventKeyState()) {
            for (Module module : Managers.MODULE.getModules()) {
                if (module.getKey() == Keyboard.getEventKey()) {
                    module.toggle();
                }
            }
        }
    }

    @Inject(method = "middleClickMouse", at = @At(value = "HEAD"), cancellable = true)
    public void middleClickMouseHook(CallbackInfo callbackInfo) {
        ClickMiddleEvent event = new ClickMiddleEvent();
        Bus.EVENT_BUS.dispatch(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At(value = "HEAD"))
    public void shutdownMinecraftAppletHook(CallbackInfo info) {
        Logger.getLogger().log(Level.INFO, "Shutting down...");
        Bus.EVENT_BUS.dispatch(new ShutDownEvent());
        Managers.THREAD.shutDown();
    }

    @Inject(method = "runTickMouse", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventButton()I", remap = false))
    public void runTickMouseHook(CallbackInfo ci) {
        Bus.EVENT_BUS.dispatch(new MouseEvent(Mouse.getEventButton(), Mouse.getEventButtonState()));
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", ordinal = 0, shift = At.Shift.AFTER))
    private void post_ScheduledTasks(CallbackInfo callbackInfo) {
        Bus.EVENT_BUS.dispatch(new GameLoopEvent());
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;" + "startSection(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.BEFORE))
    public void runTickHook(CallbackInfo info) {
        Bus.EVENT_BUS.dispatch(new TickEvent());
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;tick()V", shift = At.Shift.AFTER))
    private void postUpdateWorld(CallbackInfo info) {
        Bus.EVENT_BUS.dispatch(new TickEvent.PostWorldTick());
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    public void runTickReturnHook(CallbackInfo info) {
        Bus.EVENT_BUS.dispatch(new TickEvent.Post());
    }

    @Inject(method = "getLimitFramerate", at = @At(value = "HEAD"), cancellable = true)
    public void limitFramesHook(CallbackInfoReturnable<Integer> info) {
        final DisplayTweaks DISPLAY_TWEAKS = Managers.MODULE.get(DisplayTweaks.class);
        if (DISPLAY_TWEAKS.isCustomTitle() && this.world == null && this.currentScreen != null) {
            info.setReturnValue(DISPLAY_TWEAKS.getFps());
            return;
        }

        final FPSLimit FPS_LIMIT = Managers.MODULE.get(FPSLimit.class);
        if (FPS_LIMIT.isEnabled() && !Display.isActive() && FPS_LIMIT.getUnfocusedFPS() != 0) {
            info.setReturnValue(FPS_LIMIT.getUnfocusedFPS());
        }
    }

    @Inject(method = "shutdown", at = @At(value = "HEAD"))
    public void shutdownHook(CallbackInfo info) {
        FPSLimit FPS_LIMIT = Managers.MODULE.get(FPSLimit.class);
        this.gameSettings.limitFramerate = FPS_LIMIT.isEnabled() ? FPS_LIMIT.getFocusedFPS() : this.gameSettings.limitFramerate;
        Managers.CONFIG.saveConfig();
        Managers.FRIEND.saveFriends();
        Logger.getLogger().log(Level.INFO, "Good night :3");
    }

    @Inject(method = "displayGuiScreen", at = @At("HEAD"), cancellable = true)
    private <T extends GuiScreen> void displayGuiScreenHook(T screen, CallbackInfo info) {
        if (player == null && screen instanceof GuiChat) {
            info.cancel();
            return;
        }

        GuiScreenEvent<T> event = new GuiScreenEvent<>(screen);
        Bus.EVENT_BUS.dispatch(event, screen == null ? null : screen.getClass());

        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;" + "Ljava/lang/String;)V", at = @At("HEAD"))
    private void loadWorldHook(WorldClient worldClient, String loadingMessage, CallbackInfo info) {
        if (world != null) {
            Bus.EVENT_BUS.dispatch(new WorldClientEvent.Unload(world));
        }
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE_ASSIGN", target = "org/lwjgl/input/Keyboard.getEventKeyState()Z", remap = false))
    public void runTickKeyboardHook(CallbackInfo callbackInfo) {
        Bus.EVENT_BUS.dispatch(new KeyboardEvent(Keyboard.getEventKeyState(), Keyboard.getEventKey(), Keyboard.getEventCharacter()));
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;world" + ":Lnet/minecraft/client/multiplayer/WorldClient;", ordinal = 4, shift = At.Shift.BEFORE))
    public void post_keyboardTickHook(CallbackInfo info) {
        Bus.EVENT_BUS.dispatch(new KeyboardEvent.Post());
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void initHook(CallbackInfo ci) {
        Lithium.load();
    }

    @Inject(method = "setWindowIcon()V", at = @At(value = "HEAD"), cancellable = true)
    public void setWindowIconHook(CallbackInfo info) {
        IconUtil.setWindowIcon(new String[]{"classic_16x16.png", "classic_32x32.png"});
        info.cancel();
    }

}
