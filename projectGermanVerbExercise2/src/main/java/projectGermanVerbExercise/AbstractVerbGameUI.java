/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectGermanVerbExercise;

/**
 *
 * @author caner
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
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
import java.io.InputStream;
import javax.imageio.ImageIO;

public abstract class AbstractVerbGameUI extends javax.swing.JFrame {

    protected List<Verb> verbs;  // Declare the verbs variable
    protected Game game;
    protected Verb currentVerb;
    protected int currentPersonIndex;  // Track the current person (1st, 2nd, or 3rd)
    protected Set<Verb> usedVerbs;

    // Define styles for the verb and person
    protected Style verbStyle;
    protected Style hilfsVerbStyle;
    protected Style personStyle;
    protected Style tenseStyle;
    protected Style signStyle;

    protected StyledDocument doc;

    /**
     * Creates new form GameUI
     */
    public AbstractVerbGameUI(String title) {
        super(title);
        initComponents();
        customInitComponents();
        usedVerbs = new HashSet<>(); // Initialize the set of used verbs

        // Set JFrame size
        setSize(700, 500);  // Setting JFrame size to 700x500 pixels

    }

    
    // Custom initialization method for additional components and listeners
    private void customInitComponents() {

        jLayeredPane1 = new JLayeredPane();
        jLayeredPane1.setPreferredSize(new Dimension(700, 500)); // Set JLayeredPane size

        // Load the background image for jLabel2 and resize it
        try {
            // Load image from resources (assuming it's in the classpath)
            InputStream backgroundImageStream = getClass().getResourceAsStream("/images/blackboard.png");
            Image backgroundImage = ImageIO.read(backgroundImageStream);
            ImageIcon originalIcon = new ImageIcon(backgroundImage);

            // Calculate scaled dimensions while maintaining aspect ratio
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            int newWidth = 700;
            int newHeight = (originalHeight * newWidth) / originalWidth;

            Image scaledImage = backgroundImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            jLabel2 = new JLabel(scaledIcon);
            jLabel2.setBounds(0, 0, newWidth, newHeight); // Set bounds to match scaled image size
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception if image loading fails
        }

        // Add jLabel2 to the layered pane and set its layer
        jLayeredPane1.add(jLabel2, JLayeredPane.DEFAULT_LAYER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jLayeredPane1, BorderLayout.CENTER);

        jButton1.addActionListener(e -> startGame());
        jButton2.addActionListener(e -> processAnswer());
        jButton3.addActionListener(e -> resetGame());

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new Color(224, 224, 224));
        javax.swing.border.Border emptyBorderPane = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        javax.swing.border.Border matteBorderPane = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        javax.swing.border.Border compoundBorderPane = BorderFactory.createCompoundBorder(emptyBorderPane, matteBorderPane);
        jTextPane1.setBorder(compoundBorderPane);

        //jTextField1.setText("Antwort");
        javax.swing.border.Border emptyBorderTextField = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        javax.swing.border.Border matteBorderTextField = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        javax.swing.border.Border compoundBorderTextField = BorderFactory.createCompoundBorder(emptyBorderTextField, matteBorderTextField);
        jTextField1.setBorder(compoundBorderTextField);
        jTextField1.setBackground(new Color(224, 224, 224));

        jTextPane2.setEditable(false);
        jTextPane2.setBackground(new Color(224, 224, 224));
        jTextPane2.setBorder(compoundBorderTextField);

        Font customFont = new Font("Arial", Font.PLAIN, 18);
        jTextPane1.setFont(customFont);
        jLabel1.setFont(customFont);

        Font customFont2 = new Font("Arial", Font.BOLD, 24);
        jTextField1.setFont(customFont2);

        doc = jTextPane1.getStyledDocument();
        StyleContext sc = new StyleContext();

        verbStyle = sc.addStyle("VerbStyle", null);
        StyleConstants.setForeground(verbStyle, new Color(0, 0, 255));
        StyleConstants.setBold(verbStyle, true);
        StyleConstants.setFontSize(verbStyle, 25);

        hilfsVerbStyle = sc.addStyle("HilfsVerbStyle", null);
        StyleConstants.setForeground(hilfsVerbStyle, new Color(20, 150, 20));
        StyleConstants.setBold(hilfsVerbStyle, true);
        StyleConstants.setFontSize(hilfsVerbStyle, 22);
        StyleConstants.setUnderline(hilfsVerbStyle, true);

        personStyle = sc.addStyle("PersonStyle", null);
        StyleConstants.setForeground(personStyle, new Color(163, 21, 21));
        StyleConstants.setBold(personStyle, true);
        StyleConstants.setFontSize(personStyle, 24);

        tenseStyle = sc.addStyle("TenseStyle", null);
        StyleConstants.setForeground(tenseStyle, new Color(0, 0, 0));
        StyleConstants.setBold(tenseStyle, true);
        StyleConstants.setItalic(tenseStyle, true);
        StyleConstants.setFontSize(tenseStyle, 25);

        signStyle = sc.addStyle("signStyle", null);
        StyleConstants.setForeground(signStyle, new Color(180, 0, 0));
        StyleConstants.setBold(signStyle, true);
        StyleConstants.setFontSize(signStyle, 25);

        jLabel1.setFont(new Font("Arial", Font.BOLD, 18));

    }

    protected void updateScore(int currentScore, int totalQuestions) {
        jLabel1.setText("Punktzahl: " + currentScore + "/" + totalQuestions);
    }

    protected void updateQuestion(String newQuestion) {
        jTextPane1.setText(newQuestion);
    }

    protected String getPlayerAnswer() {
        return jTextField1.getText();
    }
    
    // Method to update pronoun on UI
    protected void updatePersonTextPane(String person, Style personStyle) {
        try {
            StyledDocument personDoc = jTextPane2.getStyledDocument();
            personDoc.remove(0, personDoc.getLength());
            personDoc.insertString(0, person, personStyle);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public void clearAnswerField() {
        jTextField1.setText("");
    }

    protected void resetGame() {
        game = null;
        verbs = null;
        currentVerb = null;
        currentPersonIndex = 0;
        usedVerbs.clear();
        jTextField1.setText("");
        updateScore(0, 0);
        updateQuestion("Game reset. Click Start to begin a new game.");
    }

    protected abstract String getCSVFilePath();

    protected void initializeButtonListeners() {
        jButton1.addActionListener(e -> startGame());
    }
    
    // Method to start the game, loads CSV file first
    protected void startGame() {
        String csvFilePath = getCSVFilePath();
        CSVReader csvReader = new CSVReader();

        InputStream csvStream = getClass().getClassLoader().getResourceAsStream(csvFilePath);

        if (csvStream == null) {
            Logger.getLogger(AbstractVerbGameUI.class.getName()).log(Level.SEVERE, "CSV file not found: " + csvFilePath);
            return;
        }

        try {
            verbs = csvReader.readVerbsFromCSV(csvStream);
        } catch (IOException ex) {
            Logger.getLogger(AbstractVerbGameUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        game = new Game(verbs, new UserInterface());
        updateScore(0, 0);
        usedVerbs.clear();
        loadNextQuestion();
    }
    
    // Method checks if the answer is correct or false then plays sound a effect and shows a message dialog
    protected void processAnswer() {
        String playerAnswer = getPlayerAnswer();
        String[] forms = {
            currentVerb.getFirstPersonForm(),
            currentVerb.getSecondPersonForm(),
            currentVerb.getThirdPersonForm(),
            currentVerb.getFourthPersonForm(),
            currentVerb.getFifthPersonForm(),
            currentVerb.getSixthPersonForm()
        };

        if (playerAnswer.equals(forms[currentPersonIndex])) {
            playSound("correct_answer_sound.wav");
            showMessageDialog("Correct!", "Correct", "right_icon.png");
            game.incrementCorrectAnswers();
        } else {
            playSound("wrong_answer_sound.wav");
            showMessageDialog("Incorrect. The correct answer is: " + forms[currentPersonIndex], "Incorrect", "wrong_icon.png");
        }

        game.incrementTotalQuestions();
        currentPersonIndex++;
        askQuestion();

        updateScore(game.getCorrectAnswers(), game.getTotalQuestions());
    }
    
    // Method to display a message dialog with given message, title and icon path
    protected void showMessageDialog(String message, String title, String iconFileName) {
        try {
            // Load icon image from resources (assuming it's in the classpath)
            InputStream iconStream = getClass().getResourceAsStream("/images/" + iconFileName);
            if (iconStream != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(iconStream));
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImage);
                JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
            } else {
                // Handle case where icon file is not found
                System.err.println("Image file not found: " + iconFileName);
                JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception if icon loading fails
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    protected void loadNextQuestion() {
        currentVerb = game.getRandomVerb();
        usedVerbs.add(currentVerb);
        currentPersonIndex = 0;
        askQuestion();
    }

    protected abstract void askQuestion();
    
    // Method to play sound effects on correct and false answers
    private void playSound(String soundFileName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/sounds/" + soundFileName);
            if (inputStream == null) {
                System.err.println("Could not find sound file: " + soundFileName);
                return;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream));
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

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextPane2 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(102, 204, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        setForeground(new java.awt.Color(102, 204, 255));
        setResizable(false);
        setSize(new java.awt.Dimension(700, 500));
        addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                formComponentAdded(evt);
            }
        });

        jLayeredPane1.setMaximumSize(new java.awt.Dimension(700, 500));
        jLayeredPane1.setMinimumSize(new java.awt.Dimension(700, 500));

        jTextPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 204, 255)));
        jTextPane1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jScrollPane2.setViewportView(jTextPane1);

        jButton1.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jButton1.setText("Start");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jTextField1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 204, 255)));

        jButton2.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jButton2.setText("Senden");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Punktzahl: 0/0");

        jButton3.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jButton3.setText("Neu starten");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextPane2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 204, 255)));
        jTextPane2.setAutoscrolls(false);
        jTextPane2.setMinimumSize(new java.awt.Dimension(62, 24));

        jLayeredPane1.setLayer(jScrollPane2, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(jButton1, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(jTextField1, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(jButton2, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(jLabel1, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(jButton3, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jTextPane2, javax.swing.JLayeredPane.PALETTE_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGap(284, 284, 284)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addComponent(jTextPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(99, 99, 99)
                                .addComponent(jLabel1)
                                .addGap(97, 97, 97)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(80, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(jTextPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(28, 28, 28))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)))
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(303, 303, 303))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_formComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentAdded

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    // End of variables declaration//GEN-END:variables
}
