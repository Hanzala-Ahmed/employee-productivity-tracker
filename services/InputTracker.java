package services;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class InputTracker {
    private int mouseClicks = 0;
    private int keyPresses = 0;

    public InputTracker() {
        try {
            Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
                if (e instanceof KeyEvent && e.getID() == KeyEvent.KEY_PRESSED) {
                    keyPresses++;
                } else if (e instanceof InputEvent && e.getID() == MouseEvent.MOUSE_CLICKED) {
                    mouseClicks++;
                }
            }, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getMouseClicks() {
        return mouseClicks;
    }

    public int getKeyPresses() {
        return keyPresses;
    }

    public void reset() {
        mouseClicks = 0;
        keyPresses = 0;
    }
}
