package i.am.cal.mojangster;

public final class Color {
    private final int color;

    private Color(int color) {
        this.color = color;
    }

    public static Color ofTransparent(int color) {
        return new Color(color);
    }

    public static Color ofOpaque(int color) {
        return new Color(-16777216 | color);
    }

    public static Color ofRGB(float r, float g, float b) {
        return ofRGBA(r, g, b, 1.0F);
    }

    public static Color ofRGB(int r, int g, int b) {
        return ofRGBA(r, g, b, 255);
    }

    public static Color ofRGBA(float r, float g, float b, float a) {
        return ofRGBA((int)((double)(r * 255.0F) + 0.5D), (int)((double)(g * 255.0F) + 0.5D), (int)((double)(b * 255.0F) + 0.5D), (int)((double)(a * 255.0F) + 0.5D));
    }

    public static Color ofRGBA(int r, int g, int b, int a) {
        return new Color((a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255);
    }

    public static Color ofHSB(float hue, float saturation, float brightness) {
        return ofOpaque(HSBtoRGB(hue, saturation, brightness));
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0F) {
            r = g = b = (int)(brightness * 255.0F + 0.5F);
        } else {
            float h = (hue - (float)Math.floor((double)hue)) * 6.0F;
            float f = h - (float)Math.floor((double)h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - saturation * (1.0F - f));
            switch((int)h) {
                case 0:
                    r = (int)(brightness * 255.0F + 0.5F);
                    g = (int)(t * 255.0F + 0.5F);
                    b = (int)(p * 255.0F + 0.5F);
                    break;
                case 1:
                    r = (int)(q * 255.0F + 0.5F);
                    g = (int)(brightness * 255.0F + 0.5F);
                    b = (int)(p * 255.0F + 0.5F);
                    break;
                case 2:
                    r = (int)(p * 255.0F + 0.5F);
                    g = (int)(brightness * 255.0F + 0.5F);
                    b = (int)(t * 255.0F + 0.5F);
                    break;
                case 3:
                    r = (int)(p * 255.0F + 0.5F);
                    g = (int)(q * 255.0F + 0.5F);
                    b = (int)(brightness * 255.0F + 0.5F);
                    break;
                case 4:
                    r = (int)(t * 255.0F + 0.5F);
                    g = (int)(p * 255.0F + 0.5F);
                    b = (int)(brightness * 255.0F + 0.5F);
                    break;
                case 5:
                    r = (int)(brightness * 255.0F + 0.5F);
                    g = (int)(p * 255.0F + 0.5F);
                    b = (int)(q * 255.0F + 0.5F);
            }
        }

        return -16777216 | r << 16 | g << 8 | b;
    }

    public int getColor() {
        return this.color;
    }

    public int getAlpha() {
        return this.color >> 24 & 255;
    }

    public int getRed() {
        return this.color >> 16 & 255;
    }

    public int getGreen() {
        return this.color >> 8 & 255;
    }

    public int getBlue() {
        return this.color & 255;
    }

    public Color brighter(double factor) {
        int r = this.getRed();
        int g = this.getGreen();
        int b = this.getBlue();
        int i = (int)(1.0D / (1.0D - 1.0D / factor));
        if (r == 0 && g == 0 && b == 0) {
            return ofRGBA(i, i, i, this.getAlpha());
        } else {
            if (r > 0 && r < i) {
                r = i;
            }

            if (g > 0 && g < i) {
                g = i;
            }

            if (b > 0 && b < i) {
                b = i;
            }

            return ofRGBA(Math.min((int)((double)r / (1.0D / factor)), 255), Math.min((int)((double)g / (1.0D / factor)), 255), Math.min((int)((double)b / (1.0D / factor)), 255), this.getAlpha());
        }
    }

    public Color darker(double factor) {
        return ofRGBA(Math.max((int)((double)this.getRed() * (1.0D / factor)), 0), Math.max((int)((double)this.getGreen() * (1.0D / factor)), 0), Math.max((int)((double)this.getBlue() * (1.0D / factor)), 0), this.getAlpha());
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            return this.color == ((Color)other).color;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.color;
    }

    public String toString() {
        return "Color{r=" + this.getRed() + "g=" + this.getGreen() + "b=" + this.getBlue() + '}';
    }
}
