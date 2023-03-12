package de.haukesomm.sokoban.gui;

import javax.swing.JFrame;

import de.haukesomm.sokoban.gui.InfoWindow;

public class ErrorWindow {
    
    public ErrorWindow(JFrame frame, Exception exception) {
        this.frame = frame;
        this.exception = exception;
    }
    
    private final JFrame frame;
    private final Exception exception;
    
    public void printMessage() {
        String[] classNameSplitted = exception.getClass().getName().split("\\.");
        String errorClassName = classNameSplitted[classNameSplitted.length - 1];
        System.err.println(errorClassName + ": " + exception.getMessage());
    }
    
    public void show() {
        printMessage();
        new InfoWindow(frame, "Error",
                exception.getClass().toString().replaceAll("class ", "") + ":"
                + "<br>" + exception.getMessage());
    }
    
}
