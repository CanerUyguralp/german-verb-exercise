/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectGermanVerbExercise;

/**
 *
 * @author caner
 */
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

// This class handles the game logic

public class Game {

    private List<Verb> verbs;
    private UserInterface userInterface;
    private Random random;
    private int correctAnswers;
    private int totalQuestions;
    private Set<Verb> usedVerbs;

    // Constructor
    public Game(List<Verb> verbs, UserInterface userInterface) {
        this.verbs = verbs;
        this.userInterface = userInterface;
        this.random = new Random();
        this.correctAnswers = 0;
        this.totalQuestions = 0;
        this.usedVerbs = new HashSet<>();
    }

    // Method to get the correct answer count
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    // Method to get the total questions count
    public int getTotalQuestions() {
        return totalQuestions;
    }

    // Method to increment correct answers
    public void incrementCorrectAnswers() {
        correctAnswers++;
    }

    // Method to increment total questions
    public void incrementTotalQuestions() {
        totalQuestions++;
    }

    // Method to set the verbs list
    public void setVerbs(List<Verb> verbs) {
        this.verbs = verbs;
    }

    // Method to start the game
    public void startGame() {
        userInterface.displayMessage("Welcome to the German Grammar Game!");

        // Check if verbs list is empty
        if (verbs == null || verbs.isEmpty()) {
            userInterface.displayMessage("No verb data found. Please check your CSV file.");
            return; // Exit the method
        }

        while (true) {
            Verb randomVerb = getRandomVerb();
            userInterface.displayMessage("Verb: " + randomVerb.getInfinitiveForm());

            String[] persons = {"first", "second", "third", "fourth", "fifth", "sixth"};
            String[] forms = {
                randomVerb.getFirstPersonForm(),
                randomVerb.getSecondPersonForm(),
                randomVerb.getThirdPersonForm(),
                randomVerb.getFourthPersonForm(),
                randomVerb.getFifthPersonForm(),
                randomVerb.getSixthPersonForm()
            };

            for (int i = 0; i < forms.length; i++) {
                totalQuestions++;
                String form = forms[i];
                String person = persons[i];

                String userInput = userInterface.promptUser("Enter the " + person + " person form for \"" + randomVerb.getInfinitiveForm() + "\":");
                if (!userInput.equals(form)) {
                    userInterface.displayMessage("Incorrect! The correct answer is: " + form);
                } else {
                    userInterface.displayMessage("Correct!");
                    correctAnswers++;
                }
            }

            userInterface.displayMessage("Score: " + correctAnswers + "/" + totalQuestions);

            String playAgain = userInterface.promptUser("Do you want to play again? (yes/no)");
            if (!playAgain.equalsIgnoreCase("yes")) {
                userInterface.displayMessage("Thanks for playing!");
                userInterface.displayMessage("Final Score: " + correctAnswers + "/" + totalQuestions);
                // Reset variables for a new game
                correctAnswers = 0;
                totalQuestions = 0;
                break;
            }
        }
    }

    // Method to get a random verb from the list
    public Verb getRandomVerb() {
        // Check if all verbs have been used, reset the set
        if (usedVerbs.size() == verbs.size()) {
            usedVerbs.clear();
        }
        Verb randomVerb;
        do {
            randomVerb = verbs.get(random.nextInt(verbs.size()));
        } while (usedVerbs.contains(randomVerb)); // Keep picking random verbs until one that hasn't been used is found
        usedVerbs.add(randomVerb); // Add the newly picked verb to the set of used verbs
        return randomVerb;
    }

    // Main method
    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader();
        csvReader.chooseFile(); // This line should open the file chooser dialog

        try {
            List<Verb> verbs = csvReader.readVerbsFromCSV();
            UserInterface userInterface = new UserInterface();
            Game game = new Game(verbs, userInterface);
            game.startGame();
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }
}