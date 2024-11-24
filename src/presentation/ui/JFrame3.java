package Java;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

public class JFrame3 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame1 frame = new JFrame1();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public JFrame3() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 430);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JRadioButton rdbtnNewRadioButton = new JRadioButton("   LINE GRAPH");
        rdbtnNewRadioButton.setForeground(new Color(40, 75, 99));
        rdbtnNewRadioButton.setFont(new Font("Lucida Grande", Font.BOLD, 35));
        rdbtnNewRadioButton.setBounds(388, 83, 294, 37);
        contentPane.add(rdbtnNewRadioButton);

        JRadioButton rdbtnHeatMap = new JRadioButton("   HEAT MAP");
        rdbtnHeatMap.setForeground(new Color(40, 75, 99));
        rdbtnHeatMap.setFont(new Font("Lucida Grande", Font.BOLD, 35));
        rdbtnHeatMap.setBounds(388, 190, 294, 37);
        contentPane.add(rdbtnHeatMap);

        JRadioButton rdbtnNewRadioButton_1_1 = new JRadioButton("   BAR GRAPH");
        rdbtnNewRadioButton_1_1.setForeground(new Color(40, 75, 99));
        rdbtnNewRadioButton_1_1.setFont(new Font("Lucida Grande", Font.BOLD, 35));
        rdbtnNewRadioButton_1_1.setBounds(388, 297, 294, 37);
        contentPane.add(rdbtnNewRadioButton_1_1);

        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setIcon(new ImageIcon("/Users/reenamarieobmina/Desktop/l.png"));
        lblNewLabel.setBounds(6, 6, 700, 390);
        contentPane.add(lblNewLabel);
    }
}
