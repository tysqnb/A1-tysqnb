import java.util.Set;

/**
 * A specialist doctor: a GP-level doctor who has trained in one area, such as
 * paediatrics, radiology or cardiology.
 *
 * <p>{@link Surgeon} builds on this class rather than on {@code Doctor},
 * because a surgeon is a specialist with an operating list.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public class Specialist extends Doctor {

    /** The area this specialist practises in, for example "Cardiology". */
    private final String specialty;

    /**
     * Creates a specialist with an explicit weekly roster.
     *
     * @param id                 the numeric identifier
     * @param name               the specialist's full name
     * @param workingDays        the days worked each week
     * @param registrationNumber the medical registration number
     * @param specialty          the area of practice
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Specialist(String id, String name, Set<Day> workingDays,
                      String registrationNumber, String specialty) {
        super(id, name, workingDays, registrationNumber);
        this.specialty = checkSpecialty(specialty);
    }

    /**
     * Creates a specialist who works Monday to Friday.
     *
     * @param id                 the numeric identifier
     * @param name               the specialist's full name
     * @param registrationNumber the medical registration number
     * @param specialty          the area of practice
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Specialist(String id, String name, String registrationNumber, String specialty) {
        super(id, name, registrationNumber);
        this.specialty = checkSpecialty(specialty);
    }

    /**
     * Validates the specialty name.
     *
     * @param specialty the specialty supplied by the caller
     * @return the trimmed specialty
     * @throws IllegalArgumentException if the specialty is missing
     */
    private static String checkSpecialty(String specialty) {
        if (specialty == null || specialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialty is required for a specialist.");
        }
        return specialty.trim();
    }

    /**
     * Returns the area of practice.
     *
     * @return the specialty, for example "Paediatrics"
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Returns the role title, including the specialty so that two specialists
     * are still distinguishable in printed output.
     *
     * @return a title such as "Specialist - Cardiology"
     */
    @Override
    public String getRole() {
        return "Specialist - " + specialty;
    }

    /**
     * Returns what a specialist does in this clinic.
     *
     * @return a one-line description of the specialist's duties
     */
    @Override
    public String describeDuties() {
        return "Sees referred patients for " + specialty.toLowerCase()
                + " assessment and ongoing management.";
    }

    /**
     * Returns the specialist's details, including the area of practice.
     *
     * @return a printable description of this specialist
     */
    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "Specialty     : " + specialty;
    }
}
