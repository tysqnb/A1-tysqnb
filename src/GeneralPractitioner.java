import java.util.Set;

/**
 * A general practitioner: the doctor a patient usually sees first.
 *
 * <p>The only extra detail a GP carries is the consulting room they work
 * from. Everything else, including prescribing, comes from {@link Doctor}.</p>
 *
 * @author tysqnb
 * @version 1.0
 */
public class GeneralPractitioner extends Doctor {

    /** The consulting room the GP works from, for example "Room 3". */
    private final String consultingRoom;

    /**
     * Creates a GP with an explicit weekly roster.
     *
     * @param id                 the numeric identifier
     * @param name               the GP's full name
     * @param workingDays        the days worked each week
     * @param registrationNumber the medical registration number
     * @param consultingRoom     the room the GP consults from
     * @throws IllegalArgumentException if any argument is invalid
     */
    public GeneralPractitioner(String id, String name, Set<Day> workingDays,
                               String registrationNumber, String consultingRoom) {
        super(id, name, workingDays, registrationNumber);
        this.consultingRoom = checkRoom(consultingRoom);
    }

    /**
     * Creates a GP who works Monday to Friday.
     *
     * @param id                 the numeric identifier
     * @param name               the GP's full name
     * @param registrationNumber the medical registration number
     * @param consultingRoom     the room the GP consults from
     * @throws IllegalArgumentException if any argument is invalid
     */
    public GeneralPractitioner(String id, String name, String registrationNumber, String consultingRoom) {
        super(id, name, registrationNumber);
        this.consultingRoom = checkRoom(consultingRoom);
    }

    /**
     * Validates the room name.
     *
     * @param consultingRoom the room supplied by the caller
     * @return the trimmed room name
     * @throws IllegalArgumentException if the room is missing
     */
    private static String checkRoom(String consultingRoom) {
        if (consultingRoom == null || consultingRoom.trim().isEmpty()) {
            throw new IllegalArgumentException("Consulting room is required for a general practitioner.");
        }
        return consultingRoom.trim();
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
     * Returns this professional's role title.
     *
     * @return "General Practitioner"
     */
    @Override
    public String getRole() {
        return "General Practitioner";
    }

    /**
     * Returns what a GP does in this clinic.
     *
     * @return a one-line description of the GP's duties
     */
    @Override
    public String describeDuties() {
        return "Sees walk-in and booked patients, diagnoses common conditions, refers on to specialists.";
    }

    /**
     * Returns the GP's details, including the consulting room.
     *
     * @return a printable description of this GP
     */
    @Override
    public String toString() {
        return super.toString() + System.lineSeparator()
                + "Consulting    : " + consultingRoom;
    }
}
