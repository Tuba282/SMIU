import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private ATMApp app;
    private JPasswordField pinField;
    private JLabel statusLabel;

    public LoginPanel(ATMApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(StyleConstants.COLOR_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40);

        JLabel titleLabel = new JLabel("Welcome to SMIU ATM", SwingConstants.CENTER);
        titleLabel.setFont(StyleConstants.FONT_TITLE);
        titleLabel.setForeground(StyleConstants.COLOR_PRIMARY);
        add(titleLabel, gbc);

        JLabel subLabel = new JLabel("Please enter your PIN", SwingConstants.CENTER);
        subLabel.setFont(StyleConstants.FONT_BODY);
        subLabel.setForeground(new Color(0, 200, 83));
        add(subLabel, gbc);

        pinField = new JPasswordField();
        pinField.setPreferredSize(new Dimension(200, 40));
        pinField.setBackground(StyleConstants.COLOR_SURFACE);
        pinField.setForeground(StyleConstants.COLOR_TEXT);
        pinField.setCaretColor(StyleConstants.COLOR_TEXT);
        pinField.setBorder(BorderFactory.createLineBorder(StyleConstants.COLOR_ACCENT));
        pinField.setHorizontalAlignment(JTextField.CENTER);
        pinField.setFont(new Font("Monospaced", Font.BOLD, 24));
        add(pinField, gbc);

        Color GREEN = new Color(0, 200, 83);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(StyleConstants.FONT_BUTTON);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(GREEN);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());

        // Hover effect: green bg + black text
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                loginButton.setBackground(GREEN);
                loginButton.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                loginButton.setBackground(Color.BLACK);
                loginButton.setForeground(GREEN);
            }
        });

        add(loginButton, gbc);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(StyleConstants.COLOR_ERROR);
        add(statusLabel, gbc);
    }

    private void handleLogin() {
        String enteredPin = new String(pinField.getPassword());
        if (enteredPin.equals(app.getDataManager().getPin())) {
            statusLabel.setText(" ");
            pinField.setText("");
            app.showScreen("LOADING");   // → Loading → Role Select → Role Password → Dashboard
        } else {
            statusLabel.setText("❌  Wrong PIN! Please try again.");
        }
    }
}
