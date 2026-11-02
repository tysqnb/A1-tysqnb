import java.util.EnumSet;
import java.util.Set;

/**
 * A dietitian: a health professional who manages patients through diet rather
 * than medication.
 *
 * <p>Dietitians share the identity and roster of every other professional but
 * have nothing in common with doctors, so they extend
 * {@link HealthProfessional} directly.</p>
 *
 * @author tysqnb
 * @version 1.0
 */
public class Dietitian extends HealthProfessional {

    /** The dietetic area this dietitian works in, for example "Diabetes". */
    private final String focusArea;

    /** The consulting room used for dietitian appointments. */
    private final String consultingRoom;

    /**
     * Creates a dietitian with an explicit weekly roster.
     *
     * @param id             the numeric identifier
     * @param name           the dietitian's full name
     * @param workingDays    the days worked each week
     * @param focusArea      the dietetic area of focus
     * @param consultingRoom the consulting room
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Dietitian(String id, String name, Set<Day> workingDays,
                     String focusArea, String consultingRoom) {
        super(id, name, workingDays);
        this.focusArea = checkText(focusArea, "Focus area");
        this.consultingRoom = checkText(consultingRoom, "Consulting room");
    }

    /**
     * Creates a dietitian who works Monday, Wednesday and Friday, the roster
     * this clinic happens to use for part time allied health staff.
     *
     * @param id             the numeric identifier
     * @param name           the dietitian's full name
     * @param focusArea      the dietetic area of focus
     * @param consultingRoom the consulting room
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Dietitian(String id, String name, String focusArea, String consultingRoom) {
        this(id, name, EnumSet.of(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY), focusArea, consultingRoom);
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
            throw new IllegalArgumentException(label + " is required for a dietitian.");
        }
        return value.trim();
    }

    /**
     * Returns the dietetic area of focus.
     *
     * @return the focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Returns the consulting room.
     *
     * @return the room name
     */
    public String getConsultingRoom() {
        return consultingRoom;
    }

    /**
     * Returns the role title.
     *
     * @return "Dietitian"
     */
    @Override
    public String getRole() {
        return "Dietitian";
    }

    /**
     * Returns what a dietitian does in this clinic.
     *
     * @return a one-line description of the dietitian's duties
     */
    @Override
    public String describeDuties() {
        return "Builds and reviews eating plans for patients referred with " + focusArea.toLowerCase() + ".";
    }

    /**
     * Records a diet plan review for a patient.
     *
     * @param patient the patient being reviewed
     * @param plan    the plan being put in place
     * @return a message describing the plan
     * @throws IllegalArgumentException if the patient or plan is missing
     */
    public String reviewDietPlan(Patient patient, String plan) {
        if (patient == null) {
            throw new IllegalArgumentException("Cannot record a diet plan without a patient.");
        }
        if (plan == null || plan.trim().isEmpty()) {
            throw new IllegalArgumentException("A diet plan description is required.");
        }
        return getName() + " set a " + focusArea.toLowerCase() + " plan for " + patient.getName()
                + ": " + plan.trim() + ".";
    }

    /**
     * Returns the dietitian's details, including the focus area and room.
     *
     * @return a printable description of this dietitian
     */
    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "Focus area    : " + focusArea + System.lineSeparator()
                + "Consulting    : " + consultingRoom;
    }
}
