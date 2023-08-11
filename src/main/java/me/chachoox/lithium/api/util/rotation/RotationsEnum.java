package me.chachoox.lithium.api.util.rotation;

public enum RotationsEnum {
    PACKET,
    NORMAL,
    NONE;

    public static final String[] ALIASES = new String[]{"Rotations", "rotate", "rots", "rotation"};

    public static final String DESCRIPTION
            = "None - Does not rotate " +
            "/ Normal - Rotates normally by changing yaw & pitch" +
            " / Packet - Uses packets to rotate, could get you kicked for too many packets.";
}
