package com.ejt.demo.server.gui;

import com.ejt.demo.helper.MultilineLabel;
import com.ejt.demo.server.ServerControl;
import com.ejt.demo.server.controls.AdjustableSimulatorControl;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSeparatorUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Dictionary;
import java.util.Hashtable;

public class ServerControlFrame extends JFrame {

    private static final int MAX_SLIDER = 100;
    private static final boolean MACOS = System.getProperty("os.name").toLowerCase().startsWith("mac");

    private ServerControl<AdjustableSimulatorControl> serverControl;

    public ServerControlFrame(ServerControl<AdjustableSimulatorControl> serverControl) {
        super("Server Control");
        this.serverControl = serverControl;
        init();
    }

    private void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLocationByPlatform(true);
        addComponents();
        pack();
        setMinimumSize(getSize());

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(ServerControlFrame.this, "Do you want to stop the demo server?", "Server Control", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    stopServer();
                }
            }
        });
    }

    private void stopServer() {
        setVisible(false);
        dispose();
        serverControl.stopServer();
    }

    private void addComponents() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("insets dialog, wrap", "[grow]"));

        contentPane.add(createHeaderPanel(), "dock north");

        addSimulator(serverControl.getRequestSimulatorControl(), "Simulate servlet requests",
                "This will split the call tree for each different \"view\" query parameter, as configured in the " +
                        "session settings. The servlet probe shows single URL invocations and URL hot spots. In addition, " +
                        "server simulates JPA calls which are annotated into the call tree and can be analyzed in the " +
                        "\"JPA/Hibernate probe\".",
                true);
        addSimulator(serverControl.getJdbcJobSimulatorControl(), "Simulate JDBC Jobs",
                "This will periodically simulate JDBC jobs with simple and prepared statements. The SQL statements will be " +
                        "annotated into the call tree. Further analysis and single events are available in the JDBC probe. " +
                        "Also, this simulator uses JNDI to look up resources. The JNDI requests are visible in the call tree and " +
                        "a dedicated JNDI probe delivers hot spots, telemetries and single events.",
                false);
        addSimulator(serverControl.getJmsSimulatorControl(), "Simulate JMS message handling",
                "This will periodically simulate JMS messages that are handled by the server. The JMS probe shows " +
                "further details like hot spot, telemetries and single events. In addition, the simulator makes " +
                "RMI and web service calls when handling a message, so the RMI probe and the web services probe show " +
                "data as well. The JVM also handles the server part of the RMI and web service calls.",
                false);

        contentPane.add(createFooterPanel(), "dock south");

    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new MigLayout("insets dialog", "[grow]"));
        panel.setBackground(Color.WHITE);
        panel.setOpaque(true);
        panel.add(adjustFont(new JLabel("Server Simulation"), 5, true), "wrap unrel");
        panel.add(new MultilineLabel(
                "This program simulates a server under load, so you can see data for various probes in JProfiler. " +
                        "Please have a look at the simulation event types below. You can disable them and change their event rate."
        ), "growx");
        panel.add(createHorizontalSeparator(), "dock south, wmin 600");
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new MigLayout("insets dialog", "[grow]"));
        panel.add(createHorizontalSeparator(), "dock north");
        JButton stopButton = new JButton("Stop Server");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
        panel.add(stopButton, "align right");
        return panel;
    }

    private void addSimulator(AdjustableSimulatorControl simulatorControl, String checkBoxText, String description, boolean first) {
        int checkboxWidth = new JCheckBox().getPreferredSize().width;
        Container contentPane = getContentPane();
        JCheckBox checkBox = createSimulatorCheckBox(checkBoxText, simulatorControl);
        contentPane.add(checkBox, first ? "" : "newline para");
        contentPane.add(associateWithToggleButton(new JLabel("Rate:"), checkBox), "split, gapleft " + checkboxWidth);
        contentPane.add(associateWithToggleButton(createSimulatorRateSlider(simulatorControl), checkBox), "growx");
        Box box = Box.createVerticalBox();
        box.add(associateWithToggleButton(adjustFont(new JLabel("evt. per"), -2, false), checkBox));
        box.add(associateWithToggleButton(adjustFont(new JLabel("minute"), -2, false), checkBox));
        contentPane.add(box, "gapright unrel, wrap unrel");
        contentPane.add(associateWithToggleButton(new MultilineLabel(description), checkBox), "wrap para, growx, gapleft " + checkboxWidth);
    }

    private <T extends JComponent> T adjustFont(T component, int delta, boolean bold) {
        Font font = component.getFont();
        int style = font.getStyle();
        if (bold) {
            style |= Font.BOLD;
        }
        component.setFont(font.deriveFont(style, (float)(font.getSize() + delta)));
        return component;
    }

    private <T extends JComponent> T associateWithToggleButton(final T component, final JToggleButton toggleButton) {
        toggleButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                component.setEnabled(toggleButton.isSelected() && toggleButton.isEnabled());
            }
        });
        toggleButton.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if ("enabled".equals(event.getPropertyName())) {
                    component.setEnabled(toggleButton.isSelected() && toggleButton.isEnabled());
                }
            }
        });

        component.setEnabled(toggleButton.isSelected() && toggleButton.isEnabled());
        return component;
    }

    private JCheckBox createSimulatorCheckBox(String text, final AdjustableSimulatorControl simulatorControl) {
        final JCheckBox checkBox = new JCheckBox(text, simulatorControl.isEnabled());
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulatorControl.setEnabled(checkBox.isSelected());
            }
        });
        return checkBox;
    }

    private JSlider createSimulatorRateSlider(final AdjustableSimulatorControl simulatorControl) {
        int value = (int)simulatorControl.getRate();
        final JSlider slider = new JSlider(0, MAX_SLIDER, value);
        Dictionary<Integer, JComponent> labels = new Hashtable<>();
        for (int i = 0; i <= MAX_SLIDER; i += MAX_SLIDER / 10) {
            labels.put(i, new JLabel(String.valueOf(i)));
        }
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                simulatorControl.setRate(slider.getValue());
            }
        });
        return slider;
    }

    private JSeparator createHorizontalSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        if (MACOS) {
            separator.setUI(new BasicSeparatorUI());
        }
        return separator;
    }

    public static void main(String[] args) {
        new ServerControlFrame(new ServerControl<AdjustableSimulatorControl>() {
            @Override
            public void stopServer() {
                System.exit(0);
            }

            @Override
            public AdjustableSimulatorControl getRequestSimulatorControl() {
                return new AdjustableSimulatorControl(50d);
            }

            @Override
            public AdjustableSimulatorControl getJdbcJobSimulatorControl() {
                return new AdjustableSimulatorControl(10d);
            }

            @Override
            public AdjustableSimulatorControl getJmsSimulatorControl() {
                return new AdjustableSimulatorControl(10d);
            }
        }).setVisible(true);
    }
}
