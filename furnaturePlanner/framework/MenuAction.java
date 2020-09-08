package furnaturePlanner.framework;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


enum MenuAction {
    LOAD_PLAN_LAYOUT {
        @Override
        void contextAction(App app) {
            int choice = app.fileChooser.showOpenDialog(app);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File file = app.fileChooser.getSelectedFile();
                try {
                    BufferedImage unscaled = ImageIO.read(file);
                    double scale = (double) App.DEFAULT_SIZE / unscaled.getWidth();
                    app.bimage = App.scaleImg(unscaled, scale);
                    App.width = app.bimage.getWidth();
                    App.height = app.bimage.getHeight();

                    app.setSize(App.width, App.height);
                    app.setPreferredSize(new Dimension(App.width, App.height));
                    App.frame.pack();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                app.repaint();
            }
        }
    },
    SAVE_DESIGN {
        @Override
        void contextAction(App app) {
            app.fileChooser.setDialogTitle("Create shopping list");
            int choice = app.fileChooser.showSaveDialog(app);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File saveFile = app.fileChooser.getSelectedFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
                    for (Furniture fu : app.furnitures) {
                        bw.write(fu.name() + " " + fu.posX + " " + fu.posY + System.lineSeparator());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    },
    LOAD_DESIGN {
        @Override
        void contextAction(App app) {
            MenuAction.CLEAR.contextAction(app);
            app.fileChooser.setDialogTitle("Select file to load");
            int choice = app.fileChooser.showOpenDialog(app);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File loadFile = app.fileChooser.getSelectedFile();
                try (BufferedReader br = new BufferedReader(new FileReader(loadFile))) {
                    String line = br.readLine();
                    while (line != null) {
                        String[] split = line.split("\\s");
                        for (Furniture fu : app.types) {
                            if (fu.name().equals(split[0])) {
                                app.furnitures.add(
                                        fu.getClass()
                                                .getConstructor(int.class, int.class)
                                                .newInstance(Integer.parseInt(split[1]), Integer.parseInt(split[2]))
                                );
                            }
                        }
                        line = br.readLine();
                    }
                } catch (IOException | InstantiationException | InvocationTargetException | NoSuchMethodException |
                        IllegalAccessException e) {
                    e.printStackTrace();
                }
                app.repaint();
            }
        }
    },
    CREATE_SHOPPING_LIST {
        @Override
        void contextAction(App app) {
            app.fileChooser.setDialogTitle("Select where to save shopping list");
            int choice = app.fileChooser.showSaveDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File saveFile = app.fileChooser.getSelectedFile();

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
                    for (Furniture fu : app.furnitures)
                        bw.write(fu.name() + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    },
    RESET {
        @Override
        void contextAction(App app) {
            App.width = App.DEFAULT_SIZE;
            App.height = App.DEFAULT_SIZE;
            app.bimage = new BufferedImage(App.width, App.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = app.bimage.createGraphics();

            g2.setColor(Color.GRAY);
            int cell = App.DEFAULT_SIZE / 20;
            for (int i = -1; i < App.DEFAULT_SIZE - 1; i += cell) {
                g2.drawLine(i, 0, i, App.DEFAULT_SIZE);
                g2.drawLine(0, i, App.DEFAULT_SIZE, i);
            }
            app.setSize(App.width, App.height);
            app.setPreferredSize(new Dimension(App.width, App.height));
            App.frame.pack();
            app.repaint();
        }
    },
    CLEAR {
        @Override
        void contextAction(App app) {
            app.furnitures.clear();
            app.repaint();
        }
    };

    abstract void contextAction(App app);
}
