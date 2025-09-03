package system;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model: Represents the system database and holds all data
 * Implements the observer pattern to notify controllers of state changes
 */
class OFWBSystemDatabase {
    // System states as defined in the design document
    enum SystemState {
        DORMANT,
        PREPARING_FOR_BOOKINGS,
        BOOKINGS_OPEN,
        BOOKINGS_CLOSED,
        WORKSHOP_IN_PROGRESS,
        WORKSHOP_COMPLETED
    }
    
    // System attributes
    private LocalDateTime bookingsOpenTime;
    private LocalDateTime bookingsCloseTime;
    private LocalDateTime workshopStartTime;
    private LocalDateTime workshopEndTime;
    private Duration classDuration;
    private SystemState systemStatus;
    
    // Collections
    private List<Trainer> trainerList;
    private List<Participant> participantList;
    private List<TimeSlot> timeSlotList;
    
    // List of observers (controllers) that need to be notified of state changes
    private List<SystemStateObserver> observers;
    
    // Constructor
    public OFWBSystemDatabase() {
        this.systemStatus = SystemState.DORMANT;
        this.trainerList = new ArrayList<>();
        this.participantList = new ArrayList<>();
        this.timeSlotList = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    // Method to initialize workshop and transition state
    public void initialiseWorkshop() {
        if (this.systemStatus == SystemState.DORMANT) {
            // Clear any old data
            this.timeSlotList.clear();
            this.participantList.clear();
            
            // Change state
            this.systemStatus = SystemState.PREPARING_FOR_BOOKINGS;
            
            // Notify observers of state change
            notifyObservers();
        }
    }
    
    // Method to set trainer availability
    public void setTrainerAvailability(Trainer trainer, List<TimeSlot> availableSlots) {
        // Update trainer's classroom with available slots
        trainer.getMyClassRoom().setAvailableSlots(availableSlots);
        
        // Update the main time slot list
        this.timeSlotList.addAll(availableSlots);
    }
    
    // Method to generate time slots based on workshop duration and class duration
    public List<TimeSlot> generateTimeSlots() {
        List<TimeSlot> slots = new ArrayList<>();
        
        // Calculate total workshop duration
        Duration workshopDuration = Duration.between(workshopStartTime, workshopEndTime);
        
        // Calculate number of slots
        long slotCount = workshopDuration.toMinutes() / classDuration.toMinutes();
        
        // Generate time slots
        LocalDateTime slotStart = workshopStartTime;
        for (int i = 0; i < slotCount; i++) {
            TimeSlot slot = new TimeSlot(slotStart);
            slots.add(slot);
            slotStart = slotStart.plus(classDuration);
        }
        
        return slots;
    }
    
    // Observer pattern implementation
    public void addObserver(SystemStateObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(SystemStateObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers() {
        for (SystemStateObserver observer : observers) {
            observer.onStateChange(this.systemStatus);
        }
    }
    
    // Check if system should transition to BookingsOpen state
    public void checkTimeForStateTransition() {
        LocalDateTime currentTime = LocalDateTime.now();
        
        if (this.systemStatus == SystemState.PREPARING_FOR_BOOKINGS && 
                currentTime.isAfter(bookingsOpenTime)) {
            this.systemStatus = SystemState.BOOKINGS_OPEN;
            notifyObservers();
        }
    }
    
    // Getters and setters
    public LocalDateTime getBookingsOpenTime() {
        return bookingsOpenTime;
    }
    
    public void setBookingsOpenTime(LocalDateTime bookingsOpenTime) {
        this.bookingsOpenTime = bookingsOpenTime;
    }
    
    public LocalDateTime getBookingsCloseTime() {
        return bookingsCloseTime;
    }
    
    public void setBookingsCloseTime(LocalDateTime bookingsCloseTime) {
        this.bookingsCloseTime = bookingsCloseTime;
    }
    
    public LocalDateTime getWorkshopStartTime() {
        return workshopStartTime;
    }
    
    public void setWorkshopStartTime(LocalDateTime workshopStartTime) {
        this.workshopStartTime = workshopStartTime;
    }
    
    public LocalDateTime getWorkshopEndTime() {
        return workshopEndTime;
    }
    
    public void setWorkshopEndTime(LocalDateTime workshopEndTime) {
        this.workshopEndTime = workshopEndTime;
    }
    
    public Duration getClassDuration() {
        return classDuration;
    }
    
    public void setClassDuration(Duration classDuration) {
        this.classDuration = classDuration;
    }
    
    public SystemState getSystemStatus() {
        return systemStatus;
    }
    
    public List<Trainer> getTrainerList() {
        return trainerList;
    }
    
    public void addTrainer(Trainer trainer) {
        this.trainerList.add(trainer);
    }
    
    public List<TimeSlot> getTimeSlotList() {
        return timeSlotList;
    }
}