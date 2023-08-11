package me.chachoox.lithium.asm.mixins.render;

import com.google.common.collect.Maps;
import me.chachoox.lithium.impl.modules.render.popcolours.util.CustomPopParticle;
import net.minecraft.client.particle.*;
import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ParticleManager.class)
public class MixinParticleManager {

    @Final
    @Shadow
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Map<Integer, IParticleFactory> particleTypes = Maps.newHashMap();

    @Overwrite
    private void registerVanillaParticles() {
        this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new ParticleExplosion.Factory());
        this.registerParticle(EnumParticleTypes.SPIT.getParticleID(), new ParticleSpit.Factory());
        this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new ParticleBubble.Factory());
        this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new ParticleSplash.Factory());
        this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new ParticleWaterWake.Factory());
        this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new ParticleRain.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new ParticleSuspend.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new ParticleSuspendedTown.Factory());
        this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new ParticleCrit.Factory());
        this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new ParticleCrit.MagicFactory());
        this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new ParticleSmokeNormal.Factory());
        this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new ParticleSmokeLarge.Factory());
        this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new ParticleSpell.Factory());
        this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new ParticleSpell.InstantFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new ParticleSpell.MobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new ParticleSpell.AmbientMobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new ParticleSpell.WitchFactory());
        this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new ParticleDrip.WaterFactory());
        this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new ParticleDrip.LavaFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new ParticleHeart.AngryVillagerFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new ParticleSuspendedTown.HappyVillagerFactory());
        this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new ParticleSuspendedTown.Factory());
        this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new ParticleNote.Factory());
        this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new ParticlePortal.Factory());
        this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new ParticleEnchantmentTable.EnchantmentTable());
        this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new ParticleFlame.Factory());
        this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new ParticleLava.Factory());
        this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new ParticleFootStep.Factory());
        this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new ParticleCloud.Factory());
        this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new ParticleRedstone.Factory());
        this.registerParticle(EnumParticleTypes.FALLING_DUST.getParticleID(), new ParticleFallingDust.Factory());
        this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new ParticleBreaking.SnowballFactory());
        this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new ParticleSnowShovel.Factory());
        this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new ParticleBreaking.SlimeFactory());
        this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new ParticleHeart.Factory());
        this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
        this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new ParticleBreaking.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new ParticleDigging.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new ParticleBlockDust.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new ParticleExplosionHuge.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new ParticleExplosionLarge.Factory());
        this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new ParticleFirework.Factory());
        this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new ParticleMobAppearance.Factory());
        this.registerParticle(EnumParticleTypes.DRAGON_BREATH.getParticleID(), new ParticleDragonBreath.Factory());
        this.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), new ParticleEndRod.Factory());
        this.registerParticle(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), new ParticleCrit.DamageIndicatorFactory());
        this.registerParticle(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), new ParticleSweepAttack.Factory());
        this.registerParticle(EnumParticleTypes.TOTEM.getParticleID(), new CustomPopParticle.Factory());
    }

    public void registerParticle(int id, IParticleFactory particleFactory) {
        this.particleTypes.put(id, particleFactory);
    }
}
