package me.chachoox.lithium.api.util.colors;

import java.awt.*;

public class ColorUtil {
    public static int toARGB(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + (b) + (a << 24);
    }

    public static int transparency(int color, double alpha) {
        Color c = new Color(color);
        float r = (1f / 255f) * c.getRed();
        float g = (1f / 255f) * c.getGreen();
        float b = (1f / 255f) * c.getBlue();
        return new Color(r, g, b, (float) alpha).getRGB();
    }

    //TODO: use this to save colors in config manager
    public static int intFromHex(String hex) {
        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return 0xFFFFFFFF;
        }
    }

    public static String hexFromInt(int color) {
        return hexFromInt(new Color(color));
    }

    public static String hexFromInt(Color color) {
        return Integer.toHexString(color.getRGB()).substring(2);
    }

    public static Color changeAlpha(Color origColor, int alpha) {
        return new Color(origColor.getRed(), origColor.getGreen(), origColor.getBlue(), alpha);
    }

    public static int staticRainbow(float offset, Color color) {
        double timer = System.currentTimeMillis() % (1750.0 * 2.5) / (850.0 * 2.5);
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = (float) (hsb[2] * Math.abs((offset + timer) % 1f - 0.55f) + 0.45f);
        return Color.HSBtoRGB(hsb[0], hsb[1], brightness);
    }


    public static float[] toArray(int color) {
        return new float[]
                {
                        (color >> 16 & 255) / 255.0F,
                        (color >> 8 & 255) / 255.0F,
                        (color & 255) / 255.0F,
                        (color >> 24 & 255) / 255.0F,
                };
    }


    public static Color toColor(float red, float green, float blue, float alpha) {
        if (!(green < 0.0f) && !(green > 100.0f)) {
            if (!(blue < 0.0f) && !(blue > 100.0f)) {
                if (!(alpha < 0.0f) && !(alpha > 1.0f)) {
                    red = red % 360.0f / 360.0f;
                    green /= 100.0f;
                    blue /= 100.0f;

                    float blueOff;
                    if (blue < 0.0) {
                        blueOff = blue * (1.0f + green);
                    } else {
                        blueOff = blue + green - green * blue;
                    }

                    green = 2.0f * blue - blueOff;
                    blue = Math.max(0.0f, getFactor(green, blueOff, red + 0.33333334f));
                    float max = Math.max(0.0f, getFactor(green, blueOff, red));
                    green = Math.max(0.0f, getFactor(green, blueOff, red - 0.33333334f));
                    blue = Math.min(blue, 1.0f);
                    max = Math.min(max, 1.0f);
                    green = Math.min(green, 1.0f);
                    return new Color(blue, max, green, alpha);
                } else {
                    throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
                }
            } else {
                throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
            }
        } else {
            throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
        }
    }

    public static float getFactor(float red, float green, float blue) {
        if (blue < 0.0f) {
            ++blue;
        }

        if (blue > 1.0f) {
            --blue;
        }

        if (6.0f * blue < 1.0f) {
            return red + (green - red) * 6.0f * blue;
        } else if (2.0f * blue < 1.0f) {
            return green;
        } else {
            return 3.0F * blue < 2.0f
                    ? red + (green - red) * 6.0f * (0.6666667f - blue)
                    : red;
        }
    }
}
