package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;

public class NoRender extends Module {

    private final Property<Boolean> totemAnimation =
            new Property<>(
                    true,
                    new String[]{"Totem", "totemanimation", "totemanim"},
                    ""
            );

    private final Property<Boolean> fire =
            new Property<>(
                    true,
                    new String[]{"Fire", "fir"},
                    ""
            );

    private final Property<Boolean> portal =
            new Property<>(
                    true,
                    new String[]{"Portals", "portal"},
                    ""
            );

    private final Property<Boolean> pumpkin =
            new Property<>(
                    true,
                    new String[]{"Pumpkins", "punpkin"},
                    ""
            );

    private final Property<Boolean> hurtCam =
            new Property<>(
                    true,
                    new String[]{"Hurtcam", "hrtcam"},
                    ""
            );

    private final Property<Boolean> toast =
            new Property<>(
                    true,
                    new String[]{"Toasts", "tutorial"},
                    ""
            );

    private final Property<Boolean> explosions =
            new Property<>(
                    true,
                    new String[]{"Explosions", "explosive"},
                    ""
            );

    private final Property<Boolean> suffocation =
            new Property<>(
                    true,
                    new String[]{"Suffocation", "georgefloyd"},
                    ""
            );

    private final Property<Boolean> noBossOverlay =
            new Property<>(
                    false,
                    new String[]{"BossBar", "boss"},
                    ""
            );

    private final Property<Boolean> noArmor =
            new Property<>(
                    false,
                    new String[]{"Armor", "aa"},
                    ""
            );

    private final Property<Boolean> boxedVines =
            new Property<>(
                    false,
                    new String[]{"Vines", "vine"},
                    ""
            );

    private final Property<Boolean> entityFire =
            new Property<>(
                    false,
                    new String[]{"EntityFire", "entityfir"},
                    ""
            );

    private final Property<Boolean> loadingScreen =
            new Property<>(
                    true,
                    new String[]{"LoadingScreen", "loadinscreen"},
                    ""
            );

    private final Property<Boolean> itemFrames =
            new Property<>(
                    false,
                    new String[]{"ItemFrames", "itemframe"},
                    ""
            );

    private final Property<Boolean> limbSwing =
            new Property<>(
                    false,
                    new String[]{"LimbSwing", "dogshitanimations"},
                    ""
            );

    private final Property<Boolean> noSpectators =
            new Property<>(
                    false,
                    new String[]{"Spectators", "spec"},
                    ""
            );

    private final Property<Boolean> noParrots =
            new Property<>(
                    true,
                    new String[]{"Parrots", "parrot"},
                    ""
            );

    private final Property<Boolean> weather =
            new Property<>(
                    true,
                    new String[]{"Weather", "weathr"},
                    ""
            );

    private final Property<Boolean> viewBobbing =
            new Property<>(
                    false,
                    new String[]{"ViewBobbing", "nobob"},
                    ""
            );

    private final Property<Boolean> tnt =
            new Property<>(
                    false,
                    new String[]{"TNT", "tn", "dynamite"},
                    ""
            );

    private final Property<Boolean> dynamicFOV =
            new Property<>(
                    false,
                    new String[]{"DynamicFov", "dynfov"},
                    ""
            );

    private final Property<Boolean> waterSplash =
            new Property<>(
                    false,
                    new String[]{"WaterSplash"},
                    ""
            );

    private final Property<Boolean> antiResources =
            new Property<>(
                    false,
                    new String[]{"Resources", "resour"},
                    ""
            );

    private final Property<Boolean> eatingParticles =
            new Property<>(
                    true,
                    new String[]{"EatingParticles", "eatparticles"},
                    ""
            );

    private final Property<Boolean> criticalParticles =
            new Property<>(
                    false,
                    new String[]{"CriticalParticles", "critparticles"},
                    ""
            );

    private final Property<Boolean> sprintingParticles =
            new Property<>(
                    false,
                    new String[]{"SprintingParticles", "sprintparticle"},
                    ""
            );

