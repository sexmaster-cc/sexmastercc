package me.chachoox.lithium.impl.modules.render.displaytweaks.util;

public enum GLSLShaders {
    BLUEGRID("/shaders/bluegrid.fsh"),
    BLUENEBULA("/shaders/bluenebula.fsh"),
    BLUEVORTEX("/shaders/bluevortex.fsh"),
    CAVE("/shaders/cave.fsh"),
    CLOUDS("/shaders/clouds.fsh"),
    JUPITER("/shaders/jupiter.fsh"),
    MATRIX("/shaders/matrix.fsh"),
    MATRIXRED("/shaders/matrixred.fsh"),
    MINECRAFT("/shaders/minecraft.fsh"),
    PINWHEEL("/shaders/pinwheel.fsh"),
    PURPLEGRID("/shaders/purplegrid.fsh"),
    PURPLEMIST("/shaders/purplemist.fsh"),
    REDGLOW("/shaders/redglow.fsh"),
    SKY("/shaders/sky.fsh"),
    SNAKE("/shaders/snake.fsh"),
    SPACE("/shaders/space.fsh"),
    SPACE2("/shaders/space2.fsh"),
    STORM("/shaders/storm.fsh"),
    WAIFU("/shaders/waifu.fsh"),
    DICK("/shaders/dick.fsh"),
    DESERT("/shaders/desert.fsh"),
    MARIO("/shaders/mario.fsh"),
    MONSTER("/shaders/monster.fsh"),
    STARS("/shaders/stars.fsh");

    final String shader;

    GLSLShaders(String shader) {
        this.shader = shader;
    }

    public String get() {
        return this.shader;
    }
}