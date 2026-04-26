package vehicles.land;
import vehicles.Vehicle;
import exceptions.InvalidOperationException;

public abstract class LandVehicle extends Vehicle {
    private final int numWheels;
    protected LandVehicle(String id, String model, double maxSpeed, int numWheels) throws InvalidOperationException {
        super(id, model, maxSpeed);
        if (numWheels <= 0) throw new InvalidOperationException("numWheels must be > 0");
        this.numWheels = numWheels;
    }
    public int getNumWheels() { return numWheels; }
    @Override
    public double estimateJourneyTime(double distance) throws InvalidOperationException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative");
        if (distance == 0) return 0.0;
        double base = distance / getMaxSpeed();
        return base * 1.10;
    }
}
