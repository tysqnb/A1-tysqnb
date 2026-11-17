/**
 * A patient who can book an appointment.
 *
 * <p>The appointment only needs a name and a mobile number, so that is all
 * this class holds. Making it a class of its own (rather than two loose
 * strings inside {@link Appointment}) means the phone number is validated in
 * one place, and a booking can be found later by comparing patients.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public class Patient {

    /** Mobile numbers in Australia are ten digits including the leading zero. */
    private static final int PHONE_DIGITS = 10;

    /** The patient's full name. */
    private final String name;

    /** The mobile number stored as digits only, for easy comparison. */
    private final String phone;

    /**
     * Creates a patient.
     *
     * @param name  the patient's full name
     * @param phone the mobile number, spaces and hyphens are allowed
     * @throws IllegalArgumentException if the name is blank or the number is
     *                                  not a ten digit mobile number
     */
    public Patient(String name, String phone) {
        this.name = checkName(name);
        this.phone = checkPhone(phone);
    }

    /**
     * Validates and tidies a patient name.
     *
     * @param name the name supplied by the caller
     * @return the trimmed name
     * @throws IllegalArgumentException if the name is null or blank
     */
    private static String checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required.");
        }
        return name.trim().replaceAll("\\s+", " ");
    }

    /**
     * Validates a mobile number and reduces it to digits.
     *
     * <p>Spaces, hyphens and brackets are removed first, so "0412 345 678" and
     * "0412-345-678" are stored the same way. That matters when the manager
     * searches for a patient's appointments by phone number.</p>
     *
     * @param phone the number supplied by the caller
     * @return the number as ten digits
     * @throws IllegalArgumentException if the number is missing or malformed
     */
    private static String checkPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient contact number is required.");
        }
        String digits = phone.replaceAll("[\\s\\-()]", "");
        if (digits.length() != PHONE_DIGITS) {
            throw new IllegalArgumentException(
                    "Contact number must be " + PHONE_DIGITS + " digits: " + phone);
        }
        for (int i = 0; i < digits.length(); i++) {
            if (!Character.isDigit(digits.charAt(i))) {
                throw new IllegalArgumentException("Contact number must contain digits only: " + phone);
            }
        }
        return digits;
    }

    /**
     * Returns the patient's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mobile number as digits only, which is the form used when
     * searching for appointments.
     *
     * @return the ten digit number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the number in a form that is easy to read, for example
     * "0412 345 678".
     *
     * @return the formatted mobile number
     */
    public String getFormattedPhone() {
        return phone.substring(0, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7);
    }

    /**
     * Two patients are treated as the same person when their mobile numbers
     * match, since the name alone is not unique.
     *
     * @param other the object to compare with
     * @return true if the other object is a patient with the same number
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient that = (Patient) other;
        return phone.equals(that.phone);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return a hash based on the phone number
     */
    @Override
    public int hashCode() {
        return phone.hashCode();
    }

    /**
     * Returns the patient's name and formatted number.
     *
     * @return a printable description of the patient
     */
    @Override
    public String toString() {
        return name + " (" + getFormattedPhone() + ")";
    }
}
