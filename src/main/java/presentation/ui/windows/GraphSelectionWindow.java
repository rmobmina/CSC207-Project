package presentation.ui.windows;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class GraphSelectionWindow extends JFrame {
    private final JButton lineGraphButton = new JButton("Line Graph");
    private final JButton barGraphButton = new JButton("Bar Graph");
    private final JButton cancelButton = new JButton("Cancel");
    private final Consumer<String> graphSelectionCallback;

    public GraphSelectionWindow(Consumer<String> graphSelectionCallback) {
        this.graphSelectionCallback = graphSelectionCallback;

        setTitle("Select Graph Type");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        add(lineGraphButton);
        add(barGraphButton);
        add(cancelButton);

        lineGraphButton.addActionListener(e -> handleGraphSelection("line"));
        barGraphButton.addActionListener(e -> handleGraphSelection("bar"));
        cancelButton.addActionListener(e -> dispose()); // Close without selection
    }

    private void handleGraphSelection(String graphType) {
        if (graphSelectionCallback != null) {
            graphSelectionCallback.accept(graphType);
        }
        dispose();
    }
}
