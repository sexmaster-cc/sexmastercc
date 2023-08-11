package me.chachoox.lithium.asm.mixins.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.asm.ducks.INetworkManager;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.*;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.concurrent.Future;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager implements INetworkManager {

    @Shadow
    public abstract boolean isChannelOpen();

    @Shadow
    protected abstract void flushOutboundQueue();

    @Shadow
    protected abstract void dispatchPacket(final Packet<?> inPacket, @Nullable final GenericFutureListener<? extends Future<? super Void >>[] futureListeners);

    @Shadow
    private Channel channel;

    @Shadow
    public abstract void setConnectionState(EnumConnectionState newState);

    @Shadow
    private INetHandler packetListener;

    @Shadow
    public abstract void sendPacket(Packet<?> packetIn);

    @Override
    public Packet<?> sendPacketNoEvent(Packet<?> packet) {
        PacketEvent.NoEvent<?> event = new PacketEvent.NoEvent<>(packet);
        Bus.EVENT_BUS.dispatch(event, packet.getClass());
        if (event.isCanceled()) {
            return packet;
        }

        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchSilently(packet);

            return packet;
        }

        return null;
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onSendPacketPre(Packet<?> packet, CallbackInfo info) {
        PacketEvent.Send<?> event = new PacketEvent.Send<>(packet);
        Bus.EVENT_BUS.dispatch(event, packet.getClass());

        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket" + "(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info) {
        PacketEvent.Receive<?> event = new PacketEvent.Receive<>(packet);
        try {
            Bus.EVENT_BUS.dispatch(event, packet.getClass());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (event.isCanceled()) {
            info.cancel();
        } else if (!event.getPostEvents().isEmpty()) {
            try {
                //noinspection unchecked
                ((Packet<INetHandler>) packet).processPacket(this.packetListener);
            } catch (ThreadQuickExitException e) {
                e.printStackTrace();
            }

            for (Runnable runnable : event.getPostEvents()) {
                Minecraft.getMinecraft().addScheduledTask(runnable);
            }

            info.cancel();
        }
    }

    @Inject(method = "dispatchPacket", at = @At("RETURN"))
    private void onSendPacketPost(final Packet<?> packetIn, @Nullable final GenericFutureListener<? extends Future <? super Void >>[] futureListeners, CallbackInfo info) {
        PacketEvent.Post<?> event = new PacketEvent.Post<>(packetIn);
        Bus.EVENT_BUS.dispatch(event, packetIn.getClass());
    }

    @Inject(method = "closeChannel", at = @At(value = "INVOKE", target = "Lio/netty/channel/Channel;isOpen()Z", remap = false))
    private void onDisconnectHook(ITextComponent component, CallbackInfo info) {
        if (this.isChannelOpen()) {
            Bus.EVENT_BUS.dispatch(new DisconnectEvent(component));
        }
    }

    private void dispatchSilently(Packet<?> inPacket) {
        final EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
        final EnumConnectionState protocolConnectionState = this.channel.attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get();

        if (protocolConnectionState != enumconnectionstate) {
            Logger.getLogger().log(Level.DEBUG, "Disabled auto read");
            this.channel.config().setAutoRead(false);
        }

        if (this.channel.eventLoop().inEventLoop()) {
            if (enumconnectionstate != protocolConnectionState) {
                this.setConnectionState(enumconnectionstate);
            }

            ChannelFuture channelfuture = this.channel.writeAndFlush(inPacket);
            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.channel.eventLoop().execute(() -> {
                if (enumconnectionstate != protocolConnectionState) {
                    setConnectionState(enumconnectionstate);
                }

                ChannelFuture channelfuture1 = channel.writeAndFlush(inPacket);
                channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            });
        }
    }

    @Inject(method = "exceptionCaught", at = @At("RETURN"))
    private void onExceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_, CallbackInfo ci) {
        p_exceptionCaught_2_.printStackTrace();
        System.out.println("----------------------------------------------");
        Thread.dumpStack();
        ci.cancel();
    }

}
