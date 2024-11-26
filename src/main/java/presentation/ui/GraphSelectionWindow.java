package presentation.ui;

import javax.swing.*;
import java.awt.*;

public class GraphSelectionWindow extends JFrame {
    private JButton lineGraphButton = new JButton("Line Graph");
    private JButton barGraphButton = new JButton("Bar Graph");
    private final GraphSelectionListener listener;

    public GraphSelectionWindow(GraphSelectionListener listener) {
        this.listener = listener;
        setTitle("Select Graph Type");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 10, 10));

        lineGraphButton.addActionListener(e -> {
            listener.onGraphSelected("line");
            dispose();
        });

        barGraphButton.addActionListener(e -> {
            listener.onGraphSelected("bar");
            dispose();
        });

        add(lineGraphButton);
        add(barGraphButton);
    }

    public interface GraphSelectionListener {
        void onGraphSelected(String graphType);
    }
}
