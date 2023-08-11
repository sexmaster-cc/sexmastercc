package me.chachoox.lithium.impl.event.events.misc;


import net.minecraft.world.chunk.Chunk;

public class UnloadChunkEvent {
    private final Chunk chunk;

    public UnloadChunkEvent(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

}