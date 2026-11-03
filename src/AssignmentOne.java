import java.util.EnumSet;

/**
 * Assignment 1 demonstration program for the PROG2004 appointment system.
 *
 * <p>Part 3 so far: the clinic's health professionals are created, printed
 * through one array, and their shared and type specific behaviour is shown.</p>
 *
 * @author tysqnb
 * @version 1.0
 */
public class AssignmentOne {

    /** Full width divider used between sections. */
    private static final String DIVIDER = "=".repeat(78);

    /** Thin divider used between individual records. */
    private static final String THIN_DIVIDER = "-".repeat(78);

    /**
     * Runs the demonstration.
     *
     * @param args command line arguments, ignored
     */
    public static void main(String[] args) {
        demonstrateHealthProfessionals();
    }

    /**
     * Creates the clinic's health professionals, prints them all through one
     * array, and shows both the behaviour they share and the behaviour that is
     * specific to some of them.
     */
    private static void demonstrateHealthProfessionals() {
        printHeading("PART 3 - HEALTH PROFESSIONALS");

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

        HealthProfessional[] team = {
            gpHart, gpMenon, gpNguyen, cardiologist,
            orthopaedicSurgeon, nurseLim, nurseBaker, dietitianRahman
        };

        for (int i = 0; i < team.length; i++) {
            System.out.println(THIN_DIVIDER);
            System.out.println("[" + (i + 1) + " of " + team.length + "]");
            System.out.println(team[i]);
        }
        System.out.println(THIN_DIVIDER);

        System.out.println();
        System.out.println("Roles and duties, called through the base type:");
        System.out.println();
        for (HealthProfessional professional : team) {
            System.out.printf("  %-32s %s%n", professional.getRole(), professional.describeDuties());
        }

        System.out.println();
        System.out.println("Type specific behaviour:");
        System.out.println();
        Patient samplePatient = new Patient("Jane Smith", "0412 345 678");
        System.out.println("  " + gpHart.prescribe(samplePatient, "Amoxicillin 500 mg, three times daily"));
        System.out.println("  " + cardiologist.prescribe(samplePatient, "Atorvastatin 20 mg nightly"));
        System.out.println("  " + nurseLim.takeBloodSample(samplePatient));
        System.out.println("  " + orthopaedicSurgeon.performProcedure(samplePatient, "knee arthroscopy"));
        System.out.println("  " + dietitianRahman.reviewDietPlan(samplePatient, "lower sodium, 1800 kcal per day"));
    }

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
}
