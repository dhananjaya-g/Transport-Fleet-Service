package vehicles.air;
import vehicles.Vehicle;
import exceptions.InvalidOperationException;

public abstract class AirVehicle extends Vehicle {
    private final double maxAltitude; // meters
    protected AirVehicle(String id, String model, double maxSpeed, double maxAltitude) throws InvalidOperationException {
        super(id, model, maxSpeed);
        if (maxAltitude <= 0) throw new InvalidOperationException("maxAltitude must be > 0");
        this.maxAltitude = maxAltitude;
    }
    public double getMaxAltitude() { return maxAltitude; }
    @Override
    public double estimateJourneyTime(double distance) throws InvalidOperationException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative");
        if (distance == 0) return 0.0;
        double base = distance / getMaxSpeed();
        return base * 0.95;
    }
}
