package system;
import java.time.Duration;
import java.time.LocalDateTime;
/**
 * Controller: AdminController
 * Manages admin inputs and validation
 */
class AdminController implements SystemStateObserver {
    private OFWBSystemDatabase model;
    private AdministratorScreen view;
    
    public AdminController(OFWBSystemDatabase model, AdministratorScreen view) {
        this.model = model;
        this.view = view;
        
        // Register as observer
        model.addObserver(this);
    }
    
    // Validate and process admin inputs
    public boolean processAdminConfiguration(LocalDateTime bookingsOpenTime,
                                          LocalDateTime bookingsCloseTime,
                                          LocalDateTime workshopStartTime,
                                          LocalDateTime workshopEndTime,
                                          Duration classDuration) {
        // Validation checks
        if (!validateTimings(bookingsOpenTime, bookingsCloseTime, workshopStartTime, workshopEndTime)) {
            view.showError("Invalid timing configuration. Please ensure logical order of times.");
            return false;
        }
        
        if (!validateClassDuration(classDuration)) {
            view.showError("Class duration must be between 30 and 60 minutes.");
            return false;
        }
        
        // Update model with validated configuration
        model.setBookingsOpenTime(bookingsOpenTime);
        model.setBookingsCloseTime(bookingsCloseTime);
        model.setWorkshopStartTime(workshopStartTime);
        model.setWorkshopEndTime(workshopEndTime);
        model.setClassDuration(classDuration);
        
        // Transition system state
        model.initialiseWorkshop();
        
        return true;
    }
    
    // Validate time order
    private boolean validateTimings(LocalDateTime bookingsOpenTime,
                                   LocalDateTime bookingsCloseTime,
                                   LocalDateTime workshopStartTime,
                                   LocalDateTime workshopEndTime) {
        return bookingsOpenTime.isBefore(bookingsCloseTime) &&
               bookingsCloseTime.isBefore(workshopStartTime) &&
               workshopStartTime.isBefore(workshopEndTime);
    }
    
    // Validate class duration
    private boolean validateClassDuration(Duration classDuration) {
        long minutes = classDuration.toMinutes();
        return minutes >= 30 && minutes <= 60;
    }
    
    @Override
    public void onStateChange(OFWBSystemDatabase.SystemState newState) {
        if (newState == OFWBSystemDatabase.SystemState.PREPARING_FOR_BOOKINGS) {
            view.showMessage("Workshop initialized. System is now preparing for bookings.");
        }
    }
}