import java.util.Set;

/**
 * A surgeon: a specialist who also has an operating theatre and an operating
 * list.
 *
 * <p>This is the deepest class in the hierarchy:
 * {@code HealthProfessional -> Doctor -> Specialist -> Surgeon}. Each step
 * reuses the validation and printing of the level above it, so the class only
 * has to deal with what is genuinely new.</p>
 *
 * @author tysqnb
 * @version 1.0
 */
public class Surgeon extends Specialist {

    /** The theatre this surgeon operates in, for example "Theatre B". */
    private final String theatre;

    /**
     * Creates a surgeon with an explicit weekly roster.
     *
     * @param id                 the numeric identifier
     * @param name               the surgeon's full name
     * @param workingDays        the days worked each week
     * @param registrationNumber the medical registration number
     * @param specialty          the surgical specialty, for example "Orthopaedics"
     * @param theatre            the operating theatre
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Surgeon(String id, String name, Set<Day> workingDays,
                   String registrationNumber, String specialty, String theatre) {
        super(id, name, workingDays, registrationNumber, specialty);
        this.theatre = checkTheatre(theatre);
    }

    /**
     * Creates a surgeon who works Monday to Friday.
     *
     * @param id                 the numeric identifier
     * @param name               the surgeon's full name
     * @param registrationNumber the medical registration number
     * @param specialty          the surgical specialty
     * @param theatre            the operating theatre
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Surgeon(String id, String name, String registrationNumber, String specialty, String theatre) {
        super(id, name, registrationNumber, specialty);
        this.theatre = checkTheatre(theatre);
    }

    /**
     * Validates the theatre name.
     *
     * @param theatre the theatre supplied by the caller
     * @return the trimmed theatre name
     * @throws IllegalArgumentException if the theatre is missing
     */
    private static String checkTheatre(String theatre) {
        if (theatre == null || theatre.trim().isEmpty()) {
            throw new IllegalArgumentException("Operating theatre is required for a surgeon.");
        }
        return theatre.trim();
    }

    /**
     * Returns the operating theatre.
     *
     * @return the theatre name
     */
    public String getTheatre() {
        return theatre;
    }

    /**
     * Returns the role title, which is deliberately narrower than the
     * specialist title because surgeons are booked differently.
     *
     * @return a title such as "Surgeon - Orthopaedics"
     */
    @Override
    public String getRole() {
        return "Surgeon - " + getSpecialty();
    }

    /**
     * Returns what a surgeon does in this clinic.
     *
     * @return a one-line description of the surgeon's duties
     */
    @Override
    public String describeDuties() {
        return "Runs an operating list in " + theatre + " and holds post-operative reviews.";
    }

    /**
     * Records a procedure for a patient.
     *
     * @param patient   the patient being operated on
     * @param procedure the procedure being performed
     * @return a message describing the procedure
     * @throws IllegalArgumentException if the patient or procedure is missing
     */
    public String performProcedure(Patient patient, String procedure) {
        if (patient == null) {
            throw new IllegalArgumentException("Cannot record a procedure without a patient.");
        }
        if (procedure == null || procedure.trim().isEmpty()) {
            throw new IllegalArgumentException("A procedure description is required.");
        }
        return getName() + " will perform " + procedure.trim() + " on " + patient.getName()
                + " in " + theatre + ".";
    }

    /**
     * Returns the surgeon's details, including the theatre.
     *
     * @return a printable description of this surgeon
     */
    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "Theatre       : " + theatre;
    }
}
