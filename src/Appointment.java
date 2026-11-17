/**
 * One booking: a patient, the health professional they are seeing, and the
 * time they are seeing them.
 *
 * <p>The class implements {@link Comparable} so that a whole collection of
 * appointments can be handed to {@code Collections.sort} and come out in time
 * order. Ordering by the time slot's minutes, rather than its text, is what
 * makes 09:30 sort before 10:00.</p>
 *
 * <p>Every argument is checked in the constructor. An appointment cannot exist
 * with a missing patient, a missing professional or no time, which is what
 * keeps the manager free of half-built bookings.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public class Appointment implements Comparable<Appointment> {

    /** Counter used to give each booking a short reference such as "A-0007". */
    private static int nextReference = 1;

    /** This booking's reference, unique within one run of the program. */
    private final String reference;

    /** The patient being seen. */
    private final Patient patient;

    /** The health professional the patient is booked with. */
    private final HealthProfessional professional;

    /** The time of the appointment. */
    private final TimeSlot time;

    /**
     * Creates an appointment.
     *
     * @param patient      the patient being seen
     * @param professional any type of health professional
     * @param time         the appointment time
     * @throws IllegalArgumentException if any argument is null
     */
    public Appointment(Patient patient, HealthProfessional professional, TimeSlot time) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment needs a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment needs a health professional.");
        }
        if (time == null) {
            throw new IllegalArgumentException("An appointment needs a time.");
        }
        this.patient = patient;
        this.professional = professional;
        this.time = time;
        this.reference = String.format("A-%04d", nextReference++);
    }

    /**
     * Returns the booking reference.
     *
     * @return the reference, for example "A-0007"
     */
    public String getReference() {
        return reference;
    }

    /**
     * Returns the patient.
     *
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the health professional. The declared type is the abstract base
     * class, so this works for every specialty.
     *
     * @return the professional
     */
    public HealthProfessional getProfessional() {
        return professional;
    }

    /**
     * Returns the appointment time.
     *
     * @return the time slot
     */
    public TimeSlot getTime() {
        return time;
    }

    /**
     * Orders appointments by time, and breaks ties so that the order is stable
     * from run to run: two bookings at the same time are ordered by role, then
     * by professional ID, then by patient name.
     *
     * @param other the appointment to compare against
     * @return a negative number if this booking comes first, zero if they are
     *         equivalent for sorting, or a positive number if it comes later
     */
    @Override
    public int compareTo(Appointment other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare an appointment with null.");
        }
        int byTime = time.compareTo(other.time);
        if (byTime != 0) {
            return byTime;
        }
        int byRole = professional.getRole().compareTo(other.professional.getRole());
        if (byRole != 0) {
            return byRole;
        }
        int byProfessional = professional.getId().compareTo(other.professional.getId());
        if (byProfessional != 0) {
            return byProfessional;
        }
        return patient.getName().compareTo(other.patient.getName());
    }

    /**
     * Returns the appointment and the professional's details. The professional
     * block is indented so a list of appointments stays readable.
     *
     * @return a printable description of this appointment
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append("Appointment ").append(reference).append(System.lineSeparator());
        text.append("  Time          : ").append(time).append(System.lineSeparator());
        text.append("  Patient       : ").append(patient.getName())
                .append(" (").append(patient.getFormattedPhone()).append(")").append(System.lineSeparator());
        text.append("  Professional  :").append(System.lineSeparator());
        text.append(indent(professional.toString(), "      "));
        return text.toString();
    }

    /**
     * Adds a prefix to every line of a block of text. Used to nest the
     * professional's details under the appointment.
     *
     * @param text   the text to indent
     * @param prefix the text to put at the start of each line
     * @return the indented text
     */
    private static String indent(String text, String prefix) {
        String[] lines = text.split("\\R");
        StringBuilder indented = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                indented.append(System.lineSeparator());
            }
            indented.append(prefix).append(lines[i]);
        }
        return indented.toString();
    }
}
