import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class LatvianWordleGUI2 extends JFrame {
    private static final String[] WORDS = {
            // Latviešu vārdu saraksts
            "galdi", "zāles", "dārzs", "laiks", "zivis", "zirgs", "sunis", "bumba",
            "krēsl", "lampa", "bļoda", "gaiss", "tinte", "rīkst",
            "dīzeļ", "klēts", "kaste", "spēks", "plūme", "plūme", "zobis",
            "zvaig", "vārds", "laiva", "cimds", "zvans", "grāfs", "pilis", "rokas", "krūms", "tilts",
            "sirds", "gults", "telpa", "reize", "cimds", "mājas", "rūcis", "spogs",
            "salde", "lāsts", "džins", "krāns", "ūdens",
            "lūpas", "lodes", "dzint", "dūres", "tāpis", "ciete", "zemes", "adata", "punkt",
            "dēlis", "teles", "spied", "kūpin", "klepe", "smērs", "zemde", "grava", "plāns", "smiek",
            "saste", "ūdens", "zāles", "žurka", "kurpe", "valis", "lācis", "māsas", "šņāks", "buris",
            "sānte", "slazd", "zemes", "lieta", "šūnās", "zvana", "sērfs", "sūnas", "darbs", "slota"
    };
    private static final int WORD_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 6;

    private String secretWord;
    private int attempts;
    private boolean gameEnded;

    private JTextField guessField;
    private JLabel attemptLabel;
    private JLabel feedbackLabel;

    private PlayerStatistics playerStats;
    private String currentPlayer;
    private JLabel statsLabel;

    public LatvianWordleGUI2() {
        // Вызываем другой конструктор с пустыми значениями
        this("", null);
    }

    public LatvianWordleGUI2(String currentPlayer, PlayerStatistics playerStats) {
        setTitle("Latvian Wordle");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeGame();
        setupUI();

        playerStats = new PlayerStatistics();
        this.currentPlayer = currentPlayer;
        this.playerStats = playerStats;

        statsLabel = new JLabel();
        updateStatsLabel();

        setResizable(false);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void initializeGame() {
        Random random = new Random();
        secretWord = WORDS[random.nextInt(WORDS.length)];
        attempts = 0;
        gameEnded = false;
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
    
        JLabel titleLabel = new JLabel("Wordle LV");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1));
    
        JLabel instructionLabel = new JLabel("Mēģiniet uzminēt latviešu vārdu (5 burtu vārds, jums ir tikai 6 mēģinājumi)");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(instructionLabel);
    
        feedbackLabel = new JLabel("");
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 18));
        centerPanel.add(feedbackLabel);
    
        guessField = new JTextField();
        guessField.setFont(new Font("Arial", Font.PLAIN, 24));
        guessField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameEnded) {
                    handleGuess();
                }
            }
        });
        centerPanel.add(guessField);
    
        attemptLabel = new JLabel("Mēģinājumu skaits: " + attempts + " no " + MAX_ATTEMPTS);
        attemptLabel.setForeground(Color.GREEN);
        attemptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(attemptLabel);
    
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    
        JButton statisticsButton = new JButton("Statistika");
        statisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerStats.setVisible(true); // Показать окно статистики при нажатии на кнопку
            }
        });
        mainPanel.add(statisticsButton, BorderLayout.SOUTH);
    
        add(mainPanel);
    }

    private void handleGuess() {
        String guess = guessField.getText().toLowerCase();
        attempts++;

        if (guess.equals(secretWord)) {
            feedbackLabel.setText("Apsveicam! Jūs uzminējāt vārdu " + secretWord + " pēc " + attempts + " mēģinājumiem.");
            gameEnded = true;
            playerStats.incrementWins(currentPlayer);
        } else {
            if (attempts >= MAX_ATTEMPTS) {
                feedbackLabel.setText("Atvainojiet, jums vairs nav mēģinājumu. Vārds bija: " + secretWord);
                gameEnded = true;
                playerStats.incrementLosses(currentPlayer);
            } else {
                String feedback = generateFeedback(secretWord, guess);
                feedbackLabel.setText("Atsaukmes: " + feedback);
                attemptLabel.setText("Mēģinājumu skaits: " + attempts + " no " + MAX_ATTEMPTS);
            }
        }
        guessField.setText("");
    }

    private String generateFeedback(String secretWord, String guess) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            char guessChar = guess.charAt(i);
            if (secretWord.indexOf(guessChar) != -1 && secretWord.charAt(i) == guessChar) {
                feedback.append(guessChar);
            } else if (secretWord.indexOf(guessChar) != -1) {
                feedback.append("+");
            } else {
                feedback.append("-");
            }
        }
        return feedback.toString();
    }

    private void updateStatsLabel() {
        if (playerStats != null && currentPlayer != null) {
            int wins = playerStats.getWins(currentPlayer);
            int losses = playerStats.getLosses(currentPlayer);
        } else {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LatvianWordleGUI2();
            }
        });
    }
}