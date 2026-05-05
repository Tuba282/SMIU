import java.io.*;
import java.nio.file.*;

public class DataManager {
    private static final String FILE_PATH = "atm_data.txt";
    private String pin;
    private double balance;

    public DataManager() {
        loadData();
    }

    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // Default values
            this.pin = "1234";
            this.balance = 1000.0;
            saveData(this.pin, this.balance);
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(",");
                    this.pin = parts[0].split(":")[1];
                    this.balance = Double.parseDouble(parts[1].split(":")[1]);
                }
            } catch (Exception e) {
                System.err.println("Error reading file: " + e.getMessage());
                // Reset to defaults if corrupted
                this.pin = "1234";
                this.balance = 1000.0;
            }
        }
    }

    public void saveData(String pin, double balance) {
        this.pin = pin;
        this.balance = balance;
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("PIN:" + pin + ",BALANCE:" + balance);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public boolean updateBalance(double amount) {
        if (balance + amount < 0) return false;
        balance += amount;
        saveData(pin, balance);
        return true;
    }
}
