package me.chachoox.lithium.impl.modules.render.popcolours.util;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.popcolours.PopColours;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("all")// minecraft code :)
@SideOnly(Side.CLIENT)
public class CustomPopParticle extends ParticleSimpleAnimated {

    public CustomPopParticle(World p_i47220_1_, double p_i47220_2_, double p_i47220_4_, double p_i47220_6_, double p_i47220_8_, double p_i47220_10_, double p_i47220_12_) {
        super(p_i47220_1_, p_i47220_2_, p_i47220_4_, p_i47220_6_, 176, 8, -0.05F);
        final PopColours POP_COLOURS = Managers.MODULE.get(PopColours.class);
        this.motionX = p_i47220_8_;
        this.motionY = p_i47220_10_;
        this.motionZ = p_i47220_12_;
        this.particleScale *= POP_COLOURS.isEnabled() ? POP_COLOURS.getScale() : 0.75F;
        this.particleMaxAge = 60 + this.rand.nextInt(12);

        if (this.rand.nextInt(4) == 0) {
            setFirstColor();
        } else {
            setSecondColor();
        }

        this.setBaseAirFriction(0.6F);
    }

    public void setFirstColor() {
        final PopColours POP_COLOURS = Managers.MODULE.get(PopColours.class);
        if (POP_COLOURS.isEnabled()) {
            this.setRBGColorF(
                    POP_COLOURS.getColor().getRed() / 255F + this.rand.nextFloat() * POP_COLOURS.getRandomRed(),
                    POP_COLOURS.getColor().getGreen() / 255F + this.rand.nextFloat() * POP_COLOURS.getRandomGreen(),
                    POP_COLOURS.getColor().getBlue() / 255F + this.rand.nextFloat() * POP_COLOURS.getRandomBlue()
            );
        } else {
            this.setRBGColorF(0.6F + this.rand.nextFloat() * 0.2F, 0.6F + this.rand.nextFloat() * 0.3F, this.rand.nextFloat() * 0.2F);
        }
    }

    public void setSecondColor() {
        final PopColours POP_COLOURS = Managers.MODULE.get(PopColours.class);
        if (POP_COLOURS.isEnabled()) {
            this.setRBGColorF(
                    POP_COLOURS.getSecondColor().getRed() / 255F + this.rand.nextFloat() * POP_COLOURS.getRandomRed(),
                    POP_COLOURS.getSecondColor().getGreen() / 255F + this.rand.nextFloat() * POP_COLOURS.getRandomGreen(),
                    POP_COLOURS.getSecondColor().getBlue() / 255F + this.rand.nextFloat() * POP_COLOURS.getRandomBlue());
        } else {
            this.setRBGColorF(0.1F + this.rand.nextFloat() * 0.2F, 0.4F + this.rand.nextFloat() * 0.3F, this.rand.nextFloat() * 0.2F);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new CustomPopParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
