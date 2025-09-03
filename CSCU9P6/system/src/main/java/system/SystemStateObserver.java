package system;
/**
 * Observer interface for the Observer pattern
 * Controllers implement this to be notified of state changes
 */
interface SystemStateObserver {
    void onStateChange(OFWBSystemDatabase.SystemState newState);
}