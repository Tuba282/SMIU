import javax.swing.*;
import java.awt.*;

public class ATMApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private DataManager dataManager;

    // Panel references for callbacks
    private LoadingPanel      loadingPanel;
    private RolePasswordPanel rolePwdPanel;
    private DashboardPanel    dashboardPanel;

    public ATMApp() {
        dataManager = new DataManager();

        setTitle("SMIU ATM - Secure Banking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(new Color(8, 8, 8));

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(new Color(8, 8, 8));

        // Create panels
        loadingPanel   = new LoadingPanel(this);
        rolePwdPanel   = new RolePasswordPanel(this);
        dashboardPanel = new DashboardPanel(this);

        mainContainer.add(new LoginPanel(this),             "LOGIN");
        mainContainer.add(loadingPanel,                     "LOADING");
        mainContainer.add(new RoleSelectPanel(this),        "ROLE_SELECT");
        mainContainer.add(rolePwdPanel,                     "ROLE_PASSWORD");
        mainContainer.add(dashboardPanel,                   "DASHBOARD");
        mainContainer.add(new BalancePanel(this),           "BALANCE");
        mainContainer.add(new TransactionPanel(this, true), "DEPOSIT");
        mainContainer.add(new TransactionPanel(this, false),"WITHDRAW");

        add(mainContainer);
        showScreen("LOGIN");
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);

        switch (screenName) {
            case "LOADING":
                loadingPanel.startLoading();
                break;
            case "ROLE_PASSWORD":
                rolePwdPanel.refresh();
                break;
            case "DASHBOARD":
                dashboardPanel.refreshUserInfo();
                break;
            case "BALANCE":
                Component[] comps = mainContainer.getComponents();
                for (Component c : comps) {
                    if (c instanceof BalancePanel) ((BalancePanel) c).updateBalanceDisplay();
                }
                break;
        }
    }

    public DataManager getDataManager() { return dataManager; }

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
