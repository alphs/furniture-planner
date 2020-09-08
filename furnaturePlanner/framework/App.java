package furnaturePlanner.framework;


import furnaturePlanner.framework.dependency.Bed;
import furnaturePlanner.framework.dependency.Chair;
import furnaturePlanner.framework.dependency.Table;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static javax.swing.SwingUtilities.invokeLater;

public class App extends JPanel {
    static final int ICON_SIZE = 20;
    static final int DEFAULT_SIZE = 500; // width wise
    static final JFrame frame = new JFrame();
    static int width;
    static int height;
    final JFileChooser fileChooser = new JFileChooser();
    final ArrayList<furnaturePlanner.framework.Furniture> furnitures;
    final furnaturePlanner.framework.Furniture[] types;
    Point point = new Point(0, 0);
    BufferedImage bimage;
    furnaturePlanner.framework.Furniture selected;
    furnaturePlanner.framework.Tools currentTool;

    App() {
        super();
        furnitures = new ArrayList<>();
        types = new furnaturePlanner.framework.Furniture[]{new Table(-1, -1), new Chair(-1, -1), new Bed(-1, -1)};
        selected = types[0]; //dummy
        currentTool = furnaturePlanner.framework.Tools.CURSOR;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                point = e.getPoint();
                currentTool.tool(App.this, point);
                //    System.out.println(selected.name());
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentTool == furnaturePlanner.framework.Tools.CURSOR) {
                    point = e.getPoint();
                    currentTool.tool(App.this, point);
                    repaint();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentTool == furnaturePlanner.framework.Tools.DRAG) {
                    //  System.out.println("mouseReleased");
                    currentTool = furnaturePlanner.framework.Tools.CURSOR;
                    repaint();
                }
            }

        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentTool == furnaturePlanner.framework.Tools.DRAG) {
                    point = e.getPoint();
                    // System.out.println("drag " + selected.name());
                    currentTool.tool(App.this, point);
                    // System.out.println(e.getPoint());
                    repaint();
                }
            }
        });
    }

    private static JButton createToolButton(String path, ActionListener al) {
        JButton toolButton = new JButton(new ImageIcon(createBuffimg(path)));
        toolButton.addActionListener(al);
        return toolButton;
    }

    static BufferedImage createBuffimg(String path) {
        BufferedImage unscaled = null;
        double scale = 1;
        try {
            unscaled = ImageIO.read(App.class.getResource("icons" + File.separator + path));
            scale = (double) ICON_SIZE / (double) unscaled.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaleImg(Objects.requireNonNull(unscaled), scale);
    }

    static BufferedImage scaleImg(BufferedImage unscaled, double scale) {
        int widthScaled = (int) (scale * unscaled.getWidth());
        int heightScaled = (int) (scale * unscaled.getHeight());
        BufferedImage scaled = new BufferedImage(widthScaled, heightScaled, BufferedImage.TYPE_INT_ARGB);

        AffineTransform scaleInst = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInst, AffineTransformOp.TYPE_BICUBIC);
        scaleOp.filter(unscaled, scaled);
        return scaled;
    }

    //      https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
    private JMenuBar createMenu() {
        // create the menu and adds a menu to it
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem loadPlan = new JMenuItem("Load plan layout");
        loadPlan.addActionListener(e -> furnaturePlanner.framework.MenuAction.LOAD_PLAN_LAYOUT.contextAction(this));
        menu.add(loadPlan);
        menu.addSeparator();

        JMenuItem saveDesign = new JMenuItem("Save design");
        saveDesign.addActionListener(e -> furnaturePlanner.framework.MenuAction.SAVE_DESIGN.contextAction(this));
        menu.add(saveDesign);

        JMenuItem loadDesign = new JMenuItem("Load design");
        loadDesign.addActionListener(e -> furnaturePlanner.framework.MenuAction.LOAD_DESIGN.contextAction(this));
        menu.add(loadDesign);

        menu.addSeparator();

        JMenuItem createShoppingList = new JMenuItem("Create shopping list");
        createShoppingList.addActionListener(e -> furnaturePlanner.framework.MenuAction.CREATE_SHOPPING_LIST.contextAction(this));
        menu.add(createShoppingList);
        menu.addSeparator();

        JMenuItem reset = new JMenuItem("Reset");
        reset.addActionListener(e -> furnaturePlanner.framework.MenuAction.RESET.contextAction(this));
        menu.add(reset);

        JMenuItem clear = new JMenuItem("Clear");
        clear.addActionListener(e -> furnaturePlanner.framework.MenuAction.CLEAR.contextAction(this));
        menu.add(clear);

        return menuBar;
    }

    //      https://docs.oracle.com/javase/tutorial/uiswing/components/rootpane.html
    private Container createContentPane() {
        this.setOpaque(true);
        return this;
    }

    private JPanel creatToolBar(furnaturePlanner.framework.Furniture... furn) {
        JPanel toolbar = new JPanel();
        //TODO: make better via checking proximity to cursor and then get the index via checking the array.
        toolbar.add(createToolButton("deleteIcon.png", e -> {
            currentTool = furnaturePlanner.framework.Tools.DELETE;
            System.out.println("delete");
        }));
        toolbar.add(createToolButton("Cursor.png", e -> {
            currentTool = furnaturePlanner.framework.Tools.CURSOR;
            System.out.println("cursor");
        }));
        for (furnaturePlanner.framework.Furniture fu : furn)
            toolbar.add(createToolButton(fu.path(), e -> {
                currentTool = furnaturePlanner.framework.Tools.valueOf(fu.name());
                System.out.println(fu.name());
            }));

        return toolbar;
    }

    public void createAndShowGUI() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        frame.setJMenuBar(this.createMenu());
        frame.setContentPane(this.createContentPane());
        frame.add(creatToolBar(types), BorderLayout.SOUTH); //TODO: extract so it more like framework.

        furnaturePlanner.framework.MenuAction.RESET.contextAction(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bimage, 0, 0, null);
        Graphics2D g2 = (Graphics2D) g.create();
        // g2.drawImage(selected.img, selected.posX, selected.posY, null);
        for (furnaturePlanner.framework.Furniture fu : furnitures)
            g2.drawImage(fu.img, fu.posX, fu.posY, null);
        g2.dispose();
    }

    public static void main(String[] args) {
        invokeLater(new App()::createAndShowGUI);
    }
}
