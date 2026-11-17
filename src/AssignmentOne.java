import java.util.EnumSet;
import java.util.List;

/**
 * Assignment 1 demonstration program for the PROG2004 appointment system.
 *
 * <p>There is no user interface, so this class stands in for the user: it
 * builds a small team of health professionals, books patients in with them,
 * prints what the system knows, and deliberately triggers the error cases to
 * show that they are handled. Everything below runs from {@link #main(String[])}
 * and prints to the console.</p>
 *
 * @author Zihao Wang (25259382)
 * @version 1.0
 */
public class AssignmentOne {

    /** Full width divider used between sections. */
    private static final String DIVIDER = "=".repeat(78);

    /** Thin divider used between individual records. */
    private static final String THIN_DIVIDER = "-".repeat(78);

    /**
     * Runs the demonstration. Not used by the assignment itself, but standard
     * for a Java entry point.
     *
     * @param args command line arguments, ignored
     */
    public static void main(String[] args) {
        demonstrateHealthProfessionals();
        demonstrateAppointments();
        System.out.println();
        System.out.println(DIVIDER);
        System.out.println("END OF DEMONSTRATION");
        System.out.println(DIVIDER);
    }

    // ------------------------------------------------------------------
    // Part 3: creating health professional objects and working with them
    // ------------------------------------------------------------------

