package me.chachoox.lithium.impl.modules.misc.middleclick;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public class MiddleClick extends Module {

    protected final Property<Boolean> friend =
            new Property<>(
                    true,
                    new String[]{"Friend", "MiddleClickFriend", "mcf", "f"},
                    "Adds the player you middle click as a friend."
            );

    protected final Property<Boolean> pearl =
            new Property<>(
                    false,
                    new String[]{"Pearl", "pearlington", "pear"},
                    "Throws a pearl whenever we middle click."
            );

    protected boolean clicked = false;

    public MiddleClick() {
        super("MiddleClick", new String[]{"MiddleClick", "mcf", "mcp"}, "Allows you to preform actions using the scroll wheel.", Category.MISC);
        this.offerProperties(friend, pearl);
        this.offerListeners(new ListenerUpdate(this));
    }

    protected boolean onEntity() {
        RayTraceResult result;
        return (result = mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityPlayer;
    }

    protected void onClick() {
        Entity entity;
        RayTraceResult result = mc.objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (Managers.FRIEND.isFriend(entity.getName())) {
                Managers.FRIEND.removeFriend(entity.getName());
            } else {
                Managers.FRIEND.addFriend(entity.getName());
            }
        }
        clicked = true;
    }

}