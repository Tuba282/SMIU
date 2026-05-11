import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TransactionPanel extends JPanel {
    private ATMApp app;
    private boolean isDeposit;
    private JTextField amountField;
    private JLabel statusLabel;

    private static final Color GREEN  = new Color(0, 200, 83);
    private static final Color BG     = new Color(8, 8, 8);
    private static final Color CARD_BG = new Color(14, 14, 14);
    private static final Color RED    = new Color(255, 60, 80);

    public TransactionPanel(ATMApp app, boolean isDeposit) {
        this.app = app;
        this.isDeposit = isDeposit;

        setLayout(new BorderLayout());
        setBackground(BG);

        // ── Header ──────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(12, 12, 12));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 200, 83, 60)),
            BorderFactory.createEmptyBorder(18, 24, 18, 24)
        ));
        JLabel headerLbl = new JLabel("Welcome to SMIU ATM");
        headerLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLbl.setForeground(GREEN);
        header.add(headerLbl, BorderLayout.WEST);

        JLabel typeLbl = new JLabel(isDeposit ? "Deposit" : "Withdraw");
        typeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLbl.setForeground(new Color(90, 90, 90));
        header.add(typeLbl, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Center ──────────────────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Icon + title card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                g2.setColor(new Color(0, 200, 83, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 18, 18));
                g2.dispose();
            }
        };
        card.setLayout(new GridBagLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(300, 90));

        GridBagConstraints cg = new GridBagConstraints();
        cg.gridwidth = GridBagConstraints.REMAINDER;
        cg.insets = new Insets(14, 20, 2, 20);

        JLabel icon = new JLabel(isDeposit ? "[+] Deposit Funds" : "[-] Withdraw Funds", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        icon.setForeground(GREEN);
        card.add(icon, cg);

        cg.insets = new Insets(2, 20, 14, 20);
        JLabel sub = new JLabel(isDeposit ? "Enter amount to deposit" : "Enter amount to withdraw", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(90, 90, 90));
        card.add(sub, cg);

        gbc.insets = new Insets(24, 30, 16, 30);
        center.add(card, gbc);

        // Amount field
        amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(200, 44));
        amountField.setBackground(new Color(14, 14, 14));
        amountField.setForeground(GREEN);
        amountField.setCaretColor(GREEN);
        amountField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 83, 100), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        amountField.setToolTipText("Enter amount in PKR");
        gbc.insets = new Insets(0, 30, 8, 30);
        center.add(amountField, gbc);

        // Enter triggers action
        amountField.addActionListener(e -> handleTransaction());

        // Status label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(GREEN);
        gbc.insets = new Insets(0, 30, 14, 30);
        center.add(statusLabel, gbc);

        // Confirm button
        String confirmText = isDeposit ? "[OK] Confirm Deposit" : "[OK] Confirm Withdrawal";
        JButton confirmBtn = makeGreenButton(confirmText);
        confirmBtn.addActionListener(e -> handleTransaction());
        gbc.insets = new Insets(0, 30, 10, 30);
        center.add(confirmBtn, gbc);

        // Cancel / back button
        JButton cancelBtn = makeOutlineButton("[X] Cancel");
        cancelBtn.addActionListener(e -> {
            statusLabel.setText(" ");
            amountField.setText("");
            app.showScreen("DASHBOARD");
        });
        gbc.insets = new Insets(0, 30, 0, 30);
        center.add(cancelBtn, gbc);

        add(center, BorderLayout.CENTER);
    }

    private JButton makeGreenButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.BLACK);
        btn.setForeground(GREEN);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN, 2),
            BorderFactory.createEmptyBorder(11, 20, 11, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(GREEN); btn.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.BLACK); btn.setForeground(GREEN);
            }
        });
        return btn;
    }

    private JButton makeOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(new Color(8, 8, 8));
        btn.setForeground(new Color(100, 100, 100));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 40, 40), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(RED);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(RED, 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(new Color(100, 100, 100));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(40, 40, 40), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });
        return btn;
    }

    private void handleTransaction() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) { showError("[ERR] Enter a valid amount!"); return; }

            double multiplier = isDeposit ? 1 : -1;
            if (app.getDataManager().updateBalance(amount * multiplier)) {
                statusLabel.setForeground(GREEN);
                statusLabel.setText("[OK] Success! Balance: $" + String.format("%.2f", app.getDataManager().getBalance()));
                amountField.setText("");
            } else {
                showError("[ERR] Insufficient funds!");
            }
        } catch (NumberFormatException e) {
            showError("[ERR] Enter a valid number!");
        }
    }

    private void showError(String msg) {
        statusLabel.setForeground(RED);
        statusLabel.setText(msg);
    }
}
