import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class BalancePanel extends JPanel {
    private ATMApp app;
    private JLabel balanceLabel;
    private JLabel balanceTitleLabel;

    private static final Color GREEN     = new Color(0, 200, 83);
    private static final Color BG        = new Color(8, 8, 8);
    private static final Color CARD_BG   = new Color(14, 14, 14);

    public BalancePanel(ATMApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(BG);

        // ── Header ──────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(12, 12, 12));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 200, 83, 60)),
            BorderFactory.createEmptyBorder(18, 24, 18, 24)
        ));
        JLabel titleLbl = new JLabel("Welcome to SMIU ATM");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLbl.setForeground(GREEN);
        header.add(titleLbl, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ── Center card ─────────────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Card panel
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                g2.setColor(new Color(0, 200, 83, 70));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 18, 18));
                g2.dispose();
            }
        };
        card.setLayout(new GridBagLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(300, 160));

        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.gridwidth = GridBagConstraints.REMAINDER;
        cgbc.insets = new Insets(12, 20, 4, 20);

        JLabel icon = new JLabel("💳", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        card.add(icon, cgbc);

        balanceTitleLabel = new JLabel("Current Balance", SwingConstants.CENTER);
        balanceTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        balanceTitleLabel.setForeground(new Color(100, 100, 100));
        cgbc.insets = new Insets(0, 20, 6, 20);
        card.add(balanceTitleLabel, cgbc);

        balanceLabel = new JLabel("$0.00", SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        balanceLabel.setForeground(GREEN);
        cgbc.insets = new Insets(0, 20, 16, 20);
        card.add(balanceLabel, cgbc);

        // ── Back button ─────────────────────────────────────────────
        JButton backBtn = makeGreenButton("← Back to Menu");
        backBtn.addActionListener(e -> app.showScreen("DASHBOARD"));
        gbc.insets = new Insets(20, 40, 10, 40);
        center.add(card, gbc);

        gbc.insets = new Insets(16, 60, 10, 60);
        center.add(backBtn, gbc);

        add(center, BorderLayout.CENTER);
    }

    private JButton makeGreenButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.BLACK);
        btn.setForeground(new Color(0, 200, 83));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 83), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(0, 200, 83));
                btn.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.BLACK);
                btn.setForeground(new Color(0, 200, 83));
            }
        });
        return btn;
    }

    public void updateBalanceDisplay() {
        balanceLabel.setText(String.format("$%.2f", app.getDataManager().getBalance()));
    }
}
