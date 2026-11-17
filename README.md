# A1-25259382

PROG2004 Object Oriented Programming, Assessment 1.

Zihao Wang (王子豪), student number 25259382.

A booking system for a small health clinic. Java only, no interface: there is
no menu and no login, so `AssignmentOne.main` does the job a receptionist
would do. It sets up the staff, books patients in, prints what is on the
books, and tries a few bookings that should not work.

## Layout

Everything lives in `src/` in the default package.

| File | What it does |
| --- | --- |
| `AssignmentOne.java` | `main`, the scripted demonstration for Parts 3 to 6 |
| `HealthProfessional.java` | Abstract base: ID, name, weekly roster, `equals`, `toString` |
| `Doctor.java` | Abstract layer for doctors, adds registration number and prescribing |
| `GeneralPractitioner.java` | GP, adds a consulting room |
| `Specialist.java` | Specialist doctor, adds an area of practice |
| `Surgeon.java` | Specialist with a theatre and an operating list |
| `Nurse.java` | Nurse, adds ward, registration level and blood samples |
| `Dietitian.java` | Dietitian, adds focus area and diet plans |
| `Patient.java` | Name and mobile number, validated |
| `TimeSlot.java` | Immutable hour/minute, `Comparable` so times sort correctly |
| `ClinicSchedule.java` | Builds the day's slots from opening, closing and slot length |
| `Appointment.java` | Patient + professional + time, `Comparable` by time |
| `AppointmentManager.java` | Add, filter, sort and cancel bookings |
| `Day.java` | Enum of week days, used for rosters |

## Running it

```bash
javac -d bin src/*.java
java -cp bin AssignmentOne
```

Or import the folder into Eclipse (it already has `.project` and
`.classpath`), then run `AssignmentOne`.

A copy of the console output from a clean run is in `docs/sample-output.txt`.

## Design notes

The hierarchy is four levels deep in one place and two in the others:
`HealthProfessional -> Doctor -> Specialist -> Surgeon`, with `Nurse` and
`Dietitian` hanging off the base class. I put prescribing on `Doctor` rather
than on `HealthProfessional` because a nurse holding a `prescribe` method
would be wrong, and the compiler enforces that for me.

`HealthProfessional` is abstract and has two abstract methods, `getRole()` and
`describeDuties()`. That means a bare health professional cannot be created
and every concrete type has to say what it is, which is what the brief asks
for in Part 2.

The weekly roster is an `EnumSet<Day>` copied into an unmodifiable set in the
constructor. A caller cannot add Saturday to someone's roster later, and the
original set they passed in is not shared with the object.

Time is a small `TimeSlot` class rather than a string. It converts to minutes
past midnight for `compareTo`, so 09:30 sorts before 10:00, and the same
comparison is reused by `Appointment.compareTo`. The clinic day is generated
by `ClinicSchedule` from an opening time, a closing time and a slot length,
so changing to 15 minute slots is a one line change and nothing else needs to
know.

Bookings are kept in an `ArrayList`. The number of appointments is not known
in advance and bookings get cancelled, so a fixed array would be awkward. The
list keeps insertion order; `getAppointmentsInTimeOrder()` sorts a copy so the
stored order is never disturbed.

The manager methods return a `String` instead of throwing. Success messages
start with "Booked" or "Cancelled", refusals start with "WARNING". That keeps
the demonstration readable and matches the brief's "a warning message is
returned if the action cannot be performed". Genuine programming errors
(nulls, bad arguments) still throw `IllegalArgumentException` so they show up
during development.

`equals` is by ID for a health professional and by phone number for a patient.
The double booking check compares professionals with `equals`, not `==`, so a
second object for the same doctor is still caught.

## Before submitting

- Zip the project as `A1-25259382.zip`, without `bin/`.
- Add the GitHub link and the video link in the MySCU comments.
- Attach the WakaTime report separately as `A1-25259382-WakaTime.png`
  (or jpg/pdf), and keep a copy in `docs/wakatime/`.
- Put the GenAI declaration in the comments. Template in
  `docs/genai-declaration.txt`.

## GenAI

Level 2 for this assessment: research, learning and debugging are allowed,
writing the code and the comments is not. I have to be able to explain every
class in the video, so nothing in here should be a surprise when I get asked
about it.
