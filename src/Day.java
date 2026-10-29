/**
 * The days of the week a health professional can be rostered on.
 *
 * <p>An enum is used instead of plain strings so that the rest of the system
 * cannot invent a day such as "Funday" or misspell "Wednesday". A
 * {@link java.util.EnumSet} of these values is what each
 * {@link HealthProfessional} stores, which keeps the set small, ordered and
 * type-safe.</p>
 *
 * @author tysqnb
 * @version 1.0
 */
public enum Day {

    /** Monday. */
    MONDAY("Monday"),
    /** Tuesday. */
    TUESDAY("Tuesday"),
    /** Wednesday. */
    WEDNESDAY("Wednesday"),
    /** Thursday. */
    THURSDAY("Thursday"),
    /** Friday. */
    FRIDAY("Friday"),
    /** Saturday. */
    SATURDAY("Saturday"),
    /** Sunday. */
    SUNDAY("Sunday");

    /** The name shown to a user, for example in a printed roster. */
    private final String label;

    /**
     * Creates a day constant.
     *
     * @param label the display name of the day
     */
    Day(String label) {
        this.label = label;
    }

    /**
     * Returns the display name of this day.
     *
     * @return the day name, for example "Monday"
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the display name, so that printing a day never shows the
     * shouting upper-case constant name.
     *
     * @return the day name
     */
    @Override
    public String toString() {
        return label;
    }
}
