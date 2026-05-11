public class User {
    private String role;     // "ADMIN" or "USER"
    private String name;
    private String password;
    private double balance;

    public User(String role, String name, String password, double balance) {
        this.role     = role;
        this.name     = name;
        this.password = password;
        this.balance  = balance;
    }

    // Getters
    public String getRole()     { return role; }
    public String getName()     { return name; }
    public String getPassword() { return password; }
    public double getBalance()  { return balance; }

    // Setters
    public void setBalance(double b)  { this.balance = b; }
    public void setPassword(String p) { this.password = p; }

    // For file storage: ROLE,NAME,PASSWORD,BALANCE
    public String toFileString() {
        return role + "," + name + "," + password + "," + balance;
    }

    // Parse from file line
    public static User fromFileString(String line) {
        String[] parts = line.trim().split(",");
        if (parts.length < 4) return null;
        try {
            return new User(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
        } catch (Exception e) {
            return null;
        }
    }
}
