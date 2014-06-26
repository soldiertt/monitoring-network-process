package be.smals.monitor.server;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JFrame
 *
 * Created by soldiertt on 23-06-14.
 */
public class MonFrame extends JFrame {

    private JLabel[][] labels;

    public MonFrame(String[] processes) throws HeadlessException {
        GridBagLayout experimentLayout = new GridBagLayout();
        this.setLayout(experimentLayout);
        ImageIcon imgGreen = new ImageIcon(getClass().getResource("green.png"));
        ImageIcon imgRed = new ImageIcon(getClass().getResource("red.png"));
        setTitle("Monitoring process");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        labels = new JLabel[processes.length][3];
        for (int i = 0; i < processes.length; i++) {
            labels[i][0] = new JLabel();
            labels[i][0].setText(processes[i]);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridx = i;
            constraints.gridy = 0;
            this.add(labels[i][0], constraints);
            labels[i][1] = new JLabel();
            labels[i][1].setIcon(imgRed);
            constraints.gridy = 1;
            this.add(labels[i][1], constraints);
            labels[i][2] = new JLabel();
            labels[i][2].setIcon(imgRed);
            constraints.gridy = 2;
            this.add(labels[i][2], constraints);
        }
    }

    public JLabel[][] getLabels() {
        return labels;
    }
}
