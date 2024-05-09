import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PlayerStatistics extends JFrame {
    private static final String STATS_FILE = "player_stats.txt";

    private Map<String, PlayerStats> playersStats;
    private JTable statsTable;

    public PlayerStatistics() {
        setTitle("Player Statistics");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        loadStats();
        setupUI();
    }

    private void loadStats() {
        playersStats = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(STATS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String playerName = parts[0];
                    int wins = Integer.parseInt(parts[1]);
                    int losses = Integer.parseInt(parts[2]);
                    playersStats.put(playerName, new PlayerStats(wins, losses));
                }
            }
            reader.close();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading player statistics: " + e.getMessage());
        }
    }

    private void setupUI() {
        String[] columnNames = {"Spēlētāja vārds", "Uzvar", "Zaudējumi"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Map.Entry<String, PlayerStats> entry : playersStats.entrySet()) {
            PlayerStats stats = entry.getValue();
            model.addRow(new Object[]{entry.getKey(), stats.getWins(), stats.getLosses()});
        }
        statsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(statsTable);
        add(scrollPane);

        JPanel buttonPanel = new JPanel();
        JButton sortAscButton = new JButton("Kārtot augošā secībā");
        sortAscButton.addActionListener(e -> sortPlayers(true));
        buttonPanel.add(sortAscButton);

        JButton sortDescButton = new JButton("Kārtot dilstošā secībā");
        sortDescButton.addActionListener(e -> sortPlayers(false));
        buttonPanel.add(sortDescButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void sortPlayers(boolean ascending) {
        java.util.List<String> sortedPlayers = new ArrayList<>(playersStats.keySet());
        if (ascending) {
            Collections.sort(sortedPlayers);
        } else {
            Collections.sort(sortedPlayers, Collections.reverseOrder());
        }
        DefaultTableModel model = (DefaultTableModel) statsTable.getModel();
        model.setRowCount(0);
        for (String playerName : sortedPlayers) {
            PlayerStats stats = playersStats.get(playerName);
            model.addRow(new Object[]{playerName, stats.getWins(), stats.getLosses()});
        }
    }
    

    private void saveStats() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(STATS_FILE));
            for (Map.Entry<String, PlayerStats> entry : playersStats.entrySet()) {
                PlayerStats stats = entry.getValue();
                writer.write(entry.getKey() + ":" + stats.getWins() + ":" + stats.getLosses() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Saglabājot spēlētāja statistiku, radās kļūda:" + e.getMessage());
        }
    }

    public void incrementWins(String playerName) {
        PlayerStats stats = playersStats.getOrDefault(playerName, new PlayerStats());
        stats.incrementWins();
        playersStats.put(playerName, stats);
        saveStats();
    }

    public void incrementLosses(String playerName) {
        PlayerStats stats = playersStats.getOrDefault(playerName, new PlayerStats());
        stats.incrementLosses();
        playersStats.put(playerName, stats);
        saveStats();
    }

    public int getWins(String playerName) {
        PlayerStats stats = playersStats.get(playerName);
        return (stats != null) ? stats.getWins() : 0;
    }

    public int getLosses(String playerName) {
        PlayerStats stats = playersStats.get(playerName);
        return (stats != null) ? stats.getLosses() : 0;
    }

    private static class PlayerStats {
        private int wins;
        private int losses;

        public PlayerStats() {
            this(0, 0);
        }

        public PlayerStats(int wins, int losses) {
            this.wins = wins;
            this.losses = losses;
        }

        public int getWins() {
            return wins;
        }

        public int getLosses() {
            return losses;
        }

        public void incrementWins() {
            wins++;
        }

        public void incrementLosses() {
            losses++;
        }
    }
}