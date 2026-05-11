import javax.swing.*;
import java.awt.*;

public class RoleSelectPanel extends JPanel {
    private ATMApp app;

    private static final Color GREEN      = new Color(0, 200, 83);
    private static final Color BG         = new Color(8, 8, 8);
    private static final Color CARD_BG    = new Color(18, 18, 18);
    private static final Color CARD_HOVER = new Color(0, 200, 83, 20);

    public RoleSelectPanel(ATMApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // ── Title ─────────────────────────────────────────────
        JLabel title = new JLabel("Welcome to SMIU ATM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(GREEN);
        add(title, gbc);

        JLabel sub = new JLabel("Select Your Role", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(160, 160, 160));
        gbc.insets = new Insets(4, 50, 30, 50);
        add(sub, gbc);

        // Divider line
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(40, 40, 40));
        sep.setBackground(new Color(40, 40, 40));
        add(sep, gbc);

        gbc.insets = new Insets(16, 50, 10, 50);

        // ── ADMIN card button ─────────────────────────────────
        JButton adminBtn = createRoleCard(
            "[A]  ADMIN",
            "Full access · System settings"
        );
        adminBtn.addActionListener(e -> {
            app.getDataManager().setCurrentRole("ADMIN");
            app.showScreen("ROLE_PASSWORD");
        });
        add(adminBtn, gbc);

        gbc.insets = new Insets(10, 50, 16, 50);

        // ── USER card button ──────────────────────────────────
        JButton userBtn = createRoleCard(
            "[U]  USER",
            "Balance · Deposit · Withdraw"
        );
        userBtn.addActionListener(e -> {
            app.getDataManager().setCurrentRole("USER");
            app.showScreen("ROLE_PASSWORD");
        });
        add(userBtn, gbc);

        gbc.insets = new Insets(24, 50, 10, 50);

        // ── Back button ───────────────────────────────────────
        JButton backBtn = new JButton("← Back to Login");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backBtn.setBackground(BG);
        backBtn.setForeground(new Color(100, 100, 100));
        backBtn.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> app.showScreen("LOGIN"));
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backBtn.setForeground(GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                backBtn.setForeground(new Color(100, 100, 100));
            }
        });
        add(backBtn, gbc);
    }

    private JButton createRoleCard(String title, String subtitle) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background
                if (getModel().isRollover()) {
                    g2.setColor(CARD_HOVER);
                } else {
                    g2.setColor(CARD_BG);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                // Border
                g2.setColor(getModel().isRollover() ? GREEN : new Color(45, 45, 45));
                g2.setStroke(new BasicStroke(getModel().isRollover() ? 2f : 1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 14, 14);

                // Title text
                g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
                g2.setColor(GREEN);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(title, 20, getHeight() / 2 - 4);

                // Subtitle text
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.setColor(new Color(120, 120, 120));
                g2.drawString(subtitle, 20, getHeight() / 2 + 16);

                // Arrow
                g2.setColor(getModel().isRollover() ? GREEN : new Color(60, 60, 60));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                String arrow = "›";
                int aw = g2.getFontMetrics().stringWidth(arrow);
                g2.drawString(arrow, getWidth() - aw - 18, getHeight() / 2 + 7);

                g2.dispose();
            }
        };

        btn.setPreferredSize(new Dimension(280, 65));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Force repaint on hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.repaint(); }
        });

        return btn;
    }
}
