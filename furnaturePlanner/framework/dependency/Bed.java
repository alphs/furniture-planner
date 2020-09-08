package furnaturePlanner.framework.dependency;

import furnaturePlanner.framework.Furniture;

public final class Bed extends Furniture {

    public Bed(int x, int y) {
        super(x, y);
    }

    @Override
    public String name() {
        return "BED";
    }

    @Override
    public String path() {
        return "Bed.png";
    }

}
