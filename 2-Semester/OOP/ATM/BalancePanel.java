import javax.swing.*;
import java.awt.*;

public class BalancePanel extends JPanel {
    private ATMApp app;
    private JLabel balanceLabel;

    public BalancePanel(ATMApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(StyleConstants.COLOR_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Current Balance");
        title.setFont(StyleConstants.FONT_SUBTITLE);
        title.setForeground(StyleConstants.COLOR_TEXT);
        add(title, gbc);

        balanceLabel = new JLabel("$0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        balanceLabel.setForeground(StyleConstants.COLOR_PRIMARY);
        add(balanceLabel, gbc);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(StyleConstants.FONT_BUTTON);
        backButton.setBackground(StyleConstants.COLOR_SURFACE);
        backButton.setForeground(StyleConstants.COLOR_TEXT);
        backButton.addActionListener(e -> app.showScreen("DASHBOARD"));
        add(backButton, gbc);
    }

    public void updateBalanceDisplay() {
        balanceLabel.setText(String.format("$%.2f", app.getDataManager().getBalance()));
    }
}
