package system;
import java.time.Duration;
import java.time.LocalDateTime;
/**
 * View: AdministratorScreen
 * UI for administrator configuration
 */
class AdministratorScreen {
    private AdminController controller;
    
    public void setController(AdminController controller) {
        this.controller = controller;
    }
    
    public void showConfigurationForm() {
        // In a real implementation, this would create and display a form
        System.out.println("Displaying admin configuration form");
    }
    
    public void submitConfiguration(LocalDateTime bookingsOpenTime,
                                    LocalDateTime bookingsCloseTime,
                                    LocalDateTime workshopStartTime,
                                    LocalDateTime workshopEndTime,
                                    Duration classDuration) {
        controller.processAdminConfiguration(bookingsOpenTime, bookingsCloseTime, 
                                           workshopStartTime, workshopEndTime, classDuration);
    }
    
    public void showError(String message) {
        System.err.println("Error: " + message);
    }
    
    public void showMessage(String message) {
        System.out.println("Message: " + message);
    }
}