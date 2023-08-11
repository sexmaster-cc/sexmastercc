package me.chachoox.lithium.impl.modules.combat.offhand;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public enum OffhandMode {
    TOTEMS(Items.TOTEM_OF_UNDYING),
    CRYSTALS(Items.END_CRYSTAL),
    GAPPLES(Items.GOLDEN_APPLE);

    public final Item item;

    OffhandMode(Item item) {
        this.item = item;
    }

}