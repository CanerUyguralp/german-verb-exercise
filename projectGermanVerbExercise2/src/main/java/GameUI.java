/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author caner
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projectGermanVerbExercise.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.text.*;
import javax.sound.sampled.*;
import java.io.File;

public class GameUI extends javax.swing.JFrame {

    private List<Verb> verbs;  // Declare the verbs variable
    private Game game;
    private Verb currentVerb;
    private int currentPersonIndex;  // Track the current person (1st, 2nd, or 3rd)
    private Set<Verb> usedVerbs;

    // Define styles for the verb and person
    private Style verbStyle;
    private Style personStyle;
    private Style tenseStyle;

    private StyledDocument doc;

    /**
     * Creates new form GameUI
     */
    public GameUI() {
        super("German Verb Exercise");
        initComponents();
        customInitComponents();
        usedVerbs = new HashSet<>(); // Initialize the set of used verbs
    }

    // Custom initialization method for additional components and listeners
    private void customInitComponents() {
        // Assign action listeners to buttons using lambdas
        jButton1.addActionListener(e -> startGame());
        jButton2.addActionListener(e -> processAnswer());
        jButton3.addActionListener(e -> resetGame());

        // Additional settings for text pane field
        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new Color(176, 224, 230));
        // Create an empty border with the desired insets
        javax.swing.border.Border emptyBorderPane = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK); // Adjust the thickness and color as needed
        // Create a matte border with the desired thickness and color
        javax.swing.border.Border matteBorderPane = BorderFactory.createEmptyBorder(10, 10, 10, 10); // Adjust the insets as needed
        // Combine the empty border and the matte border
        javax.swing.border.Border compoundBorderPane = BorderFactory.createCompoundBorder(emptyBorderPane, matteBorderPane);
        // Set the compound border to the text pane
        jTextPane1.setBorder(compoundBorderPane);

        // Additional settings for text field
        jTextField1.setText("Antwort");
        // Create an empty border with the desired insets
        javax.swing.border.Border emptyBorderTextField = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK); // Adjust the thickness and color as needed
        // Create a matte border with the desired thickness and color
        javax.swing.border.Border matteBorderTextField = BorderFactory.createEmptyBorder(10, 10, 10, 10); // Adjust the insets as needed
        // Combine the empty border and the matte border
        javax.swing.border.Border compoundBorderTextField = BorderFactory.createCompoundBorder(emptyBorderTextField, matteBorderTextField);
        // Set the compound border to the text field
        jTextField1.setBorder(compoundBorderTextField);
        jTextField1.setBackground(new Color(176, 224, 230));

        // Create and initialize the score label
        //jLabel1 = new javax.swing.JLabel();
        //jLabel1.setText("Punktzahl: 0/0"); // Initialize the label with the initial score
        //jLabel1.setBounds(20, 20, 150, 30); // Set the position and size of the label
        //add(jLabel1); // Add the label to your JFrame
        // Define a custom font
        Font customFont = new Font("Arial", Font.PLAIN, 18);
        // Set the custom font to text components
        jTextPane1.setFont(customFont);
        jTextField1.setFont(customFont);
        jLabel1.setFont(customFont);

        // Get the StyledDocument associated with the JTextArea
        doc = jTextPane1.getStyledDocument();

        // Create the StyleContext, the root of the styles hierarchy
        StyleContext sc = new StyleContext();

        // Create the verb style
        verbStyle = sc.addStyle("VerbStyle", null);
        StyleConstants.setForeground(verbStyle, new Color(0, 0, 255));  // Set color
        StyleConstants.setBold(verbStyle, true);  // Make the text bold
        StyleConstants.setFontSize(verbStyle, 22);  // Set the font size

        // Create the person style
        personStyle = sc.addStyle("PersonStyle", null);
        StyleConstants.setForeground(personStyle, new Color(163, 21, 21));
        StyleConstants.setBold(personStyle, true);  // Make the text bold
        StyleConstants.setFontSize(personStyle, 22);  // Set the font size

        //create the tesnse style
        tenseStyle = sc.addStyle("TenseStyle", null);
        StyleConstants.setForeground(tenseStyle, new Color(0, 0, 0));
        StyleConstants.setBold(tenseStyle, true);  // Make the text bold
        StyleConstants.setItalic(tenseStyle, true); // Make the text italic
        StyleConstants.setFontSize(tenseStyle, 22);  // Set the font size

        //score settings
        jLabel1.setFont(new Font("Arial", Font.BOLD, 18));

    }

    // Method to update the score dynamically
    private void updateScore(int currentScore, int totalQuestions) {
        jLabel1.setText("Punktzahl: " + currentScore + "/" + totalQuestions);
    }

    // Method to update the question displayed in the JTextArea
    private void updateQuestion(String newQuestion) {
        jTextPane1.setText(newQuestion);
    }

    // Method to reset the game
    private void resetGame() {
        game = null; // Reset the game object
        verbs = null; // Reset the list of verbs
        currentVerb = null; // Reset the current verb
        currentPersonIndex = 0; // Reset the current person index

        usedVerbs.clear(); // Clear the set of used verbs
        jTextField1.setText(""); // Clear the answer field
        updateScore(0, 0); // Reset the score display

        // Display a message indicating that the game has been reset
        updateQuestion("Game reset. Click Start to begin a new game.");
    }

    // Method to start the game logic
    private void startGame() {
        CSVReader csvReader = new CSVReader();
        csvReader.chooseFile();
        try {
            verbs = csvReader.readVerbsFromCSV();
        } catch (IOException ex) {
            Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        game = new Game(verbs, new UserInterface());
        usedVerbs.clear(); // Clear the set of used verbs at the start of the game
        loadNextQuestion();
    }
    //Method to load next question
    private void loadNextQuestion() {
        if (game == null || verbs.isEmpty()) {
            updateQuestion("No verb data available. Please check your CSV file.");
            return;
        }

        // Check if all verbs have been used
        if (usedVerbs.size() == verbs.size()) {
            updateQuestion("You've completed all the verbs. Well done!");
            return;
        }

        // Get a random verb that hasn't been used yet
        do {
            currentVerb = game.getRandomVerb();
        } while (usedVerbs.contains(currentVerb));
        usedVerbs.add(currentVerb); // Add the newly picked verb to the set of used verbs

        currentPersonIndex = 0; // Start with the first person
        askQuestion();
    }
    //Method to ask question
    private void askQuestion() {
        // Array of pronouns for the question
        String[] persons = {"ich", "du", "er/sie/es"};
        // Array of corresponding verb forms for each pronoun
        String[] forms = {
            currentVerb.getFirstPersonForm(),
            currentVerb.getSecondPersonForm(),
            currentVerb.getThirdPersonForm()
        };
        // Check if the current person index is within the array bounds
        if (currentPersonIndex < persons.length) {
            String person = persons[currentPersonIndex];
            String verbText = currentVerb.getInfinitiveForm();
            String questionText = "Wie lautet die ";

            try {
                // Clear the document first
                doc.remove(0, doc.getLength());
                // Append the question text
                doc.insertString(doc.getLength(), questionText, null);
                // Append the tense text with the tense style
                doc.insertString(doc.getLength(), "Präsensform", tenseStyle);
                // Append the rest of the text
                doc.insertString(doc.getLength(), " von\n\n", null);
                // Append the verb text with the verb style
                doc.insertString(doc.getLength(), verbText, verbStyle);
                // Append the rest of the text
                doc.insertString(doc.getLength(), " in der ", null);
                // Append the person text with the person style
                doc.insertString(doc.getLength(), person, personStyle);
                doc.insertString(doc.getLength(), " -Form?", null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            jTextField1.setText(""); // Clear the answer field for new input
        } else {
            loadNextQuestion(); // Load the next verb when all persons are asked
        }
    }
    // Method to load verbs from CSV file
    private void loadVerbs(String filePath) {
        // This should load verbs from your CSVReader
        CSVReader csvReader = new CSVReader();
        try {
            verbs = csvReader.readVerbsFromCSV();  // Initialize the verbs variable
        } catch (IOException e) {
            updateQuestion("Error reading CSV file: " + e.getMessage());
        }
    }
    //Method to check if the answer is correct or false
    private void processAnswer() {
        String playerAnswer = jTextField1.getText();
        String[] forms = {
            currentVerb.getFirstPersonForm(),
            currentVerb.getSecondPersonForm(),
            currentVerb.getThirdPersonForm()
        };

        if (playerAnswer.equals(forms[currentPersonIndex])) {
            playSound("C:\\Users\\caner\\Documents\\NetBeansProjects\\projectGermanVerbExercise2\\correct_answer_sound.wav");  // Play the sound effect for the correct answer
            showMessageDialog("Correct!", "Correct", "C:\\Users\\caner\\Documents\\NetBeansProjects\\projectGermanVerbExercise2\\right_icon.png");
            game.incrementCorrectAnswers();
        } else {
            playSound("C:\\Users\\caner\\Documents\\NetBeansProjects\\projectGermanVerbExercise2\\wrong_answer_sound.wav");  // Play the sound effect for the incorrect answer (if you have one)
            showMessageDialog("Incorrect. The correct answer is: " + forms[currentPersonIndex], "Incorrect", "C:\\Users\\caner\\Documents\\NetBeansProjects\\projectGermanVerbExercise2\\wrong_icon.png");
        }

        game.incrementTotalQuestions();
        currentPersonIndex++;
        askQuestion();

        // Update the score after each question
        updateScore(game.getCorrectAnswers(), game.getTotalQuestions());
    }
    // Method to display message dialog
    private void showMessageDialog(String message, String title, String iconPath) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        ImageIcon icon = new ImageIcon(iconPath);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // Scale image
        icon = new ImageIcon(scaledImage); // Assign the scaled image back to the icon

        JOptionPane.showMessageDialog(this, label, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }
    // Method to play sound
    private void playSound(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 204, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        setForeground(new java.awt.Color(102, 204, 255));

        jButton1.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jButton1.setText("Start");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jTextField1.setText("Antwort");
        jTextField1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 204, 255)));

        jButton2.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jButton2.setText("Senden");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Punktzahl: 0/0");

        jButton3.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jButton3.setText("Neu starten");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 204, 255)));
        jTextPane1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jTextPane1.setText("Frage");
        jScrollPane2.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)
                    .addComponent(jLabel1))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(85, 85, 85))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GameUI().setVisible(true);
                } catch (IllegalArgumentException ex) {
                    // Handle the exception here
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
