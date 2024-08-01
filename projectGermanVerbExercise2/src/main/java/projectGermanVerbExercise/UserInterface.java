    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectGermanVerbExercise;

/**
 *
 * @author caner
 */

// This class handles  basic input and output operations in a commandline application

import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;

    // Constructor
    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    // Method to display message
    public void displayMessage(String message) {
        System.out.println(message);
    }

    // Method to prompt user for input
    public String promptUser(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    // Method to close the scanner
    public void closeScanner() {
        scanner.close();
    }
}
