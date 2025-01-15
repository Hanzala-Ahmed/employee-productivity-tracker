package ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ScreenshotGallery extends JFrame {
    public ScreenshotGallery() {
        super("Screenshot Gallery");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 3));

        File folder = new File("screenshots");
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.getName().endsWith(".png")) {
                    JLabel imageLabel = new JLabel(new ImageIcon(file.getAbsolutePath()));
                    add(imageLabel);
                }
            }
        }
    }
}
