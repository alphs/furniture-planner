package furnaturePlanner.framework.dependency;

import furnaturePlanner.framework.Furniture;

public final class Table extends Furniture {

    public Table(int x, int y) {
        super(x, y);
    }

    @Override
    public String name() {
        return "TABLE";
    }

    @Override
    public String path() {
        return "Table.png";
    }
}
