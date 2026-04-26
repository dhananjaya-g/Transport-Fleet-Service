package vehicles.water.vehicle;
import services.FuelConsumable;
import services.Maintainable;
import services.CargoCarrier;
import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import vehicles.water.WaterVehicle;

public class CargoShip extends WaterVehicle implements CargoCarrier, FuelConsumable, Maintainable {
    private final double cargoCapacity;
    private double currentCargo;
    private double fuelLevel;
    private boolean maintenanceNeeded;

    public CargoShip(String id, String model, double maxSpeed, boolean hasSail, double cargoCapacity)
            throws InvalidOperationException {
        super(id, model, maxSpeed, hasSail);
        if (cargoCapacity <= 0) throw new InvalidOperationException("cargoCapacity must be > 0");
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = 0.0;
        this.fuelLevel = 0.0;
        this.maintenanceNeeded = false;
    }

    //default set 50000kg capacity
    public CargoShip(String id, String model, double maxSpeed, boolean hasSail)
            throws InvalidOperationException {
        this(id,model,maxSpeed,hasSail,50000);
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative");
        if (distance == 0) return;
        setCurrentMileage(distance);
        if (hasSail()) {
            System.out.printf("Sailing ship %s traveled %.1f km under sail.%n", getId(), distance);
        } else {
            double required =consumeFuel(distance);
            System.out.printf("CargoShip %s motored %.1f km, used %.2f L%n", getId(), distance, required);
        }
        if (getCurrentMileage() >= MAINTENANCE_KM) scheduleMaintenance();
    }

    @Override public double calculateFuelEfficiency() { return hasSail() ? 0.0 : 4.0; }

    @Override public void loadCargo(double weight) throws OverloadException, InvalidOperationException {
        if (weight <= 0) throw new InvalidOperationException("Weight must be > 0");
        if (currentCargo + weight > cargoCapacity) throw new OverloadException("Cargo capacity exceeded");
        currentCargo += weight;
    }
    @Override public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight <= 0) throw new InvalidOperationException("Weight must be > 0");
        if (weight > currentCargo) throw new InvalidOperationException("Cannot unload more than current cargo");
        currentCargo -= weight;
    }
    @Override public double getCargoCapacity() { return cargoCapacity; }
    @Override public double getCurrentCargo() { return currentCargo; }

    @Override public void refuel(double amount) throws InvalidOperationException {
        if (hasSail()) throw new InvalidOperationException("Sail-only ship cannot be refueled");
        if (amount <= 0) throw new InvalidOperationException("Refuel amount must be > 0");
        fuelLevel += amount;
    }
    @Override public double getFuelLevel() { return fuelLevel; }
    @Override public double consumeFuel(double distance) throws InsufficientFuelException, InvalidOperationException {
        if (hasSail()) throw new InvalidOperationException("Sail-only ship does not consume fuel");
        if (distance <= 0) throw new InvalidOperationException("Distance must be > 0");
        double required = distance / calculateFuelEfficiency();
        if (fuelLevel + 1e-9 < required) throw new InsufficientFuelException("Insufficient fuel");
        fuelLevel -= required; return required;
    }

    @Override public void scheduleMaintenance() { this.maintenanceNeeded = true; }
    @Override public boolean needsMaintenance() { return maintenanceNeeded; }
    @Override public void performMaintenance() { if (maintenanceNeeded) { maintenanceNeeded = false; System.out.printf("CargoShip %s maintenance performed.%n", getId()); } }
}
