package vehicles.land.vehicle;
import services.FuelConsumable;
import services.Maintainable;
import services.CargoCarrier;
import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import vehicles.land.LandVehicle;

public class Truck extends LandVehicle implements FuelConsumable, CargoCarrier, Maintainable {
    private double fuelLevel;
    private final double cargoCapacity; // kg
    private double currentCargo; // kg
    private boolean maintenanceNeeded;

    public Truck(String id, String model, double maxSpeed, int numWheels, double cargoCapacity)
            throws InvalidOperationException {
        super(id, model, maxSpeed, numWheels);
        if (cargoCapacity <= 0) throw new InvalidOperationException("cargoCapacity must be > 0");
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = 0.0;
        this.fuelLevel = 0.0;
        this.maintenanceNeeded = false;
    }

    //set cargocap to 5000kg
    public Truck(String id, String model, double maxSpeed, int numWheels) throws InvalidOperationException {
        this(id,model,maxSpeed,numWheels,5000);
    }

    private double effectiveEfficiency() {
        double base = 8.0; // km/L
        double ratio = currentCargo / cargoCapacity;
        if (ratio > 0.5) base *= 0.90; // -10% efficiency when >50% loaded
        return base;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative");
        if (distance == 0) return;
        double eff = effectiveEfficiency();
        double required = consumeFuel(distance);
        setCurrentMileage(distance);
        if (getCurrentMileage() >= MAINTENANCE_KM) scheduleMaintenance();
        System.out.printf("Truck %s hauled %.1f km (%.1f/%.1f kg), used %.2f L, left %.2f L%n",
                getId(), distance, currentCargo, cargoCapacity, required, fuelLevel);
    }

    @Override public double calculateFuelEfficiency() { return effectiveEfficiency(); }

    @Override public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) throw new InvalidOperationException("Refuel amount must be > 0");
        fuelLevel += amount;
    }
    @Override public double getFuelLevel() { return fuelLevel; }
    @Override public double consumeFuel(double distance) throws InsufficientFuelException {
        double eff = effectiveEfficiency();
        double required = distance / eff;
        if (fuelLevel + 1e-9 < required) throw new InsufficientFuelException("Insufficient fuel");
        fuelLevel -= required; return required;
    }

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

    @Override public void scheduleMaintenance() { this.maintenanceNeeded = true; }
    @Override public boolean needsMaintenance() { return maintenanceNeeded; }
    @Override public void performMaintenance() { if (maintenanceNeeded) { maintenanceNeeded = false; System.out.printf("Truck %s maintenance performed.%n", getId()); } }
}
