package furnaturePlanner.framework;

import furnaturePlanner.framework.dependency.Bed;
import furnaturePlanner.framework.dependency.Chair;
import furnaturePlanner.framework.dependency.Table;

import java.awt.Point;


public enum Tools {
    DELETE {
        @Override
        void tool(App app, Point point) {
            for (int i = 0; i < app.furnitures.size(); i++) {
                if (app.furnitures.get(i).containsPoint(point)) {
                    app.furnitures.remove(i);
                    return;
                }
            }
        }
    },
    CURSOR {
        @Override
        void tool(App app, Point point) {
            for (Furniture fu : app.furnitures) {
                if (fu.containsPoint(point)) {
                    app.selected = fu;
                    app.currentTool = DRAG;
                    return;
                }
            }
        }
    },
    TABLE {
        @Override
        void tool(App app, Point point) {
            app.selected = new Table(point.x, point.y);
            app.furnitures.add(app.selected);
        }
    },
    CHAIR {
        @Override
        void tool(App app, Point point) {
            app.selected = new Chair(point.x, point.y);
            app.furnitures.add(app.selected);
        }
    },
    BED {
        @Override
        void tool(App app, Point point) {
            app.selected = new Bed(point.x, point.y);
            app.furnitures.add(app.selected);
        }
    },
    DRAG {
        @Override
        void tool(App app, Point point) {
            app.selected.posX = point.x;
            app.selected.posY = point.y;
            //   System.out.println(app.selected.name() + "hello" + point);
        }
    };

    abstract void tool(App app, Point point);
}


