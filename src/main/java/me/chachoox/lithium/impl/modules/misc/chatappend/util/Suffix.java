package me.chachoox.lithium.impl.modules.misc.chatappend.util;

public enum Suffix {
    SEXMASTER(" \u23d0 \uff33\uff45\ua1d3\uff2d\u039b\uff53\u01ac\u03b5\u0280\uff0e\uff23\uff23"),
    CHACHOOXWARE(" \u1D04\u029C\u1D00\u1D04\u029C\u1D0F\u1D0F\u0445\u1D21\u1D00\u0280\u1D07"),
    HOCKEYWARE(" \u23d0 \uFF28\uFF4F\uFF43\uFF4B\uFF45\uFF59\uFF37\uFF41\uFF52\uFF45"),
    KONAS(" \u23D0 \uff2b\uff4f\uff4e\uff41\uff53"),
    LEUXBACKDOOR(" \u00bb L\u03a3uxB\u03b1ckdoor"),
    XULU(" \u23D0 \u166D \u144C \u14AA \u144C"),
    PHOBOS(" \u23d0 \u1d18\u029c\u1d0f\u0299\u1d0f\ua731.\u1d07\u1d1c"),
    RAION("\u4E28\u24C7\u24B6\u24BE\u24C4\u24C3"),
    TROLLHACK(" \u23d0 \uFF34\uFF32\uFF2F\uFF2C\uFF2C \uFF28\uFF21\uFF23\uFF2B"),
    GAMESENSE(" \u300b\u0262\u1d00\u1d0d\u1d07\ua731\u1d07\u0274\ua731\u1d07"),
    OSIRIS(" \u300b\u1d0f\ua731\u026a\u0280\u026a\ua731"),
    ILCLIENT(" \u2551ILCLient\u4E97"),
    LOVER(" > Lover"),
    MOONGOD(" \u23d0\u0020\uff2d\uff4f\uff4f\uff4e\uff27\uff4f\uff44"),
    YAKIGOD(" \u23d0 \u028F\u1D00\u1D0B\u026A\u0262\u1D0F\u1D05.\u1D04\u1D04"),
    BRANK(" \u23d0 \u0299\u0280\u1D00\u0274\u1D0B1"),
    TROLLGOD(" | Konas");

    private final String suffix;

    Suffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
