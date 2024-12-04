package presentation.ui.views;

import java.awt.*;

import javax.swing.*;

/**
 * The HelpInfoView class provides a help window for the Weather App.
 * It displays an informational page with an overview of the application's features and functionality.
 */
public class HelpInfoView {

    private final JFrame frame;
    private final JPanel panel;
    private final JEditorPane helpEditorPane;

    /**
     * Constructs a HelpInfoView instance.
     * Initializes the UI components and styles the help information window.
     */
    public HelpInfoView() {
        frame = new JFrame("Help");
        panel = new JPanel();
        helpEditorPane = new JEditorPane();

        // Set up the frame
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        // Style the panel and editor pane
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        helpEditorPane.setContentType("text/html");
        helpEditorPane.setText(getHelpInfo());
        helpEditorPane.setEditable(false);
        helpEditorPane.setMargin(new Insets(10, 10, 10, 10));

        // Add the editor pane to a scroll pane
        final JScrollPane scrollPane =
                new JScrollPane(helpEditorPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add the panel to the frame
        frame.add(panel);
    }

    /**
     * Displays the Help window.
     * Makes the frame visible to the user.
     */
    public void display() {
        frame.setVisible(true);
    }

    /**
     * Retrieves the HTML-formatted help information to display in the editor pane.
     *
     * @return A string containing the help information in HTML format.
     */
    private String getHelpInfo() {
        return "<html>"
                + "<h1>Welcome to the Weather App!</h1>"
                + "<p><strong>Program Specification:</strong></p>"
                + "<p>This application provides a comprehensive platform for analyzing weather data and trends. "
                + "Designed for usability, it offers detailed insights and comparisons to meet diverse user needs.</p>"
                + "<p><strong>What to Expect:</strong></p>"
                + "<ol>"
                + "  <li><strong>Interactive Graphs for Climate Trends</strong><br>"
                + "      Visualize temperature, precipitation, and other climate data over time.<br>"
                + "      Choose between line or bar for a tailored view.</li>"
                + "  <li><strong>Weekly Weather Forecasts</strong><br>"
                + "      Access a 7-day weather summary, including:<br>"
                + "      Daily highs and lows, precipitation chances, and wind speeds.</li>"
                + "  <li><strong>City Comparisons</strong><br>"
                + "      Analyze differences in climate between two cities.<br>"
                + "      Compare average temperature, precipitation, humidity, and wind speeds.</li>"
                + "  <li><strong>Interactive Map (Mercator)</strong><br>"
                + "      Click anywhere on the map to view the current temperature.<br>"
                + "      Access historical data for deeper trend analysis.</li>"
                + "  <li><strong>Favorite Locations</strong><br>"
                + "      Save frequently accessed locations for convenience.<br>"
                + "      Manage your list of favorites easily.</li>"
                + "  <li><strong>Hourly Weather Forecast and Recommendations</strong><br>"
                + "      Get detailed, hourly predictions for the day.<br>"
                + "      Receive helpful suggestions (e.g., carry an umbrella or wear sunscreen).</li>"
                + "</ol>"
                + "<p>Thank you for using our Weather App! For further assistance, please contact our support team.</p>"
                + "</html>";
    }
}
