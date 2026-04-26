package services;
public interface Maintainable {
    double MAINTENANCE_KM = 10_000;
    void scheduleMaintenance();
    boolean needsMaintenance();
    void performMaintenance();
}
