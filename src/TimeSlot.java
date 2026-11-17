/**
 * A time of day a patient can be booked in for.
 *
 * <p>The clinic works to half hour slots, so a slot only needs an hour and a
 * minute. Keeping the time in its own small class means the comparison used
 * for sorting is written once, in {@link #compareTo(TimeSlot)}, instead of
 * being rebuilt from strings every time an appointment list is sorted.</p>
 *
 * <p>Instances are immutable: there are no setters, so a slot used inside an
 * appointment can never shift underneath it.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /** Hours in a day. */
    private static final int HOURS_PER_DAY = 24;

    /** Minutes in an hour. */
    private static final int MINUTES_PER_HOUR = 60;

    /** The hour of the slot, 0 to 23. */
    private final int hour;

    /** The minutes past the hour, 0 to 59. */
    private final int minute;

    /**
     * Creates a time slot.
     *
     * @param hour   the hour, 0 to 23
     * @param minute the minutes past the hour, 0 to 59
     * @throws IllegalArgumentException if either value is outside its range
     */
    public TimeSlot(int hour, int minute) {
        if (hour < 0 || hour >= HOURS_PER_DAY) {
            throw new IllegalArgumentException("Hour must be between 0 and 23: " + hour);
        }
        if (minute < 0 || minute >= MINUTES_PER_HOUR) {
            throw new IllegalArgumentException("Minute must be between 0 and 59: " + minute);
        }
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Convenience factory for a slot, used where a full constructor call would
     * make the calling code noisy.
     *
     * @param hour   the hour, 0 to 23
     * @param minute the minutes past the hour
     * @return a new time slot
     */
    public static TimeSlot of(int hour, int minute) {
        return new TimeSlot(hour, minute);
    }

    /**
     * Returns the hour of this slot.
     *
     * @return the hour, 0 to 23
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minutes past the hour.
     *
     * @return the minute, 0 to 59
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Converts the slot to minutes past midnight. Comparing one number is much
     * simpler than comparing hours and minutes separately, and it also means
     * "09:30" correctly sorts before "10:00".
     *
     * @return the slot as minutes past midnight
     */
    public int toMinutes() {
        return hour * MINUTES_PER_HOUR + minute;
    }

    /**
     * Orders slots from earliest to latest in the day.
     *
     * @param other the slot to compare against
     * @return a negative number if this slot is earlier, zero if the same, or
     *         a positive number if later
     */
    @Override
    public int compareTo(TimeSlot other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare a time slot with null.");
        }
        return Integer.compare(toMinutes(), other.toMinutes());
    }

    /**
     * Two slots are equal when they fall on the same minute of the day.
     *
     * @param other the object to compare with
     * @return true if the other object is a slot at the same time
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TimeSlot)) {
            return false;
        }
        TimeSlot that = (TimeSlot) other;
        return hour == that.hour && minute == that.minute;
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return a hash based on the minutes past midnight
     */
    @Override
    public int hashCode() {
        return toMinutes();
    }

    /**
     * Returns the slot in 24 hour form, for example "08:30".
     *
     * @return the formatted time
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
