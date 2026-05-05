import javax.swing.*;
import java.awt.*;

public class TransactionPanel extends JPanel {
    private ATMApp app;
    private boolean isDeposit;
    private JTextField amountField;
    private JLabel statusLabel;

    public TransactionPanel(ATMApp app, boolean isDeposit) {
        this.app = app;
        this.isDeposit = isDeposit;
        
        setLayout(new GridBagLayout());
        setBackground(StyleConstants.COLOR_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        JLabel title = new JLabel(isDeposit ? "Deposit Funds" : "Withdraw Funds", SwingConstants.CENTER);
        title.setFont(StyleConstants.FONT_SUBTITLE);
        title.setForeground(StyleConstants.COLOR_TEXT);
        add(title, gbc);

        amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(200, 40));
        amountField.setBackground(StyleConstants.COLOR_SURFACE);
        amountField.setForeground(StyleConstants.COLOR_TEXT);
        amountField.setCaretColor(StyleConstants.COLOR_TEXT);
        amountField.setFont(new Font("Segoe UI", Font.BOLD, 18));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        add(amountField, gbc);

        JButton actionButton = new JButton(isDeposit ? "Confirm Deposit" : "Confirm Withdrawal");
        actionButton.setFont(StyleConstants.FONT_BUTTON);
        actionButton.setBackground(StyleConstants.COLOR_PRIMARY);
        actionButton.setForeground(Color.WHITE);
        actionButton.addActionListener(e -> handleTransaction());
        add(actionButton, gbc);

        JButton backButton = new JButton("Cancel");
        backButton.setFont(StyleConstants.FONT_BUTTON);
        backButton.setBackground(StyleConstants.COLOR_SURFACE);
        backButton.setForeground(StyleConstants.COLOR_TEXT);
        backButton.addActionListener(e -> {
            statusLabel.setText(" ");
            amountField.setText("");
            app.showScreen("DASHBOARD");
        });
        add(backButton, gbc);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(StyleConstants.COLOR_PRIMARY);
        add(statusLabel, gbc);
    }

    private void handleTransaction() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showError("Enter a valid amount!");
                return;
            }

            double multiplier = isDeposit ? 1 : -1;
            if (app.getDataManager().updateBalance(amount * multiplier)) {
                statusLabel.setForeground(StyleConstants.COLOR_PRIMARY);
                statusLabel.setText("Success! New Balance: $" + app.getDataManager().getBalance());
                amountField.setText("");
            } else {
                showError("Insufficient funds!");
            }
        } catch (NumberFormatException e) {
            showError("Invalid number format!");
        }
    }

    private void showError(String msg) {
        statusLabel.setForeground(StyleConstants.COLOR_ERROR);
        statusLabel.setText(msg);
    }
}
