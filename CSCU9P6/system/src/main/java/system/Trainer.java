package system;

/**
 * Entity class: Trainer
 * Represents a fitness trainer in the system
 */
class Trainer {
    private String name;
    private String email;
    private String classType;
    private ClassRoom myClassRoom;
    
    public Trainer(String name, String email, String classType) {
        this.name = name;
        this.email = email;
        this.classType = classType;
        this.myClassRoom = new ClassRoom(this);
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getClassType() {
        return classType;
    }
    
    public ClassRoom getMyClassRoom() {
        return myClassRoom;
    }
}