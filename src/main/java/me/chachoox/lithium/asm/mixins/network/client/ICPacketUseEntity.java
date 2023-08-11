package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketUseEntity.class)
public interface ICPacketUseEntity {
    @Accessor("entityId")
    void setEntityId(int id);

    @Accessor("entityId")
    int getEntityId();

    @Accessor("action")
    void setAction(CPacketUseEntity.Action action);

    @Accessor("hand")
    void setHand(EnumHand hand);

    @Accessor("action")
    CPacketUseEntity.Action getAction();
}
