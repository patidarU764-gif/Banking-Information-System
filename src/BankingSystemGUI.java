import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BankingSystemGUI {
    // In-memory data persistence structure
    private static ArrayList<UserAccount> database = new ArrayList<>();
    private static UserAccount currentUser = null;
    private static int accountCounter = 100001; // Base starting account number

    // Main System Frames
    private JFrame mainFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public static void main(String[] args) {
        // Load initial sandbox accounts for demonstration
        database.add(new UserAccount("100001", "Amit Sharma", "Malviya Nagar, Jaipur", "9876543210", "pass123", 5000.0));
        database.add(new UserAccount("100002", "Sneha Patel", "C-Scheme, Jaipur", "8765432109", "secure456", 12000.0));

        SwingUtilities.invokeLater(() -> {
            try {
                // Set native operating system look and feel layout
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new BankingSystemGUI().initialize();
        });
    }

    public void initialize() {
        mainFrame = new JFrame("Apex Digital Bank - Secure Core Engine v1.0");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(650, 550);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Append UI operational layers
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createRegisterPanel(), "REGISTER");
        cardPanel.add(createDashboardPanel(), "DASHBOARD");

        mainFrame.add(cardPanel);
        cardLayout.show(cardPanel, "LOGIN");
        mainFrame.setVisible(true);
    }

    // --- SCREEN LAYOUTS ---

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("APEX DIGITAL BANKING", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(26, 54, 93));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        JTextField accField = new JTextField(15);
        panel.add(accField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(15);
        panel.add(passField, gbc);

        JButton loginBtn = new JButton("Secure Login");
        loginBtn.setBackground(new Color(43, 108, 176));
        loginBtn.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        JButton toRegisterBtn = new JButton("Open New Digital Account (Register)");
        gbc.gridy = 4;
        panel.add(toRegisterBtn, gbc);

        // Authentication Event Handler
        loginBtn.addActionListener(e -> {
            String accNum = accField.getText().trim();
            String pwd = new String(passField.getPassword());

            UserAccount account = findAccount(accNum);
            if (account != null && account.getPassword().equals(pwd)) {
                currentUser = account;
                accField.setText("");
                passField.setText("");
                // Refresh dashboard perspective
                cardPanel.add(createDashboardPanel(), "DASHBOARD");
                cardLayout.show(cardPanel, "DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Invalid credentials matching core system logs.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        });

        toRegisterBtn.addActionListener(e -> cardLayout.show(cardPanel, "REGISTER"));

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        JTextField nameF = new JTextField();
        JTextField addrF = new JTextField();
        JTextField contF = new JTextField();
        JPasswordField passF = new JPasswordField();
        JTextField depF = new JTextField();

        panel.add(new JLabel("Full Name:"));
        panel.add(nameF);
        panel.add(new JLabel("Current Address:"));
        panel.add(addrF);
        panel.add(new JLabel("Contact Number:"));
        panel.add(contF);
        panel.add(new JLabel("Set Secure Password:"));
        panel.add(passF);
        panel.add(new JLabel("Initial Opening Deposit (₹):"));
        panel.add(depF);

        JButton regBtn = new JButton("Register Profile");
        regBtn.setBackground(new Color(39, 121, 105));
        regBtn.setForeground(Color.WHITE);
        JButton cancelBtn = new JButton("Return to Login");

        panel.add(regBtn);
        panel.add(cancelBtn);

        regBtn.addActionListener(e -> {
            try {
                String name = nameF.getText().trim();
                String addr = addrF.getText().trim();
                String cont = contF.getText().trim();
                String pwd = new String(passF.getPassword());
                double initialDep = Double.parseDouble(depF.getText().trim());

                if (name.isEmpty() || addr.isEmpty() || cont.isEmpty() || pwd.isEmpty()) {
                    throw new IllegalArgumentException("Fields cannot be submitted blank.");
                }
                if (initialDep < 500) {
                    throw new IllegalArgumentException("Minimum regulatory deposit required is ₹500.");
                }

                String assignedAcc = String.valueOf(++accountCounter);
                UserAccount newAcc = new UserAccount(assignedAcc, name, addr, cont, pwd, initialDep);
                database.add(newAcc);

                JOptionPane.showMessageDialog(mainFrame, "Registration Approved!\nYour unique System Account Number is: " + assignedAcc, "System Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear input structures
                nameF.setText("");
                addrF.setText("");
                contF.setText("");
                passF.setText("");
                depF.setText("");
                cardLayout.show(cardPanel, "LOGIN");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Numeric input formats required for deposits.", "Input Format Exception", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Validation Restriction", JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));

        return panel;
    }

    private JPanel createDashboardPanel() {
        if (currentUser == null) return new JPanel();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top welcome grid
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Active Session Profile"));
        infoPanel.add(new JLabel("Welcome, " + currentUser.getName()));
        infoPanel.add(new JLabel("Account No: " + currentUser.getAccountNumber(), JLabel.RIGHT));
        JLabel balLabel = new JLabel("Available Cleared Balance: ₹" + currentUser.getBalance());
        balLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        balLabel.setForeground(new Color(27, 94, 32));
        infoPanel.add(balLabel);

        JButton logoutBtn = new JButton("Logout Session");
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(cardPanel, "LOGIN");
        });
        infoPanel.add(logoutBtn);

        // Core Actions Dashboard Hub
        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: Banking operations engine
        JPanel txPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        txPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton depAction = new JButton("Make Instant Cash Deposit");
        JButton witAction = new JButton("Process Secure Cash Withdrawal");
        JButton trfAction = new JButton("Initialize Electronic Inter-Bank Fund Transfer");

        txPanel.add(depAction);
        txPanel.add(witAction);
        txPanel.add(trfAction);
        tabs.addTab("Perform Transactions", txPanel);

        // Tab 2: Ledger statement generator
        JPanel stmtPanel = new JPanel(new BorderLayout());
        String[] columns = {"Timestamp", "Description", "Transaction Value", "Settled Balance"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Transaction t : currentUser.getTransactionHistory()) {
            model.addRow(new Object[]{t.getDate(), t.getType(), "₹" + t.getAmount(), "₹" + t.getBalanceAfterTransaction()});
        }
        JTable table = new JTable(model);
        stmtPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tabs.addTab("Account Ledger Statements", stmtPanel);

        // Tab 3: Account Profile management modifier
        JPanel mgtPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mgtPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField editAddr = new JTextField(currentUser.getAddress());
        JTextField editCont = new JTextField(currentUser.getContact());
        JButton updateBtn = new JButton("Commit Information Changes");
        mgtPanel.add(new JLabel("Modify Address String:")); mgtPanel.add(editAddr);
        mgtPanel.add(new JLabel("Modify Telecommunication Contact:")); mgtPanel.add(editCont);
        mgtPanel.add(new JLabel("")); mgtPanel.add(updateBtn);
        tabs.addTab("Profile Management Form", mgtPanel);

        // --- ATTACHING ACTION LISTENERS TO OPERATIONS HUB ---

        depAction.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(mainFrame, "Specify values to deposit (₹):", "Deposit Engine", JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                try {
                    double amt = Double.parseDouble(input);
                    if (amt <= 0) throw new IllegalArgumentException("Amounts must remain positive.");
                    currentUser.deposit(amt);
                    JOptionPane.showMessageDialog(mainFrame, "Transaction Processed Successfully!\nDeposited: ₹" + amt + "\nNew Balance: ₹" + currentUser.getBalance(), "Accounting Confirmed", JOptionPane.INFORMATION_MESSAGE);
                    refreshDashboard();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid Numeric Entry.", "Operational Abort", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        witAction.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(mainFrame, "Specify values to withdraw safely (₹):", "Withdrawal Module", JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                try {
                    double amt = Double.parseDouble(input);
                    currentUser.withdraw(amt);
                    JOptionPane.showMessageDialog(mainFrame, "Cash Vault Released!\nWithdrew: ₹" + amt + "\nRemaining Balance: ₹" + currentUser.getBalance(), "Accounting Confirmed", JOptionPane.INFORMATION_MESSAGE);
                    refreshDashboard();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Transaction Denied", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        trfAction.addActionListener(e -> {
            JTextField targetField = new JTextField();
            JTextField amountField = new JTextField();
            Object[] messageFields = {
                    "Recipient Account Number:", targetField,
                    "Transfer Amount (₹):", amountField
            };

            int option = JOptionPane.showConfirmDialog(mainFrame, messageFields, "Fund Transfer Routing Configuration", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String targetAccNum = targetField.getText().trim();
                    double amt = Double.parseDouble(amountField.getText().trim());

                    if (targetAccNum.equals(currentUser.getAccountNumber())) {
                        throw new IllegalArgumentException("Self-transfer scenarios are locked natively.");
                    }

                    UserAccount recipient = findAccount(targetAccNum);
                    if (recipient == null) {
                        throw new IllegalArgumentException("Routing failed: Recipient Account profile not discovered.");
                    }

                    currentUser.withdraw(amt);
                    recipient.deposit(amt);

                    JOptionPane.showMessageDialog(mainFrame, "Electronic Inter-Bank Transfer Executed Successfully!\n" +
                            "Debited: ₹" + amt + " to Account [" + targetAccNum + "]\n" +
                            "Your Updated Settled Balance: ₹" + currentUser.getBalance(), "Transfer Success Routing Complete", JOptionPane.INFORMATION_MESSAGE);
                    refreshDashboard();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Transfer Execution Terminated", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateBtn.addActionListener(e -> {
            String newAddr = editAddr.getText().trim();
            String newCont = editCont.getText().trim();
            if(!newAddr.isEmpty() && !newCont.isEmpty()) {
                currentUser.setAddress(newAddr);
                currentUser.setContact(newCont);
                JOptionPane.showMessageDialog(mainFrame, "Core Account Registry Information Updated Successfully.", "Profile System Record Sync", JOptionPane.INFORMATION_MESSAGE);
                refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Registry Updates cannot accept blank records.", "Field Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    // Helper method to look up accounts from the system memory
    private UserAccount findAccount(String accNum) {
        for (UserAccount ua : database) {
            if (ua.getAccountNumber().equals(accNum)) {
                return ua;
            }
        }
        return null;
    }

    // Live layout card engine workspace updates handler
    private void refreshDashboard() {
        cardPanel.add(createDashboardPanel(), "DASHBOARD");
        cardLayout.show(cardPanel, "DASHBOARD");
    }
}


