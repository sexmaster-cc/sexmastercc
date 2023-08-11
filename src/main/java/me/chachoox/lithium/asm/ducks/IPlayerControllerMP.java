package me.chachoox.lithium.asm.ducks;

public interface IPlayerControllerMP {
    void syncItem();

    void setBlockHitDelay(int delay);

    void setIsHittingBlock(boolean b);

    void setCurBlockDamageMP(float damage);
}
