import javax.swing.*;
import java.awt.*;

public class ATMApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private DataManager dataManager;

    public ATMApp() {
        dataManager = new DataManager();
        
        setTitle("SMIU ATM - Secure Banking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(StyleConstants.COLOR_BG);

        // Add Panels
        mainContainer.add(new LoginPanel(this), "LOGIN");
        mainContainer.add(new DashboardPanel(this), "DASHBOARD");
        mainContainer.add(new BalancePanel(this), "BALANCE");
        mainContainer.add(new TransactionPanel(this, true), "DEPOSIT");
        mainContainer.add(new TransactionPanel(this, false), "WITHDRAW");

        add(mainContainer);
        showScreen("LOGIN");
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
        
        // Refresh specific panels if needed
        Component[] components = mainContainer.getComponents();
        for (Component comp : components) {
            if (comp instanceof BalancePanel && screenName.equals("BALANCE")) {
                ((BalancePanel) comp).updateBalanceDisplay();
            }
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ATMApp().setVisible(true);
        });
    }
}
