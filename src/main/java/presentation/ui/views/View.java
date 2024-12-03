package presentation.ui.views;

import javax.swing.*;

/**
 * A class that represents a basic view to be displayed on the dashboard.
 * It provides functionality to manage the visibility and retrieval of its panel.
 */
public class View {

    /**
     * The JPanel associated with this view.
     * This panel contains the content to be displayed on the dashboard.
     */
    public JPanel panel = new JPanel();

    /**
     * Retrieves the panel associated with this view.
     *
     * @return The {@link JPanel} object for this view.
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Makes the panel visible on the dashboard.
     * This method sets the visibility of the panel to {@code true}.
     */
    public void showPanel() {
        panel.setVisible(true);
    }

    /**
     * Hides the panel from the dashboard.
     * This method sets the visibility of the panel to {@code false}.
     */
    public void hidePanel() {
        panel.setVisible(false);
    }
}
