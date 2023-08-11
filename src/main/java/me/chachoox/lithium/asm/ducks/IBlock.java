package me.chachoox.lithium.asm.ducks;

import net.minecraft.block.state.IBlockState;

public interface IBlock {
    void setHarvestLevelNonForge(String toolClass, int level);

    String getHarvestToolNonForge(IBlockState state);

    int getHarvestLevelNonForge(IBlockState state);

    float getHardness();

}

