package de.haukesomm.sokoban.gui;

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
        this.content = "<html>" + content + "</html>";
        
        createLayout();
    }
    
    private final GridBagConstraints constraints = new GridBagConstraints();
    private final Component parent;
    private final String title;
    private final String content;
    
    private JLabel content_label;
    private JButton close;
    
    private void createLayout() {
        parent.setEnabled(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new GridBagLayout());
        setTitle(title);
        setResizable(false);
        
        content_label = new JLabel(content);
        
        close = new JButton("Close");
        close.addActionListener(l -> {
            parent.setEnabled(true);
            dispose();
        });
        
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.3;
        
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        add(content_label, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(new JLabel(), constraints);
        
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(close, constraints);
        
        constraints.gridx = 2;
        constraints.gridy = 1;
        add(new JLabel(), constraints);
        
        pack();
        setLocation((int) (parent.getX() + parent.getWidth() / 2 - getWidth() / 2),
            (int) (parent.getY() + parent.getHeight() / 2 - getHeight() / 2));
        setVisible(true);
    }
    
}
