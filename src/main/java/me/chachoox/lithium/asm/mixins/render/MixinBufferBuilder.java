package me.chachoox.lithium.asm.mixins.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(BufferBuilder.class)
public class MixinBufferBuilder {
    @Shadow
    private int vertexFormatIndex;
    @Shadow
    private VertexFormatElement vertexFormatElement;
    @Shadow
    private VertexFormat vertexFormat;

    @Overwrite
    private void nextVertexFormatIndex() {
        List<VertexFormatElement> elementList = vertexFormat.getElements();
        int size = elementList.size();
        do {
            if (++vertexFormatIndex >= size) {
                vertexFormatIndex -= size;
            }
            vertexFormatElement = elementList.get(vertexFormatIndex);
        } while (vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING);
    }
}
