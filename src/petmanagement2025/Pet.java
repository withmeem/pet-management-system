package petmanagement2025;

public class Pet {
    private int id;
    private String name;
    private String type;
    private int age;

    public Pet(int id, String name, String type, int age) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getAge() { return age; }
}