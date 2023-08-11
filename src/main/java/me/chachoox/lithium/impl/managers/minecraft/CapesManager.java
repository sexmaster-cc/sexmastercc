package me.chachoox.lithium.impl.managers.minecraft;

import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class CapesManager extends SubscriberImpl {

    private static final String[] CHACHOOX_UUIDS = {
            "6886dd45-3773-4d9f-8b06-ae0cf9b237ff",
            "c1a2424d-e3e2-40e3-b981-954b9977cc4a",
            "91dd06a5-229c-4c0f-9f83-39857afb71cd",
            "842f5a25-b957-4896-8a0d-1e903bb07498",
            "cf539dff-9a6a-4abe-9f78-98a1d89313c0",
    };

    private static final String[] ZNEUX_UUIDS = {
            "f887e9f4-c0d5-4696-80b7-c93a23fee44e",
            "d5c97c54-9b7a-45ac-9de7-2aff8fe67f00"
    };

    private static final String[] POLLOS_UUIDS = {
            "ca93ab7f-8bd6-4651-a699-1d130c81d0a2",
            "c1e16a89-360e-4c65-8e0e-80b304a6cfba"
    };

    private static final String[] SR_UUIDS = {
            "11ca1e26-526a-41e6-8f48-364ffff36533",
            "687f98e2-3de0-462d-a548-ae35f6501d45"
    };

    private static final String[] MONEYMAKER_UUIDS = {
            "d303ae13-79c6-438f-8c90-806e808b4044",
            "1bc857e5-1471-466a-a038-1216c76ed318",
            "0554301e-026c-4504-9d91-91e628da9ff1",
            "d83976e1-1d89-45ef-972b-1a8703e812f8",
            "6625e3db-ac14-4037-9bc9-861f7848c702"
    };

    private static final String[] ALI_UUIDS = {
            "7ecd6cd9-174d-4104-9b81-d0880e8f0643",
            "e1b9055f-67b4-4d9d-943b-236fa4409e9a",
            "1ff13ebb-085f-4e80-8327-0e4c38101e02",
            "26b17dfd-6afd-4e66-83c9-28a17950c137"
    };

    private static final String[] CRYSTALPVPNN_UUIDS = {
            "cecb9bd2-ec7c-44ef-9288-313dd1b7c083",
            "b1907c3f-0bc0-46e3-ba73-102e0925cfd7"
    };

    private static final String[] AETRA_UUIDS = {
            "f7ab16ae-e3d8-40ad-9556-0399879c9e59",
            "517218c9-7625-4fca-ad02-3b69833c6c39",
            "98860d0d-b2e3-4da7-90ef-13e6ffd8245b"
    };

    private static final String[] CPV_UUIDS = {
            "75aa6787-f7de-456b-9074-8dce5a611100",
            "b8e7bc40-0982-477b-8368-6b619963eb04"
    };

    private static final ResourceLocation CHACHOOX_CAPE = new ResourceLocation("lithium/textures/capes/chachooxcape.png");
    private static final ResourceLocation ZNEUX_CAPE = new ResourceLocation("lithium/textures/capes/zneuxcape.png");
    private static final ResourceLocation POLLOS_CAPE = new ResourceLocation("lithium/textures/capes/polloscape.png");
    private static final ResourceLocation MONEYMAKER_CAPE = new ResourceLocation("lithium/textures/capes/moneymakercape.png");
    private static final ResourceLocation ALI_CAPE = new ResourceLocation("lithium/textures/capes/alicape.png");
    private static final ResourceLocation CRYSTALPVPNN_CAPE = new ResourceLocation("lithium/textures/capes/crystalpvpnncape.png");
    private static final ResourceLocation SR_CAPE = new ResourceLocation("lithium/textures/capes/srcape.png");
    private static final ResourceLocation AETRA_CAPE = new ResourceLocation("lithium/textures/capes/aetracape.png");
    private static final ResourceLocation CPV_CAPE = new ResourceLocation("lithium/textures/capes/cpvcape.png");

    public static ResourceLocation getResourceLocation(UUID id) {
        String stringId = id.toString();
        for (String string : CHACHOOX_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return CHACHOOX_CAPE;
            }
        }
        for (String string : ZNEUX_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return ZNEUX_CAPE;
            }
        }
        for (String string : POLLOS_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return POLLOS_CAPE;
            }
        }
        for (String string : SR_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return SR_CAPE;
            }
        }
        for (String string : MONEYMAKER_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return MONEYMAKER_CAPE;
            }
        }
        for (String string : ALI_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return ALI_CAPE;
            }
        }
        for (String string : CRYSTALPVPNN_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return CRYSTALPVPNN_CAPE;
            }
        }
        for (String string : AETRA_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return AETRA_CAPE;
            }
        }
        for (String string : CPV_UUIDS) {
            if (string.equalsIgnoreCase(stringId)) {
                return CPV_CAPE;
            }
        }
        return null;
    }

    public boolean isCapesEnabled() {
        return Managers.MODULE.get(Hud.class).isCapeEnabled();
    }
}
