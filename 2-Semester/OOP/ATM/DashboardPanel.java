import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private ATMApp app;

    public DashboardPanel(ATMApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(StyleConstants.COLOR_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleConstants.COLOR_SURFACE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Main Menu");
        title.setFont(StyleConstants.FONT_SUBTITLE);
        title.setForeground(StyleConstants.COLOR_TEXT);
        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(StyleConstants.COLOR_BG);
        grid.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        grid.add(createMenuButton("Balance", "BALANCE"));
        grid.add(createMenuButton("Deposit", "DEPOSIT"));
        grid.add(createMenuButton("Withdraw", "WITHDRAW"));
        grid.add(createMenuButton("Logout", "LOGIN"));

        add(grid, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text, String screen) {
        JButton btn = new JButton(text);
        btn.setFont(StyleConstants.FONT_BUTTON);
        btn.setBackground(StyleConstants.COLOR_SURFACE);
        btn.setForeground(StyleConstants.COLOR_TEXT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(StyleConstants.COLOR_ACCENT, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> app.showScreen(screen));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(StyleConstants.COLOR_PRIMARY);
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(StyleConstants.COLOR_SURFACE);
                btn.setForeground(StyleConstants.COLOR_TEXT);
            }
        });

        return btn;
    }
}
