package petmanagement2025;
import java.time.LocalDate;

public class Pet {
    private int id;
    private String name;
    private String type;
    private LocalDate birthdate;

    public Pet(int id, String name, String type, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.birthdate = birthdate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public LocalDate getBirthdate() { return birthdate; }
}