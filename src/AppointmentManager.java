import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Holds and manages every appointment booked for the day.
 *
 * <p>The bookings live in an {@link ArrayList} because the number of
 * appointments is not known when the program starts, and because the list has
 * to grow and shrink as bookings are added and cancelled. The list keeps
 * insertion order; time order is produced when it is asked for, by sorting a
 * copy. Sorting a copy means the underlying bookings are never rearranged
 * behind the caller's back.</p>
 *
 * <p>Every operation returns a message. A successful operation returns a
 * "Booked" or "Cancelled" message and a refused one returns a message starting
 * with "WARNING", so the caller can print the result without having to know
 * which happened.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public class AppointmentManager {

    /** The clinic schedule that decides which times are bookable. */
    private final ClinicSchedule schedule;

    /** Every appointment currently on the books. */
    private final List<Appointment> appointments;

    /**
     * Creates a manager for a clinic schedule.
     *
     * @param schedule the clinic's daily schedule
     * @throws IllegalArgumentException if the schedule is null
     */
    public AppointmentManager(ClinicSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("An appointment manager needs a clinic schedule.");
        }
        this.schedule = schedule;
        this.appointments = new ArrayList<>();
    }

    /**
     * Returns the clinic schedule this manager works to.
     *
     * @return the schedule
     */
    public ClinicSchedule getSchedule() {
        return schedule;
    }

    /**
     * Returns how many appointments are currently booked.
     *
     * @return the number of bookings
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Adds a booking, first checking that the time is one of the clinic's
     * slots and that the professional is not already booked then.
     *
     * @param appointment the booking to add
     * @return a confirmation message, or a warning explaining why the booking
     *         was refused
     * @throws IllegalArgumentException if the appointment is null
     */
    public String addAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Cannot add a null appointment.");
        }
        TimeSlot time = appointment.getTime();
        if (!schedule.contains(time)) {
            return "WARNING: " + time + " is not one of the clinic's time slots ("
                    + schedule.getOpening() + " to " + schedule.getClosing() + ", "
                    + schedule.getSlotMinutes() + " minute slots).";
        }
        if (isBooked(appointment.getProfessional(), time)) {
            return "WARNING: " + appointment.getProfessional().getName()
                    + " (" + appointment.getProfessional().getId() + ") is already booked at "
                    + time + ".";
        }
        appointments.add(appointment);
        return "Booked " + appointment.getReference() + ": " + appointment.getPatient().getName()
                + " with " + appointment.getProfessional().getName()
                + " at " + time + ".";
    }

    /**
     * Reports whether a professional already has an appointment at a time.
     *
     * <p>The comparison uses {@link HealthProfessional#equals(Object)}, which
     * matches on ID, so a second object describing the same professional is
     * still recognised as a clash.</p>
     *
     * @param professional the professional to check
     * @param time         the time to check
     * @return true if that professional is already booked then
     */
    public boolean isBooked(HealthProfessional professional, TimeSlot time) {
        if (professional == null || time == null) {
            return false;
        }
        for (Appointment existing : appointments) {
            if (existing.getTime().equals(time)
                    && existing.getProfessional().equals(professional)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns every booking, in the order it was added. The returned list is
     * unmodifiable, so a caller cannot add or remove bookings by accident.
     *
     * @return an unmodifiable view of the bookings
     */
    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    /**
     * Returns every booking sorted from earliest to latest.
     *
     * @return a new list of bookings in time order
     */
    public List<Appointment> getAppointmentsInTimeOrder() {
        return inTimeOrder(appointments);
    }

    /**
     * Returns the bookings for one professional, sorted by time. The ID is
     * used rather than the object so the caller does not need to hold the
     * professional instance.
     *
     * @param professionalId the professional's ID, digits only
     * @return a list of that professional's bookings, in time order; empty if
     *         there are none
     * @throws IllegalArgumentException if the ID is null or blank
     */
    public List<Appointment> findAppointmentsByProfessional(String professionalId) {
        if (professionalId == null || professionalId.trim().isEmpty()) {
            throw new IllegalArgumentException("A professional ID is required for this search.");
        }
        String wantedId = professionalId.trim();
        List<Appointment> matches = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().getId().equals(wantedId)) {
                matches.add(appointment);
            }
        }
        return inTimeOrder(matches);
    }

    /**
     * Returns the bookings for one patient, found by mobile number. Spaces and
     * hyphens in the supplied number are ignored, so the user can type the
     * number the way it appears on a form.
     *
     * @param phone the patient's mobile number
     * @return that patient's bookings in time order; empty if there are none
     * @throws IllegalArgumentException if the number is null or blank
     */
    public List<Appointment> findAppointmentsByPatientPhone(String phone) {
        String wantedPhone = normalisePhone(phone);
        List<Appointment> matches = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getPhone().equals(wantedPhone)) {
                matches.add(appointment);
            }
        }
        return inTimeOrder(matches);
    }

    /**
     * Cancels the booking for one professional at one time.
     *
     * @param professionalId the professional's ID
     * @param time           the appointment time
     * @return a cancellation message, or a warning if no such booking exists
     * @throws IllegalArgumentException if either argument is null or blank
     */
    public String cancelAppointment(String professionalId, TimeSlot time) {
        if (professionalId == null || professionalId.trim().isEmpty()) {
            throw new IllegalArgumentException("A professional ID is required to cancel a booking.");
        }
        if (time == null) {
            throw new IllegalArgumentException("An appointment time is required to cancel a booking.");
        }
        String wantedId = professionalId.trim();
        // An Iterator is used because the list is changed while it is being
        // walked; removing through the iterator keeps it consistent.
        Iterator<Appointment> iterator = appointments.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.getProfessional().getId().equals(wantedId)
                    && appointment.getTime().equals(time)) {
                iterator.remove();
                return "Cancelled " + appointment.getReference() + ": "
                        + appointment.getPatient().getName() + " with "
                        + appointment.getProfessional().getName() + " at " + time + ".";
            }
        }
        return "WARNING: no appointment found for professional " + wantedId + " at " + time + ".";
    }

    /**
     * Cancels every booking belonging to one patient, found by phone number.
     *
     * @param phone the patient's mobile number
     * @return a cancellation message, or a warning if the patient has no
     *         bookings
     * @throws IllegalArgumentException if the number is null or blank
     */
    public String cancelAppointmentsForPatient(String phone) {
        String wantedPhone = normalisePhone(phone);
        List<String> cancelledReferences = new ArrayList<>();
        String patientName = null;
        Iterator<Appointment> iterator = appointments.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.getPatient().getPhone().equals(wantedPhone)) {
                cancelledReferences.add(appointment.getReference());
                patientName = appointment.getPatient().getName();
                iterator.remove();
            }
        }
        if (cancelledReferences.isEmpty()) {
            return "WARNING: no appointments found for phone number " + wantedPhone + ".";
        }
        return "Cancelled " + cancelledReferences.size() + " appointment(s) for "
                + patientName + " (" + wantedPhone + "): " + String.join(", ", cancelledReferences) + ".";
    }

    /**
     * Sorts a copy of the supplied bookings by time.
     *
     * @param source the bookings to sort
     * @return a new list in time order
     */
    private List<Appointment> inTimeOrder(List<Appointment> source) {
        List<Appointment> sorted = new ArrayList<>(source);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Reduces a phone number to digits so that formatted and unformatted
     * versions of the same number match.
     *
     * @param phone the number supplied by the caller
     * @return the number as digits only
     * @throws IllegalArgumentException if the number is null, blank or not
     *                                  ten digits
     */
    private static String normalisePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("A contact number is required for this search.");
        }
        String digits = phone.replaceAll("[\\s\\-()]", "");
        if (digits.length() != 10 || !digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("Contact number must be 10 digits: " + phone);
        }
        return digits;
    }
}
