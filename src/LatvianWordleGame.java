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
    private PlayerStatistics playerStats;

    private static final String username_regex = "[a-zA-Z0-9\\._\\-]{3,}";
    private static final String password_regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";

    public LatvianWordleGame() {
        setTitle("Wordle LV");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeDatabase();
        playerStats = new PlayerStatistics();
        setupUI();
        setResizable(false);

        
        setVisible(true);
    }

    private void initializeDatabase() {
        players = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(DATABASE_FILE));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    players.put(parts[0], parts[1]);
                }
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
        JPanel panel = new JPanel(new GridBagLayout());
    
        // Centrālā panelis ar "Username", "Password", "Login" un "Register"
        JPanel centralPanel = new JPanel(new GridBagLayout());
        GridBagConstraints centralGbc = new GridBagConstraints();
        centralGbc.gridx = 0;
        centralGbc.gridy = 0;
        centralGbc.anchor = GridBagConstraints.CENTER;
    
        JLabel usernameLabel = new JLabel("Username:");
        centralPanel.add(usernameLabel, centralGbc);
    
        centralGbc.gridy++;
        usernameField = new JTextField(10);
        centralPanel.add(usernameField, centralGbc);
    
        centralGbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        centralPanel.add(passwordLabel, centralGbc);
    
        centralGbc.gridy++;
        passwordField = new JPasswordField(10);
        centralPanel.add(passwordField, centralGbc);
    
        centralGbc.gridy++;
        centralGbc.gridwidth = 2;
        centralGbc.insets = new Insets(5, 0, 0, 0); // Pievienojiet tukšumu starp pogām
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        centralPanel.add(loginButton, centralGbc);
    
        centralGbc.gridy++;
        centralGbc.insets = new Insets(5, 0, 0, 0); // Pievienojiet vēl vienu tukšumu
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        centralPanel.add(registerButton, centralGbc);
    
        // Pievienojiet centrālo paneli centrālajam paneļa sarakstam
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(centralPanel, gbc);
    
        // Pievienojiet visu centrālo paneli galvenajam panelim
        add(panel);
    
        // Pievienojiet jaunu pogu ar parolei saistītai darbībai
        GridBagConstraints gbcPassword = new GridBagConstraints();
        gbcPassword.gridx = 0;
        gbcPassword.gridy = 2;
        gbcPassword.anchor = GridBagConstraints.CENTER;
        JButton passwordCriteriaButton = new JButton("Noklikšķiniet uz manis!");
        passwordCriteriaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(LatvianWordleGame.this, "Parolei jāsastāv no vismaz 8 rakstzīmēm ar 1 lielu burtu, tostarp !,%,&,*");
            }
        });
        panel.add(passwordCriteriaButton, gbcPassword);


        centralGbc.gridy++;
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlayer();
            }
        });
        centralPanel.add(deleteButton, centralGbc);

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordDialog();
            }
        });
        panel.add(changePasswordButton, centralGbc);


        
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (!validateUsername(username) || !validatePassword(password)) {
            JOptionPane.showMessageDialog(this, "Nederīgs lietotājvārds vai parole!");
            return;
        }

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

        if (!validateUsername(username) || !validatePassword(password)) {
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

    private void changePasswordDialog() {
        JTextField usernameField = new JTextField(10);
        JPasswordField oldPasswordField = new JPasswordField(10);
        JPasswordField newPasswordField = new JPasswordField(10);
    
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Lietotājvārds:"));
        panel.add(usernameField);
        panel.add(new JLabel("Vecā parole:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("Jauna parole:"));
        panel.add(newPasswordField);
    
        int result = JOptionPane.showConfirmDialog(null, panel, "Mainīt paroli",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
    
            if (!validateUsername(username) || !validatePassword(oldPassword) || !validatePassword(newPassword)) {
                JOptionPane.showMessageDialog(this, "Nederīgs lietotājvārds vai parole!");
                return;
            }
    
            if (players.containsKey(username) && players.get(username).equals(oldPassword)) {
                players.put(username, newPassword);
                saveDatabase();
                JOptionPane.showMessageDialog(this, "Parole veiksmīgi nomainīta!");
            } else {
                JOptionPane.showMessageDialog(this, "Nepareizs lietotājvārds vai parole!");
            }
        }
    }
    
    

    private void deletePlayer() {
        String username = usernameField.getText();
    
        if (players.containsKey(username)) {
            int choice = JOptionPane.showConfirmDialog(this, "Vai tiešām vēlaties dzēst šo kontu?", "Dzēst kontu", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                players.remove(username);
                saveDatabase();
                JOptionPane.showMessageDialog(this, "Konts veiksmīgi izdzēsts!");
                usernameField.setText("");
                passwordField.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lietotājs nav atrasts!");
        }
    }

    

    private boolean validateUsername(String username) {
        return username.matches(username_regex);
    }

    private boolean validatePassword(String password) {
        return password.matches(password_regex);
    }

    private void playGame() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LatvianWordleGUI2(currentPlayer, playerStats);
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