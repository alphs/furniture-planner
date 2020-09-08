package furnaturePlanner.framework;


import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class Furniture {
    protected final BufferedImage img = App.createBuffimg(path());
    private final int fWidth = img.getWidth();
    private final int fHeight = img.getHeight();
    protected int posX;
    protected int posY;

    protected Furniture(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    protected abstract String name();

    protected abstract String path();

    protected boolean containsPoint(Point mp) {
        int x0 = posX;
        int y0 = posY;
        int x1 = posX + fWidth;
        int y1 = posY + fHeight;

        return mp.x > x0 && mp.x < x1 &&
                mp.y > y0 && mp.y < y1;
    }
}
