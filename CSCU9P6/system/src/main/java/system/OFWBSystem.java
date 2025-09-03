package system;
import java.time.Duration;
import java.time.LocalDateTime;

public class OFWBSystem {
    
    public static void main(String[] args) {
        // Step 1: Create model
        System.out.println("Creating OFWBSystemDatabase (Model)");
        OFWBSystemDatabase model = new OFWBSystemDatabase();
        
        // Step 2: Create views (UI)
        System.out.println("Creating AdministratorScreen and TrainerView (Views)");
        AdministratorScreen adminView = new AdministratorScreen();
        TrainerView trainerView = new TrainerView();
        
        // Step 3: Create controllers
        System.out.println("Creating AdminController and TrainerController (Controllers)");
        AdminController adminController = new AdminController(model, adminView);
        TrainerController trainerController = new TrainerController(model, trainerView);
        
        // Step 4: Connect views with controllers
        System.out.println("Connecting views with controllers");
        adminView.setController(adminController);
        trainerView.setController(trainerController);
        
        // Step 5: Add some sample trainers
        System.out.println("Adding sample trainers to the system");
        Trainer trainer1 = new Trainer("John Doe", "john@example.com", "Yoga");
        Trainer trainer2 = new Trainer("Jane Smith", "jane@example.com", "Pilates");
        
        model.addTrainer(trainer1);
        model.addTrainer(trainer2);
        
        // Simulate admin configuration
        System.out.println("Displaying admin configuration form");
        adminView.showConfigurationForm();
        
        // Example: Admin submits configuration
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingsOpen = now.plusDays(1);
        LocalDateTime bookingsClose = now.plusDays(2);
        LocalDateTime workshopStart = now.plusDays(3);
        LocalDateTime workshopEnd = workshopStart.plusHours(6);
        Duration classDuration = Duration.ofMinutes(30);
        
        System.out.println("Submitting configuration...");
        adminView.submitConfiguration(bookingsOpen, bookingsClose, workshopStart, workshopEnd, classDuration);
        
        // At this point, the system state would transition to PREPARING_FOR_BOOKINGS
        // and TrainerController would be activated to collect trainer availability
        
        // The rest of the workflow would continue based on user interactions
        System.out.println("System workflow is now in progress.");
    }
}
