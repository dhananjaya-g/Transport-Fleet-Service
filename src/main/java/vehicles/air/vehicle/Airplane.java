package vehicles.air.vehicle;
import services.FuelConsumable;
import services.Maintainable;
import services.PassengerCarrier;
import services.CargoCarrier;
import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import vehicles.air.AirVehicle;

public class Airplane extends AirVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {
    private double fuelLevel; // liters (simplified)
    private final int passengerCapacity;
    private int currentPassengers;
    private final double cargoCapacity; // kg
    private double currentCargo;
    private boolean maintenanceNeeded;

    public Airplane(String id, String model, double maxSpeed, double maxAltitude,
                    int passengerCapacity, double cargoCapacity) throws InvalidOperationException {
        super(id, model, maxSpeed, maxAltitude);
        if (passengerCapacity <= 0) throw new InvalidOperationException("passengerCapacity must be > 0");
        if (cargoCapacity < 0) throw new InvalidOperationException("cargoCapacity must be >= 0");
        this.passengerCapacity = passengerCapacity;
        this.cargoCapacity = cargoCapacity;
        this.currentPassengers = 0;
        this.currentCargo = 0.0;
        this.fuelLevel = 0.0;
        this.maintenanceNeeded = false;
    }

    //default passengerCap : 200 and cargoCap : 1000kg
    public Airplane(String id, String model, double maxSpeed, double maxAltitude) throws InvalidOperationException {
        this(id,model,maxSpeed,maxAltitude,200,1000);
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative");
        if (distance == 0) return;
        double required = consumeFuel(distance);
        setCurrentMileage(distance);
        if (getCurrentMileage() >= MAINTENANCE_KM) scheduleMaintenance();
        System.out.printf("Airplane %s flew %.1f km, used %.2f L%n", getId(), distance, required);
    }

    @Override public double calculateFuelEfficiency() { return 5.0; }

    @Override public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) throw new InvalidOperationException("Refuel amount must be > 0");
        fuelLevel += amount;
    }
    @Override public double getFuelLevel() { return fuelLevel; }
    @Override public double consumeFuel(double distance) throws InsufficientFuelException{
        double required = distance / calculateFuelEfficiency();
        if (fuelLevel + 1e-9 < required) throw new InsufficientFuelException("Insufficient fuel");
        fuelLevel -= required;
        return required;
    }

    @Override public void boardPassengers(int count) throws OverloadException, InvalidOperationException {
        if (count <= 0) throw new InvalidOperationException("passengers count should be greater than 0");
        if (currentPassengers + count > passengerCapacity) throw new OverloadException("Passenger capacity exceeded than permitted");
        currentPassengers += count;
    }
    @Override public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count <= 0) throw new InvalidOperationException("passengers count should be greater than 0 to disembark");
        if (count > currentPassengers) throw new InvalidOperationException("Cannot disembark more than present");
        currentPassengers -= count;
    }
    @Override public int getPassengerCapacity() { return passengerCapacity; }
    @Override public int getCurrentPassengers() { return currentPassengers; }

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
    @Override public void performMaintenance() { if (maintenanceNeeded) { maintenanceNeeded = false; System.out.printf("Airplane %s maintenance performed.%n", getId()); } }
}
