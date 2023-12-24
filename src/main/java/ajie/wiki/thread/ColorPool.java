package ajie.wiki.thread;

import java.awt.Color;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ColorPool {
    private final Set<Color> colorSet;
    private final Random random;

    public ColorPool() {
        colorSet = new HashSet<>();
        random = new Random();
    }

    public Color get() {
        Color newColor;
        do {
            newColor = generateRandomColor();
        } while (!colorSet.add(newColor));

        return newColor;
    }

    public boolean remove(Color color) {
        return colorSet.remove(color);
    }

    private Color generateRandomColor() {
        int red;
        int green;
        int blue;
        do {
            red = random.nextInt(256);
            green = random.nextInt(256);
            blue = random.nextInt(256);
        } while (isCloseToWhite(red,green,blue));
        return new Color(red, green, blue);
    }

    private boolean isCloseToWhite(int r, int g, int b) {
        return (r + g + b) / 3 >= 230;
    }

    public Set<Color> getColorSet() {
        return colorSet;
    }
}
