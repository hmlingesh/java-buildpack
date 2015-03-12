package com.ejt.demo.helper;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextAreaUI;
import java.awt.*;

public class MultilineLabel extends JTextArea {

    private static final Insets NO_MARGIN = new Insets(0, 0, 0, 0);

    public MultilineLabel(String text) {
        setText(text);
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setRequestFocusEnabled(false);
        setFocusable(false);
        setMargin(NO_MARGIN);
        updateUI();
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        minimumSize.width = 0;
        return minimumSize;
    }

    @Override
    public final void processEvent(AWTEvent event) {
    }

    @Override
    public void updateUI() {

        setUI(new BasicTextAreaUI());
        invalidate();

        JLabel label = new JLabel();
        setFont(label.getFont());
        setBorder(null);
        setOpaque(false);

    }

}
