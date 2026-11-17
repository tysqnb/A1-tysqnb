import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The fixed set of time slots the clinic works to.
 *
 * <p>The schedule is built from an opening time, a closing time and a slot
 * length, rather than from a hard coded list of strings. That way the clinic
 * can move to 15 minute slots or a 07:00 start by changing one object, and
 * {@link AppointmentManager} keeps working without any other changes.</p>
 *
 * <p>The generated list is exposed read-only, so no caller can quietly delete
 * the 09:30 slot and break double booking checks.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public class ClinicSchedule {

    /** Shortest slot the clinic will accept, in minutes. */
    private static final int MIN_SLOT_MINUTES = 5;

    /** Longest slot the clinic will accept, in minutes. */
    private static final int MAX_SLOT_MINUTES = 120;

    /** The first bookable slot of the day. */
    private final TimeSlot opening;

    /** The time the clinic closes; not itself a bookable slot. */
    private final TimeSlot closing;

    /** The length of one slot, in minutes. */
    private final int slotMinutes;

    /** The bookable slots, in order from earliest to latest. */
    private final List<TimeSlot> slots;

    /**
     * Creates a schedule.
     *
     * @param opening     the first bookable slot
     * @param closing     the time the clinic closes, after the opening slot
     * @param slotMinutes the length of one slot in minutes
     * @throws IllegalArgumentException if the times are out of order, the slot
     *                                  length is unrealistic, or the day does
     *                                  not divide into whole slots
     */
    public ClinicSchedule(TimeSlot opening, TimeSlot closing, int slotMinutes) {
        if (opening == null || closing == null) {
            throw new IllegalArgumentException("Opening and closing times are required.");
        }
        if (closing.compareTo(opening) <= 0) {
            throw new IllegalArgumentException("Closing time must be after opening time.");
        }
        if (slotMinutes < MIN_SLOT_MINUTES || slotMinutes > MAX_SLOT_MINUTES) {
            throw new IllegalArgumentException("Slot length must be between "
                    + MIN_SLOT_MINUTES + " and " + MAX_SLOT_MINUTES + " minutes.");
        }
        int dayLength = closing.toMinutes() - opening.toMinutes();
        if (dayLength % slotMinutes != 0) {
            throw new IllegalArgumentException("The clinic day does not divide evenly into "
                    + slotMinutes + " minute slots.");
        }
        this.opening = opening;
        this.closing = closing;
        this.slotMinutes = slotMinutes;
        this.slots = buildSlots();
    }

    /**
     * Builds the standard clinic day: 08:00 to 16:00 in half hour slots, which
     * is the schedule described in the brief.
     *
     * @return the standard daily schedule
     */
    public static ClinicSchedule standardDay() {
        return new ClinicSchedule(TimeSlot.of(8, 0), TimeSlot.of(16, 0), 30);
    }

    /**
     * Generates every slot between opening and closing.
     *
     * @return an unmodifiable list of slots in time order
     */
    private List<TimeSlot> buildSlots() {
        List<TimeSlot> generated = new ArrayList<>();
        int current = opening.toMinutes();
        int end = closing.toMinutes();
        while (current < end) {
            generated.add(new TimeSlot(current / 60, current % 60));
            current += slotMinutes;
        }
        return Collections.unmodifiableList(generated);
    }

    /**
     * Returns the first bookable slot.
     *
     * @return the opening time
     */
    public TimeSlot getOpening() {
        return opening;
    }

    /**
     * Returns the closing time.
     *
     * @return the closing time
     */
    public TimeSlot getClosing() {
        return closing;
    }

    /**
     * Returns the length of one slot.
     *
     * @return the slot length in minutes
     */
    public int getSlotMinutes() {
        return slotMinutes;
    }

    /**
     * Returns every bookable slot in time order.
     *
     * @return an unmodifiable list of slots
     */
    public List<TimeSlot> getSlots() {
        return slots;
    }

    /**
     * Reports whether a time is one of the clinic's slots.
     *
     * @param time the time to check
     * @return true if the time is on the schedule
     */
    public boolean contains(TimeSlot time) {
        return time != null && slots.contains(time);
    }

    /**
     * Returns the slots as a compact string, which is easier to read than a
     * list of sixteen times on separate lines.
     *
     * @return the schedule in human readable form
     */
    @Override
    public String toString() {
        return opening + " to " + closing + ", " + slotMinutes
                + " minute slots (" + slots.size() + " slots per day)";
    }
}
