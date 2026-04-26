package vehicles.land.vehicle;
import services.FuelConsumable;
import services.Maintainable;
import services.PassengerCarrier;
import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import vehicles.land.LandVehicle;

public class Car extends LandVehicle implements FuelConsumable, PassengerCarrier, Maintainable {
    private double fuelLevel;
    private final int passengerCapacity;
    private int currentPassengers;
    private boolean maintenanceNeeded;

    public Car(String id, String model, double maxSpeed, int numWheels, int passengerCapacity )
            throws InvalidOperationException {
        super(id, model, maxSpeed, numWheels);
        if (passengerCapacity < 0) throw new InvalidOperationException("passengerCapacity must be > 0");
        this.passengerCapacity = passengerCapacity;
        this.currentPassengers = 0;
        this.fuelLevel = 0.0;
        this.maintenanceNeeded = false;
    }

    //by default set passengerCapacity as 5 in case
    public Car(String id, String model,int numWheels, double maxSpeed) throws InvalidOperationException {
        this(id, model, maxSpeed, numWheels, 5);
    }


    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative");
        if (distance == 0) return;
        double consumed = consumeFuel(distance);
        addMileage(distance);
        if (getCurrentMileage() >= MAINTENANCE_KM) scheduleMaintenance();
        System.out.printf("Car %s drove %.1f km, used %.2f L, remaining %.2f L%n",
                getId(), distance, consumed, fuelLevel);
    }

    @Override public double calculateFuelEfficiency() { return 15.0; }

    @Override public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) throw new InvalidOperationException("Refuel amount must be > 0");
        fuelLevel += amount;
    }
    @Override public double getFuelLevel() { return fuelLevel; }

    @Override public double consumeFuel(double distance) throws InsufficientFuelException {
        double eff = calculateFuelEfficiency();
        double required = distance / eff;
        //fuelLevel + 1e-9  adding such amount can nullify the noise in decimals
        if (fuelLevel + 1e-9 < required) throw new InsufficientFuelException(String.format("Need %.2f L but only %.2f L", required, fuelLevel));
        fuelLevel -= required;
        return required;
    }

    @Override public void boardPassengers(int count) throws OverloadException, InvalidOperationException {
        if (count < 0) throw new InvalidOperationException("Cannot board negative passengers");
        if (currentPassengers + count > passengerCapacity) throw new OverloadException("Passenger capacity exceeded");
        currentPassengers += count;
    }
    @Override public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count < 0) throw new InvalidOperationException("Cannot disembark negative passengers");
        if (count > currentPassengers) throw new InvalidOperationException("Cannot disembark more than present");
        currentPassengers -= count;
    }
    @Override public int getPassengerCapacity() { return passengerCapacity; }
    @Override public int getCurrentPassengers() { return currentPassengers; }

    @Override public void scheduleMaintenance() { this.maintenanceNeeded = true; }
    @Override public boolean needsMaintenance() { return maintenanceNeeded; }
    @Override public void performMaintenance() { if (maintenanceNeeded) { maintenanceNeeded = false; System.out.printf("Car %s maintenance performed.%n", getId()); } }
}
