import java.util.EnumSet;
import java.util.Set;

/**
 * A nurse: a health professional who does not prescribe, but does run the
 * clinic's sample collection and wound care.
 *
 * <p>Nurse extends {@link HealthProfessional} directly rather than
 * {@code Doctor}, which is what stops {@link #takeBloodSample(Patient)} from
 * being available on a doctor object and stops {@code prescribe} from being
 * available on a nurse.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public class Nurse extends HealthProfessional {

    /** The ward or treatment area the nurse is rostered to. */
    private final String ward;

    /** The nurse's registration level, for example "Registered Nurse". */
    private final String registrationLevel;

    /**
     * Creates a nurse with an explicit weekly roster.
     *
     * @param id                the numeric identifier
     * @param name              the nurse's full name
     * @param workingDays       the days worked each week
     * @param ward              the ward or treatment area
     * @param registrationLevel the nursing registration level
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Nurse(String id, String name, Set<Day> workingDays,
                 String ward, String registrationLevel) {
        super(id, name, workingDays);
        this.ward = checkText(ward, "Ward");
        this.registrationLevel = checkText(registrationLevel, "Registration level");
    }

    /**
     * Creates a nurse who works Monday to Friday.
     *
     * @param id                the numeric identifier
     * @param name              the nurse's full name
     * @param ward              the ward or treatment area
     * @param registrationLevel the nursing registration level
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Nurse(String id, String name, String ward, String registrationLevel) {
        this(id, name, EnumSet.range(Day.MONDAY, Day.FRIDAY), ward, registrationLevel);
    }

    /**
     * Validates a required piece of text.
     *
     * @param value the text supplied by the caller
     * @param label the field name, used in the error message
     * @return the trimmed text
     * @throws IllegalArgumentException if the text is missing
     */
    private static String checkText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " is required for a nurse.");
        }
        return value.trim();
    }

    /**
     * Returns the ward or treatment area.
     *
     * @return the ward name
     */
    public String getWard() {
        return ward;
    }

    /**
     * Returns the nursing registration level.
     *
     * @return the registration level
     */
    public String getRegistrationLevel() {
        return registrationLevel;
    }

    /**
     * Returns the role title.
     *
     * @return "Nurse"
     */
    @Override
    public String getRole() {
        return "Nurse";
    }

    /**
     * Returns what a nurse does in this clinic.
     *
     * @return a one-line description of the nurse's duties
     */
    @Override
    public String describeDuties() {
        return "Prepares patients, takes blood and other samples, and runs wound care in " + ward + ".";
    }

    /**
     * Records a blood sample being taken from a patient.
     *
     * @param patient the patient the sample is taken from
     * @return a message describing the sample collection
     * @throws IllegalArgumentException if the patient is missing
     */
    public String takeBloodSample(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Cannot take a sample without a patient.");
        }
        return getName() + " took a blood sample from " + patient.getName()
                + " in " + ward + " (sample logged).";
    }

    /**
     * Returns the nurse's details, including the ward and registration level.
     *
     * @return a printable description of this nurse
     */
    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "Ward          : " + ward + System.lineSeparator()
                + "Registration  : " + registrationLevel;
    }
}
