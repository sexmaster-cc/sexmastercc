package me.chachoox.lithium.impl.managers.client;

import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.modules.combat.antiregear.AntiRegear;
import me.chachoox.lithium.impl.modules.combat.aura.Aura;
import me.chachoox.lithium.impl.modules.combat.autoarmour.AutoArmour;
import me.chachoox.lithium.impl.modules.combat.autocrystal.AutoCrystal;
import me.chachoox.lithium.impl.modules.combat.autolog.AutoLog;
import me.chachoox.lithium.impl.modules.combat.autotrap.AutoTrap;
import me.chachoox.lithium.impl.modules.combat.bowmanip.BowManip;
import me.chachoox.lithium.impl.modules.combat.criticals.Criticals;
import me.chachoox.lithium.impl.modules.combat.fastbow.FastBow;
import me.chachoox.lithium.impl.modules.combat.holefill.HoleFill;
import me.chachoox.lithium.impl.modules.combat.instantexp.InstantEXP;
import me.chachoox.lithium.impl.modules.combat.instantweb.InstantWeb;
import me.chachoox.lithium.impl.modules.combat.offhand.Offhand;
import me.chachoox.lithium.impl.modules.combat.selffill.SelfFill;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;
import me.chachoox.lithium.impl.modules.misc.antiinteract.AntiInteract;
import me.chachoox.lithium.impl.modules.misc.autoreply.AutoReply;
import me.chachoox.lithium.impl.modules.misc.chatappend.ChatAppend;
import me.chachoox.lithium.impl.modules.misc.chattimestamps.ChatTimeStamps;
import me.chachoox.lithium.impl.modules.misc.coordexploit.CoordinatesExploit;
import me.chachoox.lithium.impl.modules.misc.deathannouncer.DeathAnnouncer;
import me.chachoox.lithium.impl.modules.misc.deathcoordslog.DeathCoordsLog;
import me.chachoox.lithium.impl.modules.misc.extratab.ExtraTab;
import me.chachoox.lithium.impl.modules.misc.fpslimit.FPSLimit;
import me.chachoox.lithium.impl.modules.misc.middleclick.MiddleClick;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import me.chachoox.lithium.impl.modules.misc.nobreakanim.NoBreakAnim;
import me.chachoox.lithium.impl.modules.misc.packetcanceller.PacketCanceller;
import me.chachoox.lithium.impl.modules.misc.packetlogger.PacketLogger;
import me.chachoox.lithium.impl.modules.misc.payloadspoof.PayloadSpoof;
import me.chachoox.lithium.impl.modules.misc.pingspoof.PingSpoof;
import me.chachoox.lithium.impl.modules.misc.popcounter.PopCounter;
import me.chachoox.lithium.impl.modules.misc.portalgodmode.PortalGodMode;
import me.chachoox.lithium.impl.modules.misc.pvpinfo.PvPInfo;
import me.chachoox.lithium.impl.modules.misc.smartreply.SmartReply;
import me.chachoox.lithium.impl.modules.misc.spammer.Spammer;
import me.chachoox.lithium.impl.modules.misc.stresser.Stresser;
import me.chachoox.lithium.impl.modules.misc.timer.Timer;
import me.chachoox.lithium.impl.modules.misc.visualrange.VisualRange;
import me.chachoox.lithium.impl.modules.movement.elytrafly.ElytraFly;
import me.chachoox.lithium.impl.modules.movement.fly.Fly;
import me.chachoox.lithium.impl.modules.movement.holepull.HolePull;
import me.chachoox.lithium.impl.modules.movement.icespeed.IceSpeed;
import me.chachoox.lithium.impl.modules.movement.inventorymove.InventoryMove;
import me.chachoox.lithium.impl.modules.movement.jesus.Jesus;
import me.chachoox.lithium.impl.modules.movement.liquidspeed.LiquidSpeed;
import me.chachoox.lithium.impl.modules.movement.noaccel.NoAccel;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import me.chachoox.lithium.impl.modules.movement.nofall.NoFall;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import me.chachoox.lithium.impl.modules.movement.phase.Phase;
import me.chachoox.lithium.impl.modules.movement.reversestep.ReverseStep;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;
import me.chachoox.lithium.impl.modules.movement.step.Step;
import me.chachoox.lithium.impl.modules.movement.tunnelspeed.TunnelSpeed;
import me.chachoox.lithium.impl.modules.movement.velocity.Velocity;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.chat.ChatBridge;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import me.chachoox.lithium.impl.modules.other.rpc.RichPresence;
import me.chachoox.lithium.impl.modules.player.antihitbox.AntiHitBox;
import me.chachoox.lithium.impl.modules.player.antivoid.AntiVoid;
import me.chachoox.lithium.impl.modules.player.autofeetplace.AutoFeetPlace;
import me.chachoox.lithium.impl.modules.player.autorespawn.AutoRespawn;
import me.chachoox.lithium.impl.modules.player.autostackfill.AutoStackFill;
import me.chachoox.lithium.impl.modules.player.autotool.AutoTool;
import me.chachoox.lithium.impl.modules.player.fakelag.FakeLag;
import me.chachoox.lithium.impl.modules.player.fakeplayer.FakePlayer;
import me.chachoox.lithium.impl.modules.player.fakerotation.FakeRotation;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import me.chachoox.lithium.impl.modules.player.fastdrop.FastDrop;
import me.chachoox.lithium.impl.modules.player.fastplace.FastPlace;
import me.chachoox.lithium.impl.modules.player.liquidinteract.LiquidInteract;
import me.chachoox.lithium.impl.modules.player.nobreakdelay.NoBreakDelay;
import me.chachoox.lithium.impl.modules.player.noeatdelay.NoEatDelay;
import me.chachoox.lithium.impl.modules.player.positionspoof.PositionSpoof;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;
import me.chachoox.lithium.impl.modules.player.scaffold.Scaffold;
import me.chachoox.lithium.impl.modules.player.selfblocker.SelfBlocker;
import me.chachoox.lithium.impl.modules.player.sprint.Sprint;
import me.chachoox.lithium.impl.modules.render.animations.Animations;
import me.chachoox.lithium.impl.modules.render.betterchat.BetterChat;
import me.chachoox.lithium.impl.modules.render.blockhighlight.BlockHighlight;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import me.chachoox.lithium.impl.modules.render.compass.Compass;
import me.chachoox.lithium.impl.modules.render.customsky.CustomSky;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import me.chachoox.lithium.impl.modules.render.esp.ESP;
import me.chachoox.lithium.impl.modules.render.fovmodifier.FovModifier;
import me.chachoox.lithium.impl.modules.render.fullbright.Fullbright;
import me.chachoox.lithium.impl.modules.render.glintmodify.GlintModify;
import me.chachoox.lithium.impl.modules.render.holeesp.HoleESP;
import me.chachoox.lithium.impl.modules.render.inventorypreview.InventoryPreview;
import me.chachoox.lithium.impl.modules.render.killeffect.KillEffect;
import me.chachoox.lithium.impl.modules.render.logoutspots.LogoutSpots;
import me.chachoox.lithium.impl.modules.render.modelchanger.ModelChanger;
import me.chachoox.lithium.impl.modules.render.nametags.Nametags;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import me.chachoox.lithium.impl.modules.render.pearltrace.PearlTrace;
import me.chachoox.lithium.impl.modules.render.pollosesp.PollosESP;
import me.chachoox.lithium.impl.modules.render.popcolours.PopColours;
import me.chachoox.lithium.impl.modules.render.skeleton.Skeleton;
import me.chachoox.lithium.impl.modules.render.storageesp.StorageESP;
import me.chachoox.lithium.impl.modules.render.tracers.Tracers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleManager {

    //private final List<Module> modules = new ArrayList<>();
    private final Map<Class<? extends Module>, Module> modules = new LinkedHashMap<>();

    @SuppressWarnings("DanglingJavadoc")
    public void init() {

        /**
         **     Other Modules
         **/

        register(new ClickGUI());
        register(new Colours());
        register(new CustomFont());
        register(new Hud());
        register(new RichPresence());
        register(new BlocksManager());
        register(new ChatBridge());

        /**
         **     Combat Modules
         **/

        register(new AntiRegear());
        register(new Aura());
        register(new AutoCrystal());
        register(new AutoLog());
        register(new AutoTrap());
        register(new Criticals());
        register(new HoleFill());
        register(new Offhand());
        register(new SelfFill());
        register(new FastBow());
        register(new AutoArmour());
        register(new InstantEXP());
        register(new BowManip());
        register(new AutoFeetPlace());
        register(new SelfBlocker());
        register(new AutoStackFill());
        register(new InstantWeb());


        /**
         **     Miscellaneous Modules
         **/

        register(new AntiInteract());
        register(new PayloadSpoof());
        register(new Announcer());
        register(new Stresser());
        register(new AutoReply());
        register(new ExtraTab());
        register(new FPSLimit());
        register(new BetterChat());
        register(new DisplayTweaks());
        register(new PvPInfo());
        register(new ChatAppend());
        register(new DeathAnnouncer());
        register(new PacketCanceller());
        register(new PacketLogger());
        register(new AutoRespawn());
        register(new DeathCoordsLog());
        register(new PopCounter());
        register(new VisualRange());
        register(new CoordinatesExploit());
        register(new PortalGodMode());
        register(new NoBreakAnim());
        register(new Timer());
        register(new MiddleClick());
        register(new PingSpoof());
        register(new SmartReply());
        register(new Spammer());

        /**
         **     Movement Modules
         **/

        register(new HolePull());
        register(new Fly());
        register(new NoClip());
        register(new NoAccel());
        register(new Velocity());
        register(new InventoryMove());
        register(new LiquidSpeed());
        register(new ReverseStep());
        register(new TunnelSpeed());
        register(new IceSpeed());
        register(new NoSlow());
        register(new ElytraFly());
        register(new Step());
        register(new Jesus());
        register(new NoFall());
        register(new Speed());
        register(new PacketFly());
        register(new Phase());
        register(new FakeLag());

        /**
         **     Player Modules
         **/

        register(new PositionSpoof());
        register(new FastBreak());
        register(new AntiHitBox());
        register(new NameProtect());
        register(new FakeRotation());
        register(new AutoTool());
        register(new LiquidInteract());
        register(new NoEatDelay());
        register(new FastDrop());
        register(new NoBreakDelay());
        register(new AntiVoid());
        register(new Scaffold());
        register(new FastPlace());
        register(new Sprint());
        register(new Quiver());
        register(new FakePlayer());

        /**
         **     Visual Modules
         **/

        register(new Animations());
        register(new BlockHighlight());
        register(new GlintModify());
        register(new Nametags());
        register(new ModelChanger());
        register(new NoRender());
        register(new HoleESP());
        register(new PearlTrace());
        register(new LogoutSpots());
        register(new Chams());
        register(new KillEffect());
        register(new ChatTimeStamps());
        register(new ESP());
        register(new StorageESP());
        register(new Skeleton());
        register(new CustomSky());
        register(new Compass());
        register(new FovModifier());
        register(new PopColours());
        register(new InventoryPreview());
        register(new Fullbright());
        register(new PollosESP());
        register(new Tracers());

        modules.values().forEach(Module::onLoad);
        modules.values().forEach(mod -> mod.displayLabel.setValue(mod.getLabel())); //chinesee
    }

    private void register(Module module) {
        modules.put(module.getClass(), module);
    }

    public Collection<Module> getModules() {
        return modules.values();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> clazz) {
        return (T) modules.get(clazz);
    }

    public Module getModuleByLabel(String label) {
        for (Module module : modules.values()) {
            if (module.getLabel().equalsIgnoreCase(label)) {
                return module;
            }
        }
        return null;
    }

    public Module getModuleByAlias(String alias) {
        for (Module module : modules.values()) {
            for (String aliases : module.getAliases()) {
                if (aliases.equalsIgnoreCase(alias)) {
                    return module;
                }
            }
        }
        return null;
    }

}
