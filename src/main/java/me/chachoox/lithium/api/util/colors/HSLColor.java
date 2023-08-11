package me.chachoox.lithium.api.util.colors;

import java.awt.*;

public class HSLColor {
    private final Color rgb;
    private final float[] hsl;
    private final float alpha;

    public HSLColor(Color rgb) {
        this.rgb = rgb;
        hsl = fromRGB( rgb );
        alpha = rgb.getAlpha() / 255.0f;
    }

    public HSLColor(float h, float s, float l) {
        this(h, s, l, 1.0f);
    }

    public HSLColor(float h, float s, float l, float alpha) {
        hsl = new float[] {h, s, l};
        this.alpha = alpha;
        rgb = toRGB(hsl, alpha);
    }

    public HSLColor(float[] hsl) {
        this(hsl, 1.0f);
    }

    public HSLColor(float[] hsl, float alpha) {
        this.hsl = hsl;
        this.alpha = alpha;
        rgb = toRGB(hsl, alpha);
    }

    public Color adjustHue(float degrees) {
        return toRGB(degrees, hsl[1], hsl[2], alpha);
    }

    public Color adjustLuminance(float percent) {
        return toRGB(hsl[0], hsl[1], percent, alpha);
    }

    public Color adjustSaturation(float percent) {
        return toRGB(hsl[0], percent, hsl[2], alpha);
    }

    public Color adjustShade(float percent) {
        float multiplier = (100.0f - percent) / 100.0f;
        float l = Math.max(0.0f, hsl[2] * multiplier);

        return toRGB(hsl[0], hsl[1], l, alpha);
    }

    public Color adjustTone(float percent) {
        float multiplier = (100.0f + percent) / 100.0f;
        float l = Math.min(100.0f, hsl[2] * multiplier);

        return toRGB(hsl[0], hsl[1], l, alpha);
    }

    public float getAlpha() {
        return alpha;
    }

    public Color getComplementary() {
        float hue = (hsl[0] + 180.0f) % 360.0f;
        return toRGB(hue, hsl[1], hsl[2]);
    }

    public float getHue() {
        return hsl[0];
    }

    public float[] getHSL() {
        return hsl;
    }

    public float getLuminance() {
        return hsl[2];
    }

    public Color getRGB() {
        return rgb;
    }

    public float getSaturation() {
        return hsl[1];
    }

    public String toString() {
        return "HSLColor[h=" + hsl[0] +
                ",s=" + hsl[1] +
                ",l=" + hsl[2] +
                ",alpha=" + alpha + "]";
    }

    public static float[] fromRGB(Color color) {
        //  Get RGB values in the range 0 - 1

        float[] rgb = color.getRGBColorComponents( null );
        float r = rgb[0];
        float g = rgb[1];
        float b = rgb[2];

        //	Minimum and Maximum RGB values are used in the HSL calculations

        float min = Math.min(r, Math.min(g, b));
        float max = Math.max(r, Math.max(g, b));

        //  Calculate the Hue

        float h = 0;

        if (max == min)
            h = 0;
        else if (max == r)
            h = ((60 * (g - b) / (max - min)) + 360) % 360;
        else if (max == g)
            h = (60 * (b - r) / (max - min)) + 120;
        else if (max == b)
            h = (60 * (r - g) / (max - min)) + 240;

        //  Calculate the Luminance

        float l = (max + min) / 2;

        //  Calculate the Saturation

        float s = 0;

        if (max == min)
            s = 0;
        else if (l <= .5f)
            s = (max - min) / (max + min);
        else
            s = (max - min) / (2 - max - min);

        return new float[] {h, s * 100, l * 100};
    }

    public static Color toRGB(float[] hsl) {
        return toRGB(hsl, 1.0f);
    }

    public static Color toRGB(float[] hsl, float alpha) {
        return toRGB(hsl[0], hsl[1], hsl[2], alpha);
    }

    public static Color toRGB(float h, float s, float l) {
        return toRGB(h, s, l, 1.0f);
    }

    public static Color toRGB(float h, float s, float l, float alpha) {
        if (s <0.0f || s > 100.0f) {
            String message = "Color parameter outside of expected range - Saturation";
            throw new IllegalArgumentException( message );
        }

        if (l <0.0f || l > 100.0f) {
            String message = "Color parameter outside of expected range - Luminance";
            throw new IllegalArgumentException( message );
        }

        if (alpha <0.0f || alpha > 1.0f) {
            String message = "Color parameter outside of expected range - Alpha";
            throw new IllegalArgumentException( message );
        }

        //  Formula needs all values between 0 - 1.

        h = h % 360.0f;
        h /= 360f;
        s /= 100f;
        l /= 100f;

        float q = 0;

        if (l < 0.5)
            q = l * (1 + s);
        else
            q = (l + s) - (s * l);

        float p = 2 * l - q;

        float r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
        float g = Math.max(0, HueToRGB(p, q, h));
        float b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

        r = Math.min(r, 1.0f);
        g = Math.min(g, 1.0f);
        b = Math.min(b, 1.0f);

        return new Color(r, g, b, alpha);
    }

    private static float HueToRGB(float p, float q, float h) {
        if (h < 0) h += 1;

        if (h > 1 ) h -= 1;

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1 ) {
            return  q;
        }

        if (3 * h < 2) {
            return p + ( (q - p) * 6 * ((2.0f / 3.0f) - h) );
        }

        return p;
    }
}