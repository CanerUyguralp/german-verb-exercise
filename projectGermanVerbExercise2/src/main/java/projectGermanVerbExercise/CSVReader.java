package projectGermanVerbExercise;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVReader is a class responsible for reading verb data from a CSV file. It
 * provides functionality to choose a file using a file chooser dialog and parse
 * the selected CSV file to extract verb information.
 * <p>
 * The CSV file is expected to have the following format:
 * infinitive_form,first_person,second_person,third_person The first line is
 * assumed to be the header and will be skipped.
 * </p>
 *
 * @author caner
 */
public class CSVReader {

    private String filePath;

    /**
     * Constructor for CSVReader class. Initializes the filePath to null.
     */
    public CSVReader() {
        this.filePath = null;
    }

    /**
     * Opens a file chooser dialog for selecting a CSV file. Updates the
     * filePath with the selected file's absolute path.
     */
    public void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            this.filePath = selectedFile.getAbsolutePath();
        } else {
            System.out.println("No file selected.");
        }
    }

    /**
     * Sets the file path directly.
     *
     * @param filePath the file path of the CSV file to be used.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Getter for filePath.
     *
     * @return the file path of the selected CSV file.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Reads the CSV file and extracts verb data.
     *
     * @return a list of Verb objects containing the verb data from the CSV
     * file.
     * @throws IOException if an error occurs while reading the file or no file
     * is selected.
     */
    public List<Verb> readVerbsFromCSV() throws IOException {
        if (filePath == null) {
            throw new IOException("No file selected.");
        }

        List<Verb> verbs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    // Skipping invalid or malformed lines
                    continue;
                }
                String infinitiveForm = parts[0];
                String firstPersonForm = parts[1];
                String secondPersonForm = parts[2];
                String thirdPersonForm = parts[3];
                String fourthPersonForm = parts.length > 4 ? parts[4] : "";
                String fifthPersonForm = parts.length > 5 ? parts[5] : "";
                String sixthPersonForm = parts.length > 6 ? parts[6] : "";

                Verb verb = new Verb(infinitiveForm, firstPersonForm, secondPersonForm, thirdPersonForm,
                        fourthPersonForm, fifthPersonForm, sixthPersonForm);
                verbs.add(verb);
            }
        } catch (IOException e) {
            throw new IOException("Error reading CSV file: " + e.getMessage());
        }
        return verbs;
    }

    /**
     * Reads the CSV file from the given InputStream and extracts verb data.
     *
     * @param inputStream the InputStream to read the CSV data from.
     * @return a list of Verb objects containing the verb data from the CSV
     * file.
     * @throws IOException if an error occurs while reading the file.
     */
    public List<Verb> readVerbsFromCSV(InputStream inputStream) throws IOException {
        List<Verb> verbs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    // Skipping invalid or malformed lines
                    continue;
                }
                String infinitiveForm = parts[0];
                String firstPersonForm = parts[1];
                String secondPersonForm = parts[2];
                String thirdPersonForm = parts[3];
                String fourthPersonForm = parts.length > 4 ? parts[4] : "";
                String fifthPersonForm = parts.length > 5 ? parts[5] : "";
                String sixthPersonForm = parts.length > 6 ? parts[6] : "";

                Verb verb = new Verb(infinitiveForm, firstPersonForm, secondPersonForm, thirdPersonForm,
                        fourthPersonForm, fifthPersonForm, sixthPersonForm);
                verbs.add(verb);
            }
        } catch (IOException e) {
            throw new IOException("Error reading CSV file: " + e.getMessage());
        }
        return verbs;
    }

}
