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

        File rootFolder = new File("screenshots");
        if (rootFolder.exists() && rootFolder.isDirectory()) {
            addScreenshotsFromFolder(rootFolder);
        } else {
            JLabel noScreenshotsLabel = new JLabel("No screenshots found.", SwingConstants.CENTER);
            add(noScreenshotsLabel);
        }
    }

    private void addScreenshotsFromFolder(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                addScreenshotsFromFolder(file);
            } else if (file.getName().endsWith(".png")) {
                JLabel imageLabel = new JLabel(new ImageIcon(file.getAbsolutePath()));
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Optional: Add border
                add(imageLabel);
            }
        }
    }
}
