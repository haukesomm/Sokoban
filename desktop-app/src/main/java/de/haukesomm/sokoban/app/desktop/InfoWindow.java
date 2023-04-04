package de.haukesomm.sokoban.app.desktop;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class InfoWindow extends JFrame {
    
    public InfoWindow(Component parent, String title, String content) {
        this.parent = parent;
        this.title = title;
        this.content = content;
    }


    private final GridBagConstraints constraints = new GridBagConstraints();

    private final Component parent;

    private final String title;

    private final String content;


    private void initLayout() {
        parent.setEnabled(false);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new GridBagLayout());
        setTitle(title);
        setResizable(false);

        var closeButton = new JButton("Close");
        closeButton.addActionListener(l -> {
            parent.setEnabled(true);
            dispose();
        });
        
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.3;

        var contentLabel = new JLabel(content);
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(contentLabel, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(new JLabel(), constraints);
        
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(closeButton, constraints);
        
        pack();

        setLocation(
                parent.getX() + parent.getWidth() / 2 - getWidth() / 2,
                parent.getY() + parent.getHeight() / 2 - getHeight() / 2
        );
    }

    public void display() {
        initLayout();
        setVisible(true);
    }
}
