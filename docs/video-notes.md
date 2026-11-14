# Video plan - four pillars of OOP

Maximum 5 minutes. The rubric wants each pillar named, explained in your own
words, pointed at in the code, and justified. Rough timing below leaves about
20 seconds spare.

Screen layout: editor with the file open on the left, terminal with the last
run on the right. Do not read from a script on screen, just keep these points
next to the camera.

## 0:00 - 0:20 What the program is

"A booking desk for a clinic. No UI, so `AssignmentOne.main` is the
receptionist. Staff inherit from one base class, appointments are compared by
time, and the manager holds the day's bookings in a list."

## 0:20 - 1:15 Encapsulation

Open `HealthProfessional.java`, scroll the fields and the constructor.

- All fields are `private final`. The only way in is the constructor.
- `checkId`, `checkName`, `copyDays` validate before anything is stored. The
  roster is copied into an `EnumSet` and wrapped with
  `Collections.unmodifiableSet`, so the caller cannot keep a reference and
  add Saturday later.
- `getWorkingDays()` hands back the unmodifiable set, not a copy of the field.
- Justify: a booking system that accepts "12A" as an ID or a nurse with no
  ward is worse than one that refuses it at the door. Doing the check once in
  the constructor means `AppointmentManager` never has to re-check.
- Demo: the two `try` blocks in `demonstrateHealthProfessionals` print the
  refusals.

## 1:15 - 2:20 Inheritance

Open `Doctor.java`, then `Surgeon.java`.

- `HealthProfessional -> Doctor -> Specialist -> Surgeon`. `Nurse` and
  `Dietitian` go straight to the base class because they do not prescribe.
- Each subclass calls `super(...)`, so the ID and roster checks run once and
  are never duplicated.
- `getSharedDetails()` writes the ID/name/roster block once. `Specialist`
  calls `super.toString()` and appends the specialty; `Surgeon` calls its
  parent and appends the theatre. Nothing is copy-pasted.
- Justify: `prescribe` lives on `Doctor`, so a `Nurse` variable simply does
  not have the method. The compiler stops a nurse prescribing.

## 2:20 - 3:15 Polymorphism

Open `AssignmentOne.java` around the `team` array, then `Appointment`.

- Eight objects, four concrete classes, one `HealthProfessional[]`. The loop
  calls `toString()` and `describeDuties()` and each object answers with its
  own version. Adding a physiotherapist means adding a class and one array
  entry; the loop does not change.
- `Appointment` stores the professional as `HealthProfessional`, so a booking
  works with any type.
- `compareTo` is the other kind of polymorphism: `Collections.sort` calls
  `Appointment.compareTo`, which calls `TimeSlot.compareTo`. The sort knows
  nothing about appointments.
- Demo: run the program and show the "Roles and duties" table.

## 3:15 - 4:00 Abstraction

Open `HealthProfessional.java` at the class declaration.

- `abstract class` plus two `abstract` methods, `getRole()` and
  `describeDuties()`. A plain health professional with no specialty makes no
  sense, so the compiler will not let you write `new HealthProfessional(...)`.
- Every concrete class is forced to answer both methods, so printing a role is
  never blank.
- `ClinicSchedule` is the other side of abstraction: `AppointmentManager` asks
  "is this time on the schedule?" and does not care how the slots were
  generated. Change 30 minutes to 15 and the manager still compiles.

## 4:00 - 4:30 Error cases and close

Run the program, scroll to the "Error cases" block: double booked 09:30, 07:30
outside the schedule, 12:15 not on a slot boundary, 16:00 is closing time.
Then the cancel block: cancelling something that is not there returns a
warning rather than crashing.

Close with: "The parts I would change next are a real date instead of
today-only, and a persistence layer, but both are outside this assessment."

## Things to check before recording

- `javac -d bin src/*.java` then `java -cp bin AssignmentOne` works from a
  clean checkout.
- No unused imports or warnings on screen.
- Everything said about the code is true for the code on screen. If a line
  number is mentioned, check it first.
