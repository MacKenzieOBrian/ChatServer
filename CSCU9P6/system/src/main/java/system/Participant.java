package system;


/**
 * Entity class: Participant
 * Represents a workshop participant
 */
class Participant {
    private String name;
    private String email;
    
    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
}