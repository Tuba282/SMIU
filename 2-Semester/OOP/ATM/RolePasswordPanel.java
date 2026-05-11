import javax.swing.*;
import java.awt.*;

public class RolePasswordPanel extends JPanel {
    private ATMApp app;
    private JTextField nameField;
    private JPasswordField passField;
    private JLabel roleLabel;
    private JLabel statusLabel;
    private JButton loginBtn;
    private JButton registerBtn;

    private static final Color GREEN = new Color(0, 200, 83);
    private static final Color BG    = new Color(8, 8, 8);

    public RolePasswordPanel(ATMApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 50, 8, 50);

        // Title
        JLabel title = new JLabel("Welcome to SMIU ATM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(GREEN);
        add(title, gbc);

        // Dynamic role label
        roleLabel = new JLabel("Login as Admin", SwingConstants.CENTER);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleLabel.setForeground(new Color(140, 140, 140));
        gbc.insets = new Insets(2, 50, 14, 50);
        add(roleLabel, gbc);

        // ── Name field ──────────────────────────────────────────
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(GREEN);
        gbc.insets = new Insets(6, 50, 2, 50);
        add(nameLabel, gbc);

        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 38));
        nameField.setBackground(new Color(14, 14, 14));
        nameField.setForeground(GREEN);
        nameField.setCaretColor(GREEN);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 83, 100), 1),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(0, 50, 8, 50);
        add(nameField, gbc);

        // ── Password field ──────────────────────────────────────
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(GREEN);
        gbc.insets = new Insets(6, 50, 2, 50);
        add(passLabel, gbc);

        passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(200, 38));
        passField.setBackground(new Color(14, 14, 14));
        passField.setForeground(GREEN);
        passField.setCaretColor(GREEN);
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 83, 100), 1),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        passField.setHorizontalAlignment(JTextField.LEFT);
        passField.setFont(new Font("Monospaced", Font.BOLD, 16));
        gbc.insets = new Insets(0, 50, 6, 50);
        add(passField, gbc);

        // Status label (errors/info)
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(255, 60, 80));
        gbc.insets = new Insets(2, 50, 6, 50);
        add(statusLabel, gbc);

        // ── LOGIN button ────────────────────────────────────────
        loginBtn = makeGreenButton("LOGIN");
        loginBtn.addActionListener(e -> handleLogin());
        gbc.insets = new Insets(6, 50, 6, 50);
        add(loginBtn, gbc);

        // ── REGISTER button ─────────────────────────────────────
        registerBtn = makeOutlineButton("New Account? REGISTER");
        registerBtn.addActionListener(e -> handleRegister());
        gbc.insets = new Insets(2, 50, 6, 50);
        add(registerBtn, gbc);

        // Enter key triggers login
        passField.addActionListener(e -> handleLogin());

        // ── Back button ─────────────────────────────────────────
        JButton backBtn = new JButton("<< Back");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backBtn.setBackground(BG);
        backBtn.setForeground(new Color(80, 80, 80));
        backBtn.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            clearFields();
            app.showScreen("ROLE_SELECT");
        });
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { backBtn.setForeground(GREEN); }
            public void mouseExited(java.awt.event.MouseEvent e)  { backBtn.setForeground(new Color(80, 80, 80)); }
        });
        gbc.insets = new Insets(10, 50, 8, 50);
        add(backBtn, gbc);
    }

    /** Refresh when shown */
    public void refresh() {
        clearFields();
        String role = app.getDataManager().getCurrentRole();
        if ("ADMIN".equals(role)) {
            roleLabel.setText("[ADMIN]  Login or Register");
        } else {
            roleLabel.setText("[USER]  Login or Register");
        }
    }

    private void clearFields() {
        nameField.setText("");
        passField.setText("");
        statusLabel.setText(" ");
        nameField.requestFocusInWindow();
    }

    // ── LOGIN logic ─────────────────────────────────────────────
    private void handleLogin() {
        String name     = nameField.getText().trim();
        String password = new String(passField.getPassword());
        String role     = app.getDataManager().getCurrentRole();

        if (name.isEmpty() || password.isEmpty()) {
            showError("Enter both name and password!");
            return;
        }

        // Check if user exists
        User found = app.getDataManager().findUser(role, name);
        if (found == null) {
            showError("Account not found! Click REGISTER.");
            return;
        }

        // Try login
        User loggedIn = app.getDataManager().login(role, name, password);
        if (loggedIn != null) {
            app.getDataManager().setCurrentUser(loggedIn);
            clearFields();
            app.showScreen("DASHBOARD");
        } else {
            showError("Wrong password! Try again.");
            passField.setText("");
        }
    }

    // ── REGISTER logic ──────────────────────────────────────────
    private void handleRegister() {
        String name     = nameField.getText().trim();
        String password = new String(passField.getPassword());
        String role     = app.getDataManager().getCurrentRole();

        if (name.isEmpty() || password.isEmpty()) {
            showError("Enter name and password to register!");
            return;
        }

        if (name.length() < 2) {
            showError("Name must be at least 2 characters!");
            return;
        }

        if (password.length() < 4) {
            showError("Password must be at least 4 characters!");
            return;
        }

        boolean ok = app.getDataManager().register(role, name, password);
        if (ok) {
            // Auto-login after register
            User newUser = app.getDataManager().findUser(role, name);
            app.getDataManager().setCurrentUser(newUser);
            showSuccess("Account created! Balance: $1000.00");

            // Brief delay then go to dashboard
            Timer t = new Timer(1200, ev -> {
                clearFields();
                app.showScreen("DASHBOARD");
            });
            t.setRepeats(false);
            t.start();
        } else {
            showError("Name already taken! Try LOGIN instead.");
        }
    }

    // ── UI Helpers ──────────────────────────────────────────────

    private void showError(String msg) {
        statusLabel.setForeground(new Color(255, 60, 80));
        statusLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        statusLabel.setForeground(GREEN);
        statusLabel.setText(msg);
    }

    private JButton makeGreenButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.BLACK);
        btn.setForeground(GREEN);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
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
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBackground(BG);
        btn.setForeground(new Color(100, 100, 100));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 40, 40), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(GREEN);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(GREEN, 1),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(new Color(100, 100, 100));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(40, 40, 40), 1),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });
        return btn;
    }
}
