import java.util.Set;

/**
 * A health professional who is a registered medical doctor.
 *
 * <p>Doctors add two things that nurses and dietitians do not have: a medical
 * registration number, and the legal ability to prescribe medication. Putting
 * both here means {@link GeneralPractitioner}, {@link Specialist} and
 * {@link Surgeon} inherit the prescribing behaviour instead of each writing
 * their own copy.</p>
 *
 * @author tysqnb
 * @version 1.0
 */
public abstract class Doctor extends HealthProfessional {

    /** The doctor's registration number, for example "MED-4417". */
    private final String registrationNumber;

    /**
     * Creates a doctor with an explicit weekly roster.
     *
     * @param id                 the numeric identifier
     * @param name               the doctor's full name
     * @param workingDays        the days worked each week
     * @param registrationNumber the medical registration number
     * @throws IllegalArgumentException if any argument is invalid
     */
    protected Doctor(String id, String name, Set<Day> workingDays, String registrationNumber) {
        super(id, name, workingDays);
        this.registrationNumber = checkRegistration(registrationNumber);
    }

    /**
     * Creates a doctor who works the standard Monday to Friday week.
     *
     * @param id                 the numeric identifier
     * @param name               the doctor's full name
     * @param registrationNumber the medical registration number
     * @throws IllegalArgumentException if any argument is invalid
     */
    protected Doctor(String id, String name, String registrationNumber) {
        super(id, name);
        this.registrationNumber = checkRegistration(registrationNumber);
    }

    /**
     * Validates a registration number. The format is not enforced beyond
     * requiring something present, because the clinic only stores it for
     * reference.
     *
     * @param registrationNumber the number supplied by the caller
     * @return the trimmed number
     * @throws IllegalArgumentException if the number is missing
     */
    private static String checkRegistration(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Medical registration number is required for a doctor.");
        }
        return registrationNumber.trim();
    }

    /**
     * Returns the medical registration number.
     *
     * @return the registration number
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Writes a prescription for a patient.
     *
     * <p>Only doctors have this method, which is the point of the
     * {@code Doctor} layer: a nurse cannot be asked to prescribe, because the
     * compiler will not allow it.</p>
     *
     * @param patient    the patient the medication is for
     * @param medication the medication being prescribed
     * @return a message describing the prescription
     * @throws IllegalArgumentException if the patient or medication is missing
     */
    public String prescribe(Patient patient, String medication) {
        if (patient == null) {
            throw new IllegalArgumentException("Cannot prescribe without a patient.");
        }
        if (medication == null || medication.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot prescribe an empty medication.");
        }
        return getName() + " prescribed " + medication.trim() + " for " + patient.getName()
                + " (registration " + registrationNumber + ").";
    }

    /**
     * Returns the shared doctor details, followed by this doctor's own
     * information. Subclasses extend this further.
     *
     * @return a printable description of this doctor
     */
    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "Registration  : " + registrationNumber;
    }
}
