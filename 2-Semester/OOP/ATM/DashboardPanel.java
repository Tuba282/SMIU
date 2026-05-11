import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardPanel extends JPanel {
    private ATMApp app;
    private JLabel userNameLabel;
    private JLabel dateLabel;
    private JLabel roleTagLabel;

    private static final Color GREEN      = new Color(0, 200, 83);
    private static final Color BG         = new Color(8, 8, 8);
    private static final Color CARD_BG    = new Color(16, 16, 16);

    public DashboardPanel(ATMApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(BG);

        // ── Header ──────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(12, 12, 12));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 200, 83, 60)),
            BorderFactory.createEmptyBorder(14, 24, 14, 24)
        ));

        // Row 1: Title + Role tag
        JPanel row1 = new JPanel(new BorderLayout());
        row1.setOpaque(false);

        JLabel titleLbl = new JLabel("Welcome to SMIU ATM");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(GREEN);
        row1.add(titleLbl, BorderLayout.WEST);

        roleTagLabel = new JLabel("[ADMIN]");
        roleTagLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        roleTagLabel.setForeground(new Color(0, 200, 83, 160));
        row1.add(roleTagLabel, BorderLayout.EAST);

        header.add(row1);
        header.add(Box.createVerticalStrut(6));

        // Row 2: Username + Date
        JPanel row2 = new JPanel(new BorderLayout());
        row2.setOpaque(false);

        userNameLabel = new JLabel("Hello, Admin");
        userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userNameLabel.setForeground(new Color(180, 180, 180));
        row2.add(userNameLabel, BorderLayout.WEST);

        dateLabel = new JLabel("2026-05-12");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setForeground(new Color(80, 80, 80));
        row2.add(dateLabel, BorderLayout.EAST);

        header.add(row2);

        add(header, BorderLayout.NORTH);

        // ── 2x2 Grid of cards ───────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 14));
        grid.setBackground(BG);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        grid.add(makeCard("[$]", "Balance",  "View account balance", "BALANCE"));
        grid.add(makeCard("[+]", "Deposit",  "Add money to account",  "DEPOSIT"));
        grid.add(makeCard("[-]", "Withdraw", "Take out money",        "WITHDRAW"));
        grid.add(makeCard("[X]", "Logout",   "Exit secure session",   "LOGIN"));

        add(grid, BorderLayout.CENTER);

        // ── Footer ──────────────────────────────────────────────────
        JPanel footer = new JPanel();
        footer.setBackground(new Color(12, 12, 12));
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0, 200, 83, 40)),
            BorderFactory.createEmptyBorder(8, 0, 8, 0)
        ));
        JLabel footerTxt = new JLabel("[*] Secure ATM Session");
        footerTxt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerTxt.setForeground(new Color(50, 50, 50));
        footer.add(footerTxt);
        add(footer, BorderLayout.SOUTH);
    }

    /** Called when dashboard is shown — updates name + date */
    public void refreshUserInfo() {
        User u = app.getDataManager().getCurrentUser();
        if (u != null) {
            userNameLabel.setText("Hello, " + u.getName());
            roleTagLabel.setText("[" + u.getRole() + "]");
        } else {
            userNameLabel.setText("Hello, Guest");
            roleTagLabel.setText("");
        }
        // Current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy  |  hh:mm a");
        dateLabel.setText(now.format(fmt));
    }

    private JPanel makeCard(String icon, String title, String subtitle, String screen) {
        JPanel card = new JPanel() {
            private boolean hovered = false;

            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true; repaint();
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false; repaint();
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        if ("LOGIN".equals(screen)) {
                            // Logout: clear current user
                            app.getDataManager().setCurrentUser(null);
                        }
                        app.showScreen(screen);
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(hovered ? new Color(0, 200, 83, 18) : CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));

                g2.setStroke(new BasicStroke(hovered ? 2f : 1.2f));
                g2.setColor(hovered ? GREEN : new Color(35, 35, 35));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 16, 16));

                // Icon
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int iconX = (getWidth() - fm.stringWidth(icon)) / 2;
                g2.setColor(hovered ? GREEN : new Color(80, 80, 80));
                g2.drawString(icon, iconX, getHeight() / 2 - 16);

                // Title
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                fm = g2.getFontMetrics();
                int titleX = (getWidth() - fm.stringWidth(title)) / 2;
                g2.setColor(hovered ? GREEN : new Color(200, 200, 200));
                g2.drawString(title, titleX, getHeight() / 2 + 14);

                // Subtitle
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                fm = g2.getFontMetrics();
                int subX = (getWidth() - fm.stringWidth(subtitle)) / 2;
                g2.setColor(hovered ? new Color(0, 200, 83, 180) : new Color(65, 65, 65));
                g2.drawString(subtitle, subX, getHeight() / 2 + 32);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBackground(CARD_BG);
        return card;
    }
}
