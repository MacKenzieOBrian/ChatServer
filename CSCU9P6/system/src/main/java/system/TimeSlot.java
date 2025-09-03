package system;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Supporting class: TimeSlot
 * Represents a single time slot for a workshop
 */
class TimeSlot {
    private LocalDateTime startTime;
    private boolean trainerAvailable;
    private List<Participant> bookedParticipants;
    
    public TimeSlot(LocalDateTime startTime) {
        this.startTime = startTime;
        this.trainerAvailable = false;  // Default to not available
        this.bookedParticipants = new ArrayList<>();
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public boolean isTrainerAvailable() {
        return trainerAvailable;
    }
    
    public void setTrainerAvailable(boolean trainerAvailable) {
        this.trainerAvailable = trainerAvailable;
    }
    
    public List<Participant> getBookedParticipants() {
        return bookedParticipants;
    }
    
    public void addParticipant(Participant participant) {
        this.bookedParticipants.add(participant);
    }
}