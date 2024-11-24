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

public class JFrame4 extends JFrame {

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
    public JFrame4() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 430);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnNewButton = new JButton("");
        btnNewButton.setIcon(new ImageIcon("/Users/reenamarieobmina/Desktop/Split_document/rest.png"));
        btnNewButton.setBounds(72, 225, 145, 133);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("");
        btnNewButton_1.setIcon(new ImageIcon("/Users/reenamarieobmina/Desktop/Split_document/icons_2.png"));
        btnNewButton_1.setBounds(284, 225, 145, 133);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_1_1 = new JButton("");
        btnNewButton_1_1.setIcon(new ImageIcon("/Users/reenamarieobmina/Desktop/Split_document/icons_3.png"));
        btnNewButton_1_1.setBounds(496, 225, 145, 133);
        contentPane.add(btnNewButton_1_1);

        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setIcon(new ImageIcon("/Users/reenamarieobmina/Desktop/The Weather App-2/3.png"));
        lblNewLabel.setBounds(6, 6, 700, 390);
        contentPane.add(lblNewLabel);
    }
}
