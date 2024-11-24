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

public class JFrame1 extends JFrame {

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
    public JFrame1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 430);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnNewButton = new JButton("START");
        btnNewButton.setFont(new Font("Lucida Grande", Font.BOLD, 18));
        btnNewButton.setForeground(new Color(40, 75, 99));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton.setBounds(62, 321, 134, 54);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("HELP");
        btnNewButton_1.setFont(new Font("Lucida Grande", Font.BOLD, 18));
        btnNewButton_1.setForeground(new Color(40, 75, 99));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton_1.setBounds(233, 321, 134, 54);
        contentPane.add(btnNewButton_1);

        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setIcon(new ImageIcon("/Users/reenamarieobmina/Desktop/The Weather App-2/1.png"));
        lblNewLabel.setBounds(6, 6, 688, 390);
        contentPane.add(lblNewLabel);
    }
}
