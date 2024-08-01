package projectGermanVerbExercise;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import projectGermanVerbExercise.CSVReader;
import projectGermanVerbExercise.Game;
import projectGermanVerbExercise.UserInterface;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author caner
 */

// Present tense

public class PresentGameUI extends AbstractVerbGameUI {

    public PresentGameUI() {
        super("German Verb Exercise - Present Tense");
    }

    
    @Override
    protected String getCSVFilePath() {
        return "csv/verbsPresent.csv";
    }

    @Override
    protected void askQuestion() {
        String[] persons = {"ich", "du", "er/sie/es", "wir", "ihr", "sie/Sie"};
        String[] forms = {
            currentVerb.getFirstPersonForm(),
            currentVerb.getSecondPersonForm(),
            currentVerb.getThirdPersonForm(),
            currentVerb.getFourthPersonForm(),
            currentVerb.getFifthPersonForm(),
            currentVerb.getSixthPersonForm()
        };

        if (currentPersonIndex < persons.length) {
            String person = persons[currentPersonIndex];
            String verbText = currentVerb.getInfinitiveForm();
            //String questionText = "Wie lautet die ";

            try {
                // Clear the document first
                doc.remove(0, doc.getLength());
                // Append the question text
                //doc.insertString(doc.getLength(), questionText, null);
                // Append the tense text with the tense style
                doc.insertString(doc.getLength(), "\n Präsensform: ", tenseStyle);
                // Append the rest of the text
                //doc.insertString(doc.getLength(), " von ", null);
                // Append the verb text with the verb style
                doc.insertString(doc.getLength(), verbText, verbStyle);
                // Append the rest of the text
                //doc.insertString(doc.getLength(), " in der ", null);
                // Append the person text with the person style
                //doc.insertString(doc.getLength(), person, personStyle);
                //doc.insertString(doc.getLength(), " -Form?", null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }

            // Update the person JTextPane with the current person
            updatePersonTextPane(person, personStyle);

            clearAnswerField(); // Clear the answer field for new input
        } else {
            loadNextQuestion(); // Load the next verb when all persons are asked
        }
    }

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
            java.util.logging.Logger.getLogger(PresentGameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PresentGameUI().setVisible(true);
                } catch (IllegalArgumentException ex) {
                    // Handle the exception here
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
