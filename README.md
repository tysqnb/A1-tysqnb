# A1-tysqnb

PROG2004 Object Oriented Programming, Assessment 1.

A booking system for a small health clinic. Java only, no interface, so
`AssignmentOne.main` does the job a receptionist would do.

## Layout

Everything lives in `src/`, default package.

| File | What it does |
| --- | --- |
| `AssignmentOne.java` | `main`, the scripted demonstration |
| `HealthProfessional.java` | Abstract base: ID, name, weekly roster |
| `Doctor.java` | Doctors only: registration number, prescribing |
| `GeneralPractitioner.java` | GP with a consulting room |
| `Specialist.java` | Specialist with an area of practice |
| `Surgeon.java` | Specialist with a theatre |
| `Nurse.java` | Nurse with a ward |
| `Dietitian.java` | Dietitian with a focus area |
| `Day.java` | Week day enum used by the rosters |

## Running it

```bash
javac -d bin src/*.java
java -cp bin AssignmentOne
```

Eclipse can import the folder as it is; `.project` and `.classpath` are
already here.

## Notes

`HealthProfessional` is abstract, so a plain professional cannot be created
and every subclass has to say what it is through `getRole()` and
`describeDuties()`.

The roster is an `EnumSet<Day>` copied into an unmodifiable set, so a caller
cannot quietly change someone's days after the object is built.
