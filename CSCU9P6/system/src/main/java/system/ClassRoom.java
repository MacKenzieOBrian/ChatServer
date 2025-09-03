package system;
import java.util.ArrayList; 
import java.util.List;

/**
 * Supporting class: ClassRoom
 * Associates a trainer with their available time slots
 */

class ClassRoom {
    private Trainer assignedTrainer;
    private List<TimeSlot> availableSlots;
    
    public ClassRoom(Trainer trainer) {
        this.assignedTrainer = trainer;
        this.availableSlots = new ArrayList<>();
    }
    
    public Trainer getAssignedTrainer() {
        return assignedTrainer;
    }
    
    public List<TimeSlot> getAvailableSlots() {
        return availableSlots;
    }
    
    public void setAvailableSlots(List<TimeSlot> availableSlots) {
        this.availableSlots = availableSlots;
    }
}