package projectGermanVerbExercise;
/**
 *
 * @author caner
 */

/* 
 *Verb class handles a verb and its conjugated forms for different persons
 *Is used in CSVReader class to populate a Verbs list
 */
public class Verb {
    private String infinitiveForm;
    private String firstPersonForm;
    private String secondPersonForm;
    private String thirdPersonForm;
    private String fourthPersonForm;
    private String fifthPersonForm;
    private String sixthPersonForm;

    // Constructor
    public Verb(String infinitiveForm, String firstPersonForm, String secondPersonForm, String thirdPersonForm,
                String fourthPersonForm, String fifthPersonForm, String sixthPersonForm) {
        this.infinitiveForm = infinitiveForm;
        this.firstPersonForm = firstPersonForm;
        this.secondPersonForm = secondPersonForm;
        this.thirdPersonForm = thirdPersonForm;
        this.fourthPersonForm = fourthPersonForm;
        this.fifthPersonForm = fifthPersonForm;
        this.sixthPersonForm = sixthPersonForm;
    }

    // Getters
    public String getInfinitiveForm() {
        return infinitiveForm;
    }

    public String getFirstPersonForm() {
        return firstPersonForm;
    }

    public String getSecondPersonForm() {
        return secondPersonForm;
    }

    public String getThirdPersonForm() {
        return thirdPersonForm;
    }
    
    public String getFourthPersonForm() {
        return fourthPersonForm;
    }

    public String getFifthPersonForm() {
        return fifthPersonForm;
    }

    public String getSixthPersonForm() {
        return sixthPersonForm;
    }
    
    // Unused method, ignore it.
    public String getQuestionForPerson(int personIndex) {
        switch (personIndex) {
            case 0:
                return "Question for first person";
            case 1:
                return "Question for second person";
            case 2:
                return "Question for third person";
            case 3:
                return "Question for fourth person";
            case 4:
                return "Question for fifth person";
            case 5:
                return "Question for sixth person";
            default:
                return "";
        }
    }
    
    

    @Override
    public String toString() {
        return "Verb{" +
                "infinitiveForm='" + infinitiveForm + '\'' +
                ", firstPersonForm='" + firstPersonForm + '\'' +
                ", secondPersonForm='" + secondPersonForm + '\'' +
                ", thirdPersonForm='" + thirdPersonForm + '\'' +
                ", fourthPersonForm='" + fourthPersonForm + '\'' +
                ", fifthPersonForm='" + fifthPersonForm + '\'' +
                ", sixthPersonForm='" + sixthPersonForm + '\'' +
                '}';
    }
}
