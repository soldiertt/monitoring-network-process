package be.smals.monitor.server;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JFrame
 *
 * Created by soldiertt on 23-06-14.
 */
public class MonFrame extends JFrame {

    private JLabel[] labels;
    private JLabel[][] icons;
    private JLabel[] etiquettes = new JLabel[2];

    public MonFrame(String[] clients) throws HeadlessException {

        etiquettes[0] = new JLabel("Agent");
        etiquettes[1] = new JLabel("Process");

        GridBagLayout experimentLayout = new GridBagLayout();
        this.setLayout(experimentLayout);
        setTitle("Monitoring process");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        labels = new JLabel[clients.length];
        icons = new JLabel[clients.length][2];

        // Place etiquettes
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(etiquettes[0], constraints);
        constraints.gridy = 2;
        this.add(etiquettes[1], constraints);


        for (int i = 0; i < clients.length; i++) {
            // Place labels
            labels[i] = new JLabel();
            labels[i].setText(clients[i]);
            constraints.gridx = i + 1;
            constraints.gridy = 0;
            this.add(labels[i], constraints);

            for (int position = 0; position < 2; position++) {
                // Place icon agent and then icon process
                icons[i][position] = new JLabel();
                icons[i][position].setIcon(TCPServer.imgRed);
                constraints.gridy = position + 1;
                this.add(icons[i][position], constraints);
            }

        }
    }

    public JLabel[][] getIcons() {
        return icons;
    }
}
