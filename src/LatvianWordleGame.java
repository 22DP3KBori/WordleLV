import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LatvianWordleGame extends JFrame {
    private static final String DATABASE_FILE = "players.txt";

    private Map<String, String> players;
    private String currentPlayer;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;

    public LatvianWordleGame() {
        setTitle("Wordle LV");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeDatabase();
        setupUI();

        
        setVisible(true);
    }

    private void initializeDatabase() {
        players = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(DATABASE_FILE));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                players.put(parts[0], parts[1]);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Database file not found. Creating a new one.");
            saveDatabase();
        }
    }

    private void saveDatabase() {
        try {
            PrintWriter writer = new PrintWriter(new File(DATABASE_FILE));
            for (Map.Entry<String, String> entry : players.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel);

        usernameField = new JTextField();
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        panel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        panel.add(registerButton);

        messageLabel = new JLabel();
        panel.add(messageLabel);

        add(panel);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (players.containsKey(username) && players.get(username).equals(password)) {
            currentPlayer = username;
            JOptionPane.showMessageDialog(this, "Pieteikšanās veiksmīga!");
            playGame();
        } else {
            JOptionPane.showMessageDialog(this, "Nederīgs lietotājvārds vai parole!");
        }
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lūdzu, ievadiet derīgu lietotājvārdu un paroli!");
            return;
        }

        if (players.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Lietotājvārds jau eksistē! Lūdzu, izvēlieties citu.");
        } else {
            players.put(username, password);
            saveDatabase();
            JOptionPane.showMessageDialog(this, "Reģistrācija veiksmīga!");
            currentPlayer = username;
            playGame();
        }
    }

    private void playGame() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LatvianWordleGUI2();
            }
        });
        dispose();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LatvianWordleGame();
            }
        });
    }
}