    private final NumberProperty<Integer> time =
            new NumberProperty<>(
                    0, 0, 24000,
                    new String[]{"TimeChanger", "time"},
                    "Changes the time client side."
            );

    public NoRender() {
        super("NoRender", new String[]{"NoRender", "lesslag", "polloshack"}, "Stops rendering certain entities or textures.", Category.RENDER);
        this.offerProperties(totemAnimation, fire, portal, pumpkin, toast, explosions, suffocation, noBossOverlay, noArmor, boxedVines,
                entityFire, loadingScreen, itemFrames, limbSwing, noSpectators, noParrots, weather,
                viewBobbing, tnt, dynamicFOV, waterSplash, antiResources, eatingParticles, criticalParticles,
                sprintingParticles, time);
        this.offerListeners(new ListenerRenderEntity(this), new ListenerGameLoop(this), new ListenerAnimation(this), new ListenerSplash(this), new ListenerRender(this));
        this.viewBobbing.addObserver(event -> mc.gameSettings.viewBobbing = viewBobbing.getValue());
        for (Property<?> property : getProperties()) { //not doin halal lol / yall leave an issue saying what u think this means cuz idk what chachoox talking about
            if (!property.getLabel().equals("TimeChanger") && !property.getLabel().equals("CrystalRange") && !property.getLabel().equals("Keybind")) {
                property.setDescription(String.format("Stops rendering %s.", property.getLabel().toLowerCase()));
            }
        }
    }

    public boolean getTotemAnimation() {
        return isEnabled() && totemAnimation.getValue();
    }

    public boolean getFire() {
        return isEnabled() && fire.getValue();
    }

    public boolean getPortal() {
        return isEnabled() && portal.getValue();
    }

    public boolean getPumpkin() {
        return isEnabled() && pumpkin.getValue();
    }

    public boolean getHurtCam() {
        return isEnabled() && hurtCam.getValue();
    }

    public boolean getToast() {
        return isEnabled() && toast.getValue();
    }

    public boolean getExplosions() {
        return isEnabled() && explosions.getValue();
    }

    public boolean getSuffocation() {
        return isEnabled() && suffocation.getValue();
    }

    public boolean getNoBossOverlay() {
        return isEnabled() && noBossOverlay.getValue();
    }

    public boolean getNoArmor() {
        return isEnabled() && noArmor.getValue();
    }

    public boolean getBoxedVines() {
        return isEnabled() && boxedVines.getValue();
    }

    public boolean getEntityFire() {
        return isEnabled() && entityFire.getValue();
    }

    public boolean getLoadingScreen() {
        return isEnabled() && loadingScreen.getValue();
    }

    public boolean getItemFrames() {
        return isEnabled() && itemFrames.getValue();
    }

    public boolean getLimbSwing() {
        return isEnabled() && limbSwing.getValue();
    }

    public boolean getNoParrots() {
        return isEnabled() && noParrots.getValue();
    }

    public boolean getWeather() {
        return isEnabled() &&  weather.getValue();
    }

    public boolean getTnt() {
        return isEnabled() && tnt.getValue();
    }

    public boolean getDynamicFov() {
        return isEnabled() && dynamicFOV.getValue();
    }

    public boolean getWaterSplash() {
        return isEnabled() && waterSplash.getValue();
    }

    public boolean getAntiResources() {
        return isEnabled() && antiResources.getValue();
    }

    public boolean getNoSpectators() {
        return isEnabled() && noSpectators.getValue();
    }

    public boolean getCriticalParticles() {
        return isEnabled() && criticalParticles.getValue();
    }

    public boolean getEatingParticles() {
        return isEnabled() && eatingParticles.getValue();
    }

    public boolean getSprintParticles() {
        return isEnabled() && sprintingParticles.getValue();
    }

    public int getTime() {
        return time.getValue();
    }

}