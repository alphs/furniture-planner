package furnaturePlanner.framework.dependency;

import furnaturePlanner.framework.Furniture;

public final class Chair extends Furniture {

    public Chair(int x, int y) {
        super(x, y);
    }

    @Override
    public String name() {
        return "CHAIR";
    }

    @Override
    public String path() {
        return "Chair.png";
    }
}
