// LatvianWordleGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class LatvianWordleGUI2 extends JFrame {
    private static final String[] WORDS = {
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
    private static final int MAX_ATTEMPTS = 6;

    private String secretWord;
    private int attempts;

    private JLabel feedbackLabel;
    private JTextField guessField;
    private JLabel attemptLabel;

    public LatvianWordleGUI2() {
        setTitle("Latvian Wordle");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeGame();
        setupUI();

        setResizable(false);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void initializeGame() {
        Random random = new Random();
        secretWord = WORDS[random.nextInt(WORDS.length)];
        attempts = MAX_ATTEMPTS;
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

        JLabel instructionLabel = new JLabel("Mēģiniet uzminēt latviešu vārdu (5 burtu vārds, jūms ir tikai 6 mēģinājumi)");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        centerPanel.add(instructionLabel);

        feedbackLabel = new JLabel("");
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(feedbackLabel);

        guessField = new JTextField();
        guessField.setFont(new Font("Arial", Font.PLAIN, 24));
        centerPanel.add(guessField);

        attemptLabel = new JLabel("Mēģinājumu skaits:  " + attempts);
        attemptLabel.setForeground(Color.GREEN);
        attemptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(attemptLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JButton guessButton = new JButton("Uzmini");
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGuess();
            }
        });
        mainPanel.add(guessButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleGuess() {
        String guess = guessField.getText().toLowerCase();
        int win = 0;
        int loss = 0;

        if (guess.length() != 5 || !guess.matches("[a-zāčēģīķļņšūž]+")) {
            feedbackLabel.setText("Lūdzu, ievadiet 5 burtu vārdu, kurā ir tikai latviešu rakstzīmes.");
            return;
        }

        attempts--;


        if (guess.equals(secretWord)) {
            win = win + 1;
            feedbackLabel.setText("Apsveicam! Jūs uzminējāt vārdu " + "\"" + secretWord + "pēc" + attempts + " mēģinājumos.");
        } else {
            String feedback = generateFeedback(secretWord, guess);
            feedbackLabel.setText("Atsaukmes: " + feedback);

            if (attempts == MAX_ATTEMPTS) {
                loss = loss + 1;
                feedbackLabel.setText("Atvainojiet, jums vairs nav mēģinājumu. Vārds bija: " + secretWord);
            }
        }
        attemptLabel.setText("Mēģinājumu skaits:  " + attempts);
        attemptLabel.setForeground(Color.GREEN);
    }

    private String generateFeedback(String secretWord, String guess) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guess.charAt(i)) {
                feedback.append(secretWord.charAt(i));
            } else if (secretWord.contains(String.valueOf(guess.charAt(i)))) {
                feedback.append("◯");
            } else {
                feedback.append("⨯");
            }
        }
        return feedback.toString();
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