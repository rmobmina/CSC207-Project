package presentation.ui.windows;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * A window to allow the user to select the type of graph for visualization.
 */
public class GraphSelectionWindow extends JFrame {

    private final JButton lineGraphButton = new JButton("Line Graph");
    private final JButton barGraphButton = new JButton("Bar Graph");
    private final JButton cancelButton = new JButton("Cancel");
    private final Consumer<String> graphSelectionCallback;

    /**
     * Constructs a new GraphSelectionWindow.
     * @param graphSelectionCallback A callback that receives the selected graph type as a string ("line" or "bar").
     */
    public GraphSelectionWindow(Consumer<String> graphSelectionCallback) {
        this.graphSelectionCallback = graphSelectionCallback;

        // Set up the JFrame properties
        setTitle("Select Graph Type");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        // Add buttons to the window
        add(lineGraphButton);
        add(barGraphButton);
        add(cancelButton);

        // Set up button actions
        lineGraphButton.addActionListener(e -> handleGraphSelection("line"));
        barGraphButton.addActionListener(e -> handleGraphSelection("bar"));
        cancelButton.addActionListener(e -> dispose());
    }

    /**
     * Handles the graph selection event and calls the callback.
     * @param graphType The type of graph selected ("line" or "bar").
     */
    private void handleGraphSelection(String graphType) {
        if (graphSelectionCallback != null) {
            graphSelectionCallback.accept(graphType);
        }
        dispose();
    }

    /**
     * Displays the GraphSelectionWindow by making it visible.
     */
    public void display() {
        setVisible(true);
    }
}
