import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

public class SimpleATM extends JFrame {
    // --- Data Persistence Layer ---
    static class DataManager {
        private static final String FILE_NAME = "atm_db.txt";
        private String pin = "1234";
        private double balance = 1000.0;

        public DataManager() {
            load();
        }

        public void load() {
            try {
                File file = new File(FILE_NAME);
                if (file.exists()) {
                    Scanner sc = new Scanner(file);
                    if (sc.hasNextLine()) {
                        String[] data = sc.nextLine().split(":");
                        pin = data[0];
                        balance = Double.parseDouble(data[1]);
                    }
                    sc.close();
                } else {
                    save();
                }
            } catch (Exception e) {
                System.out.println("Data error: " + e.getMessage());
            }
        }

        public void save() {
            try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
                out.println(pin + ":" + balance);
            } catch (IOException e) {
                System.out.println("Save error: " + e.getMessage());
            }
        }

        public boolean checkPin(String input) { return pin.equals(input); }
        public double getBalance() { return balance; }
        public void setBalance(double b) { this.balance = b; save(); }
    }

    // --- UI Constants & Theme ---
    private static final Color BG_DARK = new Color(15, 23, 42);
    private static final Color CARD_BG = new Color(30, 41, 59, 200);
    private static final Color PRIMARY = new Color(56, 189, 248);
    private static final Color ACCENT = new Color(129, 140, 248);
    private static final Font FONT_MAIN = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 24);

    private DataManager db = new DataManager();
    private JPanel cardPanel;
    private CardLayout cl = new CardLayout();

    public SimpleATM() {
        setTitle("SMIU PREMIUM ATM");
        setSize(450, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true); // Borderless for modern look
        setBackground(new Color(0, 0, 0, 0)); // Transparent background for rounded frame

        // Main content pane with rounded corners and gradient
        JPanel mainContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BG_DARK, 0, getHeight(), new Color(30, 41, 59));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        mainContent.setLayout(new BorderLayout());
        mainContent.setOpaque(false);

        // Custom Title Bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(new EmptyBorder(15, 20, 0, 20));
        JLabel title = new JLabel("SKY BANK");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JButton closeBtn = new JButton("×");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(closeBtn, BorderLayout.EAST);
        mainContent.add(titleBar, BorderLayout.NORTH);

        cardPanel = new JPanel(cl);
        cardPanel.setOpaque(false);
        
        cardPanel.add(createLoginView(), "LOGIN");
        cardPanel.add(createMenuView(), "MENU");
        cardPanel.add(createTransactionView("DEPOSIT"), "DEPOSIT");
        cardPanel.add(createTransactionView("WITHDRAW"), "WITHDRAW");
        cardPanel.add(createBalanceView(), "BALANCE");

        mainContent.add(cardPanel, BorderLayout.CENTER);
        add(mainContent);
        
        cl.show(cardPanel, "LOGIN");
    }

    // --- Views ---

    private JPanel createLoginView() {
        JPanel p = new ModernPanel();
        JLabel l = new JLabel("ENTER SECURE PIN", SwingConstants.CENTER);
        l.setFont(FONT_BOLD);
        l.setForeground(Color.WHITE);
        
        JPasswordField pf = new JPasswordField(4);
        styleField(pf);
        
        JButton btn = new ModernButton("LOGIN");
        btn.addActionListener(e -> {
            if (db.checkPin(new String(pf.getPassword()))) {
                cl.show(cardPanel, "MENU");
                pf.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Wrong PIN! (Hint: 1234)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        p.add(Box.createVerticalStrut(100));
        p.add(l);
        p.add(Box.createVerticalStrut(30));
        p.add(pf);
        p.add(Box.createVerticalStrut(30));
        p.add(btn);
        return p;
    }

    private JPanel createMenuView() {
        JPanel p = new ModernPanel();
        JLabel l = new JLabel("WELCOME BACK", SwingConstants.CENTER);
        l.setFont(FONT_BOLD);
        l.setForeground(PRIMARY);

        String[] options = {"VIEW BALANCE", "DEPOSIT", "WITHDRAW", "LOGOUT"};
        p.add(Box.createVerticalStrut(50));
        p.add(l);
        p.add(Box.createVerticalStrut(40));

        // This is where the "sare options hon" logic happens
        for (String opt : options) {
            JButton b = new ModernButton(opt);
            b.addActionListener(e -> {
                if (opt.equals("LOGOUT")) cl.show(cardPanel, "LOGIN");
                else cl.show(cardPanel, opt.split(" ")[opt.split(" ").length-1]);
            });
            p.add(b);
            p.add(Box.createVerticalStrut(15));
        }
        return p;
    }

    private JPanel createTransactionView(String type) {
        JPanel p = new ModernPanel();
        JLabel l = new JLabel(type + " AMOUNT", SwingConstants.CENTER);
        l.setFont(FONT_BOLD);
        l.setForeground(Color.WHITE);

        JTextField tf = new JTextField();
        styleField(tf);

        JButton btn = new ModernButton("CONFIRM " + type);
        btn.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(tf.getText());
                if (amt <= 0) throw new Exception();
                if (type.equals("WITHDRAW") && amt > db.getBalance()) {
                    JOptionPane.showMessageDialog(this, "Insufficient Balance!");
                } else {
                    db.setBalance(db.getBalance() + (type.equals("DEPOSIT") ? amt : -amt));
                    JOptionPane.showMessageDialog(this, "Success!");
                    cl.show(cardPanel, "MENU");
                    tf.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Amount!");
            }
        });

        JButton back = new ModernButton("BACK");
        back.addActionListener(e -> cl.show(cardPanel, "MENU"));

        p.add(Box.createVerticalStrut(80));
        p.add(l);
        p.add(Box.createVerticalStrut(30));
        p.add(tf);
        p.add(Box.createVerticalStrut(30));
        p.add(btn);
        p.add(Box.createVerticalStrut(15));
        p.add(back);
        return p;
    }

    private JPanel createBalanceView() {
        JPanel p = new ModernPanel();
        JLabel l = new JLabel("YOUR BALANCE", SwingConstants.CENTER);
        l.setFont(FONT_BOLD);
        l.setForeground(Color.WHITE);

        JLabel balLabel = new JLabel("$ 0.00", SwingConstants.CENTER);
        balLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        balLabel.setForeground(PRIMARY);

        // Update balance when shown
        p.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                balLabel.setText(String.format("$ %.2f", db.getBalance()));
            }
        });

        JButton back = new ModernButton("BACK");
        back.addActionListener(e -> cl.show(cardPanel, "MENU"));

        p.add(Box.createVerticalStrut(100));
        p.add(l);
        p.add(Box.createVerticalStrut(20));
        p.add(balLabel);
        p.add(Box.createVerticalStrut(50));
        p.add(back);
        return p;
    }

    // --- Modern Components ---

    private void styleField(JTextField f) {
        f.setMaximumSize(new Dimension(300, 50));
        f.setFont(new Font("Consolas", Font.BOLD, 24));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBackground(new Color(255, 255, 255, 20));
        f.setBorder(new CompoundBorder(new LineBorder(PRIMARY, 2, true), new EmptyBorder(5, 15, 5, 15)));
        f.setHorizontalAlignment(JTextField.CENTER);
    }

    class ModernPanel extends JPanel {
        public ModernPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setBorder(new EmptyBorder(40, 40, 40, 40));
        }
    }

    class ModernButton extends JButton {
        public ModernButton(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new LineBorder(PRIMARY, 1, true));
            setMaximumSize(new Dimension(300, 50));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(CENTER_ALIGNMENT);
            
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setForeground(PRIMARY); setBorder(new LineBorder(ACCENT, 2, true)); }
                public void mouseExited(MouseEvent e) { setForeground(Color.WHITE); setBorder(new LineBorder(PRIMARY, 1, true)); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) g2.setColor(new Color(255, 255, 255, 30));
            else g2.setColor(new Color(255, 255, 255, 10));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        // Here is the "While loop" logic requested - although the GUI handles events, 
        // we can wrap the startup in a logical flow if needed, but for Swing we just start.
        SwingUtilities.invokeLater(() -> {
            new SimpleATM().setVisible(true);
        });
    }
}
