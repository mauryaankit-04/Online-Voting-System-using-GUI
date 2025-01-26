import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineVotingSystem {

    private final ConcurrentHashMap<String, Integer> candidates;

    // Constructor initializes the voting system
    public OnlineVotingSystem() {
        candidates = new ConcurrentHashMap<>();
        candidates.put("Candidate A", 0);
        candidates.put("Candidate B", 0);
        candidates.put("Candidate C", 0);
        setupGUI();
    }

    // Main GUI setup
    private void setupGUI() {
        JFrame frame = new JFrame("Online Voting System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        // Input fields and labels
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel candidateLabel = new JLabel("Choose Candidate:");
        JComboBox<String> candidateDropdown = new JComboBox<>(candidates.keySet().toArray(new String[0]));

        // Buttons
        JButton voteButton = new JButton("Cast Vote");
        JButton resultsButton = new JButton("View Results");

        // Action for casting votes
        voteButton.addActionListener(e -> castVote(nameField, emailField, candidateDropdown, frame));
        resultsButton.addActionListener(e -> displayResults(resultsArea));

        // Add components to the input panel
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(candidateLabel);
        inputPanel.add(candidateDropdown);
        inputPanel.add(voteButton);
        inputPanel.add(resultsButton);

        // Add panels to the frame
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(mainPanel);

        frame.setVisible(true);
    }

    // Validates and casts a vote
    private void castVote(JTextField nameField, JTextField emailField, JComboBox<String> candidateDropdown, JFrame frame) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String candidate = (String) candidateDropdown.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || candidate == null) {
            showErrorDialog(frame, "All fields are required.");
            return;
        }

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showErrorDialog(frame, "Invalid email format.");
            return;
        }

        candidates.merge(candidate, 1, Integer::sum);
        showInfoDialog(frame, "Vote cast successfully for " + candidate + "!");
        nameField.setText("");
        emailField.setText("");
    }

    // Displays the results
    private void displayResults(JTextArea resultsArea) {
        StringBuilder results = new StringBuilder("Voting Results:\n");
        candidates.forEach((candidate, votes) -> results.append(candidate).append(": ").append(votes).append(" votes\n"));
        resultsArea.setText(results.toString());
    }

    // Utility: Shows an error dialog
    private void showErrorDialog(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Utility: Shows an info dialog
    private void showInfoDialog(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(OnlineVotingSystem::new);
    }
}
