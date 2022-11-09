package sparkuniverse.amo.elemental.util;

public class Color {
    public static final Color WHITE = new Color(1.0F, 1.0F, 1.0F);
    public static final Color LIGHT_GRAY = new Color(0.75F, 0.75F, 0.75F);
    public static final Color GRAY = new Color(0.5F, 0.5F, 0.5F);
    public static final Color DARK_GRAY = new Color(0.25F, 0.25F, 0.25F);
    public static final Color BLACK = new Color(0.0F, 0.0F, 0.0F);
    public static final Color RED = new Color(1.0F, 0.0F, 0.0F);
    public static final Color PINK = new Color(1.0F, 0.5F, 0.5F);
    public static final Color ORANGE = new Color(1.0F, 0.5F, 0.0F);
    public static final Color YELLOW = new Color(1.0F, 1.0F, 0.0F);
    public static final Color LIME = new Color(0.5F, 1.0F, 0.0F);
    public static final Color GREEN = new Color(0.0F, 1.0F, 0.0F);
    public static final Color CYAN = new Color(0.0F, 1.0F, 1.0F);
    public static final Color LIGHT_BLUE = new Color(0.5F, 0.5F, 1.0F);
    public static final Color BLUE = new Color(0.0F, 0.0F, 1.0F);
    public static final Color MAGENTA = new Color(1.0F, 0.0F, 1.0F);
    public static final Color PURPLE = new Color(0.5F, 0.0F, 1.0F);
    public static final Color BROWN = new Color(0.5F, 0.25F, 0.0F);
    public static final Color TRANSPARENT = new Color(0.0F, 0.0F, 0.0F, 0.0F);

    // Color class to store rgba from int
    public int r;
    public int g;
    public int b;
    public int a;
    // Constructor
    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public Color(float r, float g, float b, float a) {
        this.r = (int) (r * 255);
        this.g = (int) (g * 255);
        this.b = (int) (b * 255);
        this.a = (int) (a * 255);
    }
    public Color(float r, float g, float b) {
        this.r = (int) (r * 255);
        this.g = (int) (g * 255);
        this.b = (int) (b * 255);
        this.a = 255;
    }
    // Constructor
    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }
    // Constructor from ARGB int
    public Color(int color) {
        this.r = (color >> 16) & 0xFF;
        this.g = (color >> 8) & 0xFF;
        this.b = color & 0xFF;
        this.a = (color >> 24) & 0xFF;
    }
    // Constructor from RGB int
    public Color(int color, int a) {
        this.r = (color >> 16) & 0xFF;
        this.g = (color >> 8) & 0xFF;
        this.b = color & 0xFF;
        this.a = a;
    }
    // Constructor from hex string
    public Color(String hex) {
        this.r = Integer.valueOf(hex.substring(0, 2), 16);
        this.g = Integer.valueOf(hex.substring(2, 4), 16);
        this.b = Integer.valueOf(hex.substring(4, 6), 16);
        this.a = 255;
    }
    // Constructor from hex string
    public Color(String hex, int a) {
        this.r = Integer.valueOf(hex.substring(0, 2), 16);
        this.g = Integer.valueOf(hex.substring(2, 4), 16);
        this.b = Integer.valueOf(hex.substring(4, 6), 16);
        this.a = a;
    }

    // Get color as ARGB int
    public int getARGB() {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    // Get color as RGB int
    public int getRGB() {
        return (r << 16) | (g << 8) | b;
    }
    // Get color as hex string
    public String getHex() {
        return String.format("%02x%02x%02x", r, g, b);
    }
    // Get color as hex string
    public String getHex(int a) {
        return String.format("%02x%02x%02x%02x", a, r, g, b);
    }
    public int getRed() {
        return r;
    }
    public int getGreen() {
        return g;
    }
    public int getBlue() {
        return b;
    }
    public int getAlpha() {
        return a;
    }
    public void mix(Color color2){
        this.r = (this.r + color2.r) / 2;
        this.g = (this.g + color2.g) / 2;
        this.b = (this.b + color2.b) / 2;
        this.a = (this.a + color2.a) / 2;
    }
    public void mix(Color color2, int weight){
        this.r = (this.r + color2.r * weight) / (weight + 1);
        this.g = (this.g + color2.g * weight) / (weight + 1);
        this.b = (this.b + color2.b * weight) / (weight + 1);
        this.a = (this.a + color2.a * weight) / (weight + 1);
    }
    public void lighten(int amount){
        this.r = Math.min(this.r + amount, 255);
        this.g = Math.min(this.g + amount, 255);
        this.b = Math.min(this.b + amount, 255);
    }
    public void darken(int amount){
        this.r = Math.max(this.r - amount, 0);
        this.g = Math.max(this.g - amount, 0);
        this.b = Math.max(this.b - amount, 0);
    }
}
