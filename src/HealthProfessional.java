import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * The common state and behaviour shared by every health professional in the
 * clinic, whatever their specialty.
 *
 * <p>This class is abstract on purpose. A "plain" health professional with no
 * specialty makes no sense in this system, so the class cannot be instantiated
 * directly; every concrete subclass has to supply its own role name and duty
 * description. That is what forces {@link GeneralPractitioner},
 * {@link Specialist}, {@link Surgeon}, {@link Nurse} and {@link Dietitian} to
 * say something about themselves.</p>
 *
 * <p>All fields are private and final. Objects are fully validated by the
 * constructor, and the only way out is through read-only getters, so an
 * object can never be pushed into an invalid state after it has been
 * created.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public abstract class HealthProfessional {

    /** Smallest number of digits allowed in an ID. */
    private static final int MIN_ID_DIGITS = 3;

    /** Longest name accepted, to keep printed tables readable. */
    private static final int MAX_NAME_LENGTH = 60;

    /** The unique numeric identifier of this professional. */
    private final String id;

    /** The full name of this professional. */
    private final String name;

    /** The days of the week this professional works. Never empty. */
    private final Set<Day> workingDays;

    /**
     * Creates a health professional with an explicit weekly roster.
     *
     * @param id          the identifier, digits only
     * @param name        the professional's full name
     * @param workingDays the days worked each week; must not be empty
     * @throws IllegalArgumentException if any argument is missing or invalid
     */
    protected HealthProfessional(String id, String name, Set<Day> workingDays) {
        this.id = checkId(id);
        this.name = checkName(name);
        this.workingDays = copyDays(workingDays);
    }

    /**
     * Creates a health professional who works the standard Monday to Friday
     * week. This is the convenience constructor for the common case; callers
     * that need a different roster use the three-argument version.
     *
     * @param id   the identifier, digits only
     * @param name the professional's full name
     * @throws IllegalArgumentException if any argument is missing or invalid
     */
    protected HealthProfessional(String id, String name) {
        this(id, name, defaultWorkingWeek());
    }

    /**
     * Builds the roster used when a caller does not supply one.
     *
     * @return a new set containing Monday to Friday
     */
    private static Set<Day> defaultWorkingWeek() {
        return EnumSet.range(Day.MONDAY, Day.FRIDAY);
    }

    /**
     * Validates an identifier and normalises it by trimming surrounding space.
     *
     * @param id the identifier supplied by the caller
     * @return the trimmed identifier
     * @throws IllegalArgumentException if the ID is null, blank, shorter than
     *                                  {@value #MIN_ID_DIGITS} characters or
     *                                  contains anything other than digits
     */
    private static String checkId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Health professional ID is required.");
        }
        String trimmed = id.trim();
        if (trimmed.length() < MIN_ID_DIGITS) {
            throw new IllegalArgumentException(
                    "Health professional ID must be at least " + MIN_ID_DIGITS + " digits: " + trimmed);
        }
        for (int i = 0; i < trimmed.length(); i++) {
            if (!Character.isDigit(trimmed.charAt(i))) {
                throw new IllegalArgumentException(
                        "Health professional ID must contain digits only: " + trimmed);
            }
        }
        return trimmed;
    }

    /**
     * Validates and normalises a name.
     *
     * @param name the name supplied by the caller
     * @return the trimmed, whitespace-collapsed name
     * @throws IllegalArgumentException if the name is null, blank or too long
     */
    private static String checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Health professional name is required.");
        }
        String cleaned = name.trim().replaceAll("\\s+", " ");
        if (cleaned.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "Health professional name is too long (max " + MAX_NAME_LENGTH + " characters).");
        }
        return cleaned;
    }

    /**
     * Copies the supplied roster into an unmodifiable set so that the caller
     * cannot add or remove days through the reference it passed in.
     *
     * @param days the roster supplied by the caller
     * @return an unmodifiable copy of the roster
     * @throws IllegalArgumentException if the roster is null or empty
     */
    private static Set<Day> copyDays(Set<Day> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("A health professional must work at least one day.");
        }
        Set<Day> copy = EnumSet.noneOf(Day.class);
        for (Day day : days) {
            if (day == null) {
                throw new IllegalArgumentException("Working days cannot contain null.");
            }
            copy.add(day);
        }
        return Collections.unmodifiableSet(copy);
    }

    /**
     * Returns the numeric identifier of this professional.
     *
     * @return the ID, digits only
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the full name of this professional.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the days this professional works. The returned set is
     * unmodifiable, so it can be read safely but not changed.
     *
     * @return an unmodifiable set of working days
     */
    public Set<Day> getWorkingDays() {
        return workingDays;
    }

    /**
     * Reports whether this professional works on a given day.
     *
     * @param day the day to test
     * @return true if the day is part of the weekly roster
     */
    public boolean worksOn(Day day) {
        return day != null && workingDays.contains(day);
    }

    /**
     * Returns the roster as a readable list, for example
     * "Monday, Wednesday, Friday".
     *
     * @return the working days joined by commas
     */
    public String getWorkingDaysAsText() {
        StringBuilder text = new StringBuilder();
        for (Day day : workingDays) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(day.getLabel());
        }
        return text.toString();
    }

    /**
     * Returns the title of this professional's role, which is what makes each
     * subclass print differently even when it is handled through this type.
     *
     * @return a short role name such as "Nurse"
     */
    public abstract String getRole();

    /**
     * Returns a one-line description of what this professional does. Each
     * subclass provides its own version, so calling this through a
     * {@code HealthProfessional} reference still runs the subclass code.
     *
     * @return a description of the professional's main duty
     */
    public abstract String describeDuties();

    /**
     * Returns the shared details of this professional in a readable,
     * multi-line form. Subclasses call this and then append their own fields,
     * so the shared part is written once.
     *
     * @return the shared details, without a trailing line break
     */
    protected String getSharedDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Role          : ").append(getRole()).append(System.lineSeparator());
        details.append("ID            : ").append(id).append(System.lineSeparator());
        details.append("Name          : ").append(name).append(System.lineSeparator());
        details.append("Works         : ").append(getWorkingDaysAsText()).append(System.lineSeparator());
        details.append("Duties        : ").append(describeDuties());
        return details.toString();
    }

    /**
     * Returns every detail held about this professional. Subclasses override
     * this to add their own fields after the shared ones.
     *
     * @return a printable description of this professional
     */
    @Override
    public String toString() {
        return getSharedDetails();
    }

    /**
     * Two professionals are the same person if they carry the same ID. This is
     * what lets the appointment manager spot a double booking without holding
     * on to the exact same object.
     *
     * @param other the object to compare with
     * @return true if the other object is a health professional with the same ID
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional that = (HealthProfessional) other;
        return id.equals(that.id);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return a hash based on the ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
