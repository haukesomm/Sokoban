package de.haukesomm.sokoban.legacy.level;

import de.haukesomm.sokoban.core.LevelDescription;

import javax.swing.*;

public class LevelDescriptionListCellRenderer extends DefaultListCellRenderer {

    @Override
    public JLabel getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        var label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        String text;
        if (value instanceof LevelDescription) {
            text = ((LevelDescription) value).getName();
        } else {
            text = value.toString();
        }
        label.setText(text);

        return label;
    }
}