    /**
     * Creates the clinic's health professionals, prints them all through one
     * array, and shows both the behaviour they share and the behaviour that is
     * specific to some of them.
     */
    private static void demonstrateHealthProfessionals() {
        printHeading("PART 3 - HEALTH PROFESSIONALS");

        // Three GPs, plus one or two of each of the other types. Every object
        // is stored through the abstract HealthProfessional type.
        GeneralPractitioner gpHart = new GeneralPractitioner(
                "1001", "Dr Amelia Hart", "MED-4417", "Room 3");
        GeneralPractitioner gpMenon = new GeneralPractitioner(
                "1002", "Dr Ravi Menon",
                EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.THURSDAY, Day.FRIDAY),
                "MED-5120", "Room 5");
        GeneralPractitioner gpNguyen = new GeneralPractitioner(
                "1003", "Dr Sophie Nguyen",
                EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY),
                "MED-6684", "Room 2");
        Specialist cardiologist = new Specialist(
                "2001", "Dr Peter Vasquez",
                EnumSet.of(Day.TUESDAY, Day.THURSDAY),
                "MED-3321", "Cardiology");
        Surgeon orthopaedicSurgeon = new Surgeon(
                "2002", "Dr Helen Okafor",
                EnumSet.of(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY),
                "MED-7788", "Orthopaedics", "Theatre B");
        Nurse nurseLim = new Nurse(
                "3001", "Grace Lim", "Treatment Room 1", "Registered Nurse");
        Nurse nurseBaker = new Nurse(
                "3002", "Tom Baker", EnumSet.of(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY),
                "Treatment Room 2", "Enrolled Nurse");
        Dietitian dietitianRahman = new Dietitian(
                "4001", "Nadia Rahman", "Diabetes", "Room 7");

        // One array, eight objects, four different concrete classes. The loop
        // never asks what type it is holding.
        HealthProfessional[] team = {
            gpHart, gpMenon, gpNguyen, cardiologist,
            orthopaedicSurgeon, nurseLim, nurseBaker, dietitianRahman
        };

        System.out.println("The clinic has " + team.length
                + " health professionals. They are kept in one HealthProfessional[]");
        System.out.println("and printed in a loop, so adding a new type does not change this code.");
        System.out.println();

        for (int i = 0; i < team.length; i++) {
            System.out.println(THIN_DIVIDER);
            System.out.println("[" + (i + 1) + " of " + team.length + "]");
            System.out.println(team[i]);
        }
        System.out.println(THIN_DIVIDER);

        // The same array again, but this time through getRole() and
        // describeDuties(). Each object answers with its own version.
        System.out.println();
        System.out.println("Roles and duties, called through the base type:");
        System.out.println();
        for (HealthProfessional professional : team) {
            System.out.printf("  %-32s %s%n", professional.getRole(), professional.describeDuties());
        }

        // Behaviour that only some types have. The compiler allows these calls
        // because each variable is declared as its concrete type.
        System.out.println();
        System.out.println("Type specific behaviour:");
        System.out.println();
        Patient samplePatient = new Patient("Jane Smith", "0412 345 678");
        System.out.println("  " + gpHart.prescribe(samplePatient, "Amoxicillin 500 mg, three times daily"));
        System.out.println("  " + cardiologist.prescribe(samplePatient, "Atorvastatin 20 mg nightly"));
        System.out.println("  " + nurseLim.takeBloodSample(samplePatient));
        System.out.println("  " + orthopaedicSurgeon.performProcedure(samplePatient, "knee arthroscopy"));
        System.out.println("  " + dietitianRahman.reviewDietPlan(samplePatient, "lower sodium, 1800 kcal per day"));

        // Anything that would leave an object in an invalid state is refused
        // at construction time, so bad data never reaches the rest of the
        // system.
        System.out.println();
        System.out.println("Validation in action:");
        System.out.println();
        try {
            new GeneralPractitioner("12A", "Dr Bad Data", "MED-0000", "Room 1");
            System.out.println("  This line should never print.");
        } catch (IllegalArgumentException problem) {
            System.out.println("  Refused to create a professional: " + problem.getMessage());
        }
        try {
            new Nurse("3003", "Nurse No Ward", "", "Registered Nurse");
            System.out.println("  This line should never print.");
        } catch (IllegalArgumentException problem) {
            System.out.println("  Refused to create a nurse: " + problem.getMessage());
        }

        // equals() is based on the ID, which is what the manager relies on when
        // it looks for a double booking.
        HealthProfessional samePerson = new GeneralPractitioner(
                "1001", "Dr A. Hart", "MED-4417", "Room 3");
        System.out.println();
        System.out.println("Equality by ID:");
        System.out.println("  gpHart.equals(a second object with ID 1001) -> " + gpHart.equals(samePerson));
        System.out.println("  gpHart.equals(gpMenon)                      -> " + gpHart.equals(gpMenon));
        System.out.println("  gpHart.worksOn(Day.SATURDAY)                -> " + gpHart.worksOn(Day.SATURDAY));
    }

    // ------------------------------------------------------------------
    // Parts 4, 5 and 6: appointments
    // ------------------------------------------------------------------

    /**
     * Builds patients and appointments, then drives the manager through the
     * normal cases and the error cases.
     */
    private static void demonstrateAppointments() {
        printHeading("PARTS 4, 5 AND 6 - APPOINTMENTS");

        // The same professionals are rebuilt here so the appointment
        // demonstration can run on its own. In a larger program these would be
        // passed in.
        GeneralPractitioner gpHart = new GeneralPractitioner(
                "1001", "Dr Amelia Hart", "MED-4417", "Room 3");
        GeneralPractitioner gpMenon = new GeneralPractitioner(
                "1002", "Dr Ravi Menon", "MED-5120", "Room 5");
        GeneralPractitioner gpNguyen = new GeneralPractitioner(
                "1003", "Dr Sophie Nguyen", "MED-6684", "Room 2");
        Specialist cardiologist = new Specialist(
                "2001", "Dr Peter Vasquez", "MED-3321", "Cardiology");
        Surgeon surgeon = new Surgeon(
                "2002", "Dr Helen Okafor", "MED-7788", "Orthopaedics", "Theatre B");
        Nurse nurseLim = new Nurse(
                "3001", "Grace Lim", "Treatment Room 1", "Registered Nurse");
        Dietitian dietitianRahman = new Dietitian(
                "4001", "Nadia Rahman", "Diabetes", "Room 7");

        Patient jane = new Patient("Jane Smith", "0412 345 678");
        Patient marcus = new Patient("Marcus Webb", "0433 902 118");
        Patient aisha = new Patient("Aisha Khan", "0455-771-020");
        Patient liam = new Patient("Liam O'Brien", "0421 660 945");

        System.out.println("Patients registered for today:");
        System.out.println("  " + jane);
        System.out.println("  " + marcus);
        System.out.println("  " + aisha);
        System.out.println("  " + liam);

        // ---- Part 4: one appointment on its own ----
        System.out.println();
        System.out.println("PART 4 - a single appointment");
        System.out.println();
        Appointment firstBooking = new Appointment(jane, gpHart, TimeSlot.of(8, 0));
        System.out.println(firstBooking);

        System.out.println();
        System.out.println("An appointment cannot be built with a missing professional:");
        try {
            new Appointment(aisha, null, TimeSlot.of(9, 0));
        } catch (IllegalArgumentException problem) {
            System.out.println("  Refused: " + problem.getMessage());
        }

        // ---- Part 5: the clinic schedule and the manager ----
        System.out.println();
        System.out.println("PART 5 - managing the appointment collection");
        System.out.println();
        ClinicSchedule schedule = ClinicSchedule.standardDay();
        System.out.println("Clinic schedule: " + schedule);

        AppointmentManager manager = new AppointmentManager(schedule);
        System.out.println("Appointments on the books at the start: " + manager.getAppointmentCount());

        System.out.println();
        System.out.println("Adding appointments:");
        System.out.println("  " + manager.addAppointment(firstBooking));
        System.out.println("  " + manager.addAppointment(
                new Appointment(marcus, gpMenon, TimeSlot.of(8, 0))));
        System.out.println("  " + manager.addAppointment(
                new Appointment(aisha, cardiologist, TimeSlot.of(9, 30))));
        System.out.println("  " + manager.addAppointment(
                new Appointment(liam, nurseLim, TimeSlot.of(10, 0))));
        System.out.println("  " + manager.addAppointment(
                new Appointment(jane, dietitianRahman, TimeSlot.of(11, 30))));
        System.out.println("  " + manager.addAppointment(
                new Appointment(aisha, gpNguyen, TimeSlot.of(13, 0))));
        System.out.println("  " + manager.addAppointment(
                new Appointment(marcus, surgeon, TimeSlot.of(14, 0))));

        // Two appointments at 08:00 for different professionals are allowed;
        // the same professional twice is not.
        System.out.println();
        System.out.println("Error cases:");
        System.out.println("  " + manager.addAppointment(
                new Appointment(liam, cardiologist, TimeSlot.of(9, 30))));
        System.out.println("  " + manager.addAppointment(
                new Appointment(jane, gpHart, TimeSlot.of(7, 30))));
        System.out.println("  " + manager.addAppointment(
                new Appointment(jane, gpHart, TimeSlot.of(12, 15))));
        System.out.println("  " + manager.addAppointment(
                new Appointment(jane, gpHart, TimeSlot.of(16, 0))));

        // ---- Viewing ----
        System.out.println();
        System.out.println("All appointments, sorted from earliest to latest:");
        printAppointments(manager.getAppointmentsInTimeOrder());

        System.out.println();
        System.out.println("Filtered by health professional ID 1001:");
        printAppointments(manager.findAppointmentsByProfessional("1001"));

        System.out.println();
        System.out.println("Filtered by patient phone 0455 771 020:");
        printAppointments(manager.findAppointmentsByPatientPhone("0455 771 020"));

        System.out.println();
        System.out.println("Filtered by a professional with no bookings (9999):");
        List<Appointment> noMatches = manager.findAppointmentsByProfessional("9999");
        System.out.println("  " + noMatches.size() + " appointment(s) found.");

        // ---- Cancelling ----
        System.out.println();
        System.out.println("Cancelling:");
        System.out.println("  " + manager.cancelAppointment("2001", TimeSlot.of(9, 30)));
        System.out.println("  " + manager.cancelAppointment("2001", TimeSlot.of(10, 30)));
        System.out.println("  " + manager.cancelAppointment("2001", TimeSlot.of(9, 30)));
        System.out.println("  " + manager.cancelAppointmentsForPatient("0412 345 678"));
        System.out.println("  " + manager.cancelAppointmentsForPatient("0499 000 111"));

        System.out.println();
        System.out.println("Appointments after cancelling (" + manager.getAppointmentCount() + " left):");
        printAppointments(manager.getAppointmentsInTimeOrder());

        System.out.println();
        System.out.println("A professional who is free at 08:00 can still be booked elsewhere:");
        System.out.println("  " + manager.addAppointment(
                new Appointment(liam, gpHart, TimeSlot.of(8, 30))));
        System.out.println("  Appointments now on the books: " + manager.getAppointmentCount());
    }

    // ------------------------------------------------------------------
    // Output helpers
    // ------------------------------------------------------------------

    /**
     * Prints a section heading.
     *
     * @param title the heading text
     */
    private static void printHeading(String title) {
        System.out.println();
        System.out.println(DIVIDER);
        System.out.println(title);
        System.out.println(DIVIDER);
        System.out.println();
    }

    /**
     * Prints a list of appointments, or a message when the list is empty.
     *
     * @param appointments the appointments to print
     */
    private static void printAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("  (no appointments)");
            return;
        }
        System.out.println("  " + appointments.size() + " appointment(s):");
        for (Appointment appointment : appointments) {
            System.out.println();
            for (String line : appointment.toString().split("\\R")) {
                System.out.println("  " + line);
            }
        }
    }
}
