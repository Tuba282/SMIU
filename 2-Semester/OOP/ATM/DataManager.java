import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String FILE_PATH = "atm_data.txt";
    private static final String ATM_PIN   = "1234";    // ATM access PIN

    private List<User> users;       // All registered users
    private User currentUser;       // Currently logged-in user
    private String currentRole;     // Selected role before login

    public DataManager() {
        users = new ArrayList<>();
        loadData();
    }

    // ─── File I/O ──────────────────────────────────────────────────

    public void loadData() {
        users.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // Create default admin and user accounts
            users.add(new User("ADMIN", "Admin", "admin123", 5000.0));
            users.add(new User("USER",  "User1", "user123",  1000.0));
            saveData();
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    User u = User.fromFileString(line);
                    if (u != null) users.add(u);
                }
            } catch (Exception e) {
                System.err.println("Error reading file: " + e.getMessage());
                users.add(new User("ADMIN", "Admin", "admin123", 5000.0));
                users.add(new User("USER",  "User1", "user123",  1000.0));
            }
        }
    }

    public void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (User u : users) {
                writer.println(u.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // ─── ATM PIN ───────────────────────────────────────────────────

    public String getPin() { return ATM_PIN; }

    // ─── Role Selection ────────────────────────────────────────────

    public String getCurrentRole()         { return currentRole; }
    public void   setCurrentRole(String r) { this.currentRole = r; }

    // ─── User Lookup ───────────────────────────────────────────────

    /** Find a user by role + name */
    public User findUser(String role, String name) {
        for (User u : users) {
            if (u.getRole().equals(role) && u.getName().equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }

    /** Try to login. Returns the user if credentials match, null otherwise. */
    public User login(String role, String name, String password) {
        User u = findUser(role, name);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    /** Register a new user. Returns false if name already taken for that role. */
    public boolean register(String role, String name, String password) {
        if (findUser(role, name) != null) return false;  // Already exists
        User newUser = new User(role, name, password, 1000.0);  // Default balance
        users.add(newUser);
        saveData();
        return true;
    }

    // ─── Current User ──────────────────────────────────────────────

    public User   getCurrentUser()         { return currentUser; }
    public void   setCurrentUser(User u)   { this.currentUser = u; }

    public double getBalance() {
        return currentUser != null ? currentUser.getBalance() : 0;
    }

    public boolean updateBalance(double amount) {
        if (currentUser == null) return false;
        double newBal = currentUser.getBalance() + amount;
        if (newBal < 0) return false;
        currentUser.setBalance(newBal);
        saveData();
        return true;
    }

    // ─── All Users (for admin view) ────────────────────────────────

    public List<User> getAllUsers() { return users; }
}
