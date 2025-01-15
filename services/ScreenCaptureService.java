package services;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenCaptureService {
    private String sessionFolderPath;

    public void initializeSessionFolder(String employeeName) {
        try {
            String employeeFolderPath = "screenshots/" + employeeName;
            File employeeFolder = new File(employeeFolderPath);
            if (!employeeFolder.exists()) {
                employeeFolder.mkdirs();
            }

            String date = java.time.LocalDate.now().toString();
            String dateFolderPath = employeeFolderPath + "/" + date;
            File dateFolder = new File(dateFolderPath);
            if (!dateFolder.exists()) {
                dateFolder.mkdirs();
            }

            String session = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH-mm-ss"));
            sessionFolderPath = dateFolderPath + "/Session-" + session;
            File sessionFolder = new File(sessionFolderPath);
            if (!sessionFolder.exists()) {
                sessionFolder.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String captureScreen() {
        try {
            if (sessionFolderPath == null) {
                throw new IllegalStateException("Session folder is not initialized. Call initializeSessionFolder first.");
            }

            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenCapture = robot.createScreenCapture(screenRect);

            String screenshotPath = sessionFolderPath + "/screenshot-" + System.currentTimeMillis() + ".png";
            File screenshotFile = new File(screenshotPath);
            ImageIO.write(screenCapture, "png", screenshotFile);

            return screenshotPath;
        } catch (AWTException | IOException | IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSessionFolderPath() {
        return sessionFolderPath;
    }
}
