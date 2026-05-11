import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoadingPanel extends JPanel {
    private ATMApp app;
    private int progress = 0;
    private JLabel percentLabel;
    private JLabel statusLabel;
    private Timer timer;

    // Green theme colors
    private static final Color GREEN       = new Color(0, 200, 83);
    private static final Color GREEN_DARK  = new Color(0, 140, 55);
    private static final Color BG         = new Color(8, 8, 8);
    private static final Color TRACK_BG   = new Color(25, 25, 25);

    // Custom animated progress bar
    private JPanel progressBarPanel;

    public LoadingPanel(ATMApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 60, 12, 60);

        // ── Title ──────────────────────────────────────────────────
        JLabel title = new JLabel("Welcome to SMIU ATM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(GREEN);
        add(title, gbc);

        // ── Sub-title ──────────────────────────────────────────────
        JLabel sub = new JLabel("Initializing Secure Session...", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(160, 160, 160));
        gbc.insets = new Insets(4, 60, 20, 60);
        add(sub, gbc);

        // ── Progress Bar Track ─────────────────────────────────────
        progressBarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arc = h; // pill shape

                // Track background
                g2.setColor(TRACK_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

                // Green filled portion
                int fillW = (int) ((progress / 100.0) * w);
                if (fillW > 0) {
                    // Gradient: dark-green → bright-green
                    GradientPaint gp = new GradientPaint(0, 0, GREEN_DARK, fillW, 0, GREEN);
                    g2.setPaint(gp);
                    g2.fill(new RoundRectangle2D.Float(0, 0, fillW, h, arc, arc));

                    // Sheen highlight at top
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fill(new RoundRectangle2D.Float(0, 0, fillW, h / 2, arc, arc));
                }

                // Border
                g2.setColor(new Color(0, 200, 83, 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, arc, arc));

                g2.dispose();
            }
        };
        progressBarPanel.setPreferredSize(new Dimension(280, 18));
        progressBarPanel.setOpaque(false);
        gbc.insets = new Insets(4, 60, 8, 60);
        add(progressBarPanel, gbc);

        // ── Percentage label ───────────────────────────────────────
        percentLabel = new JLabel("0%", SwingConstants.CENTER);
        percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        percentLabel.setForeground(GREEN);
        gbc.insets = new Insets(4, 60, 6, 60);
        add(percentLabel, gbc);

        // ── Status text ───────────────────────────────────────────
        statusLabel = new JLabel("Starting...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(120, 120, 120));
        gbc.insets = new Insets(2, 60, 20, 60);
        add(statusLabel, gbc);
    }

    /** Call this every time the loading screen is shown */
    public void startLoading() {
        progress = 0;
        percentLabel.setText("0%");
        statusLabel.setText("Starting...");
        progressBarPanel.repaint();

        if (timer != null && timer.isRunning()) timer.stop();

        timer = new Timer(35, null); // fires every 35ms → ~3.5 seconds total
        timer.addActionListener(e -> {
            progress++;
            percentLabel.setText(progress + "%");
            progressBarPanel.repaint();

            // Status messages
            if (progress == 15)  statusLabel.setText("Loading account data...");
            if (progress == 40)  statusLabel.setText("Verifying credentials...");
            if (progress == 65)  statusLabel.setText("Connecting to secure server...");
            if (progress == 85)  statusLabel.setText("Applying security protocols...");
            if (progress == 100) {
                statusLabel.setText("Done!");
                timer.stop();
                // Small pause then show role select
                Timer delay = new Timer(500, ev -> app.showScreen("ROLE_SELECT"));
                delay.setRepeats(false);
                delay.start();
            }
        });
        timer.start();
    }
}
